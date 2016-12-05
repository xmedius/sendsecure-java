package com.xmedius.sendsecure.json;

public class Value<T> {

	private T value;
	private boolean modifable;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public boolean isModifable() {
		return modifable;
	}

	public void setModifable(boolean modifable) {
		this.modifable = modifable;
	}
}
