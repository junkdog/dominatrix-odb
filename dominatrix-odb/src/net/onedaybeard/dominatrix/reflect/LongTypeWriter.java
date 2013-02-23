package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;

class LongTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return long.class;
	}

	@Override
	public Object parse(String value, Field reference)
	{
		return value.startsWith("0x")
			? Long.parseLong(value.substring(2), 16)
			: Long.parseLong(value);
	}
}
