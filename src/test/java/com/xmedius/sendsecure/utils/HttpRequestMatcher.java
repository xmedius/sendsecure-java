package com.xmedius.sendsecure.utils;

import java.net.URI;

import org.apache.http.client.methods.HttpRequestBase;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

/**
 * Class HttpGetMatcher should be used to match HTTP GET, POST, PATCH and DELETE requests with a specific URL.
 * Source : https://stackoverflow.com/a/39493690
 */
public class HttpRequestMatcher extends ArgumentMatcher<HttpRequestBase> {

	private final URI expected;

	public HttpRequestMatcher(URI expected) {
		this.expected = expected;
	}

	@Override
	public boolean matches(Object actual) {
		return ((HttpRequestBase) actual).getURI().equals(expected);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(expected == null ? null : expected.toString());
	}

}

