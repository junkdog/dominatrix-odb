package net.onedaybeard.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
	}
	
	public void addParser(FieldTypeWriter typeParser)
	{
		fieldParsers.put(typeParser.getType(), typeParser);
	}
	
	public boolean isEditable(Field field)
	{
		return !isTransient(field)
			&& fieldParsers.containsKey(field.getType());
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
				
				FieldTypeWriter fieldTypeWriter = fieldParsers.get(field.getType());
				if (fieldTypeWriter == null)
					throw new RuntimeException("Missing FieldTypeWriter for " + field.getType());
				
				try
				{
					field.set(instance, fieldTypeWriter.parse(value));
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
				return object != null ? object.toString() : "";
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
