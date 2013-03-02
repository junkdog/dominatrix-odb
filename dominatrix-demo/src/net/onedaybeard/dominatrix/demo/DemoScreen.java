package net.onedaybeard.dominatrix.demo;

import net.onedaybeard.dominatrix.artemis.EntityFactoryManager;
import net.onedaybeard.dominatrix.demo.component.JsonKey;
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
import net.onedaybeard.keyflection.CommandController;
import net.onedaybeard.keyflection.CommandManager;
import net.onedaybeard.keyflection.KeyflectionInputProcessor;
import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DemoScreen implements Screen
{
	public static final String TAG = DemoScreen.class.getSimpleName();
	
	private World world;
	private EntityFactoryManager entityFactoryManager;
	
	private final Stage stage;
	private final OrthographicCamera camera;
	
	private boolean running;
	private float deltaMultiplier = 1;
	
	public DemoScreen()
	{
		camera = createCamera();
		
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		stage = new Stage(w, h, false, Director.instance.getSpriteBatch());
		
		world = initArtemis(Director.instance.getSpriteBatch(), stage);
		initEntities(world);
		initInput(stage);
		
		running = true;
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
		
		Director.instance.setEventSystem(world.setSystem(new EventSystem()));
		world.setSystem(new MouseInputSystem());
		world.setSystem(new PositionUpdateSystem());
		world.setSystem(new BoundsKeeperSystem());
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
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
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
	}

}
