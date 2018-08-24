package com.xmedius.sendsecure;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import com.xmedius.sendsecure.exception.SendSecureException;
import com.xmedius.sendsecure.utils.HttpRequestMatcher;
import com.xmedius.sendsecure.utils.RequestWrapper;

public class JsonClientTest {

	private CloseableHttpClient httpClientMock = mock(CloseableHttpClient.class);
	private CloseableHttpResponse httpResponseMock = mock(CloseableHttpResponse.class);
	private HttpEntity entityMock = mock(HttpEntity.class);
	@Spy
	private RequestWrapper requestWrapper = new RequestWrapper();
	@Spy
	private JsonClient jsonClient = new JsonClient("USER|fe54-ew45", 123456, "Test", "https://portal.awesome", "en");

	@Before
	public void setUp() throws Exception {
		this.requestWrapper.setHttpClient(httpClientMock);
		this.jsonClient.setRequestWrapper(requestWrapper);
	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testInitializeSafebox() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"guid\":\"dc6f21e0f02c4112874f8b5653b795e4\","
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"upload_url\": \"https://upload_url\"}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/new.json?locale=en&user_email=user%40example.com")));

		assertEquals(jsonClient.initializeSafeBox("user@example.com"), expectedJson);
	}

	@Test
	public void testInitializeSafeboxError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/new.json?locale=en&user_email=invalid")));
		assertThrows(SendSecureException.class, () -> { jsonClient.initializeSafeBox("invalid"); });
	}

	@Test
	public void testGetSecurityProfiles() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson ="{	\"default\": 1," +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/security_profiles.json?locale=en&user_email=user%40example.com")));

		assertEquals(jsonClient.getSecurityProfiles("user@example.com"), expectedJson);
	}

	@Test
	public void testGetSecurityProfilesError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/security_profiles.json?locale=en&user_email=invalid")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getSecurityProfiles("invalid"); });
	}

	@Test
	public void testGetEnterpriseSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{ \"created_at\": \"2016-09-08T18:54:43.018Z\"," +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/settings.json?locale=en")));

		assertEquals(jsonClient.getEnterpriseSettings(), expectedJson);
	}

	@Test
	public void testGetEnterpriseSettingsError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/settings.json?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getEnterpriseSettings(); });
	}

	@Test
	public void testGetUserSettings() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"created_at\": \"2016-08-15T21:56:45.798Z\"," +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/settings.json?locale=en")));

		assertEquals(jsonClient.getUserSettings(), expectedJson);
	}

	@Test
	public void testGetUserSettingsError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/settings.json?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getUserSettings(); });
	}

	@Test
	public void testNewFile() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"temporary_document_guid\":\"6c58a1335a3142d6b140c06003c45d58\","
				+ "\"upload_url\":\"http://fileserver.lvh.me/xmss/DOcWbl96EVtI\"}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/uploads?locale=en")));

		assertEquals(jsonClient.newFile("123", "file params"), expectedJson);
	}

	@Test
	public void testNewFileError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, "Bad request", "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/uploads?locale=en")));
		assertThrows(SendSecureException.class, () -> { jsonClient.newFile("invalid", "{\"temporary_document\": {\"document_file_size\": \"10485\"},\"multipart\": \"false\"}"); });
	}

	@Test
	public void testReply() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"result\": true,\"message\": \"SafeBox successfully updated.\"}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/messages?locale=en")));

		assertEquals(jsonClient.reply("123",
				"{\"safebox\": {\"message\": \"a note\",\"consent\": \"true\",\"document_ids\": [\"1\", \"2\"]}}"), expectedJson);
	}

	@Test
	public void testReplyError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, "{\"result\": false}", "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/messages?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.reply("123", "invalid reply params"); });
		assertEquals("{\"result\": false}", exception.getMessage());
	}

	@Test
	public void testCommitSafebox() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{" +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes.json?locale=en")));

		String safeboxJson = "{\"safebox\":{\"guid\":\"845459484b674055bec4ddf2ba5ab60e\","
				+ "\"notification_language\":\"en\","
				+ "\"email_notification_enabled\":false,"
				+ "\"security_profile_id\":1,"
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"user_email\":\"user_email@example.com\","
				+ "\"recipients\":[{\"email\":\"participant_email@example.com\",\"contact_methods\":[]}],"
				+ "\"document_ids\":[]}}";

		assertEquals(jsonClient.commitSafebox(safeboxJson), expectedJson);
	}

	@Test
	public void testCommitSafeboxError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedError = "{\"error\":\"Some entered values are incorrect.\", \"attributes\":{\"language\":[\"cannot be blank\"]}}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedError, "Bad request");
		String safeboxJson = "{\"safebox\":{\"guid\":\"845459484b674055bec4ddf2ba5ab60e\","
				+ "\"notification_language\":\"\","
				+ "\"email_notification_enabled\":false,"
				+ "\"security_profile_id\":1,"
				+ "\"public_encryption_key\":\"RBQISzMk9KwkdBKDVw8sQK0gQe4MOTBGaM7hLdVOmJJ56nCZ8N7h2J0Zhy9rb9ax\","
				+ "\"user_email\":\"user_email@example.com\","
				+ "\"recipients\":[{\"email\":\"participant_email@example.com\",\"contact_methods\":[]}],"
				+ "\"document_ids\":[]}}";
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.commitSafebox(safeboxJson); });
		assertEquals(expectedError, exception.getMessage());
	}

	@Test
	public void testGetFavorites() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"favorites\":[" +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/favorites.json?locale=en")));

		assertEquals(jsonClient.getFavorites(), expectedJson);
	}

	@Test
	public void testGetFavoritesError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, "Bad request", "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/favorites.json?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getFavorites(); });
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
										"\"destination\":\"514-555-5555\"}]," +
									"\"always_promote\":false}}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedFavoriteJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/favorites.json?locale=en")));

		assertEquals(jsonClient.createFavorite(favoriteJson), expectedFavoriteJson);
	}

	@Test
	public void testCreateFavoriteError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"error\":\"Some entered values are incorrect.\", \"attributes\":{\"email\":[\"cannot be blank\"]}}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");

		String favoriteJson = "{\"favorite\":{\"email\":\"\"," +
				"\"first_name\":\"Test\"," +
				"\"last_name\":\"User\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"order_number\":0," +
				"\"contact_methods\":[" +
					"{\"destination_type\":\"office_phone\"," +
					"\"destination\":\"514-555-5555\"}]," +
				"\"always_promote\":false}}";

		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/favorites.json?locale=en")));

		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.createFavorite(favoriteJson); });
		assertEquals(expectedMessage, exception.getMessage());
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

		String favoriteJson = "{\"favorite\":{\"email\":\"favorite@example.com\"," +
									"\"first_name\":\"Test\"," +
									"\"last_name\":\"User\"," +
									"\"company_name\":\"Test enterprise\"," +
									"\"order_number\":0," +
									"\"contact_methods\":[" +
										"{\"destination_type\":\"office_phone\"," +
										"\"destination\":\"514-555-5555\"}]," +
									"\"always_promote\":false}}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedFavoriteJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/favorites/123.json?locale=en")));

		assertEquals(jsonClient.updateFavorite(123, favoriteJson), expectedFavoriteJson);
	}

	@Test
	public void testUpdateFavoriteError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"error\":\"Some entered values are incorrect.\", \"attributes\":{\"email\":[\"cannot be blank\"]}}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");

		String favoriteJson = "{\"favorite\":{\"email\":\"\"," +
				"\"first_name\":\"Test\"," +
				"\"last_name\":\"User\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"order_number\":0," +
				"\"contact_methods\":[" +
					"{\"destination_type\":\"office_phone\"," +
					"\"destination\":\"514-555-5555\"}]," +
				"\"always_promote\":false}}";

		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/favorites/123.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.updateFavorite(123, favoriteJson); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testDeleteFavorite() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_NO_CONTENT, "No content", "");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/users/123456/favorites/123.json?locale=en")));

		jsonClient.deleteFavorite(123);
	}

	@Test
	public void testCreateParticipant() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedParticipantJson = "{ \"id\": \"23a3c8ec897548dc82f50a9a1550e52c\"," +
    			"\"first_name\": \"John\"," +
    			"\"last_name\": \"Smith\"," +
    			"\"email\": \"johny.smith@example.com\"," +
    			"\"type\": \"guest\"," +
    			"\"role\": \"guest\"," +
    			"\"privileged\": \"true\"," +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedParticipantJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/participants.json?locale=en")));

		assertEquals(jsonClient.createParticipant("123", participantJson), expectedParticipantJson);
	}

	@Test
	public void testCreateParticipantError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"error\":\"Some entered values are incorrect.\", \"attributes\":{\"email\":[\"cannot be blank\"]}}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");

		String participantJson = "{\"participant\":{" +
				"\"email\":\"\"," +
				"\"first_name\":\"John\"," +
				"\"last_name\":\"Smith\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"locked\":false," +
				"\"contact_methods\":[" +
					"{\"destination_type\":\"cell_phone\"," +
					"\"destination\":\"514-555-5555\"}]}}";

		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/participants.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.createParticipant("123", participantJson); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testUpdateParticipant() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedParticipantJson = "{ \"id\": \"23a3c8ec897548dc82f50a9a1550e52c\"," +
    			"\"first_name\": \"John\"," +
    			"\"last_name\": \"Smith\"," +
    			"\"email\": \"johny.smith@example.com\"," +
    			"\"type\": \"guest\"," +
    			"\"role\": \"guest\"," +
    			"\"privileged\": \"true\"," +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedParticipantJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/participants/23a3c8ec897548dc82f50a9a1550e52c.json?locale=en")));

		assertEquals(jsonClient.updateParticipant("123", "23a3c8ec897548dc82f50a9a1550e52c", participantJson), expectedParticipantJson);
	}

	@Test
	public void testUpdateParticipantError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"error\":\"Some entered values are incorrect.\", \"attributes\":{\"email\":[\"cannot be blank\"]}}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");

		String participantJson = "{\"participant\":{" +
				"\"email\":\"\"," +
				"\"first_name\":\"John\"," +
				"\"last_name\":\"Smith\"," +
				"\"company_name\":\"Test enterprise\"," +
				"\"locked\":false," +
				"\"contact_methods\":[" +
					"{\"destination_type\":\"cell_phone\"," +
					"\"destination\":\"514-555-5555\"}]}}";
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/participants/0a9a1550e52c.json?locale=en")));

		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.updateParticipant("123", "0a9a1550e52c", participantJson); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testDeleteFavoriteError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/documents/12334/url.json?locale=en&user_email=user%40example.com")));
		assertThrows(SendSecureException.class, () -> { jsonClient.getFileUrl("invalid", "12334", "user@example.com"); });
	}

	@Test
	public void testGetFileUrl() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"url\":\"document_url\"}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/documents/12334/url.json?locale=en&user_email=user%40example.com")));

		assertEquals(jsonClient.getFileUrl("123", "12334", "user@example.com"), expectedJson);
	}

	@Test
	public void testGetFileUrlError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/documents/12334/url.json?locale=en&user_email=user%40example.com")));
		assertThrows(SendSecureException.class, () -> { jsonClient.getFileUrl("invalid", "12334", "user@example.com"); });
	}

	@Test
	public void testGetAuditRecordPdfUrl() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"url\":\"document_url\"}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/audit_record_pdf.json?locale=en")));

		assertEquals(jsonClient.getAuditRecordPdfUrl("123"), expectedJson);
	}

	@Test
	public void testGetAuditRecordPdfUrlError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/audit_record_pdf.json?locale=en")));
		assertThrows(SendSecureException.class, () -> { jsonClient.getAuditRecordPdfUrl("invalid"); });
	}

	@Test
	public void testGetSafeboxes() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"count\":2," +
				"\"previous_page_url\":\"previous_page_url\"," +
				"\"next_page_url\":\"next_page_url\"," +
				"\"safeboxes\":[" +
					"{\"safebox\":{" +
						"\"guid\":\"73af62f766ee459e81f46e4f533085a4\"," +
						"\"subject\":\"subject1\"}}," +
					"{\"safebox\":{" +
						"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
						"\"subject\":\"subject2\"}}]}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes.json?locale=en")));

		assertEquals(jsonClient.getSafeboxes(), expectedJson);
	}

	@Test
	public void testGetSafeboxesError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, "Bad request", "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes.json?locale=en")));
		assertThrows(SendSecureException.class, () -> { jsonClient.getSafeboxes(); });
	}

	@Test
	public void testGetSafeboxInfo() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"count\":2," +
				"\"previous_page_url\":\"previous_page_url\"," +
				"\"next_page_url\":\"next_page_url\"," +
				"\"safeboxes\":[" +
					"{\"safebox\":{" +
						"\"guid\":\"73af62f766ee459e81f46e4f533085a4\"," +
						"\"subject\":\"subject1\"}}," +
					"{\"safebox\":{" +
						"\"guid\":\"845459484b674055bec4ddf2ba5ab60e\"," +
						"\"subject\":\"subject2\"}}]}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123?locale=en")));

		assertEquals(jsonClient.getSafeboxInfo(StringUtils.EMPTY, "123"), expectedJson);
	}

	@Test
	public void testGetSafeboxInfoError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, "Bad request", "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123?locale=en")));
		assertThrows(SendSecureException.class, () -> { jsonClient.getSafeboxInfo(StringUtils.EMPTY, "123"); });
	}

	@Test
	public void testGetSafeboxParticipants() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"participants\": [{" +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/participants.json?locale=en")));

		assertEquals(jsonClient.getSafeboxParticipants("123"), expectedJson);
	}

	@Test
	public void testGetSafeboxParticipantsError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/participants.json?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getSafeboxParticipants("invalid"); });
	}

	@Test
	public void testGetSafeboxMessages() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"messages\": [" +
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
					"\"url\": \"url\"}]}]}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/messages.json?locale=en")));

		assertEquals(jsonClient.getSafeboxMessages("123"), expectedJson);
	}

	@Test
	public void testGetSafeboxMessagesError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/messages.json?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getSafeboxMessages("invalid"); });
	}

	@Test
	public void testGetSafeboxSecurityOptions() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"security_options\": {" +
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
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/security_options.json?locale=en")));

		assertEquals(jsonClient.getSafeboxSecurityOptions("123"), expectedJson);
	}

	@Test
	public void testGetSafeboxSecurityOptionsError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/security_options.json?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getSafeboxSecurityOptions("invalid"); });
	}

	@Test
	public void testGetSafeboxDownloadActivity() throws ClientProtocolException, URISyntaxException, IOException, SendSecureException {
		String expectedJson = "{\"download_activity\": {" +
				"\"guests\": [{" +
					"\"id\": \"42220c777c30486e80cd3bbfa7f8e82f\"," +
					"\"documents\": [{" +
						"\"id\": \"5a3df276aaa24e43af5aca9b2204a535\"," +
						"\"downloaded_bytes\": 0," +
						"\"download_date\": null }]}]," +
				"\"owner\": {" +
					"\"id\": 72," +
					"\"documents\": [] }}}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/download_activity.json?locale=en")));

		assertEquals(jsonClient.getSafeboxDownloadActivity("123"), expectedJson);
	}

	@Test
	public void testGetSafeboxDownloadActivityError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/download_activity.json?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getSafeboxDownloadActivity("invalid"); });
	}

	@Test
	public void testGetSafeboxEventHistory() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"event_history\": [{" +
				"\"type\": \"safebox_created_owner\"," +
				"\"date\": \"2017-03-30T18:09:05.966Z\"," +
				"\"metadata\": {" +
					"\"emails\": [\"john44@example.com\"]," +
					"\"attachment_count\": 0 },"
					+ "\"message\":\"message\"}]}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/event_history.json?locale=en")));

		assertEquals(jsonClient.getSafeboxEventHistory("123"), expectedJson);
	}

	@Test
	public void testGetSafeboxEventHistoryError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_FORBIDDEN, "Access denied", "Access denied.");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/event_history.json?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getSafeboxEventHistory("invalid"); });
	}

	@Test
	public void testGetConsentGroupMessages() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"consent_message_group\": "
				+ "{\"id\": 1,"
				+ "\"name\": \"Default\","
				+ "\"created_at\": \"2016-08-29T14:52:26.085Z\","
				+ "\"updated_at\": \"2016-08-29T14:52:26.085Z\","
				+ "\"consent_messages\": [{"
					+ "\"locale\": \"en\","
					+ "\"value\": \"Lorem ipsum\","
					+ "\"created_at\": \"2016-08-29T14:52:26.085Z\","
					+ "\"updated_at\": \"2016-08-29T14:52:26.085Z\"}]}}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/consent_message_groups/1?locale=en")));

		assertEquals(jsonClient.getConsentGroupMessages(1), expectedJson);
	}

	@Test
	public void testGetConsentGroupMessagesError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, "Bad request", "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/enterprises/Test/consent_message_groups/42?locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.getConsentGroupMessages(42); });
	}

	@Test
	public void testSearchRecipient() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"results\": [{"
				+ "\"id\": 1,"
				+ "\"type\": \"favorite\","
				+ "\"first_name\": \"John\","
				+ "\"last_name\": \"Doe\","
				+ "\"email\": \"john@xmedius.com\","
				+ "\"company_name\": \"\"}]}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/participants/autocomplete?term=John&locale=en")));

		assertEquals(jsonClient.searchRecipient("John"), expectedJson);
	}

	@Test
	public void testSearchRecipientError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, "Bad request", "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/participants/autocomplete?term=invalid&locale=en")));

		assertThrows(SendSecureException.class, () -> { jsonClient.searchRecipient("invalid"); });
	}

	@Test
	public void testAddTime() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"result\": true,\"message\": \"SafeBox duration successfully extended.\",\"new_expiration\": \"2017-05-14T18:09:05.662Z\"}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/add_time.json?locale=en")));

		assertEquals(jsonClient.addTime("123", "{\"safebox\":{\"add_time_value\": 2,\"add_time_unit\": \"weeks\"}}"), expectedJson);
	}

	@Test
	public void testAddTimeError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"message\": \"Unable to extend the SafeBox duration.\"}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/add_time.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.addTime("123", "{\"safebox\":{\"add_time_value\": 2,\"add_time_unit\": \"invalid\"}}"); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testCloseSafebox() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"result\": true,\"message\": \"SafeBox successfully closed.\"}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/close.json?locale=en")));
		assertEquals(jsonClient.closeSafebox("123"), expectedJson);
	}

	@Test
	public void testCloseSafeboxError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"result\": false, \"message\": \"Unable to close the SafeBox.\"}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/close.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.closeSafebox("invalid"); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testDeleteSafeboxContent() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"result\": true,\"message\": \"SafeBox content successfully deleted.\"}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/delete_content.json?locale=en")));
		assertEquals(jsonClient.deleteSafeboxContent("123"), expectedJson);
	}

	@Test
	public void testDeleteSafeboxContentError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"result\": false, \"message\": \"Unable to delete the SafeBox content.\"}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/delete_content.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.deleteSafeboxContent("invalid"); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testMarkAsRead() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"result\": true}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/mark_as_read.json?locale=en")));
		assertEquals(jsonClient.markAsRead("123"), expectedJson);
	}

	@Test
	public void testMarkAsReadError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"result\": false}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/mark_as_read.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.markAsRead("invalid"); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testMarkAsUnread() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"result\": true}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/mark_as_unread.json?locale=en")));
		assertEquals(jsonClient.markAsUnread("123"), expectedJson);
	}

	@Test
	public void testMarkAsUnreadError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"result\": false}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/invalid/mark_as_unread.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.markAsUnread("invalid"); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testMarkAsReadMessage() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"result\": true}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/messages/12/read.json?locale=en")));
		assertEquals(jsonClient.markAsReadMessage("123", "12"), expectedJson);
	}

	@Test
	public void testMarkAsReadMessageError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"result\": false}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/messages/invalid/read.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.markAsReadMessage("123", "invalid"); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void testMarkAsUnreadMessage() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedJson = "{\"result\": true}";
		mockSendSecureEndpoint(HttpStatus.SC_OK, "OK", expectedJson);
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/messages/12/unread.json?locale=en")));
		assertEquals(jsonClient.markAsUnreadMessage("123", "12"), expectedJson);
	}

	@Test
	public void testMarkAsUnreadMessageError() throws ClientProtocolException, IOException, URISyntaxException, SendSecureException {
		String expectedMessage = "{\"result\": false}";
		mockSendSecureEndpoint(HttpStatus.SC_BAD_REQUEST, expectedMessage, "Bad request");
		doReturn(httpResponseMock).when(httpClientMock)
		.execute(aHttpRequestWithUriMatching(new URI("https://portal.sendsecure.awesome/api/v2/safeboxes/123/messages/invalid/unread.json?locale=en")));
		Throwable exception = assertThrows(SendSecureException.class, () -> { jsonClient.markAsUnreadMessage("123", "invalid"); });
		assertEquals(expectedMessage, exception.getMessage());
	}

	private void mockSendSecureEndpoint(int status, String message, String expectedJson)
			throws UnsupportedOperationException, IOException, URISyntaxException {
		when(httpResponseMock.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK"),
				new BasicStatusLine(HttpVersion.HTTP_1_1, status, message));
		when(entityMock.getContent()).thenReturn(new ByteArrayInputStream("https://portal.sendsecure.awesome/".getBytes()),
				new ByteArrayInputStream(expectedJson.getBytes()));
		when(httpResponseMock.getEntity()).thenReturn(entityMock);
		when(httpClientMock.execute(aHttpRequestWithUriMatching(new URI("https://portal.awesome/services/Test/sendsecure/server/url"))))
		.thenReturn(httpResponseMock);
	}

	private HttpRequestBase aHttpRequestWithUriMatching(URI expected) {
		return argThat(new HttpRequestMatcher(expected));
	}

}
