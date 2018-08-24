package com.xmedius.sendsecure.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xmedius.sendsecure.helper.ContactMethod.DestinationType;
import com.xmedius.sendsecure.helper.Safebox.Status;
import com.xmedius.sendsecure.helper.SecurityEnums.LongTimeUnit;
import com.xmedius.sendsecure.helper.SecurityEnums.RetentionPeriodType;
import com.xmedius.sendsecure.helper.SecurityEnums.TimeUnit;
import com.xmedius.sendsecure.json.serializer.SafeboxSerializer;

public class SafeboxTest {

	final String safeboxJson = "{ \"guid\": \"b4d898ada15f42f293e31905c514607f\"," +
								"\"user_id\": 4," +
								"\"enterprise_id\": 4," +
								"\"subject\": \"Donec rutrum congue leo eget malesuada. \"," +
								"\"notification_language\": \"de\"," +
								"\"status\": \"in_progress\"," +
								"\"security_profile_name\": \"All Contact Method Allowed!\"," +
								"\"unread_count\": 0," +
								"\"double_encryption_status\": \"disabled\"," +
								"\"audit_record_pdf\": null," +
								"\"secure_link\": null," +
								"\"secure_link_title\": null," +
								"\"email_notification_enabled\": true," +
								"\"created_at\": \"2017-05-24T14:45:35.062Z\"," +
								"\"updated_at\": \"2017-05-24T14:45:35.589Z\"," +
								"\"assigned_at\": \"2017-05-24T14:45:35.040Z\"," +
								"\"latest_activity\": \"2017-05-24T14:45:35.544Z\"," +
								"\"expiration\": \"2017-05-31T14:45:35.038Z\"," +
								"\"closed_at\": null," +
								"\"content_deleted_at\": null," +
								"\"security_options\": {" +
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
									"\"allow_manual_close\": false }," +
								"\"participants\": [{" +
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
									"\"role\": \"owner\" }]," +
								"\"messages\": [" +
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
										"\"url\": \"https://sendsecure.integration.xmedius.com/api/v2/safeboxes/b4d898ada15f42f293e31905c514607f/documents/5a3df276aaa24e43af5aca9b2204a535/url\"}]}]," +
								"\"download_activity\": {" +
									"\"guests\": [{" +
									"\"id\": \"42220c777c30486e80cd3bbfa7f8e82f\"," +
									"\"documents\": [{" +
										"\"id\": \"5a3df276aaa24e43af5aca9b2204a535\"," +
										"\"downloaded_bytes\": 0," +
										"\"download_date\": null }]}]," +
									"\"owner\": {" +
									"\"id\": 72," +
									"\"documents\": [] }}," +
								"\"event_history\": [{" +
									"\"type\": \"safebox_created_owner\"," +
									"\"date\": \"2017-03-30T18:09:05.966Z\"," +
									"\"metadata\": {" +
									"\"emails\": [\"john44@example.com\"]," +
									"\"attachment_count\": 0 }," +
									"\"message\": \"SafeBox créée par john.smith123@email.com avec 0 pièce(s) jointe(s) depuis 192.168.0.1 pour john44@example.com\" }]}";
	Safebox safebox;

