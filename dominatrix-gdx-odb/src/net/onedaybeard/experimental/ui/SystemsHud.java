package net.onedaybeard.experimental.ui;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

/**
 * Shows a table for toggling systems held by {@link World}.
 */
public final class SystemsHud
{
	private static final int PADDING = 10;
	private static final int COLUMNS = 5;
	
	private static final String SYSTEM_PACKAGE_REGEX = "^net.onedaybeard.psytripper.system.?";
	
	private Window systemsWindow;
	
	public SystemsHud(Skin skin, Stage ui, World world)
	{
		systemsWindow = createSystemWindow(skin, world);
		
		systemsWindow.setPosition(ui.getWidth() / 2 - systemsWindow.getWidth() / 2,
			ui.getHeight() - systemsWindow.getHeight() - PADDING);
		
		systemsWindow.setVisible(false);
		
		ui.addActor(systemsWindow);
	}
	
	public void toggle()
	{
		systemsWindow.setVisible(!systemsWindow.isVisible());
	}
	
	public boolean isVisible()
	{
		return systemsWindow.isVisible();
	}

	public void setVisible(boolean visible)
	{
		systemsWindow.setVisible(visible);
	}

	private static Window createSystemWindow(Skin skin, World world)
	{
		SortedMap<String, Array<EntitySystem>> systemMap = 
			new TreeMap<String, Array<EntitySystem>>();
		
		ImmutableBag<EntitySystem> systems = world.getSystems();
		for (int i = 0, s = systems.size(); s > i; i++)
		{
			addSystemToMap(systemMap, systems.get(i));
		}
		
		Window w = new Window("systems", skin);
		w.defaults().pad(PADDING).align(Align.center);
		for (Entry<String, Array<EntitySystem>> entry : systemMap.entrySet())
		{
			if (!entry.getKey().equals(""))
			{
				w.row();
				w.add(entry.getKey()).colspan(COLUMNS).fillX();
			}
			addSystemsToWindow(w, skin, entry.getValue());
		}
		w.pack();
		
		return w;
	}
	
	private static void addSystemsToWindow(Window w, Skin skin, Array<EntitySystem> systems)
	{
		Sort.instance().sort(systems, new SystemNameComparator());
		for (int i = 0; systems.size > i; i++)
		{
			if (i % COLUMNS == 0)
				w.row();
			
			w.add(addButton(skin, systems.get(i))).fillX();
		}
	}

	private static void addSystemToMap(SortedMap<String,Array<EntitySystem>> systemMap, EntitySystem system)
	{
		String p = system.getClass().getPackage().getName();
		p = p.replaceAll(SYSTEM_PACKAGE_REGEX, "");
		
		if (!systemMap.containsKey(p))
			systemMap.put(p, new Array<EntitySystem>());
		
		systemMap.get(p).add(system);
	}

	private static TextButton addButton(Skin skin, final EntitySystem system)
	{
		String title = system.getClass().getSimpleName().replaceAll("System$", "");
		final TextButton button = new TextButton(title, skin.get("toggle", TextButtonStyle.class));
		button.setChecked(!system.isEnabled());
		button.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				system.setEnabled(!system.isEnabled());
				event.cancel();
			}
		});
		return button;
	}
	
	private static final class SystemNameComparator implements Comparator<EntitySystem>
	{
		@Override
		public int compare(EntitySystem o1, EntitySystem o2)
		{
			return o1.getClass().getSimpleName().compareTo(o2.getClass().getSimpleName());
		}
	}
}