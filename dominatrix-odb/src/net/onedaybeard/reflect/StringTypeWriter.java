package net.onedaybeard.reflect;

class StringTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return String.class;
	}

	@Override
	public Object parse(String value)
	{
		return value;
	}
}
