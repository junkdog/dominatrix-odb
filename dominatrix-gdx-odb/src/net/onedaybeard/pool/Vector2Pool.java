package net.onedaybeard.pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public final class Vector2Pool
{
	private static Factory factory = new Factory();
	
	private Vector2Pool()
	{
		
	}
	
	public static Vector2 vector2()
	{
		return factory.obtain();
	}
	
	public static Vector2 vector2(float x, float y)
	{
		return factory.obtain().set(x, y);
	}
	
	public static Vector2 vector2(Vector2 vector)
	{
		return factory.obtain().set(vector);
	}
	
	public static void free(Vector2 v)
	{
		factory.free(v);
	}
	
	public static void free(Vector2... vs)
	{
		for (int i = 0; vs.length > i; i++)
			free(vs[i]);
	}
	
	private static class Factory extends Pool<Vector2>
	{
		@Override
		protected Vector2 newObject()
		{
			return new Vector2();
		}

		@Override
		public void free(Vector2 object)
		{
			object.set(0, 0);
			super.free(object);
		}
	}
}
