//#////////////////////////////////////////////////////////////////////////////
//# Copyright (c) 2016 XMedius Solutions Inc. Permission to use this work
//# for any purpose must be obtained in writing from XMedius Solutions Inc.
//# 3400 de Maisonneuve Blvd. West, suite 1135, Montreal, Quebec H3Z 3B8
//#////////////////////////////////////////////////////////////////////////////
package com.xmedius;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.xmedius.Client;
import com.xmedius.exception.SendSecureException;
import com.xmedius.helpers.RequestWrapperDummy;

public class ClientTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	Mockery context = new Mockery();
	CloseableHttpResponse response = context.mock(CloseableHttpResponse.class);
	StatusLine statusLine = context.mock(StatusLine.class);
	HttpEntity entity = context.mock(HttpEntity.class);

	Client client;

	//Or @BeforeClass ??
	@Before
	public void setUp() {
		client = new Client();
		RequestWrapperDummy wrapperTest = new RequestWrapperDummy();
		wrapperTest.setCloseableHttpResponse(response);
		client.requestWrapper = wrapperTest;
	}

	@Test
	public void testGetUserToken() throws IOException, SendSecureException {
		context.checking(new Expectations() {
			{
				allowing(response).getStatusLine();
				will(returnValue(statusLine));

				allowing(statusLine).getStatusCode();
				will(returnValue(200));

				allowing(response).getEntity();
				will(returnValue(entity));

				oneOf(entity).getContent();
				will(returnValue(new ByteArrayInputStream("http://portal.".getBytes())));

				exactly(2).of(entity).getContentLength();
				will(returnValue(14L));

				allowing(entity).getContentType();
				will(returnValue(null));

				oneOf(entity).getContent();
				will(returnValue(new ByteArrayInputStream("{token: \"USER|1234\"}".getBytes())));

				allowing(response).close();
			}
		});
		assertEquals(client.getUserToken("ACME", "USERNAME", "PASSWORD", null, null), "USER|1234");
	}

	@Test
	public void testGetUserTokenNotFound() throws IOException, SendSecureException {
		thrown.expect(SendSecureException.class);
		thrown.expectMessage("bob");

		client.getUserToken("ACME", "USERNAME", "PASSWORD", null, null);
	}
}
