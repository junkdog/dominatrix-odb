package net.onedaybeard.dominatrix.artemis;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.artemis.Component;
import com.artemis.Entity;

public final class ComponentUtils
{
	private ComponentUtils()
	{
		
	}
	
	public static Set<Class<? extends Component>> getComponents(Package inPackage)
	{
		return getComponents(inPackage.getName());
	}
	
	public static Set<Class<? extends Component>> getComponents(String inPackage)
	{
		Reflections reflections = new Reflections(new ConfigurationBuilder()
			.setUrls(ClasspathHelper.forPackage(inPackage)));
		
		return reflections.getSubTypesOf(Component.class);
	}
	
	public static void addComponent(Entity e, String component)
	{
		try
		{
			addComponent(e, Class.forName(component));
		}
		catch (ClassNotFoundException e1)
		{
			throw new RuntimeException(e1);
		}
	}
	
	public static void addComponent(Entity e, Class<?> component)
	{
		try
		{
			e.addComponent((Component)component.newInstance());
			e.changedInWorld();
		}
		catch (InstantiationException exc)
		{
			throw new RuntimeException("Missing default constructor? Unable to instantiate " + component, exc);
		}
		catch (IllegalAccessException exc)
		{
			throw new RuntimeException("Unable to instantiate " + component, exc);
		}
	}
}
