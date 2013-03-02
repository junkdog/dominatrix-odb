package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;

import net.onedaybeard.dominatrix.reflect.FieldTypeWriter;

import com.badlogic.gdx.graphics.Color;

public class ColorWriter implements FieldTypeWriter
{
	@Override
	public Class<?> getType()
	{
		return Color.class;
	}

	@Override
	public Object parse(String value, Field reference)
	{
		long c = Long.parseLong(value, 16);
		if (value.length() == 6)
			c |= 0xff << 24;

		Color color = new Color(
			(c >> 24 & 0xff) / 255f,
			(c >> 16 & 0xff) / 255f,
			(c >> 8  & 0xff) / 255f,
			(c >> 0  & 0xff) / 255f);
		
		return color;
	}
}
