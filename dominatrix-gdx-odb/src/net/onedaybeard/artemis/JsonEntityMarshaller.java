package net.onedaybeard.artemis;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public final class JsonEntityMarshaller
{
	private Json json;

	public JsonEntityMarshaller(OutputType outputType)
	{
		json = new Json();
		json.setOutputType(outputType);
		json.setTypeName(null);
	}
	
	public String toJson(Entity e, String label)
	{
		StringBuilder sb = new StringBuilder();
		if (label != null)
			sb.append(label).append(':');
		
		sb.append(json.prettyPrint(new EntityRepresentation(e), 80));
		
		String data = sb.toString().replaceAll("\n", "\n\t");
		sb.setLength(0);
		sb.append(data);
		sb.setLength(sb.length() - 2);
		sb.append('}');
		
		return sb.toString();
	}
	
	class EntityRepresentation
	{
		private Map<String,Component> components;
		
		EntityRepresentation(Entity e)
		{
			setComponents(e.getComponents(new Bag<Component>()));
		}
		
		void setComponents(Bag<Component> bag)
		{
			Bag<Component> discard = new Bag<Component>();
			for (int i = 0, s = bag.size(); s > i; i++)
			{
				if (bag.get(i).getClass().getAnnotation(ExcludeFromJson.class) != null)
					discard.add(bag.get(i));
			}
			bag.removeAll(discard);
			
			components = new HashMap<String,Component>();
			for (int i = 0, s = bag.size(); s > i; i++)
			{
				Component component = bag.get(i);
				components.put(component.getClass().getSimpleName(), component);
			}
		}
	}
}
