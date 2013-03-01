package net.onedaybeard.dominatrix.inject;

import java.lang.reflect.Field;

public interface TypeInjector
{
	void inject(InjectableProperties instance, Field field, String data)
		throws IllegalArgumentException, IllegalAccessException;
}