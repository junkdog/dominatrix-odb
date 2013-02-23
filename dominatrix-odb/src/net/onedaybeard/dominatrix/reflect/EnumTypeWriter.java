package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;

public class EnumTypeWriter implements FieldTypeWriter
{

	@Override
	public Class<?> getType()
	{
		return Enum.class;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Object parse(String value, Field reference)
	{
		if (value.matches("^\\d*$"))
			return reference.getType().getEnumConstants()[Integer.parseInt(value)];
		else
			return Enum.valueOf((Class<? extends Enum>)reference.getType(), value);
	}
}
