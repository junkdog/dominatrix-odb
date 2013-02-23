package net.onedaybeard.dominatrix.experimental.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * A simple notification window.
 */
public class NotificationHud
{
	private Label message;
	private Table messageContainer;

	public NotificationHud(Skin skin, Stage ui)
	{
		message = new Label("", skin);
		
		messageContainer = new Table(skin);
		messageContainer.getColor().a = 0;
		
		messageContainer.setPosition(20, Gdx.graphics.getHeight() - messageContainer.getMaxHeight());
		messageContainer.defaults().pad(5).fillX().expandX();
		messageContainer.setBackground(BackgroundTexture.getDrawable());
		messageContainer.row().expandX().fillX();
		messageContainer.add(message);
		messageContainer.pack();
		
		ui.addActor(messageContainer);
	}
	
	public void setText(String text)
	{
		message.setText(text);
		
		messageContainer.pack();
		messageContainer.setPosition((Gdx.graphics.getWidth() - messageContainer.getWidth()) / 2,
			Gdx.graphics.getHeight() - messageContainer.getHeight());
		
		messageContainer.clearActions();
		messageContainer.addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(3.5f), Actions.fadeOut(0.5f)));
	}
	
	public void setText(String format, Object... args)
	{
		setText(String.format(format, args));
	}
}
