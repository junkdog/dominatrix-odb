package net.onedaybeard.dominatrix.demo.system.debug;

import static net.onedaybeard.dominatrix.util.Disposer.free;
import lombok.Setter;
import net.onedaybeard.dominatrix.demo.component.Renderable;

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

public final class SpriteBoundsRenderSystem extends EntitySystem implements Disposable
{
	public static final String TAG = SpriteBoundsRenderSystem.class.getSimpleName();
	
	private final OrthographicCamera camera;
	private ShapeRenderer renderer;

	private ComponentMapper<Renderable> renderableMapper;
	
	@Setter private Entity selected;

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
		renderer.setColor(Color.YELLOW);
		for (int i = 0, s = entities.size(); s > i; i++)
		{
			processOutline((Entity)array[i]);
		}
		renderer.end();
		
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.RED);
		for (int i = 0, s = entities.size(); s > i; i++)
		{
			processOrigin((Entity)array[i]);
		}
		renderer.end();
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
		
		renderer.circle(x, y, 0.1f, 4);
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
}
