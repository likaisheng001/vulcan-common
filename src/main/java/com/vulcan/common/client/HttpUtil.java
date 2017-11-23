package com.vulcan.common.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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

/**
 * 
 * @author likaisheng
 *
 */
public class HttpUtil{
	
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
	 * 
	 * @param url
	 * @param param
	 * @return
	 */
	public HttpResponse postJson(String url,String param){
		
		HttpPost httpPost = new HttpPost(url);  // create httpPost
		try {
			// Set request header
			httpPost.setHeader("Content-Type","application/json");
			// Set request body
			StringEntity stringEntity = new StringEntity(param, encode);
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
			httpPost.setEntity(stringEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendPost(httpPost);
	}
	
	
	
	/**
	 * 
	 * @param url
	 * @param params  params(pattern:key1=value1&key2=value2)
	 * @return
	 */
	public HttpResponse postParam(String url, String params){
		HttpPost httpPost = new HttpPost(url);
		try {
			StringEntity stringEntity = new StringEntity(params,encode);
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendPost(httpPost);
	}
	
	/**
	 * Send post request
	 * @return
	 */
	public HttpResponse postMap(String url,Map<String, String> maps){
		HttpPost httpPost = new HttpPost(url);
		
		//create param queue
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for(String key : maps.keySet()){
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
	 * Send get request
	 * @return
	 */
	public HttpResponse get(String url){
		HttpGet httpGet = new HttpGet(url);
		return sendGet(httpGet);
	}

	/**
	 * Request to send GET method to the specified URL - based on URL programming
	 * @param url
	 * @param param param(pattern:key1=value1&key2=value2)
	 * @return
	 */
	public static HttpResponse sendGet(String url,String param){
		String result = "";
		BufferedReader in = null;
		
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// Open the connection to the URL
			URLConnection connection = realUrl.openConnection();
			// Set request attr
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// Create real connection
			connection.connect();
			// Get response header
			Map<String, List<String>> map = connection.getHeaderFields();
			// Item response header field
			for(String key : map.keySet()){
				System.out.println(key + "---->" + map.get(key));
			}
			
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			
			while((line = in.readLine()) != null){
				result += line;
			}
			
			return new HttpResponse(200, result);
		} catch (Exception e) {
			e.printStackTrace();
			return new HttpResponse(408, "SEND GET REQUEST ERROR!");
		}finally {
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * Request to send POST method to the specified URL - based on URL programming
	 * @param url
	 * @param param
	 * @return
	 */
	public static HttpResponse sendPost(String url,String param){
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// Open connection
			URLConnection conn = realUrl.openConnection();
			// Set request attr
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// The following two lines must be set to send the POST request
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// Get the output stream corresponding to the URLConnection object
			out = new PrintWriter(conn.getOutputStream());
			// Set request Param
			out.println(URLEncoder.encode(param, "utf-8"));
			// Flush output stream buffer
			out.flush();
			// Define the BufferedReader input stream to read the URL's response
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = in.readLine()) != null){
				result += line;
			}
			return new HttpResponse(200,result);
		} catch (Exception e) {
			e.printStackTrace();
			return new HttpResponse(408, "SEND POST REQUEST ERROR!");
		} finally {
			try {
				if(out != null){
					out.close();
				}
				if(in != null){
					in.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	private HttpResponse sendGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		
		try {
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);
			
			int statusCode = response.getStatusLine().getStatusCode();  // get response status
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity,"UTF-8");
			return new HttpResponse(statusCode, responseContent);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new HttpResponse(408, responseContent);
		} finally{
			try{
				// Close conn and release resource
				if(response != null){
					response.close();
				}
				if(httpClient != null){
					httpClient.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Send post 
	 * @param httpPost
	 * @return
	 */
	private HttpResponse sendPost(HttpPost httpPost){
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			httpClient = HttpClients.createDefault(); // create default client
			httpPost.setConfig(requestConfig);
			response = httpClient.execute(httpPost);  // execute request
			
			int statusCode = response.getStatusLine().getStatusCode(); // get response status
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity,"UTF-8");
			return new HttpResponse(statusCode, responseContent);
		} catch (Exception e) {
			
			e.printStackTrace();
			return new HttpResponse(408,responseContent);
			
		} finally{
			try{
				// close conn and release resource
				if(response != null){
					response.close();
				}
				if(httpClient != null){
					httpClient.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}
