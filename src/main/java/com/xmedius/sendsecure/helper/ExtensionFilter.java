package com.xmedius.sendsecure.helper;

import java.util.List;
import com.google.gson.annotations.Expose;

/**
 * Class ExtensionFilter represents the list of allow/forbid extension for an attachment
 */
public class ExtensionFilter {

	@Expose(serialize = false, deserialize = true)
	private String mode;
	@Expose(serialize = false, deserialize = true)
	private List<String> list;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
}
