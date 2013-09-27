package net.onedaybeard.dominatrix.artemis;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * Writes entities as json.
 */
public final class JsonEntitySerializer
{
	private Json json;

	public JsonEntitySerializer(OutputType outputType)
	{
		json = new Json();
		json.setOutputType(outputType);
		json.setTypeName("class");
	}
	
	public void setOutputType(OutputType outputType)
	{
		json.setOutputType(outputType);
	}
	
	public CharSequence toJson(Entity e, String label)
	{
		StringBuilder sb = new StringBuilder();
		
		if (label != null)
			sb.append(label).append(':');
		
		sb.append(json.prettyPrint(json.toJson(new EntityRepresentation(e), EntityRepresentation.class), 115));
		
		String data = sb.toString().replaceAll("\n", "$0\t\t");
		data = data.replaceAll("^", "$0\t");
		sb.setLength(0);
		sb.append(data);
		sb.setLength(sb.length() - 2);
		sb.append('}');
		
		return sb;
	}
	
	public CharSequence toJson(ImmutableBag<Entity> entities)
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
		@SuppressWarnings("unused") // bc: json.prettyPrint
		private final OrderedMap<String,Component> components;
		
		EntityRepresentation(Entity e)
		{
			components = getComponents(e);
		}
		
		static OrderedMap<String,Component> getComponents(Entity e)
		{
			Bag<Component> bag = e.getComponents(new Bag<Component>());
			
			Bag<Component> discard = new Bag<Component>();
			for (int i = 0, s = bag.size(); s > i; i++)
			{
				if (bag.get(i).getClass().getAnnotation(ExcludeFromJson.class) != null)
					discard.add(bag.get(i));
			}
			bag.removeAll(discard);
			
			OrderedMap<String,Component> components = new OrderedMap<String,Component>();
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
