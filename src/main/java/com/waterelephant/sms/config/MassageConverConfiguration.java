package com.waterelephant.sms.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
/**
 * FastJson替换默认的Jackson
 * @author Luyuan
 *
 */
@Configuration
public class MassageConverConfiguration {

	//如果采用注解这种方式，感觉都可以不用放在这个地方
    //只要在spring容器启动的时候被扫描到就行了
	@Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2:添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //设置驼峰转下划线
        SerializeConfig serializeConfig  = SerializeConfig.getGlobalInstance();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        fastJsonConfig.setSerializeConfig(serializeConfig);
        /**
         * SerializerFeature.PrettyFormat 格式化JSON 可以省略，毕竟这会造成额外的内存消耗和流量
　　　　　 	 * SerializerFeature.WriteNullListAsEmpty List字段如果为null,输出为[]
		 * SerializerFeature.WriteNullStringAsEmpty 字符类型字段如果为null,输出为""
         */
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        fastJsonConfig.setSerializerFeatures(
        		SerializerFeature.WriteNullStringAsEmpty,
        		SerializerFeature.WriteNullListAsEmpty,
        		SerializerFeature.PrettyFormat);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}
