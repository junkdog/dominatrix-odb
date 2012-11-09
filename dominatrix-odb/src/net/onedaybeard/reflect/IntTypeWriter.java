package net.onedaybeard.reflect;

class IntTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return int.class;
	}

	@Override
	public Object parse(String value)
	{
		return Integer.parseInt(value);
	}
}
