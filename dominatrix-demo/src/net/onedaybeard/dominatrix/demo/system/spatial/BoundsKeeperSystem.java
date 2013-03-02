package net.onedaybeard.dominatrix.demo.system.spatial;

import net.onedaybeard.dominatrix.demo.component.Renderable;
import net.onedaybeard.dominatrix.demo.component.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public final class BoundsKeeperSystem extends EntityProcessingSystem
{
	private ComponentMapper<Renderable> RenderableMapper;
	private ComponentMapper<Velocity> velocityMapper;
	private OrthographicCamera camera;

	@SuppressWarnings("unchecked")
	public BoundsKeeperSystem(OrthographicCamera camera)
	{
		super(Aspect.getAspectForAll(Renderable.class, Velocity.class));
		this.camera = camera;
	}

	@Override
	protected void initialize()
	{
		RenderableMapper = world.getMapper(Renderable.class);
		velocityMapper = world.getMapper(Velocity.class);
	}

	@Override
	protected void process(Entity e)
	{
		Sprite sprite = RenderableMapper.get(e).getSprite();
		Vector2 velocity = velocityMapper.get(e).get();
		
		Rectangle rect = sprite.getBoundingRectangle();
		
		if ((rect.x < 0 && velocity.x < 0)
			|| ((rect.x + rect.width) > camera.viewportWidth && velocity.x > 0))
		{
			velocity.x *= -1;
		}
		
		if ((rect.y < 0 && velocity.y < 0)
			|| ((rect.y + rect.height) > camera.viewportHeight && velocity.y > 0))
		{
			velocity.y *= -1;
		}
			
	}
}
