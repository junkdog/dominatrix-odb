package net.onedaybeard.dominatrix.inject;

import java.util.Properties;

/**
 * Marks a class for property injection.
 * 
 * @see InjectProperty
 */
public interface InjectableProperties
{
	/**
	 * Any values that don't correspond to fields or require some sort
	 * of processing before being assigned goes here. For all other use
	 * cases refer to {@link InjectProperty}.
	 */
	void newValues(Properties properties);
}
