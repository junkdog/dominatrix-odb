package net.onedaybeard.experimental.ui;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.lang.reflect.Field;

import lombok.Getter;
import net.onedaybeard.artemis.ComponentNameComparator;
import net.onedaybeard.reflect.ColorWriter;
import net.onedaybeard.reflect.Reflex;
import net.onedaybeard.reflect.Vector2Writer;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Array;


/**
 * For input to function, the stage owning this component must register with
 * <code>Gdx.input.setInputProcessor</code>.
 */
public final class ComponentReflexHud
{
	private Tree tree;
	private final Skin skin;
	@Getter private Entity entity;
	private Table table;
	private final Reflex reflex;
	
	private final Color errorColor;
	
	private static final int PADDING = 5;
	private static final int WIDTH_TYPE = 100;
	private static final int WIDTH_LABEL = 200;
	private static final int WIDTH_VALUE = 250;
	

	public ComponentReflexHud(Skin skin, Stage ui)
	{
		this.skin = skin;
		
		reflex = new Reflex();
		reflex.addParser(new Vector2Writer());
		reflex.addParser(new ColorWriter());
		
		errorColor = new Color(1f, 0.5f, 0.5f, 1f);
		
		initUi(ui, skin);
		ui.addActor(table);
	}
	
	private void initUi(Stage ui, Skin skin)
	{
		tree = new Tree(skin);
		table = new Table();
		table.setBackground(BackgroundTexture.getDrawable());
		table.defaults().pad(PADDING);
		table.add(tree);
		table.align(Align.top | Align.left);
		
		table.setVisible(false);
	}
	
	public void setEntity(Entity e)
	{
		if (e == entity)
			return;
		
		tree.clear();
		this.entity = e;
		
		if (e == null)
			return;
		
		Bag<Component> components = e.getComponents(new Bag<Component>());
		components.sort(new ComponentNameComparator());
		for (int i = 0, s = components.size(); s > i; i++)
		{
			tree.add(getComponentNode(components.get(i)));
		}
		
		packTable();
	}

	private void packTable()
	{
		Stage stage = table.getStage();
		if (stage == null)
			return;
		
		table.setWidth(PADDING + WIDTH_TYPE + WIDTH_LABEL + WIDTH_VALUE + PADDING + 35);
		table.setHeight(stage.getHeight() - PADDING * 2);
		
		float x = 5;
		float y = stage.getHeight() - 5 - table.getHeight();
		
		table.setPosition(x, y);
	}
	
	
	private Node getComponentNode(Component component)
	{
		Label label = new Label(component.getClass().getSimpleName(), skin);
		Node componentNode = new Tree.Node(label);
		componentNode.setObject(component);
		componentNode.addAll(getFieldNodes(component));
		return componentNode;
	}
	
	private Array<Node> getFieldNodes(Component component)
	{
		Array<Node> nodes = new Array<Tree.Node>();
		Field[] fields = component.getClass().getDeclaredFields();
		for (int i = 0, s = fields.length; s > i; i++)
		{
			Label label = new Label(fields[i].getName(), skin);
			label.getColor().mul(new Color(1f, 0.5f, 0.5f, 1f));
			Node fieldNode = new Node(getFieldActor(component, fields[i]));
			fieldNode.setObject(fields[i]);
			
			nodes.add(fieldNode);
		}
		
		return nodes;
	}
	
	private Table getFieldActor(final Component instance, final Field field)
	{
		Table t = new Table(skin);
		Color color = reflex.isEditable(field)
			? Color.WHITE
			: errorColor;
		
		Label typeLabel = new Label(field.getType().getSimpleName(), skin);
		typeLabel.setColor(color);
		t.add(typeLabel).minWidth(WIDTH_TYPE);
		
		Label fieldLabel = new Label(field.getName(), skin);
		fieldLabel.setColor(color);
		t.add(fieldLabel).minWidth(WIDTH_LABEL);
		
		String value = reflex.on(instance, field).getAsString();
		if (reflex.isEditable(field))
		{
			final TextFieldStyle style = new TextFieldStyle();
			style.font = skin.getFont("default-font");
			style.fontColor = Color.WHITE;
			style.cursor = skin.getDrawable("cursor");
			TextField textField = new TextField(value, style);
			textField.addCaptureListener(new InputListener()
			{
				@Override
				public boolean keyDown(InputEvent event, int keycode)
				{
					if (keycode == Keys.ESCAPE || keycode == Keys.ENTER)
					{
						TextField text = (TextField)event.getListenerActor();
						
						boolean success = reflex.on(instance, field).set(text.getText());
						style.fontColor = (success ? Color.WHITE : errorColor);
						if (success)
							text.setText(reflex.on(instance, field).getAsString());
					}
					if (keycode == Keys.ESCAPE)
					{
						Stage stage = event.getListenerActor().getStage();
						stage.setKeyboardFocus(event.getListenerActor().getParent());
						event.cancel();
						
						TextField text = (TextField)event.getListenerActor();
						text.setText(reflex.on(instance, field).getAsString());
					}
					
					return event.isCancelled();
				}
			});
			textField.addListener(new FocusListener()
			{
				@Override
				public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused)
				{
					TextField text = (TextField)event.getListenerActor();
					text.setText(reflex.on(instance, field).getAsString());
					
					boolean success = reflex.on(instance, field).set(text.getText());
					style.fontColor = (success ? Color.WHITE : errorColor);
				}
			});
			t.add(textField);
		}
		else
		{
			if (value.length() > 30)
				value = value.substring(0, 28) + "...";
			
			Label valueLabel = new Label(value, skin);
			valueLabel.setColor(color);
			t.add(valueLabel).minWidth(WIDTH_VALUE);
		}
		
		return t;
	}
	
	public void toggle()
	{
		table.clearActions();
		if (table.isVisible())
			table.addAction(sequence(fadeOut(0.35f, Interpolation.pow2), visible(false)));
		else
			table.addAction(sequence(visible(true), fadeIn(0.35f, Interpolation.pow2)));
	}
	
	public boolean isVisible()
	{
		return table.isVisible();
	}

	public void setVisible(boolean visible)
	{
		if (table.isVisible() != visible)
			toggle();
	}
}
