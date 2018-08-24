package com.xmedius.sendsecure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.google.gson.Gson;
import com.xmedius.sendsecure.exception.SendSecureException;
import com.xmedius.sendsecure.helper.Attachment;
import com.xmedius.sendsecure.helper.ConsentMessage;
import com.xmedius.sendsecure.helper.ConsentMessageGroup;
import com.xmedius.sendsecure.helper.ContactMethod;
import com.xmedius.sendsecure.helper.ContactMethod.DestinationType;
import com.xmedius.sendsecure.helper.DownloadActivity;
import com.xmedius.sendsecure.helper.DownloadActivityDetail;
import com.xmedius.sendsecure.helper.DownloadActivityDocument;
import com.xmedius.sendsecure.helper.EnterpriseSettings;
import com.xmedius.sendsecure.helper.EventHistory;
import com.xmedius.sendsecure.helper.ExtensionFilter;
import com.xmedius.sendsecure.helper.Favorite;
import com.xmedius.sendsecure.helper.GuestOptions;
import com.xmedius.sendsecure.helper.Message;
import com.xmedius.sendsecure.helper.MessageDocument;
import com.xmedius.sendsecure.helper.Metadata;
import com.xmedius.sendsecure.helper.Participant;
import com.xmedius.sendsecure.helper.PersonnalSecureLink;
import com.xmedius.sendsecure.helper.Reply;
import com.xmedius.sendsecure.helper.Safebox;
import com.xmedius.sendsecure.helper.Safebox.Status;
import com.xmedius.sendsecure.helper.SecurityEnums;
import com.xmedius.sendsecure.helper.SecurityEnums.LongTimeUnit;
import com.xmedius.sendsecure.helper.SecurityEnums.RetentionPeriodType;
import com.xmedius.sendsecure.helper.SecurityEnums.TimeUnit;
import com.xmedius.sendsecure.helper.SecurityOptions;
import com.xmedius.sendsecure.helper.SecurityProfile;
import com.xmedius.sendsecure.helper.UserSettings;
import com.xmedius.sendsecure.json.Recipient;
import com.xmedius.sendsecure.json.RequestResponse;
import com.xmedius.sendsecure.json.SafeboxesResponse;
import com.xmedius.sendsecure.json.SearchRecipientResponse;
import com.xmedius.sendsecure.json.Value;

public class ClientTest {

	@Mock
	private JsonClient jsonClientMock = mock(JsonClient.class);
	@Spy
	private Client client = new Client(jsonClientMock);

