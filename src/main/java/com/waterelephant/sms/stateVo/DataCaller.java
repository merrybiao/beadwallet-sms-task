package com.waterelephant.sms.stateVo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataCaller {

	private static final Logger logger = LoggerFactory.getLogger(DataCaller.class);

	final static int BUFFER_SIZE = 4096;

	private CloseableHttpClient client = null;

	private DataCaller() {
		open();
	}

	private final static DataCaller dataCaller = new DataCaller();

	public static DataCaller getCaller() {
		return dataCaller;
	}

	public void open() {
		createConnectionManager();
	}

	/**
	 * 创建连接
	 */
	private void createConnectionManager() {
		// 初始化HttpClient链接池
		PoolingHttpClientConnectionManager mgr = new PoolingHttpClientConnectionManager();
		// 设置连接池最大链接数
		mgr.setMaxTotal(300);
		// 设置链接主机的最大链接数（实际起作用的是DefaultMaxPerRoute而非MaxTotal,MaxTotal表示链接池大小，仅标识能存入多少链接
		// 而DefaultMaxPerRoute则代表同一时间最多能有多少链接访问主机）
		mgr.setDefaultMaxPerRoute(150);
		// 设置链接超时时间，ConnectTimeout链接建立三次握手时间，ConnectionRequestTimeout从连接池获取链接的时间，SocketTimeout数据传输过程中数据包传输之间最大的间隔时间
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60000).setConnectionRequestTimeout(60000)
				.setSocketTimeout(60000).setExpectContinueEnabled(false).build();
		// 设置编码格式
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Charset.forName("UTF-8")).build();
		// 设置数据通信socket
		SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(false).build();
		// http请求出错后重试接口
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 3) { // 重试三次后停止
					return false;
				}
				if (exception instanceof NoHttpResponseException) { // NoHttpResponseException异常替换为exception异常
					return true;
				} else if (exception instanceof ClientProtocolException) { //ClientProtocolException异常替换为exception异常
					return true;
				} else if (exception instanceof SocketTimeoutException) { //SocketTimeoutException异常替换为exception异常 
					return true;
				}
				return false;
			}
		};
		
		client = HttpClients.custom().setConnectionManager(mgr).setDefaultRequestConfig(requestConfig)
				.setDefaultConnectionConfig(connectionConfig).setDefaultSocketConfig(socketConfig)
				.setRetryHandler(retryHandler).build();
	}

	/**
	 * Post请求
	 * 
	 * @param serviceUrl
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public HttpResult httpPost(String serviceUrl, Map<String, String> headerMap, String message) throws Exception {
		CloseableHttpResponse response = null;
		HttpPost post = null;
		try {
			post = new HttpPost(serviceUrl);
			post.addHeader("Connection", "keep-alive");
			post.addHeader("content-type", "application/json");

			for (String str : headerMap.keySet()) {
				post.addHeader(str, headerMap.get(str));
			}
			StringEntity se = new StringEntity(message, "utf-8");
			post.setEntity(se);
			response = client.execute(post);
			return new HttpResult(getStatusCode(response), getContent(response));
		} catch (java.net.SocketException ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}

	/**
	 * POST方式请求服务器
	 * 
	 * @param url 被请求服务器地址
	 * @param params 请求参数分装在List<NameValuePair>中
	 * @return
	 */
	public HttpResult httpPost2(String url, Map<String, String> headerMap, Map<String, String> paramMap)
			throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		CloseableHttpResponse response = null;

		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity uefEntity;

		try {

			Set<String> set = paramMap.keySet();
			Iterator<String> it = set.iterator();
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			while (it.hasNext()) {
				String key = it.next();
				nameValuePairs.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
			uefEntity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
			httpPost.setEntity(uefEntity);

			if (headerMap != null) {
				for (String str : headerMap.keySet()) {
					httpPost.addHeader(str, headerMap.get(str));
				}
			}

			response = httpClient.execute(httpPost);

			return new HttpResult(getStatusCode(response), getContent(response));
		} catch (ClientProtocolException cpe) {
			throw new ClientProtocolException(cpe);
		} catch (IOException ie) {
			ie.printStackTrace();
			throw new IOException(ie);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
	}

	/**
	 * Post请求 - MultipartEntity（liuDaodao）
	 * 
	 * @param url 请求URL
	 * @param headerMap 头部信息
	 * @param paramMap 参数
	 * @param fileName 待上传文件名
	 * @param file 待上传文件
	 * @param fileType 待上传文件类型
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public HttpResult httpPostOfMultipartEntity(String url, Map<String, String> headerMap, Map<String, String> paramMap,
			String fileName, File file, String fileType) throws Exception {
		HttpPost post = null;
		CloseableHttpResponse response = null;
		try {
			// 设置header
			post = new HttpPost(url);
			post.addHeader("Connection", "keep-alive");
			post.addHeader("Content-Type", "multipart/form-data; boundary=----------ThIs_Is_tHe_bouNdaRY_$");
			if (headerMap != null) {
				for (String str : headerMap.keySet()) {
					post.addHeader(str, headerMap.get(str));
				}
			}

			// 设置body
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
					"----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
			multipartEntity.addPart(fileName, new FileBody(file)); // 待上传文件
			if (paramMap != null) {
				for (String str : paramMap.keySet()) {
					multipartEntity.addPart(str, new StringBody(paramMap.get(str), Charset.forName("UTF-8")));
				}
			}
			post.setEntity(multipartEntity);

			// 执行请求
			response = client.execute(post);

			// 返回值
			return new HttpResult(getStatusCode(response), getContent(response));
		} catch (java.net.SocketException ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (post != null) {
				// 释放链接并返回给连接池
				post.releaseConnection();
			}
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
				logger.error("httpPostOfMultipartEntity关闭response异常");
			}
		}
	}

	/**
	 * get请求
	 * 
	 * @param serviceUrl
	 * @param headerMap
	 * @return
	 * @throws Exception
	 */
	public HttpResult httpGet(String serviceUrl, Map<String, String> headerMap) throws Exception {
		String response = null;
		HttpGet httpGet = null;
		try {

			httpGet = new HttpGet(serviceUrl);
			httpGet.addHeader("Connection", "keep-alive");
			httpGet.addHeader("Accept", "application/json");
			httpGet.addHeader("Content-Type", "application/json");
			if (headerMap != null) {
				for (String str : headerMap.keySet()) {
					httpGet.addHeader(str, headerMap.get(str));
				}
			}
			response = client.execute(httpGet, new BasicResponseHandler());
			return new HttpResult(200, response);

		} catch (java.net.SocketException ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			httpGet.releaseConnection();
		}
	}

	/**
	 * 请求 - 获取状态码
	 * 
	 * @param response
	 * @return
	 */
	public int getStatusCode(CloseableHttpResponse response) {
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * 请求 - 获取请求体
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getContent(CloseableHttpResponse response) throws Exception {
		HttpEntity entity = response.getEntity();
		return InputStreamTOString(entity.getContent(), "utf-8");

	}

	/**
	 * 析构
	 */
	@Override
	public void finalize() {
		if (this.client != null) {
			try {
				this.client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.client = null;
		}
	}

	public static String InputStreamTOString(InputStream in, String encoding) throws Exception {
		ByteArrayOutputStream outStream = null;
		byte[] bb = null;
		try {
			outStream = new ByteArrayOutputStream();
			byte[] data = new byte[BUFFER_SIZE];
			int count = -1;
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
				outStream.write(data, 0, count);
			data = null;
			bb = outStream.toByteArray();
		} finally {
			try {
				if (null != outStream) {
					outStream.close();
				}
			} catch (Exception e) {
				logger.error("InputStreamTOString关闭outStream异常");
			}
		}
		return new String(bb, "ISO-8859-1");
	}
}
