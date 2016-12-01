package com.xmedius.sendsecure.utils;

import org.apache.commons.lang.StringUtils;

public class UrlUtils {

	//TODO: Modify it so it work with or without leading slash in endpoint
	// TODO: add endpoint here
	public static String getBasePortalUrl(String enterpriseAccount, String endpoint) {
		return getEndpoint(endpoint) + "/services/" + enterpriseAccount + "/portal/host";
	}

	public static String getBaseSendSecureUrl(String enterpriseAccount, String endpoint) {
		return getEndpoint(endpoint) + "/services/" + enterpriseAccount + "/sendsecure/server/url";
	}

	public static String getUserTokenUrl(String baseUrl) {
		return baseUrl + "api/user_token";
	}

	//TODO: change integration for prod
	private static String getEndpoint(String endpoint) {
		return StringUtils.isEmpty(endpoint) ? "https://portal.integration.xmedius.com" : endpoint;
	}
}
