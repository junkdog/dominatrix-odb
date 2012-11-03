package net.onedaybeard.util;

import com.badlogic.gdx.Gdx;

public final class SystemNTimer
{
	public static final String TAG = SystemNTimer.class.getSimpleName();

	private final NTimer timer;
	private long minInterval;
	private long lastLogMs;

	private final String logFormat;
	private int dropped;

	public SystemNTimer(Class<?> system, int minInterval)
	{
		timer = NTimer.obtain();
		this.minInterval = minInterval;

		logFormat = String.format("%-30s cycle %%7.2f avg %%5.2f (omitted %%d)",
			system.getSimpleName());
	}

	public SystemNTimer(Class<?> system)
	{
		this(system, 1000);
	}

	public void printLog()
	{
		if ((lastLogMs + minInterval) < System.currentTimeMillis())
		{
			Gdx.app.log(TAG, String.format(logFormat, timer.getTotalMs(), timer.getAvgMs(), dropped));

			lastLogMs = System.currentTimeMillis();
			dropped = 0;
		}
		else
		{
			dropped++;
		}
		timer.reset();
	}

	public int getRuns()
	{
		return timer.getRuns();
	}

	public void start()
	{
		timer.start();
	}

	public void stop()
	{
		timer.stop();
	}

	public void stopAndPrintLog()
	{
		timer.stop();
		printLog();
	}

	@Override
	public void finalize()
	{
		NTimer.free(timer);
	}
}
