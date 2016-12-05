package com.xmedius.sendsecure.json;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.xmedius.sendsecure.helper.Safebox;

public class CommitSafeboxRequest {

	private CommitSafebox safebox;

	public CommitSafeboxRequest(Safebox safebox) {
		this.safebox = new CommitSafebox();
		this.safebox.setGuid(safebox.getGuid());
		this.safebox.setRecipients(safebox.getRecipients());
		this.safebox.setSubject(safebox.getSubject());
		this.safebox.setMessage(safebox.getMessage());
		if (CollectionUtils.isNotEmpty(safebox.getAttachments())) {
			List<String> docIds = safebox.getAttachments().stream().map(e -> e.getGuid()).collect(Collectors.toList());
			this.safebox.setDocumentIds(docIds);
		}
		this.safebox.setSecurityProfileId(safebox.getSecurityProfile().getId());
		this.safebox.setReplyEnabled(safebox.getSecurityProfile().getReplyEnabled().getValue());
		this.safebox.setGroupReplies(safebox.getSecurityProfile().getGroupReplies().getValue());
		this.safebox.setExpirationValue(safebox.getSecurityProfile().getExpirationValue().getValue());
		this.safebox.setExpirationUnit(safebox.getSecurityProfile().getExpirationUnit().getValue());
		this.safebox.setRetentionPeriodType(safebox.getSecurityProfile().getRetentionPeriodType().getValue());
		this.safebox.setRetentionPeriodValue(safebox.getSecurityProfile().getRetentionPeriodValue().getValue());
		this.safebox.setRetentionPeriodUnit(safebox.getSecurityProfile().getRetentionPeriodUnit().getValue());
		this.safebox.setEncryptMessage(safebox.getSecurityProfile().getEncryptMessage().getValue());
		this.safebox.setDoubleEncryption(safebox.getSecurityProfile().getDoubleEncryption().getValue());
		this.safebox.setPublicEncryptionKey(safebox.getPublicEncryptionKey());
		this.safebox.setNotificationLanguage(safebox.getNotificationLanguage());
	}

}
