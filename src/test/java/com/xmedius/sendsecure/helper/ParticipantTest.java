package com.xmedius.sendsecure.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xmedius.sendsecure.helper.ContactMethod.DestinationType;
import com.xmedius.sendsecure.json.serializer.ParticipantSerializer;

public class ParticipantTest {
	@Test
	public void testParticipantFromJson() {
		final String participantJson = "{ \"id\": \"23a3c8ec897548dc82f50a9a1550e52c\"," +
			     			 "\"first_name\": \"John\"," +
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
			     			 		"\"destination\": \"+5551234\"," +
			     			 		"\"destination_type\": \"cell_phone\"," +
			     			 		"\"verified\": false," +
			     			 		"\"created_at\": \"2017-05-26T19:27:27.864Z\"," +
			     			 		"\"updated_at\": \"2017-05-26T19:27:27.864Z\" }]}}";
		Participant participant = new Gson().fromJson(participantJson, Participant.class);

		assertEquals(participant.getId(), "23a3c8ec897548dc82f50a9a1550e52c");
		assertEquals(participant.getFirstName(), "John");
		assertEquals(participant.getLastName(), "Smith");
		assertEquals(participant.getEmail(), "johny.smith@example.com");
		assertEquals(participant.getType(), "guest");
		assertEquals(participant.getRole(), "guest");
		assertTrue(participant.getGuestOptions() instanceof GuestOptions);
		assertEquals(participant.getGuestOptions().getCompanyName(), "ACME");
		assertFalse(participant.getGuestOptions().isLocked());
		assertFalse(participant.getGuestOptions().isBouncedEmail());
		assertEquals(participant.getGuestOptions().getFailedLoginAttempts(), 0);
		assertFalse(participant.getGuestOptions().isVerified());
		assertEquals(participant.getGuestOptions().getContactMethods().size(), 1);

		ContactMethod contactMethod = participant.getGuestOptions().getContactMethods().get(0);
		assertEquals(contactMethod.getId().intValue(), 35105);
		assertEquals(contactMethod.getDestination(), "+5551234");
		assertEquals(contactMethod.getDestinationType(), DestinationType.CELL_PHONE);
		assertFalse(contactMethod.isVerified());
	}

	@Test
	public void testParticipantToJsonn() {
		Participant participant = new Participant("participant@example.com");
		participant.setFirstName("Test");
		participant.setLastName("User");
		GuestOptions options = new GuestOptions();
		options.setLocked(true);
		options.setCompanyName("Test enterprise");
		options.getContactMethods().add(new ContactMethod("514-555-5555", ContactMethod.DestinationType.OFFICE_PHONE));
		participant.setGuestOptions(options);
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Participant.class, new ParticipantSerializer()).create();
		String json = gson.toJson(participant);
		String expectedJson = "{\"participant\":{" +
						"\"email\":\"participant@example.com\"," +
						"\"first_name\":\"Test\"," +
						"\"last_name\":\"User\"," +
						"\"company_name\":\"Test enterprise\"," +
						"\"locked\":true," +
						"\"contact_methods\":[" +
							"{\"destination_type\":\"office_phone\"," +
							"\"destination\":\"514-555-5555\"}]}}";
		assertEquals(expectedJson, json);
	}

}
