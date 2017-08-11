package com.cmp.res.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * HTTP工具箱
 * 
 * @author leizhimin 2009-6-19 16:36:18
 */
public final class HttpTookit {
	private static Log log = LogFactory.getLog(HttpTookit.class);

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doGet(String url, String queryString, String charset) {

		StringBuffer responseBf = new StringBuffer();
		HttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url + "?" + queryString);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
			httpget.setConfig(requestConfig);
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					responseBf.append(line);
				}
				reader.close();
			}
		} catch (ConnectionPoolTimeoutException e) {
			log.error(
					"http get throw ConnectionPoolTimeoutException(wait time out)",
					e);
			
		} catch (ConnectTimeoutException e) {
			log.error("http get throw ConnectTimeoutException", e);new RuntimeException(e);
		} catch (SocketTimeoutException e) {
			log.error("http get throw SocketTimeoutException", e);
			responseBf.append("{\"status\":\"0\",\"error\":\"数据服务连接超时\"}");
			new RuntimeException(e);
		} catch (IOException e) {
			new RuntimeException(e);
		} finally {
		}

		return responseBf.toString();
	}
	
	
	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doGet(String url, String queryString, String charset,String cookie) {

		StringBuffer responseBf = new StringBuffer();
		HttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url + "?" + queryString);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
			httpget.setConfig(requestConfig);
			httpget.addHeader("Cookie", cookie);
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					responseBf.append(line);
				}
				reader.close();
			}
		} catch (ConnectionPoolTimeoutException e) {
			log.error(
					"http get throw ConnectionPoolTimeoutException(wait time out)",
					e);
			
		} catch (ConnectTimeoutException e) {
			log.error("http get throw ConnectTimeoutException", e);new RuntimeException(e);
		} catch (SocketTimeoutException e) {
			log.error("http get throw SocketTimeoutException", e);
			responseBf.append("{\"status\":\"0\",\"error\":\"数据服务连接超时\"}");
			new RuntimeException(e);
		} catch (IOException e) {
			new RuntimeException(e);
		} finally {
		}

		return responseBf.toString();
	}

	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doPost(String url, String params, String charset) {
		long s = System.currentTimeMillis();
		StringBuffer responseBf = new StringBuffer();
		HttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// 提交两个参数及值
			if (params != null && !"".equalsIgnoreCase(params)) {
				Pattern pat = Pattern.compile("\\w+=([^&]*)");
				Matcher m1 = pat.matcher(params);
				while (m1.find()) {
					String s0 = m1.group(0);
					if (s0 != null && !"".equalsIgnoreCase(s0)) {
						String[] ps = s0.split("=");
						String key = "";
						String value = "";
						if (ps.length > 1) {
							if(ps.length>2){
								for(int i  = 1;i<ps.length;i++){
									
									if(i!=(ps.length-1)){
										value += ps[i]+"=";
									}else{
										value += ps[i];
									}
								
								}
								
							}else{
								value = ps[1];
							}
							key = ps[0];
							
						} else if (ps.length == 1) {
							key = ps[0];
						} else {

						}
						nvps.add(new BasicNameValuePair(key, value));
					}
				}
			}
			// 设置表单提交编码为UTF-8
			httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
			httpost.setConfig(requestConfig);
			HttpResponse response = httpclient.execute(httpost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					responseBf.append(line);
				}
				reader.close();
			}else if(response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND){
				responseBf.append("{\"status\":\"0\",\"error\":\"服务不存在\"}");
			}

		} catch (ConnectionPoolTimeoutException e) {
			log.error(
					"http get throw ConnectionPoolTimeoutException(wait time out)",
					e);
			
		} catch (ConnectTimeoutException e) {
			log.error("http get throw ConnectTimeoutException", e);new RuntimeException(e);
		} catch (SocketTimeoutException e) {
			log.error("http get throw SocketTimeoutException", e);
			responseBf.append("{\"status\":\"0\",\"error\":\"数据服务连接超时\"}");
			new RuntimeException(e);
		} catch (IOException e) {
			new RuntimeException(e);
		} finally {
		}
		long e = System.currentTimeMillis();
		log.debug("URL："+url+"获取数据时间为"+(e-s)/1000.000+"秒");
		return responseBf.toString();
	}
	
	
	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doPost(String url, String params, String charset,String cookie) {
		long s = System.currentTimeMillis();
		StringBuffer responseBf = new StringBuffer();
		HttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// 提交两个参数及值
			if (params != null && !"".equalsIgnoreCase(params)) {
				Pattern pat = Pattern.compile("\\w+=([^&]*)");
				Matcher m1 = pat.matcher(params);
				while (m1.find()) {
					String s0 = m1.group(0);
					if (s0 != null && !"".equalsIgnoreCase(s0)) {
						String[] ps = s0.split("=");
						String key = "";
						String value = "";
						if (ps.length > 1) {
							if(ps.length>2){
								for(int i  = 1;i<ps.length;i++){
									
									if(i!=(ps.length-1)){
										value += ps[i]+"=";
									}else{
										value += ps[i];
									}
								
								}
								
							}else{
								value = ps[1];
							}
							key = ps[0];
							
						} else if (ps.length == 1) {
							key = ps[0];
						} else {

						}
						nvps.add(new BasicNameValuePair(key, value));
					}
				}
			}
			// 设置表单提交编码为UTF-8
			httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
			httpost.setConfig(requestConfig);
			httpost.addHeader("Cookie",cookie);
			
			for(Header header : httpost.getAllHeaders()){
				System.out.println("[Header]:["+header.getName()+"]---"+header.getValue());
			}
			
			
			HttpResponse response = httpclient.execute(httpost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					responseBf.append(line);
				}
				reader.close();
			}else if(response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND){
				responseBf.append("{\"status\":\"0\",\"error\":\"服务不存在\"}");
			}

		} catch (ConnectionPoolTimeoutException e) {
			log.error(
					"http get throw ConnectionPoolTimeoutException(wait time out)",
					e);
			
		} catch (ConnectTimeoutException e) {
			log.error("http get throw ConnectTimeoutException", e);new RuntimeException(e);
		} catch (SocketTimeoutException e) {
			log.error("http get throw SocketTimeoutException", e);
			responseBf.append("{\"status\":\"0\",\"error\":\"数据服务连接超时\"}");
			new RuntimeException(e);
		} catch (IOException e) {
			new RuntimeException(e);
		} finally {
		}
		long e = System.currentTimeMillis();
		log.debug("URL："+url+"获取数据时间为"+(e-s)/1000.000+"秒");
		return responseBf.toString();
	}

	
	public static String getHtml(String urlString) {
		try {
			StringBuffer html = new StringBuffer();
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				html.append(temp).append("\n");
			}
			br.close();
			isr.close();
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) {
		/*
		 * String y = doGet("http://video.sina.com.cn/life/tips.html", null,
		 * "GBK", true); System.out.println(y);
		 */

		
	}
}