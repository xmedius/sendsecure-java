package com.xmedius.sendsecure.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;
import com.xmedius.sendsecure.helper.SecurityEnums.RetentionPeriodType;
import com.xmedius.sendsecure.helper.SecurityEnums.TimeUnit;

public class SecurityProfileTest {

	@Test
	public void testSecutityProfileFromJson() {
		final String securityProfileJson = "{\"id\": 7," +
											"\"name\": \"Default\"," +
											"\"description\": null," +
											"\"created_at\": \"2016-08-29T14:52:26.085Z\"," +
											"\"updated_at\": \"2016-08-29T14:52:26.085Z\"," +
											"\"allowed_login_attempts\": {" +
												"\"value\": 3," +
												"\"modifiable\": false }," +
											"\"allow_remember_me\": {" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"allow_sms\": {" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"allow_voice\": {" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"allow_email\": {" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"code_time_limit\": {" +
												"\"value\": \"5\"," +
												"\"modifiable\": false }," +
											"\"code_length\":{" +
												"\"value\": 4," +
												"\"modifiable\": false }," +
											"\"auto_extend_value\":{" +
												"\"value\": 3," +
												"\"modifiable\": false }," +
											"\"auto_extend_unit\":{" +
												"\"value\": \"days\"," +
												"\"modifiable\": false }," +
											"\"two_factor_required\":{" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"encrypt_attachments\":{" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"encrypt_message\":{" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"expiration_value\":{" +
												"\"value\": 1," +
												"\"modifiable\": false }," +
											"\"expiration_unit\":{" +
												"\"value\": \"months\"," +
												"\"modifiable\": false }," +
											"\"reply_enabled\":{" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"group_replies\":{" +
												"\"value\": true," +
												"\"modifiable\": false }," +
											"\"double_encryption\":{" +
												"\"value\": true," +
												"\"modifiable\": true }," +
											"\"retention_period_type\":{" +
												"\"value\": \"do_not_discard\"," +
												"\"modifiable\": false }," +
											"\"retention_period_value\":{" +
												"\"value\": null," +
												"\"modifiable\": false }," +
											"\"retention_period_unit\":{" +
												"\"value\": null," +
												"\"modifiable\": false }}";
		Gson gson = new Gson();
		final SecurityProfile securityProfile = gson.fromJson(securityProfileJson, SecurityProfile.class);

		assertEquals(securityProfile.getId(), 7);
		assertEquals(securityProfile.getName(), "Default");
		assertNull(securityProfile.getDescription());

		assertEquals(securityProfile.getAllowedLoginAttempts().getValue().intValue(), 3);
		assertFalse(securityProfile.getAllowedLoginAttempts().isModifable());

		assertTrue(securityProfile.getAllowEmail().getValue().booleanValue());
		assertFalse(securityProfile.getAllowEmail().isModifable());

		assertTrue(securityProfile.getAllowRememberMe().getValue().booleanValue());
		assertFalse(securityProfile.getAllowRememberMe().isModifable());

		assertTrue(securityProfile.getAllowSms().getValue().booleanValue());
		assertFalse(securityProfile.getAllowSms().isModifable());

		assertTrue(securityProfile.getAllowVoice().getValue().booleanValue());
		assertFalse(securityProfile.getAllowVoice().isModifable());

		assertEquals(securityProfile.getAutoExtendUnit().getValue(), TimeUnit.DAYS);
		assertFalse(securityProfile.getAutoExtendUnit().isModifable());

		assertEquals(securityProfile.getAutoExtendValue().getValue().intValue(), 3);
		assertFalse(securityProfile.getAutoExtendValue().isModifable());

		assertEquals(securityProfile.getRetentionPeriodType().getValue(), RetentionPeriodType.DO_NOT_DISCARD);
		assertFalse(securityProfile.getRetentionPeriodType().isModifable());

		assertTrue(securityProfile.getReplyEnabled().getValue().booleanValue());
		assertFalse(securityProfile.getReplyEnabled().isModifable());

		assertNull(securityProfile.getRetentionPeriodUnit().getValue());
		assertFalse(securityProfile.getRetentionPeriodUnit().isModifable());

		assertNull(securityProfile.getRetentionPeriodValue().getValue());
		assertFalse(securityProfile.getRetentionPeriodValue().isModifable());

		assertEquals(securityProfile.getCodeTimeLimit().getValue().intValue(), 5);
		assertFalse(securityProfile.getCodeTimeLimit().isModifable());

		assertEquals(securityProfile.getCodeLength().getValue().intValue(), 4);
		assertFalse(securityProfile.getCodeLength().isModifable());

		assertTrue(securityProfile.getGroupReplies().getValue().booleanValue());
		assertFalse(securityProfile.getGroupReplies().isModifable());

		assertEquals(securityProfile.getExpirationUnit().getValue(), TimeUnit.MONTHS);
		assertFalse(securityProfile.getExpirationUnit().isModifable());

		assertEquals(securityProfile.getExpirationValue().getValue().intValue(), 1);
		assertFalse(securityProfile.getExpirationValue().isModifable());

		assertTrue(securityProfile.getEncryptAttachments().getValue().booleanValue());
		assertFalse(securityProfile.getEncryptAttachments().isModifable());

		assertTrue(securityProfile.getEncryptMessage().getValue().booleanValue());
		assertFalse(securityProfile.getEncryptMessage().isModifable());

		assertTrue(securityProfile.getDoubleEncryption().getValue().booleanValue());
		assertFalse(securityProfile.getDoubleEncryption().isModifable());
		}
}
