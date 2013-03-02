package net.onedaybeard.dominatrix.demo.system.debug;

import static net.onedaybeard.dominatrix.demo.event.CommandEvent.Type.HOVERED_ENTITY;
import static net.onedaybeard.dominatrix.demo.event.CommandEvent.Type.NO_HOVERED_ENTITY;
import lombok.Getter;
import net.onedaybeard.dominatrix.demo.Director;
import net.onedaybeard.dominatrix.demo.artemis.TimerIntervalEntityProcessingSystem;
import net.onedaybeard.dominatrix.demo.component.Position;
import net.onedaybeard.dominatrix.demo.component.Renderable;
import net.onedaybeard.dominatrix.demo.event.CommandEvent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class MouseInputSystem extends TimerIntervalEntityProcessingSystem
{
	private ComponentMapper<Position> positionMapper;
	private ComponentMapper<Renderable> renderableMapper;
	
	private final Vector2 coordinate;

	private Entity hovered;
	
	@Getter private InputProcessor inputProcessor;
	
	@SuppressWarnings("unchecked")
	public MouseInputSystem()
	{
		super(Aspect.getAspectForAll(Position.class,  Renderable.class), .1f);
		
		coordinate = new Vector2();
	}

	@Override
	protected void initialize()
	{
		inputProcessor = new MouseClickListener();
		
		positionMapper = world.getMapper(Position.class);
		renderableMapper = world.getMapper(Renderable.class);
		
	}
	
	@Override
	protected void begin()
	{
		hovered = null;
		coordinate.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
	}
	
	@Override
	protected void process(Entity e)
	{
		if(!renderableMapper.get(e).getSprite().getBoundingRectangle().contains(coordinate.x, coordinate.y))
			return;
		
		float distanceToCursor = getDistanceToCursor(e);
		if (hovered == null || distanceToCursor < getDistanceToCursor(hovered))
			hovered = e;
	}
	
	
	private float getDistanceToCursor(Entity e)
	{
		return positionMapper.get(e).point().dst(coordinate);
	}
	
	@Override
	protected void end()
	{
		if (hovered != null)
			Director.instance.send(HOVERED_ENTITY, hovered.getId());
		else
			Director.instance.send(NO_HOVERED_ENTITY);
	}
	
	private class MouseClickListener implements InputProcessor 
	{
		public MouseClickListener()
		{
			
		}
		
		@Override
		public boolean keyDown(int keycode)
		{
			return false;
		}

		@Override
		public boolean keyUp(int keycode)
		{
			return false;
		}

		@Override
		public boolean keyTyped(char character)
		{
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button)
		{
			if (button != 0 || hovered == null)
				return false;
			
			Director.instance.send(CommandEvent.Type.ENTITY_SELECTED, hovered.getId());
			
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button)
		{
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer)
		{
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY)
		{
			return false;
		}

		@Override
		public boolean scrolled(int amount)
		{
			return false;
		}
	}
}
