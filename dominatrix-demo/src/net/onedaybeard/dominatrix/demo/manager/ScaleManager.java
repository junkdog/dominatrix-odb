package net.onedaybeard.dominatrix.demo.manager;

import net.onedaybeard.dominatrix.demo.component.Renderable;
import net.onedaybeard.dominatrix.demo.component.Scale;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Sprite;

public final class ScaleManager extends Manager
{
	private ComponentMapper<Renderable> renderableMapper;
	private ComponentMapper<Scale> scaleMapper;
	
	@Override
	protected void initialize()
	{
		renderableMapper = world.getMapper(Renderable.class);
		scaleMapper = world.getMapper(Scale.class);
	}
	
	@Override
	protected void setWorld(World world)
	{
		super.setWorld(world);
	}

	@Override
	public void changed(Entity e)
	{
		Renderable renderable = renderableMapper.getSafe(e);
		if (renderable == null)
			return;
		
		Scale variableScale = scaleMapper.getSafe(e);
		float scale = 1;
		if (variableScale != null)
			scale = variableScale.value();
		
		updateSpriteScale(renderable.getSprite(), scale);
	}	
	
	private static void updateSpriteScale(Sprite sprite, float scale)
	{
		sprite.setScale(scale);
	}
}
