package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;

class IntTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return int.class;
	}

	@Override
	public Object parse(String value, Field reference)
	{
		return value.startsWith("0x")
			? Integer.parseInt(value.substring(2), 16)
			: Integer.parseInt(value);
	}
}
