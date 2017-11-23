package com.vulcan.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.vulcan.common.client.HttpResponse;

public class HttpUtil {
	private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(180000)
            .setConnectTimeout(180000)
            .setConnectionRequestTimeout(180000)
            .build();
	
	private String encode;

	public HttpUtil(){
		this.encode="UTF-8";
	}
	
	public HttpUtil(String encode){
		this.encode=encode==null?"UTF-8":encode;
	}
	
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param params 参数(格式:key1=value1&key2=value2)
	 */
	public HttpResponse postParam(String httpUrl, String params) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		try {
			//设置参数
			StringEntity stringEntity = new StringEntity(params, encode);
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendPost(httpPost);
	}
	
	public HttpResponse postJson(String httpUrl, String json) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		try {
			//设置参数
			httpPost.setHeader("Content-Type", "application/json");
			StringEntity stringEntity = new StringEntity(json, encode);
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));  
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendPost(httpPost);
	}
	
	
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param maps 参数
	 */
	public HttpResponse postMap(String httpUrl, Map<String, String> maps) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		// 创建参数队列  
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, encode));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendPost(httpPost);
	}

	/**
	 * 发送 get请求Http
	 * @param httpUrl
	 */
	public HttpResponse get(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendGet(httpGet);
	}


	/**
	 * 发送Post请求
	 * @param httpPost
	 * @return
	 */
	private HttpResponse sendPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);

			int stateCode = response.getStatusLine().getStatusCode();
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			return new HttpResponse(stateCode, responseContent);
		} catch (Exception e) {
			e.printStackTrace();
			return new HttpResponse(408, responseContent);
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送Get请求
	 * @param httpPost
	 * @return
	 */
	private HttpResponse sendGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			return new HttpResponse(stateCode, responseContent);
		} catch (Exception e) {
			e.printStackTrace();
			return new HttpResponse(408, responseContent);
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
}
