package com.xmedius.sendsecure.helper;

import com.google.gson.annotations.SerializedName;

/**
 * Class PersonnalSecureLink represent the settings of a Personal Secure Link
 */
public class PersonnalSecureLink {

    private Boolean enabled;
    private String url;
    @SerializedName("security_profile_id")
    private Integer securityProfileId;

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSecurityProfileId() {
        return securityProfileId;
    }

    public void setSecurityProfileId(Integer securityProfileId) {
        this.securityProfileId = securityProfileId;
    }

}