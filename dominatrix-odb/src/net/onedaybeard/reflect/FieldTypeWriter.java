package net.onedaybeard.reflect;

public interface FieldTypeWriter
{
	Class<?> getType();
	Object parse(String value);
}
