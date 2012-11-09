package net.onedaybeard.reflect;

class FloatTypeWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return float.class;
	}

	@Override
	public Object parse(String value)
	{
		return Float.parseFloat(value);
	}
}
