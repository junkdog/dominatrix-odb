package net.onedaybeard.dominatrix.demo.system.render;

import net.onedaybeard.dominatrix.demo.component.Renderable;
import net.onedaybeard.dominatrix.demo.component.Rotation;
import net.onedaybeard.dominatrix.demo.component.Tint;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class SpriteRenderSystem extends EntityProcessingSystem
{
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private ComponentMapper<Renderable> renderableMapper;
	private ComponentMapper<Rotation> rotationMapper;
	private ComponentMapper<Tint> tintMapper;
	
	private Color defaultTint;

	@SuppressWarnings("unchecked")
	public SpriteRenderSystem(SpriteBatch batch, OrthographicCamera camera)
	{
		super(Aspect.getAspectForAll(Renderable.class));
		this.batch = batch;
		this.camera = camera;
		
		defaultTint = Color.WHITE;

	}

	@Override
	protected void initialize()
	{
		renderableMapper = world.getMapper(Renderable.class);
		rotationMapper = world.getMapper(Rotation.class);
		tintMapper = world.getMapper(Tint.class);
	}

	@Override
	protected void begin()
	{
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override
	protected void process(Entity e)
	{
		Renderable renderable = renderableMapper.get(e);
		Sprite sprite = renderable.getSprite();
		
		Rotation rotationComponent = rotationMapper.getSafe(e);
		if (rotationComponent != null)
			sprite.setRotation(rotationComponent.degrees());

		Tint tint = tintMapper.getSafe(e);
		sprite.setColor((tint != null ? tint.color() : defaultTint));
		
		sprite.draw(batch);
	}

	@Override
	protected void end()
	{
		batch.end();
	}
}
