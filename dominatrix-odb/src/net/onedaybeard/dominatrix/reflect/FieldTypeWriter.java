package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;

public interface FieldTypeWriter
{
	Class<?> getType();
	Object parse(String value, Field reference);
}
