package com.xmedius.sendsecure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
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
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xmedius.sendsecure.exception.SendSecureException;
import com.xmedius.sendsecure.exception.UnexpectedServerResponseException;
import com.xmedius.sendsecure.helper.Attachment;
import com.xmedius.sendsecure.helper.ConsentMessageGroup;
import com.xmedius.sendsecure.helper.DownloadActivity;
import com.xmedius.sendsecure.helper.EnterpriseSettings;
import com.xmedius.sendsecure.helper.EventHistory;
import com.xmedius.sendsecure.helper.Favorite;
import com.xmedius.sendsecure.helper.Message;
import com.xmedius.sendsecure.helper.Participant;
import com.xmedius.sendsecure.helper.Reply;
import com.xmedius.sendsecure.helper.Safebox;
import com.xmedius.sendsecure.helper.SecurityEnums;
import com.xmedius.sendsecure.helper.SecurityOptions;
import com.xmedius.sendsecure.helper.SecurityProfile;
import com.xmedius.sendsecure.helper.UserSettings;
import com.xmedius.sendsecure.json.NewFileResponse;
import com.xmedius.sendsecure.json.RequestResponse;
import com.xmedius.sendsecure.json.SafeboxesResponse;
import com.xmedius.sendsecure.json.SearchRecipientResponse;
import com.xmedius.sendsecure.json.TemporaryDocument;
import com.xmedius.sendsecure.json.UserToken;
import com.xmedius.sendsecure.json.serializer.CommitSafeboxDeserializer;
import com.xmedius.sendsecure.json.serializer.FavoriteSerializer;
import com.xmedius.sendsecure.json.serializer.ParticipantSerializer;
import com.xmedius.sendsecure.json.serializer.ReplySerializer;
import com.xmedius.sendsecure.json.serializer.SafeboxSerializer;
import com.xmedius.sendsecure.json.serializer.SafeboxesResponseDeserializer;
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
	 * @param userId
	 *            The User Id of the User Account.
	 * @param enterpriseAccount
	 *            The SendSecure enterprise account
	 * @param endpoint
	 *            The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
	 */
	public Client(String token, int userId, String enterpriseAccount, String endpoint) {
		jsonClient = new JsonClient(token, userId, enterpriseAccount, endpoint, null);
	}

	/**
	 * Client object constructor. Used to make call to create a SendSecure
	 *
	 * @param apiToken
	 *            The API Token to be used for authentication with the SendSecure service
	 * @param userId
	 *            The User Id of the User Account.
	 * @param enterpriseAccount
	 *            The SendSecure enterprise account
	 * @param endpoint
	 *            The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
	 * @param locale
	 *            The locale in which the server errors will be returned ("en" will be used by default if empty)
	 */
	public Client(String token, int userId, String enterpriseAccount, String endpoint, String locale) {
		jsonClient = new JsonClient(token, userId, enterpriseAccount, endpoint, locale);
	}

	/**
	 * Client object constructor. Used to make call to create a SendSecure
	 *
	 * @param jsonClient
	 *            A JsonClient object
	 */
	public Client(JsonClient jsonClient) {
		this.jsonClient = jsonClient;
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
	 *            The type/name of the application used to get the Token ("SendSecure Java" will be used by default if
	 *            empty)
	 * @param endpoint
	 *            The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
	 * @param oneTimePassword
	 *            The one-time password of this user (if any)
	 * @return API Token to be used for the specified user
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public static UserToken getUserToken(String enterpriseAccount, String username, String password, String deviceId, String deviceName,
			String applicationType, String endpoint, String oneTimePassword) throws IOException, SendSecureException {
		String portalUrl = getPortalUrl(enterpriseAccount, endpoint);

		String content = "";
		try (CloseableHttpResponse response = requestWrapper.post(UrlUtils.getUserTokenUrl(portalUrl),
				getUserTokenParams(enterpriseAccount, username, password, oneTimePassword, deviceId, deviceName, applicationType))) {
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
			return userToken;
		} catch (JsonSyntaxException e) {
			throw new UnexpectedServerResponseException(content, e);
		}
	}

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
	 * Uploads the specified file as an Attachment of the specified existing SafeBox.
	 *
	 * @param uploadUrl
	 *            The file upload URL.
	 * @param attachment
	 *            An Attachment object - the file to upload to the SendSecure system
	 * @return The updated Attachment object with the GUID parameter filled out.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Attachment uploadReplyAttachment(String uploadUrl, Attachment attachment) throws ClientProtocolException, IOException, SendSecureException {
		if (attachment == null) {
		    throw new SendSecureException("Attachment cannot be null");
		}
		String result;
		if (attachment.getStream() != null) {
		    result = jsonClient.uploadFile(uploadUrl, attachment.getStream(), attachment.getContentType(), attachment.getFilename(),
		            attachment.getSize());
		} else {
		    if (!attachment.getFile().exists()) {
		        throw new FileNotFoundException();
		    }
		    result = jsonClient.uploadFile(uploadUrl, attachment.getFile(), attachment.getContentType());
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
	 * @return {@link com.xmedius.sendsecure.helper.Safebox Safebox}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Safebox commitSafebox(Safebox safebox) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if (safebox.getSecurityProfileId() == null) {
		    throw new SendSecureException("No Security Profile configured");
		}
		if (CollectionUtils.isEmpty(safebox.getParticipants())) {
		    throw new SendSecureException("Participants cannot be empty");
		}
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Safebox.class, new SafeboxSerializer()).create();
		String safeboxJson = gson.toJson(safebox);
		String result = jsonClient.commitSafebox(safeboxJson);
		gson = new GsonBuilder().registerTypeAdapter(Safebox.class, new CommitSafeboxDeserializer()).create();
		return gson.fromJson(result, Safebox.class);
	}

	/**
	 * This method is a high-level combo that {@link #initializeSafebox initializes} the SafeBox,
	 * {@link #uploadAttachment uploads} all attachments and {@link #commitSafebox commits} the SafeBox.
	 *
	 * @param safebox
	 *            A non-initialized Safebox object with security profile, recipient(s), subject, message and attachments
	 *            (not yet uploaded) already defined.
	 * @return {@link com.xmedius.sendsecure.helper.Safebox Safebox}
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public Safebox submitSafebox(Safebox safebox) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		initializeSafebox(safebox);
		for (Attachment a : safebox.getAttachments()) {
		    uploadAttachment(safebox, a);
		}
		if (safebox.getSecurityProfileId() == null) {
		    int publicProfileId = getEnterpriseSettings().getDefaultSecurityProfileId();
		    if (publicProfileId == 0) {
		        throw new SendSecureException("400", "No Security Profile configured", null);
		    } else {
		        safebox.setSecurityProfileId(publicProfileId);
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
		JsonArray securityProfilesArray = gson.fromJson(result, JsonObject.class).getAsJsonArray("security_profiles");
		return gson.fromJson(securityProfilesArray, new TypeToken<ArrayList<SecurityProfile>>(){}.getType());
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
		return new Gson().fromJson(result, EnterpriseSettings.class);
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

	/**
	 * Retrieves all the current user account's settings specific to SendSecure Account
	 *
	 * @return All values/properties of the user account's settings specific to SendSecure.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public UserSettings getUserSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String result = jsonClient.getUserSettings();
		return new Gson().fromJson(result, UserSettings.class);
	}

	/**
	 * Retrieves all favorites associated to a specific user.
	 *
	 * @return The list of all favorites of the user account, with all their properties.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public List<Favorite> getFavorites()
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String result = jsonClient.getFavorites();
		Gson gson = new Gson();
		JsonArray favoritesArray = gson.fromJson(result, JsonObject.class).getAsJsonArray("favorites");
		return gson.fromJson(favoritesArray, new TypeToken<ArrayList<Favorite>>(){}.getType());
	}

	/**
	 * Create a new favorite for the current user account.
	 *
	 * @param favorite
	 *            A Favorite object
	 * @return The updated {@link com.xmedius.sendsecure.helper.Favorite favorite}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Favorite createFavorite(Favorite favorite)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(favorite.getEmail() == null) {
		  throw new SendSecureException("No email configured");
		}
		InstanceCreator<Favorite> favoriteCreator = new InstanceCreator<Favorite>() {
		  @Override
		public Favorite createInstance(Type type) { return favorite; }
		};
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Favorite.class, new FavoriteSerializer()).registerTypeAdapter(Favorite.class, favoriteCreator).create();
		String favoriteJson = gson.toJson(favorite);
		String result = jsonClient.createFavorite(favoriteJson);
		gson.fromJson(result, Favorite.class);

		return favorite;
	}

	/**
	 * Update an existing favorite associated to a specific user.
	 *
	 * @param favorite
	 *            A Favorite object
	 * @return The updated {@link com.xmedius.sendsecure.helper.Favorite favorite}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Favorite updateFavorite(Favorite favorite)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		InstanceCreator<Favorite> favoriteCreator = new InstanceCreator<Favorite>() {
		  @Override
		public Favorite createInstance(Type type) { return favorite; }
		};
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Favorite.class, new FavoriteSerializer()).registerTypeAdapter(Favorite.class, favoriteCreator).create();
		String favoriteJson = gson.toJson(favorite);
		String result = jsonClient.updateFavorite(favorite.getId(), favoriteJson);
		gson.fromJson(result, Favorite.class);

		return  favorite;
	}

	/**
	 * Delete contact methods of an existing favorite associated to a specific user.
	 *
	 * @param favorite
	 *            A Favorite object
	 * @param contactMethodIds
	 *            A list of contact methods ids
	 * @return The updated {@link com.xmedius.sendsecure.helper.Favorite favorite}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Favorite deleteFavoriteContactMethods(Favorite favorite, List<Integer> contactMethodIds) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		favorite.prepareToDestroyContactMethods(contactMethodIds);
		return updateFavorite(favorite);
	}

	/**
	 * Delete an existing favorite associated to a specific user.
	 *
	 * @param favoriteId
	 *            The id of the Favorite to be deleted.
	 * @return True if the request was successfully completed
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Boolean deleteFavorite(int favoriteId)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		jsonClient.deleteFavorite(favoriteId);
		return true;
	}

	/**
	 * Create a new participant and add it to a specific safebox.
	 *
	 * @param participant
	 *            A Participant object
	 * @param safebox
	 *            A Safebox object
	 * @return The updated {@link com.xmedius.sendsecure.helper.Participant participant}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Participant createParticipant(Participant participant, Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(participant.getEmail() == null) {
		  throw new SendSecureException("No email configured");
		}
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		InstanceCreator<Participant> participantCreator = new InstanceCreator<Participant>() {
		  @Override
		public Participant createInstance(Type type) { return participant; }
		};
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Participant.class, new ParticipantSerializer()).registerTypeAdapter(Participant.class, participantCreator).create();
		String participantJson = gson.toJson(participant);
		String result = jsonClient.createParticipant(safebox.getGuid(), participantJson);
		safebox.getParticipants().add(gson.fromJson(result, Participant.class));

		return participant;
	}

	/**
	 * Update an existing participant of a specific safebox.
	 *
	 * @param participant
	 *            A Participant object
	 * @param safebox
	 *            A Safebox object
	 * @return The updated {@link com.xmedius.sendsecure.helper.Participant participant}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Participant updateParticipant(Participant participant, Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(participant.getId() == null) {
		  throw new SendSecureException("The participant id cannot be null.");
		}
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		InstanceCreator<Participant> participantCreator = new InstanceCreator<Participant>() {
		  @Override
		public Participant createInstance(Type type) { return participant; }
		};
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Participant.class, new ParticipantSerializer()).registerTypeAdapter(Participant.class, participantCreator).create();
		String participantJson = gson.toJson(participant);
		String result = jsonClient.updateParticipant(safebox.getGuid(), participant.getId(), participantJson);
		gson.fromJson(result, Participant.class);

		return participant;
	}

	/**
	 * Delete contact methods of an existing participant of a specific safebox.
	 *
	 * @param participant
	 *            A Participant object
	 * @param safebox
	 *            A Safebox object
	 * @param contactMethodsIds
	 *            A list of contact methods ids
	 * @return The updated {@link com.xmedius.sendsecure.helper.Participant participant}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Participant deleteParticipantContactMethods(Participant participant, Safebox safebox, List<Integer> contactMethodIds) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		participant.prepareToDestroyContactMethods(contactMethodIds);
		return updateParticipant(participant, safebox);
	}

	/**
	 * Add time to the expiration date of a specific safebox.
	 *
	 * @param safebox
	 *            A Safebox object
	 * @param value
	 *            Value of the time to add
	 * @param unit
	 *            Unit of the time to add
	 * @return The request response.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public RequestResponse addTime(Safebox safebox, int value, SecurityEnums.TimeUnit unit)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		Gson gson = new Gson();
		JsonObject addTimeParams = new JsonObject();
		addTimeParams.addProperty("add_time_value", value);
		addTimeParams.addProperty("add_time_unit", SecurityEnums.getSerializedName(unit));
		JsonObject addTimeJson = new JsonObject();
		addTimeJson.add("safebox", addTimeParams);
		String result = jsonClient.addTime(safebox.getGuid(), addTimeJson.toString());
		JsonObject responseJson = gson.fromJson(result, JsonObject.class);
		safebox.setExpiration(gson.fromJson(responseJson.get("new_expiration"), Date.class));
		return toResponse(result);
	}

	/**
	 * Manually close a specific safebox.
	 *
	 * @param safebox
	 *            The safebox to be closed.
	 * @return The request response.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public RequestResponse closeSafebox(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.closeSafebox(safebox.getGuid());
		return toResponse(result);
	}

	/**
	 * Manually delete the content of a closed safebox.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The request response.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public RequestResponse deleteSafeboxContent(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.deleteSafeboxContent(safebox.getGuid());
		return toResponse(result);
	}

	/**
	 * Mark all messages as read for an existing safebox.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The request response.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public RequestResponse markAsRead(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.markAsRead(safebox.getGuid());
		return toResponse(result);
	}

	/**
	 * Mark as all messages as unread for an existing safebox.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The request response.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public RequestResponse markAsUnread(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.markAsUnread(safebox.getGuid());
		return toResponse(result);
	}

	/**
	 * Mark a message as read of a specific safebox associated to the current user's account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @param message
	 *           A message object.
	 * @return The request response.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public RequestResponse markAsReadMessage(Safebox safebox, Message message) throws SendSecureException, ClientProtocolException, URISyntaxException, IOException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		if(message.getId() == null) {
			throw new SendSecureException("The Message Id cannot be null.");
		}
		String result = jsonClient.markAsReadMessage(safebox.getGuid(), message.getId());
		return toResponse(result);
	}

	/**
	 * Mark a message as unread of a specific safebox associated to the current user's account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @param message
	 *           A message object.
	 * @return The request response.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public RequestResponse markAsUnreadMessage(Safebox safebox, Message message) throws SendSecureException, ClientProtocolException, URISyntaxException, IOException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		if(message.getId() == null) {
			throw new SendSecureException("The Message Id cannot be null.");
		}
		String result = jsonClient.markAsUnreadMessage(safebox.getGuid(), message.getId());
		return toResponse(result);
	}

	/**
	 * Retrieve a specific file url from a specific safebox.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @param document
	 *            An Attachment object.
	 * @return The file url.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getFileUrl(Safebox safebox, Attachment document)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		if(safebox.getUserEmail() == null) {
		  throw new SendSecureException("The Safebox user email cannot be null.");
		}
		String result = jsonClient.getFileUrl(safebox.getGuid(), document.getGuid(), safebox.getUserEmail());
		JsonObject url = new Gson().fromJson(result, JsonObject.class);
		return url.get("url").toString();
	}

	/**
	 * Retrieve the audit record url of a specific safebox.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The audit record url.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getAuditRecordPdfUrl(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.getAuditRecordPdfUrl(safebox.getGuid());
		JsonObject url = new Gson().fromJson(result, JsonObject.class);
		return url.get("url").getAsString();
	}

	/**
	 * Retrieve the audit record of a specific safebox.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The audit record url.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public String getAuditRecordPdf(Safebox safebox) throws SendSecureException, ClientProtocolException, URISyntaxException, IOException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String url = getAuditRecordPdfUrl(safebox);

		return jsonClient.getAuditRecordPdf(url);
	}

	/**
	 * Retrieves a filtered list of safeboxes for the current user account.
	 *
	 * @param searchTerm
	 *            The Search Term in SafeBox subject and ID, message text, participant email, first name and last name, file name and fingerprint.
	 * @param page
	 *            The page to return.
	 * @param safeboxesPerPage
	 *            The maximum number of Safebox per pages with 0 < safeboxesPerPage <= 1000 (default is 100)
	 * @param status
	 *            The status filter to be applied.
	 * @return {@link com.xmedius.sendsecure.helper.SafeboxesResponse SafeboxesResponse}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public SafeboxesResponse getSafeboxes(String searchTerm, int page, int safeboxesPerPage, Safebox.Status status)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Gson gson = new GsonBuilder().registerTypeAdapter(SafeboxesResponse.class, new SafeboxesResponseDeserializer()).create();
		List<NameValuePair> searchParams = new ArrayList<>();
		searchParams.add(new BasicNameValuePair("status", Safebox.getStatusSerializedName(status)));
		searchParams.add(new BasicNameValuePair("search", searchTerm));
		searchParams.add(new BasicNameValuePair("per_page", String.valueOf(safeboxesPerPage)));
		searchParams.add(new BasicNameValuePair("page", String.valueOf(page)));
		String result = jsonClient.getSafeboxes(searchParams);

		return gson.fromJson(result, SafeboxesResponse.class);
	}

	/**
	 * Retrieves a filtered list of safeboxes for the current user account.
	 *
	 * @param searchUrl
	 *            The complete search url.
	 * @return {@link com.xmedius.sendsecure.helper.SafeboxesResponse SafeboxesResponse}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public SafeboxesResponse getSafeboxes(String searchUrl) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Gson gson = new GsonBuilder().registerTypeAdapter(SafeboxesResponse.class, new SafeboxesResponseDeserializer()).create();
		String result = jsonClient.getSafeboxes(searchUrl);

		return gson.fromJson(result, SafeboxesResponse.class);
	}

	/**
	 * Retrieves The list of all the safeboxes for the current user account.
	 *
	 * @return {@link com.xmedius.sendsecure.helper.SafeboxesResponse SafeboxesResponse}
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public SafeboxesResponse getSafeboxes() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Gson gson = new GsonBuilder().registerTypeAdapter(SafeboxesResponse.class, new SafeboxesResponseDeserializer()).create();
		String result = jsonClient.getSafeboxes();

		return gson.fromJson(result, SafeboxesResponse.class);
	}

	/**
	 * Retrieve all info of an existing safebox for the current user account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @param sections
	 *            The list of sections to be retrieved.
	 * @return The list of all safeboxes of the user account, with all their properties.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public Safebox getSafeboxInfo(Safebox safebox, List<String> sections)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		InstanceCreator<Safebox> safeboxCreator = new InstanceCreator<Safebox>() {
		  @Override
		public Safebox createInstance(Type type) { return safebox; }
		};
		String sectionsString = StringUtils.join(sections, ",");
		String result = jsonClient.getSafeboxInfo(sectionsString, safebox.getGuid());
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Safebox.class, safeboxCreator).create();
		JsonObject responseJson = gson.fromJson(result, JsonObject.class);

		return gson.fromJson(responseJson.get("safebox"), Safebox.class);
	}

	/**
	 * Retrieves all participants of an existing Safebox for the current user account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The list of all participants of safebox, with all their properties.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public List<Participant> getSafeboxParticipants(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.getSafeboxParticipants(safebox.getGuid());
		Gson gson = new Gson();
		JsonArray participantsArray = gson.fromJson(result, JsonObject.class).getAsJsonArray("participants");
		List<Participant> participants = gson.fromJson(participantsArray, new TypeToken<ArrayList<Participant>>(){}.getType());

		return participants;
	}

	/**
	 * Retrieves all messages of an existing Safebox for the current user account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The list of all messages of the safebox, with all their properties.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public List<Message> getSafeboxMessages(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.getSafeboxMessages(safebox.getGuid());
		Gson gson = new Gson();
		JsonArray messagesArray = gson.fromJson(result, JsonObject.class).getAsJsonArray("messages");
		List<Message> messages = gson.fromJson(messagesArray, new TypeToken<ArrayList<Message>>(){}.getType());

		return messages;
	}

	/**
	 * Retrieves security options info of an existing Safebox for the current user account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The security options.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public SecurityOptions getSafeboxSecurityOptions(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.getSafeboxSecurityOptions(safebox.getGuid());
		Gson gson = new Gson();
		JsonObject optionsJson = gson.fromJson(result, JsonObject.class);
		SecurityOptions options = gson.fromJson(optionsJson.get("security_options"), SecurityOptions.class);

		return options;
	}

	/**
	 * Retrieves the download activity info of an existing Safebox for the current user account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The download activity.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public DownloadActivity getSafeboxDownloadActivity(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.getSafeboxDownloadActivity(safebox.getGuid());
		Gson gson = new Gson();
		JsonObject downloadActivityJson = gson.fromJson(result, JsonObject.class);
		DownloadActivity downloadActivity = gson.fromJson(downloadActivityJson.get("download_activity"), DownloadActivity.class);

		return downloadActivity;
	}

	/**
	 * Retrieves the event history of an existing Safebox for the current user account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @return The list of all event history of the safebox, with all their properties.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public List<EventHistory> getSafeboxEventHistory(Safebox safebox)
	  throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		if(safebox.getGuid() == null) {
		  throw new SendSecureException("The Safebox guid cannot be null.");
		}
		String result = jsonClient.getSafeboxEventHistory(safebox.getGuid());
		Gson gson = new Gson();
		JsonArray historyArray = gson.fromJson(result, JsonObject.class).getAsJsonArray("event_history");
		List<EventHistory> eventHistory = gson.fromJson(historyArray, new TypeToken<ArrayList<EventHistory>>(){}.getType());

		return eventHistory;
	}

	/**
	 * Reply to a specific safebox associated to the current user's account.
	 *
	 * @param safebox
	 *            A Safebox object.
	 * @param reply
	 *           A Reply object.
	 * @return RequestResponse object containing the request result.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws SendSecureException
	 * @throws URISyntaxException
	 */
	public RequestResponse reply(Safebox safebox, Reply reply) throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		if(safebox.getGuid() == null) {
			throw new SendSecureException("The Safebox guid cannot be null.");
		}
		for(Attachment attachment : reply.getAttachments()) {
			String fileParams = safebox.getTemporaryDocumentParams(attachment.getSize());
			String newFileResponse = jsonClient.newFile(safebox.getGuid(), fileParams);
			NewFileResponse temporaryFile = new Gson().fromJson(newFileResponse, NewFileResponse.class);
			attachment = uploadReplyAttachment(temporaryFile.getUploadUrl(), attachment);
			reply.getDocumentIds().add(attachment.getGuid());
		}
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Reply.class, new ReplySerializer()).create();
		String replyResponse = jsonClient.reply(safebox.getGuid(), gson.toJson(reply));

		return toResponse(replyResponse);
	}

	/**
	 * Call to get the list of all the localized messages of a consent group.
	 *
	 * @param consentGroupId
	 *                The id of the consent group.
	 * @return The list of all the localized messages.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public ConsentMessageGroup getConsentGroupMessages(int consentGroupId) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String response = jsonClient.getConsentGroupMessages(consentGroupId);
		Gson gson = new Gson();
		JsonObject responseJson = gson.fromJson(response, JsonObject.class);

		return gson.fromJson(responseJson.get("consent_message_group"), ConsentMessageGroup.class);
	}

	/**
	 * Search the recipients for a SafeBox.
	 *
	 * @param term
	 *         A string intended to match a portion of name, email address or company.
	 * @return A SearchRecipientResponse containing the list of recipients that matches the search term.
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SendSecureException
	 */
	public SearchRecipientResponse searchRecipient(String term) throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String response = jsonClient.searchRecipient(term);
		return new Gson().fromJson(response, SearchRecipientResponse.class);
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

	private static List<NameValuePair> getUserTokenParams(String enterpriseAccount, String username, String password, String oneTimePassword,
			String deviceId, String deviceName, String applicationType) {
		List<NameValuePair> params = new ArrayList<NameValuePair>(7);
		params.add(new BasicNameValuePair("permalink", enterpriseAccount));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("otp", oneTimePassword));
		params.add(new BasicNameValuePair("application_type", StringUtils.isEmpty(applicationType) ? "SendSecure Java" : applicationType));
		params.add(new BasicNameValuePair("device_id", deviceId));
		params.add(new BasicNameValuePair("device_name", deviceName));
		return params;
	}

	private RequestResponse toResponse(String result) {
	    return new Gson().fromJson(result, RequestResponse.class);
	}
}