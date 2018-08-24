package com.xmedius.sendsecure.helper;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

/**
 * Class GuestOptions represents the options of a Participant
 */
public class GuestOptions {

    @Expose
    @SerializedName("company_name")
    private String companyName;
    @Expose
    private Boolean locked;
    @Expose(serialize = false, deserialize = true)
    @SerializedName("bounced_email")
    private boolean bouncedEmail;
    @Expose(serialize = false, deserialize = true)
    @SerializedName("failed_login_attempts")
    private int failedLoginAttempts;
    @Expose(serialize = false, deserialize = true)
    private boolean verified;
    @Expose
    @SerializedName("contact_methods")
    private List<ContactMethod> contactMethods = new ArrayList<ContactMethod>();
    @Expose(serialize = false, deserialize = true)
    @SerializedName("created_at")
    private Date createdAt;
    @Expose(serialize = false, deserialize = true)
    @SerializedName("updated_at")
    private Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isBouncedEmail() {
        return bouncedEmail;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public List<ContactMethod> getContactMethods() {
        return contactMethods;
    }
}