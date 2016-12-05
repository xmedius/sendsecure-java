package com.xmedius.sendsecure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

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

	/**
	 * JsonClient object constructor. Used to make call to create a SendSecure
	 *
	 * @param apiToken
	 *            The API Token to be used for authentication with the SendSecure service
	 * @param enterpriseAccount
	 *            The SendSecure enterprise account
	 * @param endpoint
	 *            The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
	 * @param locale
	 *            The locale in which the server errors will be returned ("en" will be used by default if empty)
	 */
	public JsonClient(String apiToken, String enterpriseAccount, String endpoint, String locale) {
		this.apiToken = apiToken;
		this.enterpriseAccount = enterpriseAccount;
		this.endpoint = endpoint;
		this.locale = StringUtils.isEmpty(locale) ? "en" : locale;
	}

	/**
	 * Pre-creates a SafeBox on the SendSecure system and initializes the Safebox object accordingly.
	 *
	 * @param userEmail
	 *            The email address of a SendSecure user of the current enterprise account
	 * @return The json containing the guid, public encryption key and upload url of the initialize SafeBox
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public String initializeSafeBox(String userEmail) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		String url = getActionUrl(userEmail, "api/v2/safeboxes/new");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Uploads the specified file as an Attachment of the specified SafeBox.
	 *
	 * @param uploadUrl
	 *            The url returned by the initializeSafeBox. Can be used multiple time
	 * @param file
	 *            The file to upload
	 * @param contentType
	 *            The MIME content type of the uploaded file
	 * @return The json containing the guid of the uploaded file
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String uploadFile(String uploadUrl, File file, String contentType) throws ClientProtocolException, IOException, SendSecureException {
		return requestWrapper.postFileJson(uploadUrl, file, contentType);
	}

	/**
	 * Uploads the specified file as an Attachment of the specified SafeBox.
	 *
	 * @param uploadUrl
	 *            The url returned by the initializeSafeBox. Can be used multiple time
	 * @param stream
	 *            The InputStream containing the file to upload
	 * @param contentType
	 *            The MIME content type
	 * @param filename
	 *            The filename (with extension)
	 * @param size
	 *            The size
	 * @return The json containing the guid of the uploaded file
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String uploadFile(String uploadUrl, InputStream stream, String contentType, String filename, long size)
			throws ClientProtocolException, IOException, SendSecureException {
		return requestWrapper.postStreamJson(uploadUrl, stream, contentType, filename, size);
	}

	/**
	 * Finalizes the creation (commit) of the SafeBox on the SendSecure system. This actually "Sends" the SafeBox with all content and contact info previously specified.
	 *
	 * @param safeboxJson
	 *            The full json expected by the server
	 * @return The json containing the guid, preview url and encryption key of the created SafeBox
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String commitSafebox(String safeboxJson) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl(null, "api/v2/safeboxes");
		return requestWrapper.postJson(url, safeboxJson, apiToken);
	}

	/**
	 * Retrieves all available security profiles of the enterprise account for a specific user.
	 *
	 * @param userEmail
	 *            The email address of a SendSecure user of the current enterprise account
	 * @return The json containing a list of Security Profile
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSecurityProfiles(String userEmail) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl(userEmail, "api/v2/enterprises/" + enterpriseAccount + "/security_profiles");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Get the Enterprise Settings of the current enterprise account
	 *
	 * @return The json containing the enterprise settings
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getEnterpriseSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl(null, "api/v2/enterprises/" + enterpriseAccount + "/settings");
		return requestWrapper.getJson(url, apiToken);
	}

	private String getActionUrl(String userEmail, String action)
			throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		URIBuilder builder = new URIBuilder(getSendSecureEndpoint() + action);
		if (StringUtils.isNotEmpty(userEmail)) {
			builder.addParameter("user_email", userEmail);
		}
		builder.addParameter("locale", locale);
		return builder.build().toString();
	}

	private String getSendSecureEndpoint() throws ClientProtocolException, IOException, SendSecureException {
		if (StringUtils.isEmpty(sendSecureEndpoint)) {
			String url = UrlUtils.getBaseSendSecureUrl(enterpriseAccount, endpoint);
			try (CloseableHttpResponse response = requestWrapper.get(url, null)) {
				if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
					throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()),
							response.getStatusLine().getReasonPhrase());
				}
				HttpEntity entity = response.getEntity();
				sendSecureEndpoint = EntityUtils.toString(entity, "UTF-8");
			}
		}
		return sendSecureEndpoint;
	}
}
