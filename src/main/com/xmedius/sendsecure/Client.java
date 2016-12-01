package com.xmedius.sendsecure;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

	public Client(String apiToken, String enterpriseAccount, String endpoint) {
		jsonClient = new JsonClient(apiToken, enterpriseAccount, endpoint, null);
	}

	public Client(String apiToken, String enterpriseAccount, String endpoint, String locale) {
		jsonClient = new JsonClient(apiToken, enterpriseAccount, endpoint, locale);
	}

	public static String getUserToken(String enterpriseAccount, String username, String password) throws IOException, SendSecureException {
		return Client.getUserToken(enterpriseAccount, username, password, null, null);
	}

	public static String getUserToken(String enterpriseAccount, String username, String password, String otp, String endpoint) throws IOException, SendSecureException {
		String portalUrl = getPortalUrl(enterpriseAccount, endpoint);

		try(CloseableHttpResponse response = requestWrapper.post(UrlUtils.getUserTokenUrl(portalUrl), getUserTokenParams(enterpriseAccount, username, password, otp))) {
			HttpEntity entity = response.getEntity();
			if (entity == null) {
	            throw new UnexpectedServerResponseException();
	        }
			Gson gson = new GsonBuilder().create();
	        Reader reader = new InputStreamReader(entity.getContent(), ContentType.getOrDefault(entity).getCharset());
	        UserToken userToken = gson.fromJson(reader, UserToken.class);
	        if(response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
	        	if( userToken != null && StringUtils.isNotEmpty(userToken.getCode()) ) {
	        		throw new SendSecureException(userToken.getCode(), userToken.getMessage());
	        	} else {
	        		throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase());
	        	}
	        }
	        return userToken.getToken();
		} catch (JsonSyntaxException e) {
			throw new UnexpectedServerResponseException(e);
		}
	}

	public Safebox initializeSafebox(Safebox safebox) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		String result = jsonClient.newSafeBox(safebox.getUserEmail());
		Gson gson = new Gson();
		Safebox returnedSafebox = gson.fromJson(result, Safebox.class);
		safebox.setGuid(returnedSafebox.getGuid());
		safebox.setPublicEncryptionKey(returnedSafebox.getPublicEncryptionKey());
		safebox.setUploadUrl(returnedSafebox.getUploadUrl());
		return safebox;
	}

	public Attachment uploadAttachment(Safebox safebox, Attachment attachment) throws ClientProtocolException, IOException, SendSecureException {
		String result;
		if(attachment.getStream() != null) {
			result = jsonClient.uploadFile(safebox.getUploadUrl(), attachment.getStream(), attachment.getContentType(), attachment.getFilename(), attachment.getSize());
		} else {
			result = jsonClient.uploadFile(safebox.getUploadUrl(), attachment.getFile(), attachment.getContentType());
		}
		Gson gson = new Gson();
		TemporaryDocument document = gson.fromJson(result, TemporaryDocument.class);
		attachment.setGuid(document.getGuid());
		return attachment;
	}

	public SafeboxResponse commitSafebox(Safebox safebox) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		CommitSafeboxRequest commitSafeboxRequest = new CommitSafeboxRequest(safebox);
		Gson gson = new Gson();
		String safeboxJson = gson.toJson(commitSafeboxRequest);
		System.out.println(safeboxJson);
		String result = jsonClient.commitSafebox(safeboxJson);
		return gson.fromJson(result, SafeboxResponse.class);
	}

	public SafeboxResponse submitSafebox(Safebox safebox) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		initializeSafebox(safebox);
		for(Attachment a : safebox.getAttachments()) {
			uploadAttachment(safebox, a);
		}
		if( safebox.getSecurityProfile() == null ) {
			SecurityProfile defaultProfile = getDefaultSecurityProfile(safebox.getUserEmail());
			if(defaultProfile == null ) {
				throw new SendSecureException("400", "No Security Profile configured");
			} else {
				safebox.setSecurityProfile(defaultProfile);
			}
		}
		return commitSafebox(safebox);
	}

	public List<SecurityProfile> getSecurityProfiles(String userEmail) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String result = jsonClient.getSecurityProfiles(userEmail);
		Gson gson = new Gson();
		return gson.fromJson(result, SecurityProfiles.class).getSecurityProfiles();
	}

	public EnterpriseSettings getEnterpriseSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String result = jsonClient.getEnterpriseSettings();
		Gson gson = new Gson();
		return gson.fromJson(result, EnterpriseSettings.class);
	}

	public SecurityProfile getDefaultSecurityProfile(String userEmail) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		List<SecurityProfile> profiles = getSecurityProfiles(userEmail);
		EnterpriseSettings settings = getEnterpriseSettings();
		return profiles.stream().filter(p -> p.getId() == settings.getDefaultSecurityProfileId()).findFirst().orElse(null);
	}

	/**
	 * Private functions
	 */

	private static String getPortalUrl(String enterpriseAccount, String endpoint) throws IOException, SendSecureException {
		String url = UrlUtils.getBasePortalUrl(enterpriseAccount, endpoint);
		try(CloseableHttpResponse response = requestWrapper.get(url)) {
			if(response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
				throw new SendSecureException(String.valueOf(response.getStatusLine().getStatusCode()), response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}
	}

	private static List<NameValuePair> getUserTokenParams(String enterpriseAccount, String username, String password, String otp) {
		List<NameValuePair> params = new ArrayList<NameValuePair>(7);
		params.add(new BasicNameValuePair("permalink", enterpriseAccount));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("otp", otp));
		params.add(new BasicNameValuePair("application_type", "SendSecure Java"));
		params.add(new BasicNameValuePair("device_id", "device_id"));
		params.add(new BasicNameValuePair("device_name", "Something"));
		return params;
	}

}