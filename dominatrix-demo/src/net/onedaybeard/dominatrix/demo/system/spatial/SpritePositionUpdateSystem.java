package net.onedaybeard.dominatrix.demo.system.spatial;

import net.onedaybeard.dominatrix.demo.component.Position;
import net.onedaybeard.dominatrix.demo.component.Renderable;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;

public final class SpritePositionUpdateSystem extends EntityProcessingSystem
{
	private ComponentMapper<Position> positionMapper;
	private ComponentMapper<Renderable> renderableMapper;

	@SuppressWarnings("unchecked")
	public SpritePositionUpdateSystem()
	{
		super(Aspect.getAspectForAll(Position.class, Renderable.class));
	}

	@Override
	protected void initialize()
	{
		positionMapper = world.getMapper(Position.class);
		renderableMapper = world.getMapper(Renderable.class);
	}

	@Override
	protected void process(Entity e)
	{
		Renderable renderable = renderableMapper.get(e);
		Position position = positionMapper.get(e);

		Sprite s = renderable.getSprite();
		s.setPosition(position.x() - (s.getOriginX()), position.y() - (s.getOriginY()));
	}
}
