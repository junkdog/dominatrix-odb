package net.onedaybeard.experimental.ui;

import lombok.Setter;
import net.onedaybeard.annotation.Sloppy;
import net.onedaybeard.artemis.JsonEntityMarshaller;
import net.onedaybeard.experimental.artemis.CommandUtils;
import net.onedaybeard.experimental.artemis.CommandUtils.ObjectNode;
import net.onedaybeard.util.Tree;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

@Sloppy("")
public final class EntityInspectorHud
{
	private Skin skin;
	
	private Table table;
	private Label hovered;
	
	private Table statusTable;
	private Label status;
	
	private Entity lastEntity;
	
	private enum View {ENTITY, JSON};
	private View view;

	
	@Setter private JsonKeyResolver jsonKeyResolver;
	
	private JsonEntityMarshaller marshaller;
	
	public EntityInspectorHud(Skin skin, Stage ui, World world)
	{
		this.skin = skin;
		
		initialize(world);
		ui.addActor(table);
		
		status = new Label("", skin);
		
		statusTable = new Table(skin);
		statusTable.getColor().a = 0;
		
		statusTable.setPosition(20, Gdx.graphics.getHeight() - statusTable.getMaxHeight());
		statusTable.defaults().pad(5).fillX().expandX();
		statusTable.setBackground(BackgroundTexture.getDrawable());
		statusTable.row().expandX().fillX();
		statusTable.add(status);
		statusTable.pack();
		
		ui.addActor(statusTable);
		
		table.setVisible(false);
		view = View.ENTITY;
		
		marshaller = new JsonEntityMarshaller(OutputType.json);
	}
	
	public void setHovered(Entity e)
	{
		if (lastEntity == e)
			return;
		
		lastEntity = e;
		
		if (hovered != null)
			hovered.setText(getTextForEntity(e));
		else
			hovered.setText("");
		
		table.pack();
	}
	
	public void setStatusText(String text)
	{
		status.setText(text);
		statusTable.pack();
		statusTable.setPosition((Gdx.graphics.getWidth() - statusTable.getWidth()) / 2,
			Gdx.graphics.getHeight() - statusTable.getHeight());
		
		statusTable.clearActions();
		statusTable.addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(3.5f), Actions.fadeOut(0.5f)));
	}

	protected void initialize(final World world)
	{
		table = new Table(skin);
		
		hovered = new Label("", skin);
		
		table.setBackground(BackgroundTexture.getDrawable());
		table.defaults().pad(5);
		table.row();
		table.add(hovered);
		table.pack();
	}
	
	private String getTextForEntity(Entity e)
	{
		String text = null;
		switch (view)
		{
			case ENTITY:
				text = CommandUtils.formatTree(
					CommandUtils.feedComponents(new Tree<ObjectNode>(), e), null).toString();
				break;
			case JSON:
				text = getJsonForHovered().replace("\t", "    ");
				break;
			default:
				assert false : "Missing view " + view;
			break;
		}
		
		return text;
	}
	
	public String getJsonForHovered()
	{
		if (lastEntity == null)
			return "Nothing hovered";
		
		String key = jsonKeyResolver != null ? jsonKeyResolver.getKey(lastEntity) : null;
		String text = marshaller.toJson(lastEntity, key);
		return text;
	}
	
	public void toggle()
	{
		table.setVisible(!table.isVisible());
	}
	
	public boolean isVisible()
	{
		return table.isVisible();
	}

	public void setVisible(boolean visible)
	{
		table.setVisible(visible);
	}
	
	public void cycleInspectorView()
	{
		View[] views = View.values();
		view = views[(view.ordinal() + 1) % views.length];
		
		hovered.setText(getTextForEntity(lastEntity));
		table.pack();
	}
	
	public void setOutputType(OutputType outputType)
	{
		marshaller.setOutputType(outputType);
	}

	public static interface JsonKeyResolver
	{
		String getKey(Entity e);
	}
}
