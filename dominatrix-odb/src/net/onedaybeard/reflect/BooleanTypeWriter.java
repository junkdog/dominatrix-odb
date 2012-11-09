package net.onedaybeard.reflect;

class BooleanTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return boolean.class;
	}

	@Override
	public Object parse(String value)
	{
		return Boolean.parseBoolean(value);
	}
}
