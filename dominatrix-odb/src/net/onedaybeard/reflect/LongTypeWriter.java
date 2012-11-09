package net.onedaybeard.reflect;

class LongTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return long.class;
	}

	@Override
	public Object parse(String value)
	{
		return Long.parseLong(value);
	}
}
