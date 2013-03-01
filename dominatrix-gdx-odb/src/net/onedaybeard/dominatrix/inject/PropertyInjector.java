package net.onedaybeard.dominatrix.inject;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import net.onedaybeard.dominatrix.util.Logger;

import com.badlogic.gdx.utils.Array;


public class PropertyInjector
{
	public static final String TAG = PropertyInjector.class.getSimpleName();
	
	private final List<WeakReference<InjectableProperties>> tweakables;
	private Properties properties;
	
	public static boolean debug = false;

	private Injector injector;
	
	public PropertyInjector()
	{
		tweakables = new LinkedList<WeakReference<InjectableProperties>>();
		injector = new Injector();
	}
	
	public void setTypeInjector(Class<?> klazz, TypeInjector injector)
	{
		this.injector.setTypeInjector(klazz, injector);
	}

	public Array<InjectableProperties> getRegistered()
	{
		Array<InjectableProperties> collect = new Array<InjectableProperties>();
		for (WeakReference<InjectableProperties> ref : tweakables)
		{
			InjectableProperties tp = ref.get();
			if (tp != null)
				collect.add(tp);
		}
		
		return collect;
	}
	
	public void clear()
	{
		tweakables.clear();
	}
	
	/**
	 * Registers an instance for injection via {@link #injectRegistered()} and 
	 * {@link #inject(InjectableProperties)}
	 */
	public void register(InjectableProperties injectable)
	{
		tweakables.add(new WeakReference<InjectableProperties>(injectable));
		if (debug)
			Logger.log(TAG, "Tracking %d tweakable objects", tweakables.size());
	}
	
	/**
	 * Re-feeds previously feed properties, if any. Useful for game restarts etc.
	 */
	public void injectRegistered()
	{
		if (properties != null)
			injectRegistered(properties);
	}
	
	public void injectRegistered(Properties properties)
	{
		System.gc();
		
		this.properties = properties;
		int nulledReferences = 0;
		
		if (debug)
			Logger.log(TAG, "Feeding new set of properties to registered objects.");
		
		Iterator<WeakReference<InjectableProperties>> iterator = tweakables.iterator();
		while (iterator.hasNext())
		{
			WeakReference<InjectableProperties> reference = iterator.next();
			InjectableProperties tweakable = reference.get();
			if (tweakable == null)
			{
				iterator.remove();
				nulledReferences++;
			}
			else
			{
				safeInject(properties, tweakable);
			}
		}
		
		if (debug && nulledReferences > 0)
			Logger.log(TAG, "Cleared %d nulled references.", nulledReferences);
	}

	private void safeInject(Properties properties, InjectableProperties tweakable)
	{
		try
		{
			injector.inject(tweakable, properties);
			tweakable.newValues(properties);
		}
		catch (Exception e)
		{
			Logger.error(TAG, e, "Failed feeding properties.");
		}
	}

	public void inject(InjectableProperties tweakable)
	{
		if (properties != null)
			safeInject(properties, tweakable);
	}
}
