package com.xmedius.sendsecure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.xmedius.sendsecure.JsonClient;
import com.xmedius.sendsecure.exception.SendSecureException;
import com.xmedius.sendsecure.exception.UnexpectedServerResponseException;
import com.xmedius.sendsecure.helper.Attachment;
import com.xmedius.sendsecure.helper.EnterpriseSettings;
import com.xmedius.sendsecure.helper.Safebox;
import com.xmedius.sendsecure.helper.SafeboxResponse;
import com.xmedius.sendsecure.helper.SecurityProfile;
import com.xmedius.sendsecure.json.CommitSafeboxRequest;
import com.xmedius.sendsecure.json.SecurityProfiles;
import com.xmedius.sendsecure.json.TemporaryDocument;
import com.xmedius.sendsecure.json.UserToken;
import com.xmedius.sendsecure.utils.RequestWrapper;
import com.xmedius.sendsecure.utils.UrlUtils;

public class Client {

	private JsonClient jsonClient;
	protected static RequestWrapper requestWrapper = new RequestWrapper();

	/**
	 * Client object constructor. Used to make call to create a SendSecure
	 *
	 * @param apiToken
	 *            The API Token to be used for authentication with the SendSecure service
	 * @param enterpriseAccount
	 *            The SendSecure enterprise account
	 * @param endpoint
	 *            The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
	 */
	public Client(String apiToken, String enterpriseAccount, String endpoint) {
		jsonClient = new JsonClient(apiToken, enterpriseAccount, endpoint, null);
	}

	/**
	 * Client object constructor. Used to make call to create a SendSecure
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
	public Client(String apiToken, String enterpriseAccount, String endpoint, String locale) {
		jsonClient = new JsonClient(apiToken, enterpriseAccount, endpoint, locale);
	}

	/**
	 * Gets an API Token for a specific user within a SendSecure enterprise account.
	 *
	 * @param enterpriseAccount
	 *            The SendSecure enterprise account
	 * @param username
	 *            The username of a SendSecure user of the current enterprise account
	 * @param password
	 *            The password of this user
	 * @param deviceId
	 *            The unique ID of the device used to get the Token
	 * @param deviceName
	 *            The name of the device used to get the Token
	 * @param applicationType
	 *            The type/name of the application used to get the Token ("SendSecure Java" will be used by default if empty)
	 * @param otp
	 *            The one-time password of this user (if any)
	 * @param endpoint
	 *            The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
	 * @return API Token to be used for the specified user
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public static String getUserToken(String enterpriseAccount, String username, String password, String deviceId, String deviceName,
			String applicationType, String otp, String endpoint) throws IOException, SendSecureException {
		String portalUrl = getPortalUrl(enterpriseAccount, endpoint);

		String content = "";
		try (CloseableHttpResponse response = requestWrapper.post(UrlUtils.getUserTokenUrl(portalUrl),
				getUserTokenParams(enterpriseAccount, username, password, otp, deviceId, deviceName, applicationType))) {
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new UnexpectedServerResponseException(content);
			}
			Gson gson = new GsonBuilder().create();
			Reader reader = new InputStreamReader(entity.getContent(), ContentType.getOrDefault(entity).getCharset());
			UserToken userToken = gson.fromJson(reader, UserToken.class);
			content = IOUtils.toString(reader);
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
				if (userToken != null && StringUtils.isNotEmpty(userToken.getCode())) {
					throw new SendSecureException(userToken.getCode(), userToken.getMessage(), content);
				} else {
					throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()),
							response.getStatusLine().getReasonPhrase(), content);
				}
			}
			return userToken.getToken();
		} catch (JsonSyntaxException e) {
			throw new UnexpectedServerResponseException(content, e);
		}
	}

	/*
	 * TODO: Rework the exceptions returned by the functions. Some of them are too precise for the user. Should they all
	 * be wrapped in SendSecureException?
	 */

