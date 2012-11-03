package net.onedaybeard.artemis;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Component;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * A factory for extracting JSON serialized components by entity type.
 * <p/>
 * The factory performs internal caching of json data.
 * @param <T>
 */
public final class JsonComponentFactory
{
	private Object jsonData;
	private final String componentPackage;
	
	private JsonComponentFactory(Object jsonData, String componentPackage)
	{		
		this.jsonData = jsonData;
		this.componentPackage = componentPackage;
	}
	
	public static FactoryInstance from(FileHandle file, String componentPackageName)
	{
		return new FileHandleCacher(file, componentPackageName);
	}
	
	private Component getComponent(String entity, Class<?> component)
	{
		Json json = new Json();
		return (Component)json.readValue(component.getSimpleName(), component,
			jsonDataForKeyPath(entity + ".components", this.jsonData));
	}
	
	private Array<Component> getComponents(String entityName)
	{
		Array<Component> components = new Array<Component>();
		Array<Class<?>> componentTypes = getComponentTypes(entityName);
		for (int i = 0, s = componentTypes.size; s > i; i++)
		{
			components.add(getComponent(entityName, componentTypes.get(i)));
		}
		
		return components;
	}
	
	private Array<Class<?>> getComponentTypes(String entityType)
	{
		Array<String> types = getKeys(entityType + ".components");
		Array<Class<?>> components = new Array<Class<?>>(false, types.size);
		
		for (int i = 0, s = types.size; s > i; i++)
		{
			try
			{
				Class<?> component = Class.forName(componentPackage + "." + types.get(i));
				components.add(component);
			}
			catch (ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}
		return components;
	}
	
	public Array<String> getEntityTypes()
	{
		return getKeys(".");
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private Array<String> getKeys(String path)
	{
		ObjectMap<String, ObjectMap> data = 
			(ObjectMap<String, ObjectMap>)jsonDataForKeyPath(path, jsonData);
		
		return data.keys().toArray();
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Object jsonDataForKeyPath(String path, Object jsonData)
	{
		String[] keys = path.split("\\.");
		ObjectMap<String, ObjectMap> data = (ObjectMap<String, ObjectMap>)jsonData;
		for (int i = 0; i<keys.length; i++)
		{
			if (data == null)
				break;
			data = (ObjectMap<String, ObjectMap>)data.get(keys[i]);
			
		}
		return data;
	}
	
	
	public interface FactoryInstance
	{
		Array<Component> getComponents(String entityType);
		Array<String> getEntityTypes();
	}
	
	private static class FileHandleCacher implements FactoryInstance
	{
		private static Map<String, Object> cache = new HashMap<String, Object>();

		private JsonComponentFactory factory;
		private Object jsonData;

		private String componentPackageName;
		
		public FileHandleCacher(FileHandle file, String componentPackageName)
		{
			this.componentPackageName = componentPackageName;
			String path = file.path();
			if (!cache.containsKey(path))
			{
				JsonReader jsonReader = new JsonReader();
				cache.put(path, jsonReader.parse(file.readString()));
			}
			
			this.jsonData = cache.get(path);
		}
		
		@Override
		public Array<Component> getComponents(String entityType)
		{
			if (factory == null)
				factory = new JsonComponentFactory(jsonData, componentPackageName);
			
			return factory.getComponents(entityType);
		}

		@Override
		public Array<String> getEntityTypes()
		{
			if (factory == null)
				factory = new JsonComponentFactory(jsonData, componentPackageName);
			
			return factory.getEntityTypes();
		}

	}
}
