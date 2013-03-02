package net.onedaybeard.dominatrix.demo;

import static net.onedaybeard.dominatrix.util.Disposer.free;
import lombok.Getter;
import lombok.Setter;
import net.onedaybeard.dominatrix.demo.event.CommandEvent;
import net.onedaybeard.dominatrix.demo.system.EventSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


public enum Director implements Disposable
{
	instance;
	
	@Getter	private final SpriteBatch spriteBatch;
	@Getter private Screen screen;
	
	private AssetManager assetManager;
	@Setter @Getter private EventSystem eventSystem;

	private Array<Screen> screenStack;
	
	private Director()
	{
		spriteBatch = new SpriteBatch();
		assetManager = new AssetManager();
		
		Assets.loadAssetManager(assetManager);
	}
	
	public void setScreen(Screen screen)
	{
		this.screen = screen;
		screen.resume();
	}
	
	public void update()
	{
		assert screen != null : "No screen to direct.";
		
		screen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose()
	{
		if (screen != null)
			free(screen);
		
		free(spriteBatch);
		
		free(assetManager);
		assetManager = null;
	}
	
	public void send(Event event)
	{
		eventSystem.send(event);
	}
	
	public void send(CommandEvent.Type type)
	{
		eventSystem.send(type, 0);
	}
		
	public void send(CommandEvent.Type type, float value)
	{
		eventSystem.send(type, value);
	}
	
	public void send(CommandEvent.Type type, int value)
	{
		eventSystem.send(type, value);
	}
}
