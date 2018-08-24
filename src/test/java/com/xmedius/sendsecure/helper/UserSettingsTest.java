package com.xmedius.sendsecure.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;

public class UserSettingsTest {

	@Test
	public void testUserSettingsFromJson() {
		final String userSettingsJson = "{\"created_at\": \"2016-08-15T21:56:45.798Z\"," +
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
		Gson gson = new Gson();
		final UserSettings userSettings = gson.fromJson(userSettingsJson, UserSettings.class);

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

}
