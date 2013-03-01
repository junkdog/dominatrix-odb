package net.onedaybeard.dominatrix.inject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

final class Injector
{
	public static final String TAG = Injector.class.getSimpleName();
	
	private final Map<Class<?>,TypeInjector> typeInjectors;
	
	Injector()
	{
		typeInjectors = new HashMap<Class<?>,TypeInjector>();
		typeInjectors.put(String.class, new InjectString());
		typeInjectors.put(boolean.class, new InjectBoolean());
		typeInjectors.put(short.class, new InjectShort());
		typeInjectors.put(byte.class, new InjectByte());
		typeInjectors.put(int.class, new InjectInt());
		typeInjectors.put(long.class, new InjectLong());
		typeInjectors.put(double.class, new InjectDouble());
		typeInjectors.put(float.class, new InjectFloat());
	}
	
	void setTypeInjector(Class<?> klazz, TypeInjector injector)
	{
		typeInjectors.put(klazz, injector);
	}
	
	void inject(InjectableProperties instance, Properties properties)
	{
		Field[] fields = instance.getClass().getDeclaredFields();
		for (int i = 0; fields.length > i; i++)
		{
			InjectProperty injectProperty = fields[i].getAnnotation(InjectProperty.class);
			if (injectProperty != null)
				inject(instance, fields[i], properties.getProperty(injectProperty.value()));
		}
	}

	private void inject(InjectableProperties instance, Field field, String data)
	{
		if (data == null)
			return;
		
		if (!field.isAccessible())
			field.setAccessible(true);
		
		TypeInjector injector = typeInjectors.get(field.getType());
		assert injector != null : "Missing injector for type " + field.getType();
		
		try
		{
			injector.inject(instance, field, data);
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
	
	private static class InjectString implements TypeInjector
	{
		@Override
		public void inject(InjectableProperties instance, Field field, String data)
			throws IllegalArgumentException, IllegalAccessException
		{
			field.set(instance, data);
		}
	}
	
	private static class InjectLong implements TypeInjector
	{
		@Override
		public void inject(InjectableProperties instance, Field field, String data)
			throws IllegalArgumentException, IllegalAccessException
		{
			field.set(instance, Long.parseLong(data));
		}
	}
	
	private static class InjectInt implements TypeInjector
	{
		@Override
		public void inject(InjectableProperties instance, Field field, String data)
			throws IllegalArgumentException, IllegalAccessException
		{
			field.set(instance, Integer.parseInt(data));
		}
	}
	
	private static class InjectDouble implements TypeInjector
	{
		@Override
		public void inject(InjectableProperties instance, Field field, String data)
			throws IllegalArgumentException, IllegalAccessException
		{
			field.set(instance, Double.parseDouble(data));
		}
	}
	
	private static class InjectFloat implements TypeInjector
	{
		@Override
		public void inject(InjectableProperties instance, Field field, String data)
			throws IllegalArgumentException, IllegalAccessException
		{
			field.set(instance, Float.parseFloat(data));
		}
	}
	
	private static class InjectBoolean implements TypeInjector
	{
		@Override
		public void inject(InjectableProperties instance, Field field, String data)
			throws IllegalArgumentException, IllegalAccessException
		{
			data = data.toLowerCase();
			boolean v = (data.equals("1") || data.startsWith("y") || data.startsWith("t"));
			
			field.set(instance, v);
		}
	}
	
	private static class InjectShort implements TypeInjector
	{
		@Override
		public void inject(InjectableProperties instance, Field field, String data)
			throws IllegalArgumentException, IllegalAccessException
		{
			field.set(instance, Short.parseShort(data));
		}
	}
	
	private static class InjectByte implements TypeInjector
	{
		@Override
		public void inject(InjectableProperties instance, Field field, String data)
			throws IllegalArgumentException, IllegalAccessException
		{
			field.set(instance, Byte.parseByte(data));
		}
	}
}
