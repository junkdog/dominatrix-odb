package net.onedaybeard.dominatrix.artemis;

import net.onedaybeard.dominatrix.artemis.JsonComponentFactory.FactoryInstance;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

final class UncachedJsonContents implements FactoryInstance
{
	private final JsonComponentFactory factory;
	private final JsonValue jsonData;
//	private final Object jsonData;

	public UncachedJsonContents(String json, String componentPackageName)
	{
		JsonReader jsonReader = new JsonReader();
		this.jsonData = jsonReader.parse(json);
		factory = new JsonComponentFactory(jsonData, componentPackageName);
	}
	
	@Override
	public Array<Component> getComponents(String entityType)
	{
		return factory.getComponents(entityType);
	}

	@Override
	public Array<String> getEntityTypes()
	{
		return factory.getEntityTypes();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Array<JsonValue> getArray(String path)
	{
		return factory.getArray(path);
	}
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public Array<ObjectMap<String, ObjectMap<?, ?>>> getArray(String path)
//	{
//		return (Array<ObjectMap<String,ObjectMap<?,?>>>)factory.getArray(path);
//	}

	@Override
//	public Array<Component> getComponents(ObjectMap<?,?> data)
	public Array<Component> getComponents(JsonValue data)
	{
		try
		{
			return factory.getComponents(data);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}