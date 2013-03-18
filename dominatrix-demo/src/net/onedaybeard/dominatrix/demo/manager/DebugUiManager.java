package net.onedaybeard.dominatrix.demo.manager;

import java.lang.reflect.Method;
import java.util.Comparator;

import lombok.Getter;
import net.onedaybeard.dominatrix.demo.Assets;
import net.onedaybeard.dominatrix.demo.Director;
import net.onedaybeard.dominatrix.demo.component.JsonKey;
import net.onedaybeard.dominatrix.demo.event.CommandEvent;
import net.onedaybeard.dominatrix.demo.event.CommandEventListener;
import net.onedaybeard.dominatrix.demo.ui.CommandHelpOverlay;
import net.onedaybeard.dominatrix.experimental.ui.ComponentReflexHud;
import net.onedaybeard.dominatrix.experimental.ui.ComponentsHud;
import net.onedaybeard.dominatrix.experimental.ui.ComponentsHud.OnEntityChangedListener;
import net.onedaybeard.dominatrix.experimental.ui.EntityInspectorHud;
import net.onedaybeard.dominatrix.experimental.ui.EntityInspectorHud.JsonKeyResolver;
import net.onedaybeard.dominatrix.experimental.ui.NotificationHud;
import net.onedaybeard.dominatrix.experimental.ui.SystemsHud;
import net.onedaybeard.keyflection.CommandController;
import net.onedaybeard.keyflection.KeyflectionInputProcessor;
import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;
import net.onedaybeard.keyflection.sort.ShortcutComparator;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class DebugUiManager extends Manager
{
	@Getter private Stage stage;
	private EntityInspectorHud inspectorHud;
	private ComponentReflexHud reflexHud;
	private CommandHelpOverlay helpOverlay;
	private NotificationHud notificationHud;
	private ComponentsHud componentsHud;
	private SystemsHud systemsHud;

	@Getter private InputProcessor inputProcessor;
	
	public DebugUiManager(Stage ui)
	{
		super();
		this.stage = ui;
	}

	@Override
	protected void initialize()
	{
		Skin skin = Assets.getAssetManager().get("ui/uiskin.json", Skin.class);
		
		inspectorHud = new EntityInspectorHud(skin, stage, world);
		inspectorHud.setJsonKeyResolver(new JsonKeyResolver()
		{
			@Override
			public String getKey(Entity e)
			{
				JsonKey json = e.getComponent(JsonKey.class);
				return json != null ? json.name() : null;
			}
		});
		
		reflexHud = new ComponentReflexHud(skin, stage);
		helpOverlay = new CommandHelpOverlay(skin, stage);
		notificationHud = new NotificationHud(skin, stage);
		systemsHud = new SystemsHud(skin, stage, world);
		componentsHud = new ComponentsHud(skin, stage, JsonKey.class);
		componentsHud.setOnEntityChangedListener(new OnEntityChangedListener()
		{
			@Override
			public void onComponentAdded(int entityId, Class<? extends Component> addedType)
			{
				setEntity(null);
				setEntity(world.getEntity(entityId));
			}
		});
		
		inspectorHud.setVisible(true);
		initInput();
	}
	
	private void initInput()
	{
		inputProcessor = new KeyflectionInputProcessor(new Shortcuts());
		
		Director.instance.getEventSystem().addReceiver(new CommandEventListener()
		{
			@Override
			protected boolean onReceive(CommandEvent event, CommandEvent.Type type)
			{
				int id = event.getValue();
				
				switch (type)
				{
					case ENTITY_SELECTED:
						setEntity(id != -1 ? world.getEntity(id) : null);
						break;
					case HOVERED_ENTITY:
						inspectorHud.setEntity(world.getEntity(id));
						break;
					case NO_HOVERED_ENTITY:
						inspectorHud.setEntity(null);
						break;
					case PROPERTIES_INJECTED:
						notificationHud.setText("Properties injected (%d)", id);
						break;
					case TOGGLE_HELP:
						helpOverlay.toggle();
						break;
					default:
						break;
				}
				
				return false;
			}
		});
	}
	
	private void setEntity(Entity e)
	{
		inspectorHud.setEntity(e);
		reflexHud.setEntity(e);
	}

	protected class Shortcuts implements CommandController
	{
		@Command(name="quit/close component window", bindings=@Shortcut(Keys.ESCAPE))
		public void exit()
		{
			if (componentsHud.isVisible())
				componentsHud.setVisible(false);
			else if (reflexHud.isVisible())
				toggleComponentEditor();
			else
				Gdx.app.exit();
		}
		
		@Command(name="help!", bindings=@Shortcut(Keys.F1))
		public void toggleHelp()
		{
			helpOverlay.toggle();
		}
		
		@Command(name="cycle entity inspector view", bindings=@Shortcut(Keys.F2))
		public void toggleEntityInspectorView()
		{
			inspectorHud.cycleInspectorView();
		}
		
		@Command(name="entity inspector", bindings=@Shortcut({Keys.SHIFT_LEFT, Keys.F2}))
		public void toggleEntityInspector()
		{
			inspectorHud.toggle();
		}
		
		@Command(name="component editor", bindings=@Shortcut(Keys.F3))
		public void toggleComponentEditor()
		{
			reflexHud.toggle();
		}
		
		@Command(name="add component(s)", bindings=@Shortcut(Keys.F4))
		public void addComponentsToEditor()
		{
			if (componentsHud.isVisible())
			{
				componentsHud.setVisible(false);
				return;
			}
			
			Entity selected = reflexHud.getEntity();
			if (selected != null)
				componentsHud.showFor(selected);
		}
		
		@Command(name="systems", bindings=@Shortcut(Keys.F5))
		public void toggleSystems()
		{
			systemsHud.toggle();
		}
		
		@Command(name="copy json for hovered entity", bindings=@Shortcut({Keys.CONTROL_LEFT, Keys.C}))
		public void copyHoveredEntity()
		{
			String json = inspectorHud.getJsonForHovered();
			if (json != null)
			{
				notificationHud.setText("%s copied to clipboard.", reflexHud.getEntity());
				Gdx.app.getClipboard().setContents(json);
			}
			else
			{
				notificationHud.setText("No entity to copy to clipboard.");
			}
		}

		@Override
		public String groupName()
		{
			return "Systems";
		}

		@Override
		public Comparator<Method> commandComparator()
		{
			return new ShortcutComparator();
		}
	}
}
