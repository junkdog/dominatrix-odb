package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;

class BooleanTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return boolean.class;
	}

	@Override
	public Object parse(String value, Field reference)
	{
		return Boolean.parseBoolean(value);
	}
}
