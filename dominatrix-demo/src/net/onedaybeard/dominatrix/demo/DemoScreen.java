package net.onedaybeard.dominatrix.demo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Properties;

import net.onedaybeard.dominatrix.artemis.EntityFactoryManager;
import net.onedaybeard.dominatrix.demo.component.JsonKey;
import net.onedaybeard.dominatrix.demo.event.CommandEvent.Type;
import net.onedaybeard.dominatrix.demo.manager.DebugUiManager;
import net.onedaybeard.dominatrix.demo.manager.RenderableResolverManager;
import net.onedaybeard.dominatrix.demo.manager.ScaleManager;
import net.onedaybeard.dominatrix.demo.system.EventSystem;
import net.onedaybeard.dominatrix.demo.system.debug.MouseInputSystem;
import net.onedaybeard.dominatrix.demo.system.debug.SpriteBoundsRenderSystem;
import net.onedaybeard.dominatrix.demo.system.render.SpriteRenderSystem;
import net.onedaybeard.dominatrix.demo.system.spatial.BoundsKeeperSystem;
import net.onedaybeard.dominatrix.demo.system.spatial.PositionUpdateSystem;
import net.onedaybeard.dominatrix.demo.system.spatial.SpritePositionUpdateSystem;
import net.onedaybeard.dominatrix.inject.InjectProperty;
import net.onedaybeard.dominatrix.inject.InjectableProperties;
import net.onedaybeard.dominatrix.inject.PropertyInjector;
import net.onedaybeard.dominatrix.inject.TypeInjector;
import net.onedaybeard.dominatrix.util.Logger;
import net.onedaybeard.keyflection.CommandController;
import net.onedaybeard.keyflection.CommandManager;
import net.onedaybeard.keyflection.KeyflectionInputProcessor;
import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;

import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.managers.UuidEntityManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DemoScreen implements Screen, InjectableProperties
{
	public static final String TAG = DemoScreen.class.getSimpleName();
	
	private World world;
	private EntityFactoryManager entityFactoryManager;
	
	private PropertyInjector injector;
	
	private final Stage stage;
	private final OrthographicCamera camera;
	
	private boolean running;
	@InjectProperty("delta_multiplier") private float deltaMultiplier = 1;
	
	@InjectProperty("background_clear_color") private Color bg = Color.BLACK;

	private ScreenViewport viewport;

	
	public DemoScreen()
	{
		camera = createCamera();
		
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		
		viewport = new ScreenViewport(camera);
		stage = new Stage(viewport, Director.instance.getSpriteBatch());
		
		world = initArtemis(Director.instance.getSpriteBatch(), stage);
		initEntities(world);
		initInput(stage);
		injector = initPropertyInjection(world);
		
		running = true;
	}

	private PropertyInjector initPropertyInjection(World world)
	{
		PropertyInjector injector = new PropertyInjector();
		injector.setTypeInjector(Color.class, new TypeInjector()
		{
			@Override
			public void inject(InjectableProperties instance, Field field, String data) throws IllegalArgumentException,
				IllegalAccessException
			{
				String s = data.toUpperCase().replace(' ', '_');
				try
				{
					Field color = Color.class.getField(s);
					field.set(instance, color.get(null));
				}
				catch (SecurityException e)
				{
					Logger.error(TAG, e, e.getMessage());
				}
				catch (NoSuchFieldException e)
				{
					Logger.error(TAG, e, "No color matching " + data);
				}
			}
		});
		
		injector.register(this);
		
		ImmutableBag<EntitySystem> systems = world.getSystems();
		for (int i = 0, s = systems.size(); s > i; i++)
		{
			if (systems.get(i) instanceof InjectableProperties)
				injector.register((InjectableProperties)systems.get(i));
		}
		
		return injector;
	}

	private World initArtemis(SpriteBatch spriteBatch, Stage stage)
	{
		entityFactoryManager = EntityFactoryManager.from(
			Gdx.files.internal("data/archetypes.json"),
			JsonKey.class, true);
		
		World world = new World();
		world.setManager(entityFactoryManager);
		world.setManager(new RenderableResolverManager());
		world.setManager(new ScaleManager());
		world.setManager(new DebugUiManager(stage));
		world.setManager(new UuidEntityManager());
		
		Director.instance.setEventSystem(world.setSystem(new EventSystem()));
		world.setSystem(new MouseInputSystem());
		world.setSystem(new PositionUpdateSystem());
		world.setSystem(new BoundsKeeperSystem(camera));
		world.setSystem(new SpritePositionUpdateSystem());
		world.setSystem(new SpriteRenderSystem(Director.instance.getSpriteBatch(), camera));
		world.setSystem(new SpriteBoundsRenderSystem(camera));
		
		world.initialize();
		
		return world;
	}
	
	private void initEntities(World world)
	{
		entityFactoryManager.create("penguin");
		entityFactoryManager.create("penguin2");
	}

	private void initInput(Stage stage)
	{
		CommandManager.instance.setSingleModifierKeys(true);
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(world.getManager(DebugUiManager.class).getInputProcessor());
		multiplexer.addProcessor(world.getSystem(MouseInputSystem.class).getInputProcessor());
		multiplexer.addProcessor(new KeyflectionInputProcessor(new Shortcuts()));
		
		Gdx.input.setInputProcessor(multiplexer);
		
		Director.instance.send(Type.TOGGLE_HELP);
	}

	private static OrthographicCamera createCamera()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		OrthographicCamera cam = new OrthographicCamera(w, h);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
		return cam;
	}

	@Override
	public void render(float delta)
	{
		if (running)
			delta = Math.min(delta, 1f/24);
		else
			delta = 0;
		
		Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		entityFactoryManager.addNewToWorld();

		world.setDelta(delta * deltaMultiplier);
		world.process();

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void show()
	{

	}

	@Override
	public void hide()
	{

	}

	@Override
	public void pause()
	{
		this.running = false;
	}

	@Override
	public void resume()
	{
		this.running = true;
	}

	@Override
	public void dispose()
	{

	}
	
	protected class Shortcuts implements CommandController
	{
		@Command(name="pause", bindings=@Shortcut(Keys.P))
		public void pause()
		{
			deltaMultiplier = (deltaMultiplier == 0) ? 1 : 0;
		}
		
		@Command(name="inject config1.properties", bindings=@Shortcut(Keys.NUM_1))
		public void inject1()
		{
			injector.injectRegistered(Gdx.files.internal("config1.properties"));
			Director.instance.send(Type.PROPERTIES_INJECTED, 1);
		}
		
		@Command(name="inject config2.properties", bindings=@Shortcut(Keys.NUM_2))
		public void inject2()
		{
			injector.injectRegistered(Gdx.files.internal("config2.properties"));
			Director.instance.send(Type.PROPERTIES_INJECTED, 2);
		}

		@Override
		public String groupName()
		{
			return null;
		}

		@Override
		public Comparator<Method> commandComparator()
		{
			return null;
		}
	}

	@Override
	public void newValues(Properties properties)
	{
		
	}
}
