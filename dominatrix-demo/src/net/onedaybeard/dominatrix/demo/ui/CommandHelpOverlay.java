package net.onedaybeard.dominatrix.demo.ui;
import net.onedaybeard.dominatrix.experimental.ui.BackgroundTexture;
import net.onedaybeard.keyflection.BoundCommand;
import net.onedaybeard.keyflection.CommandManager;
import net.onedaybeard.keyflection.KeyFormatter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;

public class CommandHelpOverlay
{
	private static final int PADDING = 5;
	
	private final Skin skin;
	private final Table table;

	public CommandHelpOverlay(Skin skin, Stage ui)
	{
		this.skin = skin;
		
		table = new Table();
		table.setBackground(BackgroundTexture.getDrawable());
		table.defaults().pad(PADDING);
		table.align(Align.top | Align.left);
		table.setVisible(false);
		
		ui.addActor(table);
	}
	
	public void toggle()
	{
		table.setVisible(!table.isVisible());
		if (table.isVisible())
		{
			table.clear();
			
			populateTable();
			positionTable();
		}
	}

	private void positionTable()
	{
		float x = (Gdx.graphics.getWidth() - table.getWidth()) / 2;
		float y = (Gdx.graphics.getHeight() - table.getHeight()) / 2;
		table.setPosition(x, y);
	}

	private void populateTable()
	{
		KeyFormatter formatter = new KeyFormatter();
		
		table.add();
		table.row();
		
		for (BoundCommand command : CommandManager.instance.getCommands())
		{
			table.add(newLabel(command.getName())).pad(2).padLeft(8).align(Align.left | Align.top);
			populateCommands(table, formatter, command);
		}
		table.pack();
	}

	private void populateCommands(Table overlay, KeyFormatter formatter, BoundCommand command)
	{
		Array<String> shortcuts = command.formatShortcuts(formatter);
		for (int i = 0; shortcuts.size > i; i++)
		{
			if (i > 0)
				overlay.add();
			
			overlay.add(newLabel(shortcuts.get(i))).pad(2).padLeft(16).padRight(8).align(Align.right);
			overlay.row();
		}
		overlay.add();
		overlay.row();
	}
	
	private Label newLabel(String s)
	{
		Label label =  new Label(s, skin);
		label.setColor(Color.GREEN);
		return label;
	}
}