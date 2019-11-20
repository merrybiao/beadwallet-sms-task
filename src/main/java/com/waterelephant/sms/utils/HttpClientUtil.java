package com.waterelephant.sms.utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {
		
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

		
	/**
	 * 通过POST请求服务器
	 * @param url 被请求服务器地址
	 * @param jsonParams 请求参数为Json类型的字符串
	 * @return
	 */
	public static String httpPostToServer(String url, String jsonParams){
		String responseContent = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		CloseableHttpResponse response = null;
		
		HttpPost httpPost = new HttpPost(url);
		try {
			StringEntity paramEntity = new StringEntity(jsonParams,"UTF-8");
			httpPost.setEntity(paramEntity);
			httpPost.addHeader("Content-Type","application/json");
			response = httpClient.execute(httpPost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			
			logger.info("HttpConnetUtil: httpPostToServer url = " + url);
			logger .info("HttpConnetUtil: statusCode = " + statusCode);
			
			HttpEntity entity = response.getEntity();
			if(entity != null){
				responseContent = EntityUtils.toString(entity,"UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return responseContent;
	}
	
	/**
	 * 通过POST请求服务器
	 * @param url 被请求服务器地址
	 * @param jsonParams 请求参数为Json类型的字符串
	 * @return
	 */
	public static JSONObject httpPostToServer2(String url, String jsonParams){
		JSONObject jsonStr = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		CloseableHttpResponse response = null;
		
		HttpPost httpPost = new HttpPost(url);
		try {
			StringEntity paramEntity = new StringEntity(jsonParams,"UTF-8");
			httpPost.setEntity(paramEntity);
			httpPost.addHeader("Content-Type","application/json");
			response = httpClient.execute(httpPost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			
			logger.info("HttpConnetUtil: httpPostToServer url = " + url);
			logger .info("HttpConnetUtil: statusCode = " + statusCode);
			
			HttpEntity entity = response.getEntity();
			if(entity != null){
				jsonStr = JSONObject.parseObject(EntityUtils.toString(entity,HTTP.UTF_8));
				System.out.println(jsonStr);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		System.out.println(JSONObject.toJSONString(jsonStr));
		return jsonStr;
	}
	
	/**
	 * HTTP的POST请求
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	public static String httpPostMap(String url, Map<String, String> paramMap) throws Exception {
		JSONObject jsonObject = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity uefEntity;

		try {
			Set<String> set = paramMap.keySet();
			Iterator<String> it = set.iterator();
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			while (it.hasNext()) {
				String key = it.next();
				nameValuePairs.add(new BasicNameValuePair(key, paramMap
						.get(key)));
			}
			uefEntity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
			httpPost.setEntity(uefEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				jsonObject = JSONObject.parseObject(EntityUtils.toString(entity,
						HTTP.UTF_8));
			}

			return JSONObject.toJSONString(jsonObject);
		} catch (ClientProtocolException cpe) {
			throw new ClientProtocolException(cpe);
		} catch (IOException ie) {
			throw new IOException(ie);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
	}
	
		
	/**
	 * 通过GET的方式请求服务器
	 * @param url 被请求服务器地址
	 * @return
	 */
	public static String httpGetServer(String url){
		String responseContent = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		CloseableHttpResponse response = null;
		
		try {
			httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
		
			response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				if(entity != null){
					responseContent = EntityUtils.toString(entity,"UTF_8");
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseContent;
	}
}
