package com.xmedius.sendsecure.helper;

import java.io.File;
import java.io.InputStream;

import com.google.gson.annotations.SerializedName;

/**
 * Class Attachment builds an object to be uploaded to the server. Can be created either with a File or InputStream
 */
public class Attachment {

	@SerializedName("document_guid")
	private String guid;
	private String filename;
	@SerializedName("content_type")
	private String contentType;
	private long size;
	private InputStream stream;
	private File file;

	public Attachment(File file, String contentType) {
		this.file = file;
		this.contentType = contentType;
	}

	public Attachment(InputStream stream, String filename, String contentType, long size) {
		this.stream = stream;
		this.filename = filename;
		this.contentType = contentType;
		this.size = size;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
