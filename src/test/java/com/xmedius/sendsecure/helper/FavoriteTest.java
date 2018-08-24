package com.xmedius.sendsecure.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xmedius.sendsecure.helper.ContactMethod.DestinationType;
import com.xmedius.sendsecure.json.serializer.FavoriteSerializer;


public class FavoriteTest {
	@Test
	public void tesFavoriteFromJson() {
		final String favoriteJson = "{\"id\":123," +
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
		Favorite favorite = new Gson().fromJson(favoriteJson, Favorite.class);

		assertEquals(favorite.getEmail(), "favorite@example.com");
		assertEquals(favorite.getFirstName(), "Test");
		assertEquals(favorite.getLastName(), "User");
		assertEquals(favorite.getCompanyName(), "Test enterprise");
		assertEquals(favorite.getOrderNumber(), 1);
		assertEquals(favorite.getContactMethods().size(), 1);

		ContactMethod contactMethod = favorite.getContactMethods().get(0);
		assertTrue(contactMethod instanceof ContactMethod);
		assertEquals(contactMethod.getDestinationType(), DestinationType.OFFICE_PHONE);
		assertEquals(contactMethod.getDestination(), "514-555-5555");
		assertEquals(contactMethod.getId().intValue(), 1);
		assertFalse(contactMethod.isVerified());
	}

	@Test
	public void testFavoriteToJson() {
		Favorite favorite = new Favorite("favorite@example.com");
		favorite.setFirstName("Test");
		favorite.setLastName("User");
		favorite.setCompanyName("Test enterprise");
		favorite.setOrderNumber(1);
		ContactMethod contact = new ContactMethod("514-555-5555", ContactMethod.DestinationType.OFFICE_PHONE);
		favorite.getContactMethods().add(contact);
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Favorite.class, new FavoriteSerializer()).create();
		String json = gson.toJson(favorite);
		String expectedJson = "{\"favorite\":{\"email\":\"favorite@example.com\"," +
									"\"first_name\":\"Test\"," +
									"\"last_name\":\"User\"," +
									"\"company_name\":\"Test enterprise\"," +
									"\"order_number\":1," +
									"\"contact_methods\":[" +
										"{\"destination_type\":\"office_phone\"," +
										"\"destination\":\"514-555-5555\"}]}}";
		assertEquals(expectedJson, json);
	}
}
