package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;

import net.onedaybeard.dominatrix.reflect.FieldTypeWriter;

import com.badlogic.gdx.math.Vector2;

public class Vector2Writer implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return Vector2.class;
	}

	@Override
	public Object parse(String value, Field reference)
	{
		String[] xy = value.substring(1, value.length() - 1).split(":");
		Vector2 vector2 = new Vector2(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
		return vector2;
	}
}