	/**
	 * Pre-creates a SafeBox on the SendSecure system and initializes the Safebox object accordingly.
	 *
	 * @param safebox
	 *            A Safebox object to be initialized by the SendSecure system
	 * @return The updated SafeBox object with the necessary system parameters (GUID, public encryption key, upload URL)
	 *         filled out.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public Safebox initializeSafebox(Safebox safebox) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		if (safebox == null) {
			throw new SendSecureException("Safebox cannot be null");
		}
		String result = jsonClient.initializeSafeBox(safebox.getUserEmail());
		Gson gson = new Gson();
		Safebox returnedSafebox = gson.fromJson(result, Safebox.class);
		safebox.setGuid(returnedSafebox.getGuid());
		safebox.setPublicEncryptionKey(returnedSafebox.getPublicEncryptionKey());
		safebox.setUploadUrl(returnedSafebox.getUploadUrl());
		return safebox;
	}

	/**
	 * Uploads the specified file as an Attachment of the specified SafeBox.
	 *
	 * @param safebox
	 *            An initialized Safebox object
	 * @param attachment
	 *            An Attachment object - the file to upload to the SendSecure system
	 * @return The updated Attachment object with the GUID parameter filled out.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Attachment uploadAttachment(Safebox safebox, Attachment attachment) throws ClientProtocolException, IOException, SendSecureException {
		if (safebox == null || attachment == null) {
			throw new SendSecureException("Safebox/Attachment cannot be null");
		}
		String result;
		if (attachment.getStream() != null) {
			result = jsonClient.uploadFile(safebox.getUploadUrl(), attachment.getStream(), attachment.getContentType(), attachment.getFilename(),
					attachment.getSize());
		} else {
			if (!attachment.getFile().exists()) {
				throw new FileNotFoundException();
			}
			result = jsonClient.uploadFile(safebox.getUploadUrl(), attachment.getFile(), attachment.getContentType());
		}
		Gson gson = new Gson();
		TemporaryDocument document = gson.fromJson(result, TemporaryDocument.class);
		attachment.setGuid(document.getGuid());
		return attachment;
	}

	/**
	 * Finalizes the creation (commit) of the SafeBox on the SendSecure system. This actually "Sends" the SafeBox with
	 * all content and contact info previously specified.
	 *
	 * @param safebox
	 *            A Safebox object already initialized, with security profile, recipient(s), subject and message already
	 *            defined, and attachments already uploaded.
	 * @return {@link com.xmedius.sendsecure.helper.SafeboxResponse SafeboxResponse}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public SafeboxResponse commitSafebox(Safebox safebox) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if (safebox.getSecurityProfile() == null) {
			throw new SendSecureException("Security Profile cannot be null");
		}
		if (CollectionUtils.isEmpty(safebox.getRecipients())) {
			throw new SendSecureException("Recipient cannot be empty");
		}
		CommitSafeboxRequest commitSafeboxRequest = new CommitSafeboxRequest(safebox);
		Gson gson = new Gson();
		String safeboxJson = gson.toJson(commitSafeboxRequest);
		String result = jsonClient.commitSafebox(safeboxJson);
		return gson.fromJson(result, SafeboxResponse.class);
	}

	/**
	 * This method is a high-level combo that {@link #initializeSafebox initializes} the SafeBox,
	 * {@link #uploadAttachment uploads} all attachments and {@link #commitSafebox commits} the SafeBox.
	 *
	 * @param safebox
	 *            A non-initialized Safebox object with security profile, recipient(s), subject, message and attachments
	 *            (not yet uploaded) already defined.
	 * @return {@link com.xmedius.sendsecure.helper.SafeboxResponse SafeboxResponse}
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public SafeboxResponse submitSafebox(Safebox safebox) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		initializeSafebox(safebox);
		for (Attachment a : safebox.getAttachments()) {
			uploadAttachment(safebox, a);
		}
		if (safebox.getSecurityProfile() == null) {
			SecurityProfile defaultProfile = getDefaultSecurityProfile(safebox.getUserEmail());
			if (defaultProfile == null) {
				throw new SendSecureException("400", "No Security Profile configured", null);
			} else {
				safebox.setSecurityProfile(defaultProfile);
			}
		}
		return commitSafebox(safebox);
	}

	/**
	 * Retrieves all available security profiles of the enterprise account for a specific user.
	 *
	 * @param userEmail
	 *            The email address of a SendSecure user of the current enterprise account
	 * @return The list of all security profiles of the enterprise account, with all their setting values/properties.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public List<SecurityProfile> getSecurityProfiles(String userEmail)
			throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String result = jsonClient.getSecurityProfiles(userEmail);
		Gson gson = new Gson();
		return gson.fromJson(result, SecurityProfiles.class).getSecurityProfiles();
	}

	/**
	 * Retrieves all the current enterprise account's settings specific to SendSecure Account
	 *
	 * @return All values/properties of the enterprise account's settings specific to SendSecure.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public EnterpriseSettings getEnterpriseSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String result = jsonClient.getEnterpriseSettings();
		Gson gson = new Gson();
		return gson.fromJson(result, EnterpriseSettings.class);
	}

	/**
	 * Retrieves the default {@link com.xmedius.sendsecure.helper.SecurityProfile security profile} of the enterprise
	 * account for a specific user. A default security profile must have been set in the enterprise account, otherwise
	 * the method will return nothing.
	 *
	 * @param userEmail
	 *            The email address of a SendSecure user of the current enterprise account
	 * @return Default security profile of the enterprise, with all its setting values/properties.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public SecurityProfile getDefaultSecurityProfile(String userEmail)
			throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		List<SecurityProfile> profiles = getSecurityProfiles(userEmail);
		EnterpriseSettings settings = getEnterpriseSettings();
		return profiles.stream().filter(p -> p.getId() == settings.getDefaultSecurityProfileId()).findFirst().orElse(null);
	}

	private static String getPortalUrl(String enterpriseAccount, String endpoint) throws IOException, SendSecureException {
		String url = UrlUtils.getBasePortalUrl(enterpriseAccount, endpoint);
		try (CloseableHttpResponse response = requestWrapper.get(url, null)) {
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
				String responseContent = EntityUtils.toString(response.getEntity());
				throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase(),
						responseContent);
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}
	}

	private static List<NameValuePair> getUserTokenParams(String enterpriseAccount, String username, String password, String otp, String deviceId,
			String deviceName, String applicationType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>(7);
		params.add(new BasicNameValuePair("permalink", enterpriseAccount));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("otp", otp));
		params.add(new BasicNameValuePair("application_type", StringUtils.isEmpty(applicationType) ? "SendSecure Java" : applicationType));
		params.add(new BasicNameValuePair("device_id", deviceId));
		params.add(new BasicNameValuePair("device_name", deviceName));
		return params;
	}
}