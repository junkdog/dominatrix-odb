package net.onedaybeard.artemis;

import java.util.Map;
import java.util.TreeMap;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

/**
 * Writes entities as json.
 */
public final class JsonEntityMarshaller
{
	private Json json;

	public JsonEntityMarshaller(OutputType outputType)
	{
		json = new Json();
		json.setOutputType(outputType);
		json.setTypeName(null);
	}
	
	public void setOutputType(OutputType outputType)
	{
		json.setOutputType(outputType);
	}
	
	public StringBuilder toJson(Entity e, String label)
	{
		StringBuilder sb = new StringBuilder();
		
		if (label != null)
			sb.append(label).append(':');
		
		sb.append(json.prettyPrint(new EntityRepresentation(e), 80));
		
		String data = sb.toString().replaceAll("\n", "$0\t\t");
		data = data.replaceAll("^", "$0\t");
		sb.setLength(0);
		sb.append(data);
		sb.setLength(sb.length() - 2);
		sb.append('}');
		
		return sb;
	}
	
	public StringBuilder toJson(ImmutableBag<Entity> entities)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (int i = 0, s = entities.size(); s > i; i++)
		{
			if (i != 0)
				sb.append(",\n");
			
			sb.append(toJson(entities.get(i), null));
		}
		sb.append("\n]");
		
		return sb;
	}
	
	final private static class EntityRepresentation
	{
		@SuppressWarnings("unused")
		private final Map<String,Component> components;
		
		EntityRepresentation(Entity e)
		{
			components = getComponents(e);
		}
		
		static Map<String,Component> getComponents(Entity e)
		{
			Bag<Component> bag = e.getComponents(new Bag<Component>());
			
			Bag<Component> discard = new Bag<Component>();
			for (int i = 0, s = bag.size(); s > i; i++)
			{
				if (bag.get(i).getClass().getAnnotation(ExcludeFromJson.class) != null)
					discard.add(bag.get(i));
			}
			bag.removeAll(discard);
			
			Map<String,Component> components = new TreeMap<String,Component>();
			for (int i = 0, s = bag.size(); s > i; i++)
			{
				Component component = bag.get(i);
				components.put(getComponentName(component), component);
			}
			
			return components;
		}

		private static String getComponentName(Component component)
		{
			Class<? extends Component> klazz = component.getClass();
			if (klazz.isAnnotationPresent(JsonComponentAlias.class))
				return klazz.getAnnotation(JsonComponentAlias.class).value();
			else
				return klazz.getSimpleName();
		}
	}
}
