package net.onedaybeard.dominatrix.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Reflex
{
	private final Map<Class<?>, FieldTypeWriter> fieldParsers;
	
	public Reflex()
	{
		fieldParsers = new HashMap<Class<?>, FieldTypeWriter>();
		addParser(new BooleanTypeWriter());
		addParser(new FloatTypeWriter());
		addParser(new IntTypeWriter());
		addParser(new LongTypeWriter());
		addParser(new StringTypeWriter());
		addParser(new EnumTypeWriter());
	}
	
	public void addParser(FieldTypeWriter typeParser)
	{
		fieldParsers.put(typeParser.getType(), typeParser);
	}
	
	public boolean isEditable(Field field)
	{
		return !isTransient(field)
			&& getFieldTypeWriter(field) != null;
	}
	

	private FieldTypeWriter getFieldTypeWriter(final Field field)
	{
		FieldTypeWriter fieldTypeWriter;
		if (field.getType().isEnum())
			fieldTypeWriter = fieldParsers.get(Enum.class);
		else
			fieldTypeWriter = fieldParsers.get(field.getType());
		
		return fieldTypeWriter;
	}
	
	private static boolean isTransient(Field field)
	{
		return (field.getModifiers() & Modifier.TRANSIENT) > 0;
	}
	
	public FieldReflex on(final Object instance, final Field field)
	{
		return new FieldReflex()
		{
			@Override
			public boolean set(String value)
			{
				if (!field.isAccessible())
					field.setAccessible(true);

				boolean success = true;
				
				FieldTypeWriter fieldTypeWriter = getFieldTypeWriter(field);
				if (fieldTypeWriter == null)
					throw new RuntimeException("Missing FieldTypeWriter for " + field.getType());
				
				try
				{
					field.set(instance, fieldTypeWriter.parse(value, field));
				}
				catch (Exception e)
				{
					success = false;
				}
				
				return success;
			}
			
			@Override @SuppressWarnings("unchecked")
			public <T> T get()
			{
				if (!field.isAccessible())
					field.setAccessible(true);
				
				try
				{
					return (T)field.get(instance);
				}
				catch (IllegalArgumentException e)
				{
					throw new RuntimeException(e);
				}
				catch (IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}

			@Override
			public String getAsString()
			{
				Object object = get();
				if (object == null)
					return "";
				else if (object.getClass().isArray())
					return formatArray(object);
				else
					return object.toString();
			}
			
			private String formatArray(Object array)
			{
				if (array instanceof boolean[])
					return Arrays.toString((boolean[])array);
				else if (array instanceof byte[])
					return Arrays.toString((byte[])array);
				else if (array instanceof char[])
					return Arrays.toString((char[])array);
				else if (array instanceof int[])
					return Arrays.toString((int[])array);
				else if (array instanceof long[])
					return Arrays.toString((long[])array);
				else if (array instanceof float[])
					return Arrays.toString((float[])array);
				else if (array instanceof double[])
					return Arrays.toString((double[])array);
				else
					return Arrays.toString((Object[])array);
			}
		};
	}
	
	public FieldReflex on(final Object instance, String field)
	{
		try
		{
			return on(instance, instance.getClass().getDeclaredField(field));
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public interface FieldReflex
	{
		<T> T get();
		String getAsString();
		boolean set(String value);
	}
}
