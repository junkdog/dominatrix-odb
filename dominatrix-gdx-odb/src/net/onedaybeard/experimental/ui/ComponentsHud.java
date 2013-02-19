package net.onedaybeard.experimental.ui;

import java.util.Comparator;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import net.onedaybeard.artemis.ComponentUtils;
import net.onedaybeard.artemis.JsonId;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * SAdds components to entities.
 */
public final class ComponentsHud
{
	private static final int PADDING = 10;
	private static final int COLUMNS = 5;
	
	private final Window componentsWindow;
	private Entity entity;
	
	@Setter @Getter private OnEntityChangedListener onEntityChangedListener;
	
	public ComponentsHud(Skin skin, Stage ui, Class<? extends JsonId> jsonComponent)
	{
		componentsWindow = createComponentsWindow(skin, jsonComponent.getPackage());
		
		componentsWindow.setPosition(ui.getWidth() / 2 - componentsWindow.getWidth() / 2,
			ui.getHeight() - componentsWindow.getHeight() - PADDING);
		
		componentsWindow.setVisible(false);
		
		ui.addActor(componentsWindow);
	}
	
	public void showFor(Entity entity)
	{
		this.entity = entity;
		componentsWindow.setTitle("Add component to " + entity);
		componentsWindow.setVisible(true);
	}
	
	public boolean isVisible()
	{
		return componentsWindow.isVisible();
	}
	
	public void setVisible(boolean visible)
	{
		if (!visible)
			entity = null;
		
		componentsWindow.setVisible(visible);
	}

	private Window createComponentsWindow(Skin skin, Package componentPackage)
	{
		Set<Class<? extends Component>> componentSet = ComponentUtils.getComponents(componentPackage);
		Array<Class<?>> components = new Array<Class<?>>();
		for (Class<? extends Component> c : componentSet)
		{
			components.add(c);
		}
		
		components.sort(new ClassNameComparator());
		
		Window w = new Window("", skin);
		w.defaults().pad(PADDING).align(Align.center);
		addComponentsToWindow(w, skin, components);
		w.pack();
		
		return w;
	}
	
	private void addComponentsToWindow(Window w, Skin skin, Array<Class<?>> components)
	{
		for (int i = 0; components.size > i; i++)
		{
			if (i % COLUMNS == 0)
				w.row();
			
			w.add(addButton(skin, components.get(i))).fillX();
		}
	}

	private TextButton addButton(Skin skin, final Class<?> component)
	{
		String title = component.getSimpleName();
		final TextButton button = new TextButton(title, skin);
		button.setName(component.getSimpleName());
		button.addListener(new ClickListener()
		{
			@SuppressWarnings("unchecked") @Override
			public void clicked(InputEvent event, float x, float y)
			{
				ComponentUtils.addComponent(entity, component);
				
				if (onEntityChangedListener != null)
					onEntityChangedListener.onComponentAdded(entity.getId(), (Class<? extends Component>)component);
				
				componentsWindow.setVisible(false);
				event.cancel();
			}
		});
		return button;
	}
	
	private static final class ClassNameComparator implements Comparator<Class<?>>
	{
		@Override
		public int compare(Class<?> o1, Class<?> o2)
		{
			return o1.getSimpleName().compareTo(o2.getSimpleName());
		}
	}
	
	public static interface OnEntityChangedListener
	{
		void onComponentAdded(int entityId, Class<? extends Component> addedType);
	}
}