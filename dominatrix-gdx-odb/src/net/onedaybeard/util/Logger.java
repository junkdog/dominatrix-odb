package net.onedaybeard.util;

import com.badlogic.gdx.Gdx;

public final class Logger
{
	private Logger()
	{
		
	}
	
	public static void log(String tag, String format, Object... args)
	{
		Gdx.app.log(tag, format(format, args));
	}
	
	public static void error(String tag, String format, Object... args)
	{
		Gdx.app.error(tag, format(format, args));
	}

	public static void error(String tag, Exception e, String format, Object... args)
	{
		Gdx.app.error(tag, format(format, args), e);
	}

	private static String format(String format, Object... args)
	{
		return String.format(format, args);
	}
}
