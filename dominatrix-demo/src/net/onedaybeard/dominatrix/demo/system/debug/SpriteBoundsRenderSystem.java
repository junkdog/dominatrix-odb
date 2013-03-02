package net.onedaybeard.dominatrix.demo.system.debug;

import static net.onedaybeard.dominatrix.util.Disposer.free;

import java.util.Properties;

import lombok.Setter;
import net.onedaybeard.dominatrix.demo.Director;
import net.onedaybeard.dominatrix.demo.component.Renderable;
import net.onedaybeard.dominatrix.demo.component.Tint;
import net.onedaybeard.dominatrix.demo.event.CommandEvent;
import net.onedaybeard.dominatrix.demo.event.CommandEventListener;
import net.onedaybeard.dominatrix.demo.event.CommandEvent.Type;
import net.onedaybeard.dominatrix.inject.InjectProperty;
import net.onedaybeard.dominatrix.inject.InjectableProperties;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public final class SpriteBoundsRenderSystem extends EntitySystem implements Disposable, InjectableProperties
{
	public static final String TAG = SpriteBoundsRenderSystem.class.getSimpleName();
	
	private final OrthographicCamera camera;
	private ShapeRenderer renderer;

	@InjectProperty("bounds_color_from_tint") private boolean colorFromTint = false;
	
	private ComponentMapper<Renderable> renderableMapper;
	private ComponentMapper<Tint> tintMapper;
	
	private Entity selected;

	@SuppressWarnings("unchecked")
	public SpriteBoundsRenderSystem(OrthographicCamera camera)
	{
		super(Aspect.getAspectForAll(Renderable.class));
		this.camera = camera;
	}

	@Override
	protected void initialize()
	{
		this.renderer = new ShapeRenderer();
		renderableMapper = world.getMapper(Renderable.class);
		tintMapper = world.getMapper(Tint.class);
		
		Director.instance.getEventSystem().addReceiver(new CommandEventListener()
		{
			@Override
			protected boolean accepts(CommandEvent event, Type type)
			{
				return type == Type.ENTITY_SELECTED;
			}
			
			@Override
			protected boolean onReceive(CommandEvent event, Type type)
			{
				int id = event.getValue();
				if (id == -1)
					selected = null;
				else
					selected = world.getEntity(id);
				
				return false;
			}
		});
	}
	
	@Override
	protected void begin()
	{
		renderer.setProjectionMatrix(camera.combined);
	}
	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities)
	{
		Object[] array = ((Bag<Entity>)entities).getData();
		
		renderer.begin(ShapeType.Line);
		for (int i = 0, s = entities.size(); s > i; i++)
		{
			Entity e = (Entity)array[i];
			renderer.setColor(getBoundsColor(e));
			processOutline(e);
		}
		renderer.end();
		
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.WHITE);
		for (int i = 0, s = entities.size(); s > i; i++)
		{
			processOrigin((Entity)array[i]);
		}
		renderer.end();
	}

	private Color getBoundsColor(Entity e)
	{
		if (colorFromTint && tintMapper.has(e))
			return tintMapper.get(e).color();
		else
			return Color.YELLOW;
	}

	private void processOutline(Entity e)
	{
		Sprite sprite = renderableMapper.get(e).getSprite();
		Rectangle bounds = sprite.getBoundingRectangle();
		renderer.box(bounds.x, bounds.y, 0, bounds.width, bounds.height, 0);
	}
	
	private void processOrigin(Entity e)
	{
		Sprite sprite = renderableMapper.get(e).getSprite();
		float x = sprite.getX() + sprite.getOriginX();
		float y = sprite.getY() + sprite.getOriginY();
		
		renderer.circle(x, y, 25, 4);
	}

	@Override
	protected void end()
	{
		if (selected != null)
		{
			renderer.begin(ShapeType.Line);
			renderer.setColor(Color.GREEN);
			processOutline(selected);
			renderer.end();
		}
	}
	
	@Override
	public void dispose()
	{
		free(renderer);
	}
	
	@Override
	protected boolean checkProcessing()
	{
		return true;
	}

	@Override
	public void newValues(Properties properties)
	{
		// nothing to see here, but if any additional steps need to be taken.
	}
}
