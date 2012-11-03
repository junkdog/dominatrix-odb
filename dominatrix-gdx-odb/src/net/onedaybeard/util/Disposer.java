package net.onedaybeard.util;

import static net.onedaybeard.util.Logger.log;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public final class Disposer
{
	public static final String TAG = Disposer.class.getSimpleName();
	
	private Disposer()
	{
		
	}
	
	public static void free(Disposable... disposables)
	{
		for (Disposable disposable : disposables)
			free(disposable);
	}
	
	private static void free(Disposable disposable)
	{
		try
		{
			disposable.dispose();
		}
		catch (Exception e)
		{
			log(TAG, "failed to dispose '%s', %s", disposable.getClass(), e.getMessage());
		}
	}
	
	public static void free(ShapeRenderer renderer)
	{
		try
		{
			renderer.dispose();
		}
		catch (Exception e)
		{
			log(TAG, "failed to dispose '%s', %s", renderer.getClass(), e.getMessage());
		}
	}
	
	public static void free(Screen screen)
	{
		try
		{
			screen.dispose();
		}
		catch (Exception e)
		{
			log(TAG, "failed to dispose '%s', %s", screen.getClass(), e.getMessage());
		}
	}
}
