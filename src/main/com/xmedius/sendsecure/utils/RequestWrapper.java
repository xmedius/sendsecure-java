package com.xmedius.sendsecure.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.xmedius.sendsecure.exception.SendSecureException;

public class RequestWrapper {

	private static final String TOKEN_HEADER_NAME = "Authorization-Token";

	public CloseableHttpResponse get(String url, String apiToken) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(url);
		if (StringUtils.isNotEmpty(apiToken)) {
			httpGet.addHeader(TOKEN_HEADER_NAME, apiToken);
		}
		return httpClient.execute(httpGet);
	}

	public String getJson(String url, String apiToken) throws SendSecureException, ClientProtocolException, IOException {
		try (CloseableHttpResponse response = get(url, apiToken)) {
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
				throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}
	}

	public CloseableHttpResponse post(String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(params);
		httpPost.setEntity(urlEntity);
		return httpClient.execute(httpPost);
	}

	public CloseableHttpResponse postFile(String url, File file, String contentType) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("file", file, ContentType.create(contentType), file.getName());
		httpPost.setEntity(builder.build());
		return httpClient.execute(httpPost);
	}

	private CloseableHttpResponse postFile(String url, InputStream stream, String contentType, String filename, long size)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		InputStreamEntity entity = new InputStreamEntity(stream, size, ContentType.create(contentType));
		httpPost.setEntity(entity);
		return httpClient.execute(httpPost);
	}

	public String postJson(String url, String json, String apiToken) throws ClientProtocolException, IOException, SendSecureException {
		try (CloseableHttpResponse response = post(url, json, apiToken)) {
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
				throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}
	}

	public String postFileJson(String url, File file, String contentType) throws ClientProtocolException, IOException, SendSecureException {
		try (CloseableHttpResponse response = postFile(url, file, contentType)) {
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
				throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}
	}

	public String postStreamJson(String url, InputStream stream, String contentType, String filename, long size)
			throws ClientProtocolException, IOException, SendSecureException {
		try (CloseableHttpResponse response = postFile(url, stream, contentType, filename, size)) {
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
				throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}
	}

	private CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
	}

	private CloseableHttpResponse post(String url, String json, String apiToken) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(json);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		if (StringUtils.isNotEmpty(apiToken)) {
			httpPost.addHeader(TOKEN_HEADER_NAME, apiToken);
		}
		return httpClient.execute(httpPost);
	}
}
