package net.onedaybeard.reflect;

import java.lang.reflect.Field;

class FloatTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return float.class;
	}

	@Override
	public Object parse(String value, Field reference)
	{
		return Float.parseFloat(value);
	}
}
