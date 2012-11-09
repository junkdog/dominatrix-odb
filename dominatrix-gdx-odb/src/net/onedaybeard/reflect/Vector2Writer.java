package net.onedaybeard.reflect;

import com.badlogic.gdx.math.Vector2;

public class Vector2Writer implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return Vector2.class;
	}

	@Override
	public Object parse(String value)
	{
		return value;
	}
}
