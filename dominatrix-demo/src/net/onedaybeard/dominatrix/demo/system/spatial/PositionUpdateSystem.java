package net.onedaybeard.dominatrix.demo.system.spatial;

import static net.onedaybeard.dominatrix.pool.Vector2Pool.free;
import static net.onedaybeard.dominatrix.pool.Vector2Pool.vector2;
import net.onedaybeard.dominatrix.demo.component.Position;
import net.onedaybeard.dominatrix.demo.component.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;

public final class PositionUpdateSystem extends EntityProcessingSystem
{
	private ComponentMapper<Position> positionMapper;
	private ComponentMapper<Velocity> velocityMapper;

	@SuppressWarnings("unchecked")
	public PositionUpdateSystem()
	{
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
	}

	@Override
	protected void initialize()
	{
		positionMapper = world.getMapper(Position.class);
		velocityMapper = world.getMapper(Velocity.class);
	}

	@Override
	protected void process(Entity e)
	{
		Vector2 point = positionMapper.get(e).point();
		Vector2 velocity = vector2(velocityMapper.get(e).get());
		
		point.add(velocity.scl(world.delta));
		
		free(velocity);
	}
}
