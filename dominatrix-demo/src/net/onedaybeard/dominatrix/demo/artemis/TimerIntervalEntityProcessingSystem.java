package net.onedaybeard.dominatrix.demo.artemis;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

public abstract class TimerIntervalEntityProcessingSystem extends TimerIntervalEntitySystem
{

	public TimerIntervalEntityProcessingSystem(Aspect aspect, float interval)
	{
		super(aspect, interval);
	}

	protected abstract void process(Entity e);

	@Override
	protected void processEntities(ImmutableBag<Entity> entities)
	{
		Object[] data = ((Bag<Entity>)entities).getData();
		for (int i = 0, s = entities.size(); s > i; i++)
		{
			process((Entity)data[i]);
		}
	}
}
