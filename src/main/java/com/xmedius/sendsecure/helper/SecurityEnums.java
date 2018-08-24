package com.xmedius.sendsecure.helper;

import java.lang.reflect.Field;
import java.lang.NoSuchFieldException;

import com.google.gson.annotations.SerializedName;

/**
 * Class SecurityEnums containing util enums
 */
public abstract class SecurityEnums {

	public enum TimeUnit
	{
		@SerializedName("hours") HOURS,
		@SerializedName("days") DAYS,
		@SerializedName("weeks") WEEKS,
		@SerializedName("months") MONTHS
	}

	public enum LongTimeUnit
	{
		@SerializedName("hours") HOURS,
		@SerializedName("days") DAYS,
		@SerializedName("weeks") WEEKS,
		@SerializedName("months") MONTHS,
		@SerializedName("years") YEARS
	}

	public enum RetentionPeriodType
	{
		@SerializedName("discard_at_expiration") DISCARD_AT_EXPIRATION,
		@SerializedName("retain_at_expiration") RETAIN_AT_EXPIRATION,
		@SerializedName("do_not_discard") DO_NOT_DISCARD
	}

	static public String getSerializedName(Enum<?> e){
		try{
			Field f = e.getClass().getField(e.name());
			SerializedName a = f.getAnnotation(SerializedName.class);
			return a.value();
		} catch (NoSuchFieldException exception){
			return null;
		}
	}

}