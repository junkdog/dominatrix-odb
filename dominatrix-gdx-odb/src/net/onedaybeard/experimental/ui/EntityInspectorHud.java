package net.onedaybeard.experimental.ui;

import lombok.Setter;
import net.onedaybeard.artemis.JsonEntityMarshaller;
import net.onedaybeard.experimental.artemis.CommandUtils;
import net.onedaybeard.experimental.artemis.CommandUtils.ObjectNode;
import net.onedaybeard.util.Tree;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public final class EntityInspectorHud
{
	private Skin skin;
	
	private Table table;
	private Label hovered;
	
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
		
		table.setVisible(false);
		view = View.ENTITY;
		
		marshaller = new JsonEntityMarshaller(OutputType.json);
	}
	
	public void setEntity(Entity e)
	{
		if (lastEntity == e)
			return;
		
		lastEntity = e;
		
		if (lastEntity != null)
			hovered.setText(getTextForEntity(e));
		else
			hovered.setText("");
		
		table.pack();
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
			return null;
		
		String key = jsonKeyResolver != null ? jsonKeyResolver.getKey(lastEntity) : null;
		String text = marshaller.toJson(lastEntity, key).toString();
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
		
		if (lastEntity != null)
			hovered.setText(getTextForEntity(lastEntity));
		else
			hovered.setText("");
		
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
