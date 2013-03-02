package net.onedaybeard.dominatrix.demo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DemoApp implements ApplicationListener
{
	@Override
	public void create()
	{
		Director.instance.setScreen(new DemoScreen());
	}

	@Override
	public void resize(int width, int height)
	{
		Director.instance.getScreen().resize(width, height);
	}

	@Override
	public void render()
	{
		Director.instance.update();
	}

	@Override
	public void pause()
	{
		Director.instance.getScreen().pause();
	}

	@Override
	public void resume()
	{
		Director.instance.getScreen().resume();
	}

	@Override
	public void dispose()
	{
		Director.instance.dispose();
	}
	
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = 1280;
		config.height = 800;
		config.resizable = false;
		config.title = "dominatrix api demo";
		new LwjglApplication(new DemoApp(), config);
	}
}
