package net.onedaybeard.dominatrix.artemis;

import java.util.HashMap;
import java.util.Map;

import net.onedaybeard.dominatrix.artemis.JsonComponentFactory.FactoryInstance;

import com.artemis.Component;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

class FileHandleCacher implements FactoryInstance
{
	private static Map<String, JsonValue> cache = new HashMap<String, JsonValue>();

	private JsonComponentFactory factory;
	private JsonValue jsonData;

	public FileHandleCacher(FileHandle file, String componentPackageName)
	{
		String path = file.path();
		if (!cache.containsKey(path))
		{
			JsonReader jsonReader = new JsonReader();
			cache.put(path, jsonReader.parse(file.readString()));
		}
		
		this.jsonData = cache.get(path);
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

	public Array<JsonValue> getArray(String path)
	{
		return factory.getArray(path);
	}

	@Override
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