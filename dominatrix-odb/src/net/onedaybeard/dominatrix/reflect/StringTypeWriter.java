package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;

class StringTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return String.class;
	}

	@Override
	public Object parse(String value, Field reference)
	{
		return value;
	}
}
