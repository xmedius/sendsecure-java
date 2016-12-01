package com.xmedius.sendsecure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import com.xmedius.sendsecure.exception.SendSecureException;
import com.xmedius.sendsecure.utils.RequestWrapper;
import com.xmedius.sendsecure.utils.UrlUtils;

public class JsonClient {

	private String apiToken;
	private String enterpriseAccount;
	private String endpoint;
	private String sendSecureEndpoint;
	private String locale;
	private RequestWrapper requestWrapper = new RequestWrapper();;

	public JsonClient(String apiToken, String enterpriseAccount, String endpoint, String locale) {
		this.apiToken = apiToken;
		this.enterpriseAccount = enterpriseAccount;
		this.endpoint = endpoint;
		this.locale = StringUtils.isEmpty(locale) ? "en" : locale;
	}

	/**
	 *
	 * @param userEmail
	 * @return Strin
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public String newSafeBox(String userEmail) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		String url = getActionUrl(userEmail, "api/v2/safeboxes/new");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 *
	 * @param uploadUrl le param upload url
	 * @param file
	 * @param contentType
	 * @return something cool
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String uploadFile(String uploadUrl, File file, String contentType) throws ClientProtocolException, IOException, SendSecureException {
		return requestWrapper.postFileJson(uploadUrl, file, contentType);
	}

	public String uploadFile(String uploadUrl, InputStream stream, String contentType, String filename, long size) throws ClientProtocolException, IOException, SendSecureException {
		return requestWrapper.postStreamJson(uploadUrl, stream, contentType, filename, size);
	}

	public String commitSafebox(String safeboxJson) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl(null, "api/v2/safeboxes");
		return requestWrapper.postJson(url, safeboxJson, apiToken);
	}

	public String getSecurityProfiles(String userEmail) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl(userEmail, "api/v2/enterprises/" + enterpriseAccount + "/security_profiles");
		return requestWrapper.getJson(url, apiToken);
	}

	public String getEnterpriseSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl(null, "api/v2/enterprises/" + enterpriseAccount + "/settings");
		return requestWrapper.getJson(url, apiToken);
	}


	/**
	 * Private function
	 */

	private String getActionUrl(String userEmail, String action) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		URIBuilder builder = new URIBuilder(getSendSecureEndpoint() + action);
		if(StringUtils.isNotEmpty(userEmail)) {
			builder.addParameter("user_email", userEmail);
		}
		builder.addParameter("locale", locale);
		return builder.build().toString();
	}

	// Make sure the call to get the sendsecureUrl is cached in the same json client
	private String getSendSecureEndpoint() throws ClientProtocolException, IOException, SendSecureException {
		if( StringUtils.isEmpty(sendSecureEndpoint) ) {
			String url = UrlUtils.getBaseSendSecureUrl(enterpriseAccount, endpoint);
			try(CloseableHttpResponse response = requestWrapper.get(url)) {
				if(response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
					String result = IOUtils.toString(response.getEntity().getContent());
					System.out.println(result);
					throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase());
				}
				HttpEntity entity = response.getEntity();
				sendSecureEndpoint = EntityUtils.toString(entity, "UTF-8");
			}
		}
		return sendSecureEndpoint;
	}
}