	final String uploadFileJson = "{\"temporary_document\": {\"document_guid\": \"65f53ec1282c454fa98439dbda134093\" }}";

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testInitializeSafebox() throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		Safebox safebox = new Safebox("user@email.com");
		String expectedJson = "{\"guid\":\"dc6f21e0f02c4112874f8b5653b795e4\","
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"upload_url\": \"https://upload_url\"}";
		when(jsonClientMock.initializeSafeBox("user@email.com")).thenReturn(expectedJson);
		safebox = client.initializeSafebox(safebox);
		assertEquals(safebox.getGuid(), "dc6f21e0f02c4112874f8b5653b795e4");
		assertEquals(safebox.getPublicEncryptionKey(), "RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax");
		assertEquals(safebox.getUploadUrl(), "https://upload_url");
	}

	@Test
	public void testInitializeSafeboxShouldThrowException() throws SendSecureException {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.initializeSafebox(null); });
		assertEquals("Safebox cannot be null", exception.getMessage());
	}

	@Test
	public void testUploadAttachmentWithStream() throws ClientProtocolException, IOException, SendSecureException {
		FileInputStream stream = mock(FileInputStream.class);
		when(jsonClientMock.uploadFile("upload_url", stream, "content_type", "file_name", 10)).thenReturn(uploadFileJson);
		Safebox safebox = new Safebox("email@example.com");
		safebox.setUploadUrl("upload_url");
		Attachment attachment = new Attachment(stream, "file_name", "content_type", 10);
		attachment = client.uploadAttachment(safebox, attachment);
		assertEquals("65f53ec1282c454fa98439dbda134093", attachment.getGuid());
	}

	@Test
	public void testUploadAttachmentWithFile() throws ClientProtocolException, IOException, SendSecureException {
		File file = mock(File.class);
		when(file.exists()).thenReturn(true);
		when(jsonClientMock.uploadFile("upload_url", file, "content_type")).thenReturn(uploadFileJson);
		Safebox safebox = new Safebox("email@example.com");
		safebox.setUploadUrl("upload_url");
		Attachment attachment = new Attachment(file, "content_type");
		attachment = client.uploadAttachment(safebox, attachment);
		assertEquals("65f53ec1282c454fa98439dbda134093", attachment.getGuid());
	}

	@Test
	public void testUploadAttachmentWithNullParameters() throws ClientProtocolException, IOException {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.uploadAttachment(null, null); });
		assertEquals("Safebox/Attachment cannot be null", exception.getMessage());
	}

	@Test
	public void testUploadAttachmentFileNotFoundException() throws ClientProtocolException, IOException, SendSecureException {
		assertThrows(FileNotFoundException.class,() -> { client.uploadAttachment(new Safebox("email@example.com"),
				new Attachment(new File("test_document.pdf"), "application/pdf")); });
	}

	@Test
	public void testUploadReplyAttachmentWithStream() throws ClientProtocolException, IOException, SendSecureException {
		FileInputStream stream = mock(FileInputStream.class);
		when(jsonClientMock.uploadFile("upload_url", stream, "content_type", "file_name", 10)).thenReturn(uploadFileJson);
		Attachment attachment = new Attachment(stream, "file_name", "content_type", 10);
		attachment = client.uploadReplyAttachment("upload_url", attachment);
		assertEquals("65f53ec1282c454fa98439dbda134093", attachment.getGuid());
	}

	@Test
	public void testUploadReplyAttachmentWithFile() throws ClientProtocolException, IOException, SendSecureException {
		File file = mock(File.class);
		when(file.exists()).thenReturn(true);
		when(jsonClientMock.uploadFile("upload_url", file, "content_type")).thenReturn(uploadFileJson);
		Attachment attachment = new Attachment(file, "content_type");
		attachment = client.uploadReplyAttachment("upload_url", attachment);
		assertEquals("65f53ec1282c454fa98439dbda134093", attachment.getGuid());
	}

	@Test
	public void testUploadReplyAttachmentWithNullParameters() {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.uploadReplyAttachment("upload_url", null); });
		assertEquals("Attachment cannot be null", exception.getMessage());
	}

	@Test
	public void testUploadReplyAttachmentFileNotFoundException() throws ClientProtocolException, IOException, SendSecureException {
		assertThrows(FileNotFoundException.class, () -> { client.uploadReplyAttachment("upload_url",
			new Attachment(new File("test_document.pdf"), "application/pdf")); });
	}

	@Test
	public void testCommitSafeboxWithNoSecurityProfileId() throws ClientProtocolException, URISyntaxException, IOException {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.commitSafebox(new Safebox("email@example.com")); });
		assertEquals("No Security Profile configured", exception.getMessage());
	}

	@Test
	public void testCommitSafeboxWithEmptyParticipants() throws ClientProtocolException, URISyntaxException, IOException {
		Safebox safebox = new Safebox("email@example.com");
		safebox.setSecurityProfileId(1);
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.commitSafebox(safebox); });
		assertEquals("Participants cannot be empty", exception.getMessage());
	}

	@Test
	public void testSubmitSafebox() throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		String initializeSafeboxJson = "{\"guid\":\"845459484b674055bec4ddf2ba5ab60e\","
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"upload_url\":\"https://upload_url\"}";
		FileInputStream stream = mock(FileInputStream.class);
		when(jsonClientMock.uploadFile("https://upload_url", stream, "content_type", "file_name", 10)).thenReturn(uploadFileJson);
		when(jsonClientMock.initializeSafeBox("user_email@example.com")).thenReturn(initializeSafeboxJson);
		String commitSafeboxJson = "{\"safebox\":{\"guid\":\"845459484b674055bec4ddf2ba5ab60e\","
				+ "\"notification_language\":\"en\","
				+ "\"email_notification_enabled\":false,"
				+ "\"security_profile_id\":1,"
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"user_email\":\"user_email@example.com\","
				+ "\"recipients\":[{\"email\":\"participant_email@example.com\",\"contact_methods\":[]}],"
				+ "\"document_ids\":[\"65f53ec1282c454fa98439dbda134093\"]}}";
		String returnedSafeboxJson = "{" +
				"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
				"\"user_id\":1," +
				"\"enterprise_id\":4," +
				"\"subject\":\"Donec rutrum congue leo eget malesuada. \"," +
				"\"expiration\":\"2017-05-31T14:42:27.258Z\"," +
				"\"notification_language\":\"fr\"," +
				"\"status\":\"in_progress\"," +
				"\"security_profile_name\":\"All Contact Method Allowed!\"," +
				"\"security_code_length\":6," +
				"\"allowed_login_attempts\":10," +
				"\"allow_remember_me\":true," +
				"\"allow_sms\":true," +
				"\"allow_voice\":true," +
				"\"allow_email\":true," +
				"\"reply_enabled\":true," +
				"\"group_replies\":true," +
				"\"code_time_limit\":5," +
				"\"encrypt_message\":true," +
				"\"two_factor_required\":true," +
				"\"auto_extend_value\":6," +
				"\"auto_extend_unit\":\"hours\"," +
				"\"retention_period_type\":\"discard_at_expiration\"," +
				"\"retention_period_value\":null," +
				"\"retention_period_unit\":\"hours\"," +
				"\"allow_manual_delete\":true," +
				"\"allow_manual_close\":true," +
				"\"email_notification_enabled\":true," +
				"\"preview_url\":\"https://preview\"," +
				"\"encryption_key\":null," +
				"\"created_at\":\"2017-05-24T14:42:27.289Z\"," +
				"\"updated_at\":\"2017-05-24T14:42:27.526Z\"," +
				"\"latest_activity\":\"2017-05-24T14:42:27.463Z\"}";
		when(jsonClientMock.commitSafebox(commitSafeboxJson)).thenReturn(returnedSafeboxJson);

		Safebox safebox = new Safebox("user_email@example.com");
		safebox.setSecurityProfileId(1);
		safebox.getAttachments().add(new Attachment(stream, "file_name", "content_type", 10));
		safebox.getParticipants().add(new Participant("participant_email@example.com"));
		safebox = client.submitSafebox(safebox);
		assertEquals("845459484b674055bec4ddf2ba5ab60e", safebox.getGuid());
		assertEquals(1, safebox.getUserId());
		assertEquals(4, safebox.getEnterpriseId());
		assertEquals(Status.IN_PROGRESS, safebox.getStatus());
		assertNotNull(safebox.getSecurityOptions());
	}

	@Test
	public void testSubmitSafeboxWithDefaultSecurityProfile() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String initializeSafeboxJson = "{\"guid\":\"845459484b674055bec4ddf2ba5ab60e\","
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"upload_url\":\"https://upload_url\"}";
		String returnedSafeboxJson = "{" +
				"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
				"\"user_id\":1," +
				"\"enterprise_id\":4," +
				"\"subject\":\"Donec rutrum congue leo eget malesuada. \"," +
				"\"expiration\":\"2017-05-31T14:42:27.258Z\"," +
				"\"notification_language\":\"fr\"," +
				"\"status\":\"in_progress\"," +
				"\"security_profile_name\":\"All Contact Method Allowed!\"," +
				"\"force_expiry_date\":null," +
				"\"security_code_length\":6," +
				"\"allowed_login_attempts\":10," +
				"\"allow_remember_me\":true," +
				"\"allow_sms\":true," +
				"\"allow_voice\":true," +
				"\"allow_email\":true," +
				"\"reply_enabled\":true," +
				"\"group_replies\":true," +
				"\"code_time_limit\":5," +
				"\"encrypt_message\":true," +
				"\"two_factor_required\":true," +
				"\"auto_extend_value\":6," +
				"\"auto_extend_unit\":\"hours\"," +
				"\"retention_period_type\":\"discard_at_expiration\"," +
				"\"retention_period_value\":null," +
				"\"retention_period_unit\":\"hours\"," +
				"\"delete_content_on\": null," +
				"\"allow_manual_delete\":true," +
				"\"allow_manual_close\":true," +
				"\"email_notification_enabled\":true," +
				"\"preview_url\":\"https://preview\"," +
				"\"encryption_key\":null," +
				"\"created_at\":\"2017-05-24T14:42:27.289Z\"," +
				"\"updated_at\":\"2017-05-24T14:42:27.526Z\"," +
				"\"latest_activity\":\"2017-05-24T14:42:27.463Z\"}";
		when(jsonClientMock.initializeSafeBox("user_email@example.com")).thenReturn(initializeSafeboxJson);
		String enterpriseSettingsJson =  "{\"default_security_profile_id\": 1 }";
		when(jsonClientMock.getEnterpriseSettings()).thenReturn(enterpriseSettingsJson);
		String commitSafeboxJson = "{\"safebox\":{\"guid\":\"845459484b674055bec4ddf2ba5ab60e\","
				+ "\"notification_language\":\"en\","
				+ "\"email_notification_enabled\":false,"
				+ "\"security_profile_id\":1,"
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"user_email\":\"user_email@example.com\","
				+ "\"recipients\":[{\"email\":\"participant_email@example.com\",\"contact_methods\":[]}],"
				+ "\"document_ids\":[]}}";
		when(jsonClientMock.commitSafebox(commitSafeboxJson)).thenReturn(returnedSafeboxJson);

		Safebox safebox = new Safebox("user_email@example.com");
		safebox.getParticipants().add(new Participant("participant_email@example.com"));

		safebox = client.submitSafebox(safebox);
		assertEquals("845459484b674055bec4ddf2ba5ab60e", safebox.getGuid());
		assertEquals(1, safebox.getUserId());
		assertEquals(4, safebox.getEnterpriseId());
		assertEquals(Status.IN_PROGRESS, safebox.getStatus());
		assertNotNull(safebox.getSecurityOptions());
	}

	@Test
	public void testSubmitSafeboxWithNoDefaultSecurityProfile() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String initializeSafeboxJson = "{\"guid\":\"845459484b674055bec4ddf2ba5ab60e\","
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"upload_url\":\"https://upload_url\"}";
		when(jsonClientMock.initializeSafeBox("user_email@example.com")).thenReturn(initializeSafeboxJson);
		String enterpriseSettingsJson = "{\"include_users_in_autocomplete\": true }";
		when(jsonClientMock.getEnterpriseSettings()).thenReturn(enterpriseSettingsJson);

		Throwable exception = assertThrows(SendSecureException.class, () -> { client.submitSafebox(new Safebox("user_email@example.com")); });
		assertEquals("No Security Profile configured", exception.getMessage());
	}

	@Test
	public void testGetSecurityProfiles() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String securityProfilesJson ="{	\"default\": 1," +
										"\"security_profiles\": [{" +
										"\"name\": \"test profile\"," +
										"\"id\": 1," +
										"\"description\": \"test description\"," +
										"\"created_at\": \"2017-04-28T17:18:30.850Z\"," +
										"\"updated_at\": \"2017-04-28T17:18:30.850Z\"," +
										"\"allowed_login_attempts\": {\"value\": 3, \"modifiable\": true }}," +
										"{\"name\": \"test profile 2\"," +
										"\"id\": 2," +
										"\"description\": \"test description 2\"," +
										"\"created_at\": \"2017-04-28T17:18:30.850Z\"," +
										"\"updated_at\": \"2017-04-28T17:18:30.850Z\"," +
										"\"allowed_login_attempts\": {\"value\": 5, \"modifiable\": false }}]}";
		when(jsonClientMock.getSecurityProfiles("email@example.com")).thenReturn(securityProfilesJson);
		List<SecurityProfile> securityProfiles = client.getSecurityProfiles("email@example.com");
		assertEquals(2, securityProfiles.size());
		assertTrue(securityProfiles.get(0).getAllowedLoginAttempts() instanceof Value);
	}

	@Test
	public void testGetEnterpriseSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String enterpriseSettingsJson = "{ \"created_at\": \"2016-09-08T18:54:43.018Z\"," +
				"\"updated_at\": \"2017-03-23T16:12:09.411Z\"," +
				"\"default_security_profile_id\": 1," +
				"\"pdf_language\": \"en\"," +
				"\"use_pdfa_audit_records\": false," +
				"\"international_dialing_plan\": \"ca\"," +
				"\"extension_filter\": {" +
					"\"mode\": \"forbid\"," +
					"\"list\": [" +
						"\"bat\"," +
						"\"bin\"]}," +
				"\"virus_scan_enabled\": false," +
				"\"max_file_size_value\": null," +
				"\"max_file_size_unit\": null," +
				"\"include_users_in_autocomplete\": true," +
				"\"include_favorites_in_autocomplete\": true," +
				"\"users_public_url\": true}";
		when(jsonClientMock.getEnterpriseSettings()).thenReturn(enterpriseSettingsJson);
		EnterpriseSettings enterpriseSettings = client.getEnterpriseSettings();
		assertEquals(enterpriseSettings.getDefaultSecurityProfileId(), 1);
		assertEquals(enterpriseSettings.getPdfLanguage(), "en");
		assertFalse(enterpriseSettings.isUsePdfaAudit_records());
		assertEquals(enterpriseSettings.getInternationalDialingPlan(), "ca");
		assertTrue(enterpriseSettings.getExtensionFilter() instanceof ExtensionFilter);
		ExtensionFilter filter = enterpriseSettings.getExtensionFilter();
		assertEquals(filter.getMode(), "forbid");
		assertEquals(filter.getList().get(0), "bat");
		assertEquals(filter.getList().get(1), "bin");
		assertFalse(enterpriseSettings.getVirusScanEnabled());
		assertNull(enterpriseSettings.getMaxFileSizeUnit());
		assertEquals(enterpriseSettings.getMaxFileSizeValue(), 0);
		assertTrue(enterpriseSettings.isIncludeUsersInAutocomplete());
		assertTrue(enterpriseSettings.isIncludeFavoritesInAutocomplete());
		assertTrue(enterpriseSettings.getUsersPublicUrl());
	}

	@Test
	public void testGetDefaultSecurityProfile() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String securityProfilesJson ="{	\"default\": 1," +
				"\"security_profiles\": [{" +
				"\"name\": \"test profile\"," +
				"\"id\": 1," +
				"\"description\": \"test description\"," +
				"\"created_at\": \"2016-09-08T18:54:43.018Z\"," +
				"\"updated_at\": \"2017-03-23T16:12:09.411Z\"," +
				"\"allowed_login_attempts\": {\"value\": 3, \"modifiable\": true }}," +
				"{\"name\": \"test profile 2\"," +
				"\"id\": 2," +
				"\"description\": \"test description 2\"," +
				"\"created_at\": \"2017-04-28T17:18:30.850Z\"," +
				"\"updated_at\": \"2017-04-28T17:18:30.850Z\"," +
				"\"allowed_login_attempts\": {\"value\": 5, \"modifiable\": false }}]}";
		String enterpriseSettingsJson = "{ \"created_at\": \"2016-09-08T18:54:43.018Z\"," +
				"\"updated_at\": \"2017-03-23T16:12:09.411Z\"," +
				"\"default_security_profile_id\": 1," +
				"\"pdf_language\": \"en\"," +
				"\"use_pdfa_audit_records\": false," +
				"\"international_dialing_plan\": \"ca\"," +
				"\"extension_filter\": {" +
					"\"mode\": \"forbid\"," +
					"\"list\": [" +
						"\"bat\"," +
						"\"bin\"]}," +
				"\"virus_scan_enabled\": false," +
				"\"max_file_size_value\": null," +
				"\"max_file_size_unit\": null," +
				"\"include_users_in_autocomplete\": true," +
				"\"include_favorites_in_autocomplete\": true," +
				"\"users_public_url\": true}";
		when(jsonClientMock.getSecurityProfiles("email@example.com")).thenReturn(securityProfilesJson);
		when(jsonClientMock.getEnterpriseSettings()).thenReturn(enterpriseSettingsJson);
		assertEquals(1, client.getDefaultSecurityProfile("email@example.com").getId());
	}

	@Test
	public void testGetNotAvailableDefaultSecurityProfile() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String securityProfilesJson ="{	\"default\": 1," +
				"\"security_profiles\": [{" +
				"\"name\": \"test profile\"," +
				"\"id\": 1," +
				"\"description\": \"test description\"," +
				"\"created_at\": \"2016-09-08T18:54:43.018Z\"," +
				"\"updated_at\": \"2017-03-23T16:12:09.411Z\"," +
				"\"allowed_login_attempts\": {\"value\": 3, \"modifiable\": true }}," +
				"{\"name\": \"test profile 2\"," +
				"\"id\": 2," +
				"\"description\": \"test description 2\"," +
				"\"created_at\": \"2017-04-28T17:18:30.850Z\"," +
				"\"updated_at\": \"2017-04-28T17:18:30.850Z\"," +
				"\"allowed_login_attempts\": {\"value\": 5, \"modifiable\": false }}]}";
		String enterpriseSettingsJson = "{ \"created_at\": \"2016-09-08T18:54:43.018Z\"," +
				"\"updated_at\": \"2017-03-23T16:12:09.411Z\"," +
				"\"default_security_profile_id\": 3," +
				"\"pdf_language\": \"en\"," +
				"\"use_pdfa_audit_records\": false," +
				"\"international_dialing_plan\": \"ca\"," +
				"\"extension_filter\": {" +
					"\"mode\": \"forbid\"," +
					"\"list\": [" +
						"\"bat\"," +
						"\"bin\"]}," +
				"\"virus_scan_enabled\": false," +
				"\"max_file_size_value\": null," +
				"\"max_file_size_unit\": null," +
				"\"include_users_in_autocomplete\": true," +
				"\"include_favorites_in_autocomplete\": true," +
				"\"users_public_url\": true}";
		when(jsonClientMock.getSecurityProfiles("email@example.com")).thenReturn(securityProfilesJson);
		when(jsonClientMock.getEnterpriseSettings()).thenReturn(enterpriseSettingsJson);
		assertNull(client.getDefaultSecurityProfile("email@example.com"));
	}

	@Test
	public void testGetUserSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String userSettingsJson = "{\"created_at\": \"2016-08-15T21:56:45.798Z\"," +
				 "\"updated_at\": \"2017-04-10T18:58:59.356Z\"," +
				 "\"mask_note\": false," +
				 "\"open_first_transaction\": false," +
				 "\"mark_as_read\": true," +
				 "\"mark_as_read_delay\": 5," +
				 "\"remember_key\": true," +
				 "\"default_filter\": \"everything\"," +
				 "\"recipient_language\": \"en\"," +
				 "\"secure_link\": {" +
				 	"\"enabled\": true," +
				 	"\"url\": \"https://sendsecure.integration.xmedius.com/r/612328d944b842c68418375ffdc87b3f\"," +
				 	"\"security_profile_id\": 13 }}";
		when(jsonClientMock.getUserSettings()).thenReturn(userSettingsJson);
		UserSettings userSettings = client.getUserSettings();
		assertFalse(userSettings.isMaskNote());
		assertFalse(userSettings.isOpenFirstTransaction());
		assertTrue(userSettings.isMarkAsRead());
		assertEquals(userSettings.getMarkAsReadDelay().intValue(), 5);
		assertTrue(userSettings.isRememberKey());
		assertEquals(userSettings.getDefaultFilter(), "everything");
		assertTrue(userSettings.getSecureLink() instanceof PersonnalSecureLink);
		assertTrue(userSettings.getSecureLink().isEnabled());
		assertEquals(userSettings.getSecureLink().getUrl(), "https://sendsecure.integration.xmedius.com/r/612328d944b842c68418375ffdc87b3f");
		assertEquals(userSettings.getSecureLink().getSecurityProfileId().intValue(), 13);
	}

	@Test
	public void testGetFavorites() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String favoritesJson = "{\"favorites\":[" +
									"{\"email\":\"favorite1@example.com\"," +
									"\"contact_methods\":[" +
										"{\"destination_type\":\"cell_phone\"," +
										"\"destination\":\"514-555-0001\"," +
										"\"id\":1}," +
										"{\"destination_type\":\"office_phone\"," +
										"\"destination\":\"514-555-0002\"," +
										"\"id\":2}]}," +
									"{\"email\":\"favorite2@example.com\"," +
									"\"contact_methods\":[" +
										"{\"destination_type\":\"home_phone\"," +
										"\"destination\":\"514-555-0003\"," +
										"\"id\":3}]}]}";
		when(jsonClientMock.getFavorites()).thenReturn(favoritesJson);
		List<Favorite> favorites = client.getFavorites();
		assertEquals(2, favorites.size());
		Favorite favorite = favorites.get(0);
		assertEquals("favorite1@example.com", favorite.getEmail());
		assertEquals(2, favorite.getContactMethods().size());

		ContactMethod contactMethod = favorite.getContactMethods().get(0);
		assertTrue(contactMethod instanceof ContactMethod);
		assertEquals(contactMethod.getDestinationType(), DestinationType.CELL_PHONE);
		assertEquals(contactMethod.getDestination(), "514-555-0001");
		assertEquals(contactMethod.getId().intValue(), 1);

		contactMethod = favorite.getContactMethods().get(1);
		assertTrue(contactMethod instanceof ContactMethod);
		assertEquals(contactMethod.getDestinationType(), DestinationType.OFFICE_PHONE);
		assertEquals(contactMethod.getDestination(), "514-555-0002");
		assertEquals(contactMethod.getId().intValue(), 2);

		favorite = favorites.get(1);
		assertEquals("favorite2@example.com", favorite.getEmail());
		assertEquals(1, favorite.getContactMethods().size());

		contactMethod = favorite.getContactMethods().get(0);
		assertTrue(contactMethod instanceof ContactMethod);
		assertEquals(contactMethod.getDestinationType(), DestinationType.HOME_PHONE);
		assertEquals(contactMethod.getDestination(), "514-555-0003");
		assertEquals(contactMethod.getId().intValue(), 3);
	}

	@Test
	public void testCreateFavorite() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedFavoriteJson = "{\"id\":123," +
				"\"email\":\"favorite@example.com\"," +
				"\"first_name\":\"Test\"," +
				"\"last_name\":\"User\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"order_number\":1," +
				"\"created_at\":\"2017-05-26T19:27:27.798Z\"," +
				"\"updated_at\":\"2017-05-24T14:45:35.062Z\"," +
				"\"contact_methods\":[" +
					"{\"id\":1," +
					"\"destination_type\":\"office_phone\"," +
					"\"destination\":\"514-555-5555\"," +
					"\"verified\":false," +
					"\"created_at\":\"2017-05-26T19:27:27.798Z\"," +
					"\"updated_at\":\"2017-05-24T14:45:35.062Z\"}]}";

		String favoriteJson = "{\"favorite\":{\"email\":\"favorite@example.com\"," +
									"\"first_name\":\"Test\"," +
									"\"last_name\":\"User\"," +
									"\"company_name\":\"Test enterprise\"," +
									"\"order_number\":0," +
									"\"contact_methods\":[" +
										"{\"destination_type\":\"office_phone\"," +
										"\"destination\":\"514-555-5555\"}]}}";

		when(jsonClientMock.createFavorite(favoriteJson)).thenReturn(expectedFavoriteJson);
		Favorite favorite = new Favorite("favorite@example.com");
		favorite.setFirstName("Test");
		favorite.setLastName("User");
		favorite.setCompanyName("Test enterprise");
		ContactMethod contact = new ContactMethod("514-555-5555", ContactMethod.DestinationType.OFFICE_PHONE);
		favorite.getContactMethods().add(contact);
		client.createFavorite(favorite);
		assertEquals(123, favorite.getId());
		assertEquals(1, favorite.getOrderNumber());
		assertEquals(1, favorite.getContactMethods().get(0).getId().intValue());
		assertFalse(favorite.getContactMethods().get(0).isVerified());
	}

	@Test
	public void testCreateFavoriteNoEmail() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.createFavorite(new Favorite(null)); });
		assertEquals("No email configured", exception.getMessage());
	}

	@Test
	public void testUpdateFavorite() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedFavoriteJson = "{\"id\":123," +
				"\"email\":\"favorite@example.com\"," +
				"\"first_name\":\"Test\"," +
				"\"last_name\":\"User\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"order_number\":1," +
				"\"created_at\":\"2017-05-26T19:27:27.798Z\"," +
				"\"updated_at\":\"2017-05-24T14:45:35.062Z\"," +
				"\"contact_methods\":[" +
					"{\"id\":1," +
					"\"destination_type\":\"office_phone\"," +
					"\"destination\":\"514-555-5555\"," +
					"\"verified\":false," +
					"\"created_at\":\"2017-05-26T19:27:27.798Z\"," +
					"\"updated_at\":\"2017-05-24T14:45:35.062Z\"}]}";

		String updatedFavoriteJson = "{\"favorite\":{\"email\":\"favorite@example.com\"," +
									"\"first_name\":\"Test\"," +
									"\"last_name\":\"User\"," +
									"\"company_name\":\"Test enterprise\"," +
									"\"order_number\":3," +
									"\"contact_methods\":[]," +
									"\"always_promote\":false}}";

		when(jsonClientMock.updateFavorite(123, updatedFavoriteJson)).thenReturn(expectedFavoriteJson);
		Favorite favorite = new Favorite("favorite@example.com");
		favorite.setFirstName("Test");
		favorite.setLastName("User");
		favorite.setCompanyName("Test enterprise");
		favorite.setOrderNumber(3);
		client.updateFavorite(favorite);
		assertEquals(3, favorite.getOrderNumber());
	}

	@Test
	public void testDeleteFavoriteContactmethods() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedFavoriteJson = "{\"id\":123," +
				"\"email\":\"favorite@example.com\"," +
				"\"first_name\":\"Test\"," +
				"\"last_name\":\"User\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"order_number\":1," +
				"\"created_at\":\"2017-05-26T19:27:27.798Z\"," +
				"\"updated_at\":\"2017-05-24T14:45:35.062Z\"," +
				"\"contact_methods\":[]}";

		String updatedFavoriteJson = "{\"favorite\":{\"email\":\"favorite@example.com\","
											+ "\"first_name\":\"Test\","
											+ "\"last_name\":\"User\","
											+ "\"company_name\":\"Test enterprise\","
											+ "\"order_number\":1,"
											+ "\"contact_methods\":[{\"destination_type\":\"office_phone\","
												+ "\"destination\":\"514-555-5555\","
												+ "\"id\":1,"
												+ "\"_destroy\":true}]}}";

		when(jsonClientMock.updateFavorite(123, updatedFavoriteJson)).thenReturn(expectedFavoriteJson);
		Favorite favorite = new Gson().fromJson("{\"id\":123," +
				"\"email\":\"favorite@example.com\"," +
				"\"first_name\":\"Test\"," +
				"\"last_name\":\"User\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"order_number\":1," +
				"\"created_at\":\"2017-05-26T19:27:27.798Z\"," +
				"\"updated_at\":\"2017-05-24T14:45:35.062Z\"," +
				"\"contact_methods\":[" +
					"{\"id\":1," +
					"\"destination_type\":\"office_phone\"," +
					"\"destination\":\"514-555-5555\"," +
					"\"verified\":false," +
					"\"created_at\":\"2017-05-26T19:27:27.798Z\"," +
					"\"updated_at\":\"2017-05-24T14:45:35.062Z\"}]}", Favorite.class);

		client.deleteFavoriteContactMethods(favorite, Arrays.asList(1));
		assertEquals(123, favorite.getId());
		assertTrue(favorite.getContactMethods().isEmpty());
	}

	@Test
	public void testDeleteFavorite() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Mockito.doNothing().when(jsonClientMock).deleteFavorite(1);
		Boolean response = client.deleteFavorite(1);
		assertTrue(response);
	}

	@Test
	public void testCreateParticipant() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedParticipantJson = "{ \"id\": \"23a3c8ec897548dc82f50a9a1550e52c\"," +
    			"\"first_name\": \"John\"," +
    			"\"last_name\": \"Smith\"," +
    			"\"email\": \"johny.smith@example.com\"," +
    			"\"type\": \"guest\"," +
    			"\"role\": \"guest\"," +
    			"\"guest_options\": {" +
    			 	"\"company_name\": \"Test enterprise\"," +
    			 	"\"locked\": false," +
    			 	"\"bounced_email\": false," +
    			 	"\"failed_login_attempts\": 0," +
    			 	"\"verified\": false," +
    			 	"\"created_at\": \"2017-05-26T19:27:27.798Z\"," +
    			 	"\"updated_at\": \"2017-05-26T19:27:27.798Z\"," +
    			 	"\"contact_methods\": [{" +
    			 		"\"id\": 35105," +
    			 		"\"destination\": \"514-555-5555\"," +
    			 		"\"destination_type\": \"cell_phone\"," +
    			 		"\"verified\": false," +
    			 		"\"created_at\": \"2017-05-26T19:27:27.864Z\"," +
    			 		"\"updated_at\": \"2017-05-26T19:27:27.864Z\" }]}}";

		String participantJson = "{\"participant\":{" +
				"\"email\":\"johny.smith@example.com\"," +
				"\"first_name\":\"John\"," +
				"\"last_name\":\"Smith\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"locked\":false," +
				"\"contact_methods\":[" +
					"{\"destination_type\":\"cell_phone\"," +
					"\"destination\":\"514-555-5555\"}]}}";
		when(jsonClientMock.createParticipant("845459484b674055bec4ddf2ba5ab60e", participantJson)).thenReturn(expectedParticipantJson);
		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");

		Participant participant = new Participant("johny.smith@example.com");
		participant.setFirstName("John");
		participant.setLastName("Smith");
		GuestOptions options = new GuestOptions();
		options.setLocked(false);
		options.setCompanyName("Test enterprise");
		options.getContactMethods().add(new ContactMethod("514-555-5555", ContactMethod.DestinationType.CELL_PHONE));
		participant.setGuestOptions(options);

		client.createParticipant(participant, safebox);
		assertEquals(1, safebox.getParticipants().size());
		participant = safebox.getParticipants().get(0);
		assertEquals("23a3c8ec897548dc82f50a9a1550e52c", participant.getId());
		assertEquals("guest", participant.getType());
		assertEquals("guest", participant.getRole());
		assertTrue(participant.getGuestOptions() instanceof GuestOptions);
		assertEquals(participant.getGuestOptions().getCompanyName(), "Test enterprise");
		assertFalse(participant.getGuestOptions().isLocked());
		assertFalse(participant.getGuestOptions().isBouncedEmail());
		assertEquals(participant.getGuestOptions().getFailedLoginAttempts(), 0);
		assertFalse(participant.getGuestOptions().isVerified());
		assertEquals(participant.getGuestOptions().getContactMethods().size(), 1);

		ContactMethod contactMethod = participant.getGuestOptions().getContactMethods().get(0);
		assertEquals(contactMethod.getId().intValue(), 35105);
		assertEquals(contactMethod.getDestination(), "514-555-5555");
		assertEquals(contactMethod.getDestinationType(), DestinationType.CELL_PHONE);
		assertFalse(contactMethod.isVerified());
	}

	@Test
	public void testCreateParticipantNoEmail() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.createParticipant(new Participant(null), null); });
		assertEquals("No email configured", exception.getMessage());
	}

	@Test
	public void testCreateParticipantNoGuid() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.createParticipant(new Participant("participant@example.com"), new Safebox("user@example.com")); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testUpdateParticipant() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedParticipantJson = "{ \"id\": \"23a3c8ec897548dc82f50a9a1550e52c\"," +
    			"\"first_name\": \"Johny\"," +
    			"\"last_name\": \"Smith\"," +
    			"\"email\": \"johny.smith@example.com\"," +
    			"\"type\": \"guest\"," +
    			"\"role\": \"guest\"," +
    			"\"guest_options\": {" +
    			 	"\"company_name\": \"ACME\"," +
    			 	"\"locked\": false," +
    			 	"\"bounced_email\": false," +
    			 	"\"failed_login_attempts\": 0," +
    			 	"\"verified\": false," +
    			 	"\"created_at\": \"2017-05-26T19:27:27.798Z\"," +
    			 	"\"updated_at\": \"2017-05-26T19:27:27.798Z\"," +
    			 	"\"contact_methods\": [{" +
    			 		"\"id\": 35105," +
    			 		"\"destination\": \"514-555-5555\"," +
    			 		"\"destination_type\": \"cell_phone\"," +
    			 		"\"verified\": false," +
    			 		"\"created_at\": \"2017-05-26T19:27:27.864Z\"," +
    			 		"\"updated_at\": \"2017-05-26T19:27:27.864Z\" }]}}";

		String participantJson = "{\"participant\":{\"email\":\"johny.smith@example.com\",\"first_name\":\"Johny\",\"last_name\":\"Smith\"}}";
		when(jsonClientMock.updateParticipant("845459484b674055bec4ddf2ba5ab60e", "23a3c8ec897548dc82f50a9a1550e52c", participantJson)).thenReturn(expectedParticipantJson);
		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");

		Participant participant = new Gson().fromJson("{\"id\":\"23a3c8ec897548dc82f50a9a1550e52c\","
				+ "\"email\":\"johny.smith@example.com\","
				+ "\"first_name\":\"Johny\","
				+ "\"last_name\":\"Smith\"}", Participant.class);

		client.updateParticipant(participant, safebox);
		assertEquals("23a3c8ec897548dc82f50a9a1550e52c", participant.getId());
		assertEquals("guest", participant.getType());
		assertEquals("guest", participant.getRole());
		assertTrue(participant.getGuestOptions() instanceof GuestOptions);
		assertEquals(participant.getGuestOptions().getCompanyName(), "ACME");
		assertFalse(participant.getGuestOptions().isLocked());
		assertFalse(participant.getGuestOptions().isBouncedEmail());
		assertEquals(participant.getGuestOptions().getFailedLoginAttempts(), 0);
		assertFalse(participant.getGuestOptions().isVerified());
		assertEquals(participant.getGuestOptions().getContactMethods().size(), 1);

		ContactMethod contactMethod = participant.getGuestOptions().getContactMethods().get(0);
		assertEquals(contactMethod.getId().intValue(), 35105);
		assertEquals(contactMethod.getDestination(), "514-555-5555");
		assertEquals(contactMethod.getDestinationType(), DestinationType.CELL_PHONE);
		assertFalse(contactMethod.isVerified());
	}

	@Test
	public void testUpdateParticipantNoId() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.updateParticipant(new Participant("participant@example.com"), null); });
		assertEquals("The participant id cannot be null.", exception.getMessage());
	}

	@Test
	public void testUpdateParticipantNoGuid() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		Participant participant = new Gson().fromJson("{\"email\":\"johny.smith@example.com\",\"id\":\"23a3c8ec897548dc82f50a9a1550e52c\"}", Participant.class);
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.updateParticipant(participant, new Safebox("user@example.com")); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testDeleteParticipantContactMethods() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedParticipantJson = "{ \"id\": \"23a3c8ec897548dc82f50a9a1550e52c\"," +
    			"\"first_name\": \"Johny\"," +
    			"\"last_name\": \"Smith\"," +
    			"\"email\": \"johny.smith@example.com\"," +
    			"\"type\": \"guest\"," +
    			"\"role\": \"guest\"," +
    			"\"guest_options\": {" +
    			 	"\"company_name\": \"ACME\"," +
    			 	"\"locked\": false," +
    			 	"\"bounced_email\": false," +
    			 	"\"failed_login_attempts\": 0," +
    			 	"\"verified\": false," +
    			 	"\"created_at\": \"2017-05-26T19:27:27.798Z\"," +
    			 	"\"updated_at\": \"2017-05-26T19:27:27.798Z\"," +
    			 	"\"contact_methods\": []}}";

		String participantJson = "{\"participant\":{"
				+ "\"email\":\"johny.smith@example.com\","
				+ "\"first_name\":\"Johny\","
				+ "\"last_name\":\"Smith\","
				+ "\"company_name\":\"ACME\","
				+ "\"locked\":false,"
				+ "\"contact_methods\":[{\"destination_type\":\"cell_phone\","
					+ "\"destination\":\"514-555-5555\","
					+ "\"id\":35105,"
					+ "\"_destroy\":true}]}}";
		when(jsonClientMock.updateParticipant("845459484b674055bec4ddf2ba5ab60e", "23a3c8ec897548dc82f50a9a1550e52c", participantJson)).thenReturn(expectedParticipantJson);
		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");

		Participant participant = new Gson().fromJson("{" +
				"\"id\": \"23a3c8ec897548dc82f50a9a1550e52c\"," +
    			"\"first_name\": \"Johny\"," +
    			"\"last_name\": \"Smith\"," +
    			"\"email\": \"johny.smith@example.com\"," +
    			"\"type\": \"guest\"," +
    			"\"role\": \"guest\"," +
    			"\"guest_options\": {" +
    			 	"\"company_name\": \"ACME\"," +
    			 	"\"locked\": false," +
    			 	"\"bounced_email\": false," +
    			 	"\"failed_login_attempts\": 0," +
    			 	"\"verified\": false," +
    			 	"\"created_at\": \"2017-05-26T19:27:27.798Z\"," +
    			 	"\"updated_at\": \"2017-05-26T19:27:27.798Z\"," +
    			 	"\"contact_methods\": [{" +
    			 		"\"id\": 35105," +
    			 		"\"destination\": \"514-555-5555\"," +
    			 		"\"destination_type\": \"cell_phone\"," +
    			 		"\"verified\": false," +
    			 		"\"created_at\": \"2017-05-26T19:27:27.864Z\"," +
    			 		"\"updated_at\": \"2017-05-26T19:27:27.864Z\" }]}}", Participant.class);

		client.deleteParticipantContactMethods(participant, safebox, Arrays.asList(35105));
		assertEquals("23a3c8ec897548dc82f50a9a1550e52c", participant.getId());
		assertTrue(participant.getGuestOptions().getContactMethods().isEmpty());
	}

	@Test
	public void testAddTime() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String addTimeJson = "{\"safebox\":{\"add_time_value\":1,\"add_time_unit\":\"hours\"}}";
		String expectedResponseJson = "{\"result\":true," +
				"\"message\":\"SafeBox duration successfully extended.\"," +
				"\"new_expiration\":\"2017-05-14T18:09:05.662Z\"}";
		when(jsonClientMock.addTime("845459484b674055bec4ddf2ba5ab60e", addTimeJson)).thenReturn(expectedResponseJson);

		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");
		RequestResponse response = client.addTime(safebox, 1, SecurityEnums.TimeUnit.HOURS);
		assertTrue(safebox.getExpiration().compareTo(Date.from(Instant.parse("2017-05-14T18:09:05.662Z"))) == 0);
		assertEquals("SafeBox duration successfully extended.", response.getMessage());
	}

	@Test
	public void testAddTimeNoGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.addTime(safebox, 0, null); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testCloseSafebox() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedResponseJson = "{\"result\":true,\"message\":\"SafeBox successfully closed.\"}";
		when(jsonClientMock.closeSafebox("845459484b674055bec4ddf2ba5ab60e")).thenReturn(expectedResponseJson);

		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");
		RequestResponse response = client.closeSafebox(safebox);
		assertEquals("SafeBox successfully closed.", response.getMessage());
	}

	@Test
	public void testCloseSafeboxNoGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.closeSafebox(safebox); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testDeleteSafeboxContent() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedResponseJson = "{\"result\":true,\"message\":\"SafeBox content successfully deleted.\"}";
		when(jsonClientMock.deleteSafeboxContent("845459484b674055bec4ddf2ba5ab60e")).thenReturn(expectedResponseJson);

		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");
		RequestResponse response = client.deleteSafeboxContent(safebox);
		assertEquals("SafeBox content successfully deleted.", response.getMessage());
	}

	@Test
	public void testDeleteSafeboxContentNoGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.deleteSafeboxContent(safebox); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testMarkAsRead() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedResponseJson = "{\"result\":true}";
		when(jsonClientMock.markAsRead("845459484b674055bec4ddf2ba5ab60e")).thenReturn(expectedResponseJson);

		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");
		RequestResponse response = client.markAsRead(safebox);
		assertTrue(response.getResult());
	}

	@Test
	public void testMarkAsReadNoGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.markAsRead(safebox); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testMarkAsUnread() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedResponseJson = "{\"result\":true}";
		when(jsonClientMock.markAsUnread("845459484b674055bec4ddf2ba5ab60e")).thenReturn(expectedResponseJson);

		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");
		RequestResponse response = client.markAsUnread(safebox);
		assertTrue(response.getResult());
	}

	@Test
	public void testMarkAsUnreadNoGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.markAsUnread(safebox); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetFileUrl() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedResponseJson =  "{\"url\":\"document_url\"}";
		when(jsonClientMock.getFileUrl("845459484b674055bec4ddf2ba5ab60e", "65f53ec1282c454fa98439dbda134093", "user@example.com")).thenReturn(expectedResponseJson);

		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");
		FileInputStream stream = mock(FileInputStream.class);
		Attachment attachment = new Attachment(stream, "file_name", "content_type", 10);
		attachment.setGuid("65f53ec1282c454fa98439dbda134093");
		String url = client.getFileUrl(safebox, attachment);
		assertTrue(url.contains("document_url"));
	}

	@Test
	public void testGetFileUrlNoSafeboxGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getFileUrl(safebox, null); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetFileUrlNoUserEmail() {
		Safebox safebox = new Safebox(null);
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getFileUrl(safebox, null); });
		assertEquals("The Safebox user email cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetAuditRecordPdfUrl() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedResponseJson = "{\"url\":\"audit_record_url\"}";
		when(jsonClientMock.getAuditRecordPdfUrl("845459484b674055bec4ddf2ba5ab60e")).thenReturn(expectedResponseJson);

		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("845459484b674055bec4ddf2ba5ab60e");
		String url = client.getAuditRecordPdfUrl(safebox);
		assertTrue(url.contains("audit_record_url"));
	}

	@Test
	public void testGetAuditRecordPdfUrlNoGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getAuditRecordPdfUrl(safebox); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetSafeboxInfo() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String safeboxJson = "{\"safebox\":{" +
									"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
									"\"user_id\":1," +
									"\"enterprise_id\":1," +
									"\"security_options\":{" +
									"\"security_code_length\":4 }," +
									"\"participants\":[" +
										"{\"id\":\"23a3c8ec897548dc82f50a9a1550e52c\"," +
										" \"role\":\"guest\"}," +
										"{\"id\":1," +
										" \"role\":\"owner\"}]," +
									"\"messages\":[" +
										"{\"note\":\"note\"," +
										" \"note_size\":4}]," +
									"\"download_activity\":{" +
										"\"guests\":[" +
										"{\"id\":\"42220c777c30486e80cd3bbfa7f8e82f\"}]," +
										"\"owner\":{" +
											"\"id\":1 }}," +
									"\"event_history\":[" +
										"{\"type\":\"safebox_created_owner\"," +
										" \"metadata\":{\"emails\":[\"email@example.com\"]}," +
										" \"message\":\"message\"}]}}";
		when(jsonClientMock.getSafeboxInfo("", "73af62f766ee459e81f46e4f533085a4")).thenReturn(safeboxJson);

		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		client.getSafeboxInfo(safebox, new ArrayList<String>());
		assertNotNull(safebox.getSecurityOptions());
		assertEquals(2, safebox.getParticipants().size());
		assertEquals(1, safebox.getMessages().size());
		assertNotNull(safebox.getDownloadActivity());
		assertNotNull(safebox.getDownloadActivity().getGuests().get(0));
		assertEquals(1, safebox.getEventHistory().size());
		assertNotNull(safebox.getEventHistory().get(0).getMetadata());
	}

	@Test
	public void testGetSafeboxInfoWithSections() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String safeboxJson = "{\"safebox\":{" +
									"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
									"\"user_id\":1," +
									"\"enterprise_id\":1," +
									"\"security_options\":{" +
									"\"security_code_length\":4 }," +
									"\"participants\":[" +
										"{\"id\":\"23a3c8ec897548dc82f50a9a1550e52c\"," +
										" \"role\":\"guest\"}," +
										"{\"id\":1," +
										" \"role\":\"owner\"}]," +
									"\"messages\":[" +
										"{\"note\":\"note\"," +
										" \"note_size\":4}]}}";
		when(jsonClientMock.getSafeboxInfo("security_options,participants,messages", "73af62f766ee459e81f46e4f533085a4")).thenReturn(safeboxJson);

		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		client.getSafeboxInfo(safebox, Arrays.asList("security_options", "participants", "messages"));
		assertNotNull(safebox.getSecurityOptions());
		assertEquals(2, safebox.getParticipants().size());
		assertEquals(1, safebox.getMessages().size());
	}

	@Test
	public void testGetSafeboxInfoNoGuid() {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getSafeboxInfo(new Safebox("participant@example.com"), null); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetSafeboxesWithSearchParams() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String safeboxesJson = "{\"count\":2," +
								"\"previous_page_url\":\"previous_page_url\"," +
								"\"next_page_url\":\"next_page_url\"," +
								"\"safeboxes\":[" +
									"{\"safebox\":{" +
										"\"guid\":\"73af62f766ee459e81f46e4f533085a4\"," +
										"\"subject\":\"subject1\"}}," +
									"{\"safebox\":{" +
										"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
										"\"subject\":\"subject2\"}}]}";
		List<NameValuePair> searchParams = new ArrayList<>();
		searchParams.add(new BasicNameValuePair("status", "in_progress"));
		searchParams.add(new BasicNameValuePair("search", "search_term"));
		searchParams.add(new BasicNameValuePair("per_page", "2"));
		searchParams.add(new BasicNameValuePair("page", "1"));
		when(jsonClientMock.getSafeboxes(searchParams)).thenReturn(safeboxesJson);

		SafeboxesResponse response = client.getSafeboxes("search_term", 1, 2, Safebox.Status.IN_PROGRESS);
		assertEquals(2, response.getSafeboxes().size());
		assertEquals("73af62f766ee459e81f46e4f533085a4", response.getSafeboxes().get(0).getGuid());
		assertEquals("previous_page_url", response.getPreviousPageUrl());
	}

	@Test
	public void testGetSafeboxesWithUrl() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String safeboxesJson = "{\"count\":2," +
				"\"previous_page_url\":\"previous_page_url\"," +
				"\"next_page_url\":\"next_page_url\"," +
				"\"safeboxes\":[" +
					"{\"safebox\":{" +
						"\"guid\":\"73af62f766ee459e81f46e4f533085a4\"," +
						"\"subject\":\"subject1\"}}," +
					"{\"safebox\":{" +
						"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
						"\"subject\":\"subject2\"}}]}";
		when(jsonClientMock.getSafeboxes("api/v2/safeboxes?status=read&search=test&per_page=20&page=2")).thenReturn(safeboxesJson);

		SafeboxesResponse response = client.getSafeboxes("api/v2/safeboxes?status=read&search=test&per_page=20&page=2");
		assertEquals(2, response.getSafeboxes().size());
		assertEquals("73af62f766ee459e81f46e4f533085a4", response.getSafeboxes().get(0).getGuid());
		assertEquals("previous_page_url", response.getPreviousPageUrl());
	}

	@Test
	public void testGetSafeboxes() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String safeboxesJson = "{\"count\":2," +
				"\"previous_page_url\":\"previous_page_url\"," +
				"\"next_page_url\":\"next_page_url\"," +
				"\"safeboxes\":[" +
					"{\"safebox\":{" +
						"\"guid\":\"73af62f766ee459e81f46e4f533085a4\"," +
						"\"subject\":\"subject1\"}}," +
					"{\"safebox\":{" +
						"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
						"\"subject\":\"subject2\"}}]}";
		when(jsonClientMock.getSafeboxes()).thenReturn(safeboxesJson);

		SafeboxesResponse response = client.getSafeboxes();
		assertEquals(2, response.getSafeboxes().size());
		assertEquals("73af62f766ee459e81f46e4f533085a4", response.getSafeboxes().get(0).getGuid());
		assertEquals("previous_page_url", response.getPreviousPageUrl());
	}

	@Test
	public void testGetSafeboxParticipants() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String participantsJson = "{\"participants\": [{" +
				"\"id\": \"7a3c51e00a004917a8f5db807180fcc5\"," +
				"\"first_name\": \"\"," +
				"\"last_name\": \"\"," +
				"\"email\": \"john.smith@example.com\"," +
				"\"type\": \"guest\"," +
				"\"role\": \"guest\"," +
				"\"guest_options\": {" +
					"\"company_name\": \"\"," +
					"\"locked\": false," +
					"\"bounced_email\": false," +
					"\"failed_login_attempts\": 0," +
					"\"verified\": false," +
					"\"contact_methods\": [{" +
						"\"id\": 35016," +
						"\"destination\": \"+15145550000\"," +
						"\"destination_type\": \"cell_phone\"," +
						"\"verified\": false," +
						"\"created_at\": \"2017-05-24T14:45:35.453Z\"," +
						"\"updated_at\": \"2017-05-24T14:45:35.453Z\" }," +
						"{\"id\": 35017," +
						"\"destination\": \"+15145551111\"," +
						"\"destination_type\": \"office_phone\"," +
						"\"verified\": false," +
						"\"created_at\": \"2017-05-24T14:45:35.537Z\"," +
						"\"updated_at\": \"2017-05-24T14:45:35.537Z\" }]}}," +
				"{\"id\": 34208," +
				"\"first_name\": \"Jane\"," +
				"\"last_name\": \"Doe\"," +
				"\"email\": \"jane.doe@example.com\"," +
				"\"type\": \"user\"," +
				"\"role\": \"owner\" }]}";
		when(jsonClientMock.getSafeboxParticipants("73af62f766ee459e81f46e4f533085a4")).thenReturn(participantsJson);
		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		List<Participant> participants = client.getSafeboxParticipants(safebox);
		assertEquals(participants.size(), 2);
		Participant participant = participants.get(0);
		assertEquals(participant.getId(), "7a3c51e00a004917a8f5db807180fcc5");
		assertEquals(participant.getEmail(), "john.smith@example.com");
		assertEquals(participant.getFirstName(), "");
		assertEquals(participant.getLastName(), "");
		assertEquals(participant.getType(), "guest");
		assertEquals(participant.getRole(), "guest");
		assertTrue(participant.getGuestOptions() instanceof GuestOptions);

		GuestOptions guestOptions = participant.getGuestOptions();
		assertEquals(guestOptions.getCompanyName(), "");
		assertEquals(guestOptions.getFailedLoginAttempts(), 0);
		assertFalse(guestOptions.isVerified());
		assertFalse(guestOptions.isLocked());
		assertFalse(guestOptions.isBouncedEmail());
		assertTrue(guestOptions.getContactMethods().get(0) instanceof ContactMethod);

		ContactMethod contactMethod = guestOptions.getContactMethods().get(0);
		assertEquals(contactMethod.getId().intValue(), 35016);
		assertEquals(contactMethod.getDestination(), "+15145550000");
		assertEquals(contactMethod.getDestinationType(), DestinationType.CELL_PHONE);
		assertFalse(contactMethod.isVerified());
	}

	@Test
	public void testGetSafeboxParticipantsNoGuid() {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getSafeboxParticipants(new Safebox("participant@example.com")); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetSafeboxMessages() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String messagesJson = "{\"messages\": [" +
				"{\"note\": \"Lorem Ipsum...\"," +
				"\"note_size\": 148," +
				"\"read\": true," +
				"\"author_id\": \"3\"," +
				"\"author_type\": \"guest\"," +
				"\"created_at\": \"2017-04-05T14:49:35.198Z\"," +
				"\"documents\": [{\"id\": \"5a3df276aaa24e43af5aca9b2204a535\"," +
					"\"name\": \"Axient-soapui-project.xml\"," +
					"\"sha\": \"724ae04430315c60ca17f4dbee775a37f5b18c91fde6eef24f77a605aee99c9c\"," +
					"\"size\": 129961," +
					"\"url\": \"https://sendsecure.integration.xmedius.com/api/v2/safeboxes/b4d898ada15f42f293e31905c514607f/documents/5a3df276aaa24e43af5aca9b2204a535/url\"}]}]}";

		when(jsonClientMock.getSafeboxMessages("73af62f766ee459e81f46e4f533085a4")).thenReturn(messagesJson);
		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		List<Message> messages = client.getSafeboxMessages(safebox);
		assertEquals(messages.size(), 1);
		Message message = messages.get(0);
		assertEquals(message.getNote(), "Lorem Ipsum...");
		assertEquals(message.getAuthorId(), "3");
		assertEquals(message.getAuthorType(), "guest");
		assertEquals(message.getNoteSize(), 148);
		assertTrue(message.isRead());
		assertEquals(message.getDocuments().size(), 1);
		assertTrue(message.getDocuments().get(0) instanceof MessageDocument);

		MessageDocument document = message.getDocuments().get(0);
		assertEquals(document.getId(), "5a3df276aaa24e43af5aca9b2204a535");
		assertEquals(document.getName(), "Axient-soapui-project.xml");
		assertEquals(document.getSha(), "724ae04430315c60ca17f4dbee775a37f5b18c91fde6eef24f77a605aee99c9c");
		assertEquals(document.getSize().intValue(), 129961);
		assertEquals(document.getUrl(), "https://sendsecure.integration.xmedius.com/api/v2/safeboxes/b4d898ada15f42f293e31905c514607f/documents/5a3df276aaa24e43af5aca9b2204a535/url");
	}

	@Test
	public void testGetSafeboxMessagesNoGuid() {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getSafeboxMessages(new Safebox("participant@example.com")); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetSafeboxSecurityOptions() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String securityOptionsJson = "{\"security_options\": {" +
				"\"security_code_length\": 4," +
				"\"allowed_login_attempts\": 3," +
			  	"\"allow_remember_me\": true," +
			  	"\"allow_sms\": true," +
			  	"\"allow_voice\": true," +
			  	"\"allow_email\": false," +
			  	"\"reply_enabled\": true," +
			  	"\"group_replies\": false," +
			  	"\"code_time_limit\": 5," +
			  	"\"encrypt_message\": true," +
			  	"\"two_factor_required\": true," +
			  	"\"auto_extend_value\": 3," +
			  	"\"auto_extend_unit\": \"days\"," +
			  	"\"retention_period_type\": \"do_not_discard\"," +
			  	"\"retention_period_value\": null," +
			  	"\"retention_period_unit\": \"hours\"," +
			  	"\"delete_content_on\": null," +
			  	"\"allow_manual_delete\": true," +
			  	"\"allow_manual_close\": false }}";
		when(jsonClientMock.getSafeboxSecurityOptions("73af62f766ee459e81f46e4f533085a4")).thenReturn(securityOptionsJson);

		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		SecurityOptions securityOptions = client.getSafeboxSecurityOptions(safebox);
		assertEquals(securityOptions.getSecurityCodeLength().intValue(), 4);
		assertEquals(securityOptions.getAllowedLoginAttempts().intValue(), 3);
		assertTrue(securityOptions.getAllowRememberMe());
		assertTrue(securityOptions.getAllowSms());
		assertTrue(securityOptions.getAllowVoice());
		assertFalse(securityOptions.getAllowEmail());
		assertTrue(securityOptions.getReplyEnabled());
		assertFalse(securityOptions.getGroupReplies());
		assertEquals(securityOptions.getCodeTimeLimit().intValue(), 5);
		assertTrue(securityOptions.getEncryptMessage());
		assertTrue(securityOptions.getTwoFactorRequired());
		assertEquals(securityOptions.getAutoExtendValue().intValue(), 3);
		assertEquals(securityOptions.getAutoExtendUnit(), TimeUnit.DAYS);
		assertEquals(securityOptions.getRetentionPeriodType(), RetentionPeriodType.DO_NOT_DISCARD);
		assertNull(securityOptions.getRetentionPeriodValue());
		assertEquals(securityOptions.getRetentionPeriodUnit(), LongTimeUnit.HOURS);
		assertTrue(securityOptions.getAllowManualDelete());
		assertFalse(securityOptions.getAllowManualClose());
	}

	@Test
	public void testGetSafeboxSecurityOptionsNoGuid() {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getSafeboxSecurityOptions(new Safebox("participant@example.com")); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetSafeboxDownloadActivity() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String downloadActivityJson = "{\"download_activity\": {" +
												"\"guests\": [{" +
													"\"id\": \"42220c777c30486e80cd3bbfa7f8e82f\"," +
													"\"documents\": [{" +
														"\"id\": \"5a3df276aaa24e43af5aca9b2204a535\"," +
														"\"downloaded_bytes\": 0," +
														"\"download_date\": null }]}]," +
												"\"owner\": {" +
													"\"id\": 72," +
													"\"documents\": [] }}}";
		when(jsonClientMock.getSafeboxDownloadActivity("73af62f766ee459e81f46e4f533085a4")).thenReturn(downloadActivityJson);

		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		DownloadActivity downloadActivity = client.getSafeboxDownloadActivity(safebox);
		assertEquals(downloadActivity.getGuests().size(), 1);
		assertTrue(downloadActivity.getGuests().get(0) instanceof DownloadActivityDetail);

		DownloadActivityDetail guest = downloadActivity.getGuests().get(0);
		assertEquals(guest.getId(), "42220c777c30486e80cd3bbfa7f8e82f");
		assertEquals(guest.getDocuments().size(), 1);
		assertTrue(guest.getDocuments().get(0) instanceof DownloadActivityDocument);

		DownloadActivityDocument document = guest.getDocuments().get(0);
		assertEquals(document.getId(), "5a3df276aaa24e43af5aca9b2204a535");
		assertEquals(document.getDownloadedBytes(), 0);
		assertNull(document.getDownloadDate());

		assertTrue(downloadActivity.getOwner() instanceof DownloadActivityDetail);
		assertEquals(downloadActivity.getOwner().getId(), "72");
		assertTrue(downloadActivity.getOwner().getDocuments().isEmpty());

	}

	@Test
	public void testGetSafeboxDownloadActivityNoGuid() {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getSafeboxDownloadActivity(new Safebox("participant@example.com")); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testGetSafeboxEventHistory() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String eventHistoryJson = "{\"event_history\": [{" +
									"\"type\": \"safebox_created_owner\"," +
									"\"date\": \"2017-03-30T18:09:05.966Z\"," +
									"\"metadata\": {" +
										"\"emails\": [\"john44@example.com\"]," +
										"\"attachment_count\": 0 },"
										+ "\"message\":\"SafeBox cr��e par john48@example.com avec 0 pi�ce(s) jointe(s) depuis 192.168.0.1 pour john44@example.com\"}]}";
		when(jsonClientMock.getSafeboxEventHistory("73af62f766ee459e81f46e4f533085a4")).thenReturn(eventHistoryJson);

		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		List<EventHistory> eventHistoryList = client.getSafeboxEventHistory(safebox);
		assertEquals(eventHistoryList.size(), 1);
		assertTrue(eventHistoryList.get(0) instanceof EventHistory);
		EventHistory eventHistory = eventHistoryList.get(0);
		assertEquals(eventHistory.getType(), "safebox_created_owner");
		assertTrue(eventHistory.getMetadata() instanceof Metadata);
		assertEquals(eventHistory.getMessage(), "SafeBox cr��e par john48@example.com avec 0 pi�ce(s) jointe(s) depuis 192.168.0.1 pour john44@example.com");

		Metadata metadata = eventHistory.getMetadata();
		assertEquals(metadata.getEmails().size(), 1);
		assertEquals(metadata.getEmails().get(0), "john44@example.com");
		assertEquals(metadata.attachmentCount().intValue(), 0);
	}

	@Test
	public void testGetSafeboxEventHistoryNoGuid() {
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.getSafeboxEventHistory(new Safebox("participant@example.com")); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testMarkAsReadMessage() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		when(jsonClientMock.markAsReadMessage("73af62f766ee459e81f46e4f533085a4", "123")).thenReturn("{\"result\": true}");
		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		Message message = new Gson().fromJson("{\"id\":123}", Message.class);
		RequestResponse response = client.markAsReadMessage(safebox, message);
		assertTrue(response.getResult());
	}

	@Test
	public void testMarkAsReadMessageNoGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Message message = new Gson().fromJson("{\"id\":123}", Message.class);
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.markAsReadMessage(safebox, message); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testMarkAsReadMessageNoMessageId() {
		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		Message message = new Message();
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.markAsReadMessage(safebox, message); });
		assertEquals("The Message Id cannot be null.", exception.getMessage());
	}

	@Test
	public void testMarkAsUnreadMessage() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		when(jsonClientMock.markAsUnreadMessage("73af62f766ee459e81f46e4f533085a4", "123")).thenReturn("{\"result\": true}");
		Safebox safebox = new Safebox("user@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		Message message = new Gson().fromJson("{\"id\":123}", Message.class);
		RequestResponse response = client.markAsUnreadMessage(safebox, message);
		assertTrue(response.getResult());
	}

	@Test
	public void testMarkAsUnreadMessageNoGuid() {
		Safebox safebox = new Safebox("email@example.com");
		Message message = new Gson().fromJson("{\"id\":123}", Message.class);
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.markAsUnreadMessage(safebox, message); });
		assertEquals("The Safebox guid cannot be null.", exception.getMessage());
	}

	@Test
	public void testMarkAsUnreadMessageNoMessageId() {
		Safebox safebox = new Safebox("email@example.com");
		safebox.setGuid("73af62f766ee459e81f46e4f533085a4");
		Message message = new Message();
		Throwable exception = assertThrows(SendSecureException.class, () -> { client.markAsUnreadMessage(safebox, message); });
		assertEquals("The Message Id cannot be null.", exception.getMessage());
	}

	@Test
	public void testReply() throws ClientProtocolException, IOException, SendSecureException, URISyntaxException {
		FileInputStream stream = mock(FileInputStream.class);
		Safebox safebox = new Safebox("user@email.com");
		safebox.setGuid("0e4ac5a4d0994gda567aerd6d44c3a9d");
		Reply reply = new Reply("Reply message");
		reply.setConsent(true);
		Attachment attachment = new Attachment(stream, "file_name", "content_type", 10);
		reply.getAttachments().add(attachment);
		when(jsonClientMock.newFile("0e4ac5a4d0994gda567aerd6d44c3a9d", safebox.getTemporaryDocumentParams(attachment.getSize()))).thenReturn("{"
				+ "\"temporary_document_guid\":\"6c58a1335a3142d6b140c06003c45d58\","
				+ "\"upload_url\":\"http://fileserver.lvh.me/xmss/DOcWbl96EVtI\"}");
		when(jsonClientMock.uploadFile("http://fileserver.lvh.me/xmss/DOcWbl96EVtI", stream, "content_type", "file_name", 10)).thenReturn(uploadFileJson);
		when(jsonClientMock.reply("0e4ac5a4d0994gda567aerd6d44c3a9d", "{\"safebox\":{"
				+ "\"message\":\"Reply message\","
				+ "\"consent\":true,"
				+ "\"document_ids\":[\"65f53ec1282c454fa98439dbda134093\"]}}"))
		.thenReturn("{\"result\": true,\"message\": \"SafeBox successfully updated.\"}");
		RequestResponse response = client.reply(safebox, reply);
		assertTrue(response.getResult());
		assertEquals(response.getMessage(), "SafeBox successfully updated.");
	}

	@Test
	public void testGetConsentGroupMessages() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		when(jsonClientMock.getConsentGroupMessages(1)).thenReturn("{\"consent_message_group\": "
				+ "{\"id\": 1,"
				+ "\"name\": \"Default\","
				+ "\"created_at\": \"2016-08-29T14:52:26.085Z\","
				+ "\"updated_at\": \"2016-08-29T14:52:26.085Z\","
				+ "\"consent_messages\": [{"
					+ "\"locale\": \"en\","
					+ "\"value\": \"Lorem ipsum\","
					+ "\"created_at\": \"2016-08-29T14:52:26.085Z\","
					+ "\"updated_at\": \"2016-08-29T14:52:26.085Z\"}]}}");
		ConsentMessageGroup group = client.getConsentGroupMessages(1);
		assertEquals(1, group.getId());
		assertEquals("Default", group.getName());
		assertEquals(1, group.getConsentMessages().size());
		assertTrue(group.getConsentMessages().get(0) instanceof ConsentMessage);
		ConsentMessage message = group.getConsentMessages().get(0);
		assertEquals("en", message.getLocale());
		assertEquals("Lorem ipsum", message.getValue());
	}

	@Test
	public void testSearchRecipient() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		when(jsonClientMock.searchRecipient("John")).thenReturn("{\"results\": [{"
				+ "\"id\": 1,"
				+ "\"type\": \"favorite\","
				+ "\"first_name\": \"John\","
				+ "\"last_name\": \"Doe\","
				+ "\"email\": \"john@xmedius.com\","
				+ "\"company_name\": \"\"}]}");
		SearchRecipientResponse response = client.searchRecipient("John");
		assertEquals(1, response.getResults().size());
		Recipient recipient = response.getResults().get(0);
		assertEquals(1, recipient.getId());
		assertEquals("favorite", recipient.getType());
		assertEquals("John", recipient.getFirstName());
		assertEquals("Doe", recipient.getLastName());
		assertEquals("john@xmedius.com", recipient.getEmail());
		assertEquals("", recipient.getCompanyName());
	}

}
