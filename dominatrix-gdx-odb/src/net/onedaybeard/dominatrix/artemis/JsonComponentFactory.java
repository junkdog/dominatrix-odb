package net.onedaybeard.dominatrix.artemis;

import static net.onedaybeard.dominatrix.util.Logger.error;

import java.util.Iterator;

import com.artemis.Component;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * A factory for extracting JSON serialized components by entity type.
 * <p/>
 * The factory performs internal caching of json data.
 * @param <T>
 */
public final class JsonComponentFactory
{
	public static final String TAG = JsonComponentFactory.class.getSimpleName();
	
	private Object jsonData;
	private final String componentPackage;
	private final Json json;
	
	JsonComponentFactory(Object jsonData, String componentPackage)
	{		
		json = new Json();
		this.jsonData = jsonData;
		this.componentPackage = componentPackage;
	}
	
	public static FactoryInstance from(FileHandle file, String componentPackageName)
	{
		return new FileHandleCacher(file, componentPackageName);
	}
	
	public static FactoryInstance from(String json, String componentPackageName)
	{
		return new UncachedJsonContents(json, componentPackageName);
	}
	
	private Component getComponent(String entity, Class<?> component)
	{
		return (Component)json.readValue(component.getSimpleName(), component,
			jsonDataForKeyPath(entity + ".components", this.jsonData));
	}
	
	Array<Component> getComponents(String entityName)
	{
		Array<Component> components = new Array<Component>();
		Array<Class<?>> componentTypes = getComponentTypes(entityName);
		for (int i = 0, s = componentTypes.size; s > i; i++)
		{
			components.add(getComponent(entityName, componentTypes.get(i)));
		}
		
		return components;
	}
	
	@SuppressWarnings("unchecked")
	Array<Component> getComponents(ObjectMap<?,?> data) throws ClassNotFoundException
	{
		ObjectMap<String,?> componentData = (ObjectMap<String,?>)jsonDataForKeyPath("components", data);
		Iterator<String> componentIterator = componentData.keys().iterator();
		
		Array<Component> components = new Array<Component>();
		while (componentIterator.hasNext())
		{
			String componentName = componentIterator.next();
			Class<?> componentClass = Class.forName(componentPackage + "." + componentName);
			components.add((Component)json.readValue(componentName, componentClass,
				jsonDataForKeyPath("components", data)));
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
	
	public Array<?> getArray(String path)
	{
		try
		{
			Object jsonData = jsonDataForKeyPath(path, this.jsonData);
			return (Array<?>)jsonData;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
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
		Object data = jsonData;
		for (int i = 0; i<keys.length; i++)
		{
			if (data == null)
				break;
			
			if (data instanceof ObjectMap<?, ?>)
				data = ((ObjectMap<String, ObjectMap>)data).get(keys[i]);
			else
				error(TAG, "data is of type " + data.getClass());
		}
		return data;
	}
	
	
	public interface FactoryInstance
	{
		Array<Component> getComponents(String entityType);
		Array<Component> getComponents(ObjectMap<?,?> data);
		Array<String> getEntityTypes();
		Array<ObjectMap<String, ObjectMap<?, ?>>> getArray(String path);
	}
}