	@Before
		public void setUp() {
		Gson gson = new Gson();
		safebox = gson.fromJson(safeboxJson, Safebox.class);
	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testSafeboxBasicAttributes() {
		assertEquals(safebox.getGuid(), "b4d898ada15f42f293e31905c514607f");
		assertEquals(safebox.getEnterpriseId(), 4);
		assertEquals(safebox.getUserId(), 4);
		assertEquals(safebox.getSubject(), "Donec rutrum congue leo eget malesuada. ");
		assertEquals(safebox.getNotificationLanguage(), "de");
		assertEquals(safebox.getStatus(), Status.IN_PROGRESS);
		assertEquals(safebox.getSecurityProfileName(), "All Contact Method Allowed!");
		assertEquals(safebox.getUnreadCount(), 0);
		assertEquals(safebox.getDoubleEncryptionStatus(), "disabled");
		assertNull(safebox.getAuditRecordPdf());
		assertNull(safebox.getSecureLink());
		assertNull(safebox.getSecureLinkTitle());
		assertTrue(safebox.isEmailNotificationEnabled());
		assertNull(safebox.getClosedAt());
		assertNull(safebox.getContentDeletedAt());
	}

	@Test
	public void testSafeboxParticipants() {
		assertEquals(safebox.getParticipants().size(), 2);
		Participant participant = safebox.getParticipants().get(0);
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
	public void testSafeboxSecurityOptions() {
		assertTrue(safebox.getSecurityOptions() instanceof SecurityOptions);
		SecurityOptions securityOptions = safebox.getSecurityOptions();
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
	public void testSafeboxMessages() {
		assertEquals(safebox.getMessages().size(), 1);
		Message message = safebox.getMessages().get(0);
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
	public void testSafeboxDownloadActivity() {
		assertTrue(safebox.getDownloadActivity() instanceof DownloadActivity);
		DownloadActivity downloadActivity = safebox.getDownloadActivity();
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
	public void testSafeboxEventHistory() {
		assertEquals(safebox.getEventHistory().size(), 1);
		assertTrue(safebox.getEventHistory().get(0) instanceof EventHistory);
		EventHistory eventHistory = safebox.getEventHistory().get(0);
		assertEquals(eventHistory.getType(), "safebox_created_owner");
		assertTrue(eventHistory.getMetadata() instanceof Metadata);
		assertEquals(eventHistory.getMessage(), "SafeBox créée par john.smith123@email.com avec 0 pièce(s) jointe(s) depuis 192.168.0.1 pour john44@example.com");

		Metadata metadata = eventHistory.getMetadata();
		assertEquals(metadata.getEmails().size(), 1);
		assertEquals(metadata.getEmails().get(0), "john44@example.com");
		assertEquals(metadata.attachmentCount().intValue(), 0);
	}

	@Test
	public void testSerializeSafebox() {
		Safebox newSafebox = new Safebox(safebox.getUserEmail());
		newSafebox.setSubject(safebox.getSubject());
		Participant recipient = new Participant("lukeskywalker@rebels.com");
		recipient.setFirstName("Test");
		recipient.setLastName("Participant");
		ContactMethod contactMethod = new ContactMethod("555-232-5334", DestinationType.CELL_PHONE);
		recipient.addContactMethod(contactMethod);
		newSafebox.getParticipants().add(recipient);
		    newSafebox.setGuid(safebox.getGuid());
		    newSafebox.setNotificationLanguage(safebox.getNotificationLanguage());
		    newSafebox.getSecurityOptions().setRetentionPeriodType(RetentionPeriodType.DO_NOT_DISCARD);
		    newSafebox.getSecurityOptions().setRetention_periodValue(1);
		    newSafebox.getSecurityOptions().setRetentionPeriodUnit(LongTimeUnit.HOURS);
		    newSafebox.getSecurityOptions().setEncryptMessage(true);
		    newSafebox.getSecurityOptions().setReplyEnabled(true);
		    newSafebox.getSecurityOptions().setGroupReplies(false);
		String expectedJson = "{\"safebox\":{\"guid\":\"b4d898ada15f42f293e31905c514607f\","
			+ "\"subject\":\"Donec rutrum congue leo eget malesuada. \","
			+ "\"notification_language\":\"de\","
			+ "\"email_notification_enabled\":false,"
			+ "\"recipients\":[{\"email\":\"lukeskywalker@rebels.com\","
					+ "\"first_name\":\"Test\","
					+ "\"last_name\":\"Participant\","
					+ "\"contact_methods\":[{\"destination_type\":\"cell_phone\","
								+ "\"destination\":\"555-232-5334\"}]}],"
			+ "\"document_ids\":[],"
			+ "\"encrypt_message\":true,"
			+ "\"reply_enabled\":true,"
			+ "\"group_replies\":false,"
			+ "\"retention_period_type\":\"do_not_discard\","
			+ "\"retention_period_value\":1,"
			+ "\"retention_period_unit\":\"hours\"}}";
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Safebox.class, new SafeboxSerializer()).create();
		assertEquals(gson.toJson(newSafebox), expectedJson);
	}

}
