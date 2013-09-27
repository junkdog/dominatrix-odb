package net.onedaybeard.dominatrix.artemis;

import com.artemis.Component;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * A factory for extracting JSON serialized components by entity type.
 * <p/>
 * The factory performs internal caching of json data.
 * @param <T>
 */
public final class JsonComponentFactory
{
	public static final String TAG = JsonComponentFactory.class.getSimpleName();
	
	private JsonValue jsonData;
	private final String componentPackage;
	private final Json json;
	
	JsonComponentFactory(JsonValue jsonData, String componentPackage)
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
	
	public Array<Component> getComponents(String entityName)
	{
		Array<Component> components = new Array<Component>();
		Array<Class<?>> componentTypes = getComponentTypes(entityName);
		for (int i = 0, s = componentTypes.size; s > i; i++)
		{
			components.add(getComponent(entityName, componentTypes.get(i)));
		}
		
		return components;
	}
	
	Array<Component> getComponents(JsonValue data) throws ClassNotFoundException
	{
		JsonValue componentData = data.get("components");
		
		Array<Component> components = new Array<Component>();
		for (JsonValue entry = componentData.child(); entry != null; entry = entry.next())
		{
			String componentName = entry.name();
			Class<?> componentClass = Class.forName(componentPackage + "." + componentName);
			
			Object value = json.fromJson(componentClass, entry.toString());
			components.add((Component)value);
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
	
	public Array<JsonValue> getArray(String path)
	{
		try
		{
			JsonValue jsonData = jsonDataForKeyPath(path, this.jsonData);
			Array<JsonValue> children = new Array<JsonValue>();
			for (JsonValue child = jsonData.child(); child != null; child = child.next())
			{
				children.add(child);
			}
			return children;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private Array<String> getKeys(String path)
	{
		JsonValue data = jsonDataForKeyPath(path, jsonData);
		Array<String> keys = new Array<String>();
		for (JsonValue key = data.child(); key != null; key = key.next())
		{
			keys.add(key.name());
		}
		
		return keys;
	}
	
	private static JsonValue jsonDataForKeyPath(String path, JsonValue jsonData)
	{
		if (path.equals(".")) return jsonData;
		
		String[] keys = path.split("\\.");
		JsonValue data = jsonData;
		for (int i = 0; i<keys.length - 1; i++)
		{
			if (data == null)
				break;
			
			data = data.getChild(keys[i]);
		}
		
		if (data == null || !data.name().equals(keys[keys.length - 1]))
			throw new RuntimeException("Unable to find " + path);

		return data;
	}
	
	
	public interface FactoryInstance
	{
		Array<Component> getComponents(String entityType);
		Array<Component> getComponents(JsonValue data);
		Array<String> getEntityTypes();
		Array<JsonValue> getArray(String path);
	}
}
