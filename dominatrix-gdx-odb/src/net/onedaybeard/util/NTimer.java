package net.onedaybeard.util;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

public final class NTimer
{
	public static final String TAG = NTimer.class.getSimpleName();

	private static Factory factory = new Factory();

	private long total;
	private long lastStart;
	@Getter private int runs;
	
	private boolean running;

	private NTimer()
	{
		reset();
	}

	public void start()
	{
		if (running)
		{
			Gdx.app.error(TAG, "Timer already started");
		}
		else
		{
			running = true;
			runs++;

			lastStart = System.nanoTime();
		}
	}

	public void stop()
	{
		total += System.nanoTime() - lastStart;
		running = false;
	}

	public void reset()
	{
		total = 0;
		runs = 0;
		running = false;
	}

	public static void free(NTimer object)
	{
		factory.free(object);
	}

	public static NTimer obtain()
	{
		return factory.obtain();
	}

	public long getTotalNano()
	{
		return this.total;
	}

	public float getTotalMs()
	{
		return this.total / 1000000f;
	}

	public float getAvgMs()
	{
		return getTotalMs() / runs;
	}

	public long getAvgNano()
	{
		return getTotalNano() / runs;
	}
	
	public void addNanos(long nanos)
	{
		if (!running)
			throw new IllegalStateException("Timer isn't running");
		
		lastStart -= nanos;
	}

	private static class Factory extends Pool<NTimer>
	{
		@Override
		protected NTimer newObject()
		{
			return new NTimer();
		}

		@Override
		public void free(NTimer object)
		{
			object.reset();
			super.free(object);
		}
	}
}
