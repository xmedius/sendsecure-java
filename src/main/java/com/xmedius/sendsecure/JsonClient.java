package com.xmedius.sendsecure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
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
	private int userId;
	private RequestWrapper requestWrapper;

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
	public JsonClient(String apiToken, int userId, String enterpriseAccount, String endpoint, String locale) {
		this.apiToken = apiToken;
		this.userId = userId;
		this.enterpriseAccount = enterpriseAccount;
		this.endpoint = endpoint;
		this.locale = StringUtils.isEmpty(locale) ? "en" : locale;
		this.requestWrapper = new RequestWrapper();
	}

	public void setRequestWrapper(RequestWrapper requestWrapper) {
		this.requestWrapper = requestWrapper;
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
		String url = getActionUrl("user_email", userEmail, "api/v2/safeboxes/new.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Pre-creates a document on the SendSecure system.
	 *
	 * @param safeboxGuid
	 *                The guid of the existing safebox
	 * @param fileParams
	 *                The full json expected by the server
	 * @return The json containing the temporary document GUID and the upload URL
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String newFile(String safeboxGuid, String fileParams) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/uploads");
		return requestWrapper.postJson(url, fileParams, apiToken);
	}

	/**
	 * Reply to a specific safebox associated to the current user's account.
	 *
	 * @param safeboxGuid
	 *                The guid of the safebox
	 * @param replyJson
	 *                The reply json expected by the server
	 * @return The json containing the request result
	 * @throws SendSecureException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 */
	public String reply(String safeboxGuid, String replyJson) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/messages");
		return requestWrapper.postJson(url, replyJson, apiToken);
	}

	/**
	 * Uploads the specified file as an Attachment of the specified SafeBox.
	 *
	 * @param uploadUrl
	 *            The url returned by the initializeSafeBox. Can be used multiple times
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
	 * Finalizes the creation (commit) of the SafeBox on the SendSecure system. This actually "Sends" the SafeBox with
	 * all content and contact info previously specified.
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
		String url = getActionUrl("api/v2/safeboxes.json");
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
		String url = getActionUrl("user_email", userEmail, "api/v2/enterprises/" + enterpriseAccount + "/security_profiles.json");
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
		String url = getActionUrl("api/v2/enterprises/" + enterpriseAccount + "/settings.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Get the SendSecure User Settings of the current user account
	 *
	 * @return The json containing the user settings
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getUserSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/enterprises/" + enterpriseAccount + "/users/" + userId + "/settings.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieves all favorites for the current user account.
	 *
	 * @return The json containing a list of Favorite
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getFavorites() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/enterprises/" + enterpriseAccount + "/users/" + userId + "/favorites.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Create a new favorite for the current user account.
	 *
	 * @param favoriteJson
	 *            The full json expected by the server
	 * @return The json containing the new favorite informations
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public String createFavorite(String favoriteJson) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/enterprises/" + enterpriseAccount + "/users/" + userId + "/favorites.json");
		return requestWrapper.postJson(url, favoriteJson, apiToken);
	}

	/**
	 * Update an existing favorite for the current user account.
	 *
	 * @param favoriteId
	 *            The id of the favorite to be updated.
	 * @param favoriteJson
	 *            The full json expected by the server.
	 * @return The json containing the favorite new informations
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public String updateFavorite(int favoriteId, String favoriteJson) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/enterprises/" + enterpriseAccount + "/users/" + userId + "/favorites/" + favoriteId + ".json");
		return requestWrapper.patchJson(url, favoriteJson, apiToken);
	}

	/**
	 * Delete an existing favorite for the current user account.
	 *
	 * @param favoriteId
	 *            The id of the favorite to be deleted.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public void deleteFavorite(int favoriteId) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/enterprises/" + enterpriseAccount + "/users/" + userId + "/favorites/" + favoriteId + ".json");
		requestWrapper.deleteAction(url, apiToken);
	}

	/**
	 * Creates a Participant for a specific SafeBox of the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the associated Safebox.
	 * @param participantJson
	 *            The full json expected by the server
	 * @return The json containing the new participant informations
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public String createParticipant(String safeboxGuid, String participantJson) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/participants.json");
		return requestWrapper.postJson(url, participantJson, apiToken);
	}

	/**
	 * Update a Participant for a specific SafeBox of the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the associated Safebox.
	 * @param participantGuid
	 *            The guid of the participant to be modified.
	 * @param participantJson
	 *            The full json expected by the server
	 * @return The json containing the participant new informations
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public String updateParticipant(String safeboxGuid, String participantId, String participantJson) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/participants/" + participantId + ".json");
		return requestWrapper.patchJson(url, participantJson, apiToken);
	}

	/**
	 * Manually add time to expiration date for a specific safebox of the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @param addTimeJson
	 *            The full json expected by the server
	 * @return The json containing request result and new expiration date
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String addTime(String safeboxGuid, String addTimeJson) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/add_time.json");
		return requestWrapper.patchJson(url, addTimeJson, apiToken);
	}

	/**
	 * Manually close an existing safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing request result
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String closeSafebox(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/close.json");
		return requestWrapper.patchJson(url, StringUtils.EMPTY, apiToken);
	}

	/**
	 * Manually delete the content of a closed safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing request result
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String deleteSafeboxContent(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/delete_content.json");
		return requestWrapper.patchJson(url, StringUtils.EMPTY, apiToken);
	}

	/**
	 * Manually mark as read an existing safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing request result
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String markAsRead(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/mark_as_read.json");
		return requestWrapper.patchJson(url, StringUtils.EMPTY, apiToken);
	}

	/**
	 * Manually mark as unread an existing safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing request result
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String markAsUnread(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/mark_as_unread.json");
		return requestWrapper.patchJson(url, StringUtils.EMPTY, apiToken);
	}

	/**
	 * Mark a message as read of a specific safebox associated to the current user's account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @param messageId
	 *            The message Id.
	 * @return The json containing request result
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String markAsReadMessage(String safeboxGuid, String messageId) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/messages/" + messageId + "/read.json");
		return requestWrapper.patchJson(url, StringUtils.EMPTY, apiToken);
	}

	/**
	 * Mark a message as unread of a specific safebox associated to the current user's account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @param messageId
	 *            The message Id.
	 * @return The json containing request result
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String markAsUnreadMessage(String safeboxGuid, String messageId) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/messages/" + messageId + "/unread.json");
		return requestWrapper.patchJson(url, StringUtils.EMPTY, apiToken);
	}

	/**
	 * Retrieve a specific file url of an existing safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @param documentGuid
	 *            The guid of the file.
	 * @param userEmail
	 *            The email address of a SendSecure user of the current enterprise account
	 * @return The json containing the file url on the fileserver
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getFileUrl(String safeboxGuid, String documentGuid, String userEmail) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("user_email", userEmail, "api/v2/safeboxes/" + safeboxGuid + "/documents/" + documentGuid + "/url.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieve the url of the audit record of an existing safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing the pdf url
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getAuditRecordPdfUrl(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/audit_record_pdf.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieve the audit record of an existing safebox for the current user account.
	 *
	 * @param url
	 *            The url of the safebox audit record.
	 * @return The pdf stream
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getAuditRecordPdf(String url) throws URISyntaxException, ClientProtocolException, SendSecureException, IOException {
		URIBuilder builder = new URIBuilder(url);
		return requestWrapper.getJson(builder.build().toString(), apiToken);
	}

	/**
	 * Retrieves a filtered list of safeboxes for the current user account.
	 *
	 * @param searchParams
	 *            A list of all search parameters.
	 * @return The json containing a list of Safebox
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxes(List<NameValuePair> searchParams) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl(searchParams, "api/v2/safeboxes.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieves a filtered list of safeboxes for the current user account.
	 *
	 * @param searchUrl
	 *            The complete search url.
	 * @return The json containing a list of Safebox
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxes(String searchUrl) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl(searchUrl);
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieves the list of all the safeboxes for the current user account.
	 *
	 * @return The json containing a list of Safebox
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxes() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieve all info of an existing safebox for the current user account.
	 *
	 * @param sections
	 *            The string containing the list of sections to be retrieve.
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing a list of Safebox
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxInfo(String sections, String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = StringUtils.isEmpty(sections) ? getActionUrl("api/v2/safeboxes/" + safeboxGuid)
												: getActionUrl("sections", sections, "api/v2/safeboxes/" + safeboxGuid  + ".json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieves all participants of an existing Safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing a list of Participant
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxParticipants(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/participants.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieves all messages of an existing Safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing a list of Participant
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxMessages(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/messages.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieves security options info of an existing Safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing all values/properties of the security options
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxSecurityOptions(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/security_options.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieves download activity of an existing Safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing all values/properties of the download activity
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxDownloadActivity(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/download_activity.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Retrieves event history of an existing Safebox for the current user account.
	 *
	 * @param safeboxGuid
	 *            The guid of the Safebox.
	 * @return The json containing a list of EventHistory
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getSafeboxEventHistory(String safeboxGuid) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/safeboxes/" + safeboxGuid + "/event_history.json");
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Call to get the list of all the localized messages of a consent group.
	 *
	 * @param consentGroupId
	 *                 The id of the consent group
	 * @return The json containing the list of all the localized messages
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getConsentGroupMessages(int consentGroupId) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/enterprises/" + enterpriseAccount + "/consent_message_groups/" + consentGroupId);
		return requestWrapper.getJson(url, apiToken);
	}

	/**
	 * Search the recipients for a SafeBox.
	 *
	 * @param term
	 *          A search term.
	 * @return The list of recipients that matches the search term.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String searchRecipient(String term) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String url = getActionUrl("api/v2/participants/autocomplete?term=" + term);
		return requestWrapper.getJson(url, apiToken);
	}

	private String getActionUrl(List<NameValuePair> params, String action)
			throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		URIBuilder builder = getBasicURIBuilder(action);
		if(params != null){
			builder.addParameters(params);
		}
		return builder.build().toString();
	}

	private String getActionUrl(String paramName, String paramValue, String action)
			throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		URIBuilder builder = getBasicURIBuilder(action);
		builder.addParameter(paramName, paramValue);
		return builder.build().toString();
	}

	private String getActionUrl(String action)
			throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		return getBasicURIBuilder(action).build().toString();
	}

	private URIBuilder getBasicURIBuilder(String action)
			throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		URIBuilder builder = new URIBuilder(getSendSecureEndpoint() + action);
		builder.addParameter("locale", locale);
		return builder;
	}

	private String getSendSecureEndpoint() throws ClientProtocolException, IOException, SendSecureException {
		if (StringUtils.isEmpty(sendSecureEndpoint)) {
			String url = UrlUtils.getBaseSendSecureUrl(enterpriseAccount, endpoint);
			try (CloseableHttpResponse response = requestWrapper.get(url, null)) {
				if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
					String responseContent = EntityUtils.toString(response.getEntity());
					throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()),
							response.getStatusLine().getReasonPhrase(), responseContent);
				}
				HttpEntity entity = response.getEntity();
				sendSecureEndpoint = EntityUtils.toString(entity, "UTF-8");
			}
		}
		return sendSecureEndpoint;
	}
}
