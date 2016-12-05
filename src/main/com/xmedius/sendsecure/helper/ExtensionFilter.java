package com.xmedius.sendsecure.helper;

import java.util.List;

/**
 * Class ExtensionFilter represent the list of allow/forbid extension for an attachment
 */
public class ExtensionFilter {

	private String mode;
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
