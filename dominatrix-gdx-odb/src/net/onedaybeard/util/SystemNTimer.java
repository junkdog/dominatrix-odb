package net.onedaybeard.util;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.Gdx;

public final class SystemNTimer
{
	public static final String TAG = SystemNTimer.class.getSimpleName();

	private final NTimer timer;
	private long minInterval;
	private long lastLogMs;

	private final String logFormat;
	private int dropped;

	@Getter @Setter private boolean enabled;

	public SystemNTimer(Class<?> system, int minInterval, boolean enabled)
	{
		this.timer = NTimer.obtain();
		this.minInterval = minInterval;
		this.enabled = enabled;

		logFormat = String.format("%-30s cycle %%7.2f avg %%5.2f (omitted %%d)",
			system.getSimpleName());
	}

	public SystemNTimer(Class<?> system)
	{
		this(system, 1000, true);
	}
	
	public SystemNTimer(Class<?> system, boolean enabled)
	{
		this(system, 1000, enabled);
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
		if (enabled)
			timer.start();
	}

	public void stop()
	{
		timer.stop();
	}

	public void stopAndPrintLog()
	{
		if (!enabled)
			return;
		
		timer.stop();
		printLog();
	}

	@Override
	public void finalize()
	{
		NTimer.free(timer);
	}
}
