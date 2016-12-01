package com.xmedius.helpers;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;

public class RequestWrapperDummy extends RequestWrapper {

	CloseableHttpResponse response;

	@Override
	public CloseableHttpResponse get(String url) throws ClientProtocolException, IOException {
		return response;
	}

	@Override
	public CloseableHttpResponse post(String url, List<NameValuePair> params) {
		return response;
	}

	public void setCloseableHttpResponse(CloseableHttpResponse response) {
		this.response = response;
	}
}
