package net.onedaybeard.dominatrix.demo.artemis;

import com.artemis.Aspect;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;

/**
 * Modified to use raw frame time.
 */
public abstract class TimerIntervalEntitySystem extends EntitySystem
{
	private float acc;
	private float interval;

	public TimerIntervalEntitySystem(Aspect aspect, float interval)
	{
		super(aspect);
		this.interval = interval;
	}

	@Override
	protected boolean checkProcessing()
	{
		acc += Gdx.graphics.getDeltaTime();
		if (acc >= interval)
		{
			acc -= interval;
			return true;
		}
		return false;
	}

}
