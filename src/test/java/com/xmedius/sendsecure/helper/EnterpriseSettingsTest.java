package com.xmedius.sendsecure.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;

public class EnterpriseSettingsTest {

	@Test
	public void testEnterpriseSettingsFromJson() {
		final String enterpriseSettingsJson = "{ \"created_at\": \"2016-09-08T18:54:43.018Z\"," +
				"\"updated_at\": \"2017-03-23T16:12:09.411Z\"," +
				"\"default_security_profile_id\": 14," +
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
		Gson gson = new Gson();
		final EnterpriseSettings enterpriseSettings = gson.fromJson(enterpriseSettingsJson, EnterpriseSettings.class);

		assertEquals(enterpriseSettings.getDefaultSecurityProfileId(), 14);
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

}
