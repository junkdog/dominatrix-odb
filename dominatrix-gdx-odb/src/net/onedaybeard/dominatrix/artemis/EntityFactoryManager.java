package net.onedaybeard.dominatrix.artemis;

import lombok.Getter;
import lombok.Setter;
import net.onedaybeard.dominatrix.artemis.JsonComponentFactory.FactoryInstance;
import net.onedaybeard.dominatrix.util.Logger;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Artemis manager for creating entities defined in json data files.
 * 
 * @see EntityFactoryManager#addNewToWorld()
 */
public class EntityFactoryManager extends Manager
{
	public static final String TAG = EntityFactoryManager.class.getSimpleName();
	
	private final Array<Entity> notOfThisWorld;

	private final FileHandle jsonFile;
	private String componentPackage;

	private ComponentMapper<? extends Component> jsonKeyMapper;
	
	@Setter @Getter private boolean debugEntityCreation;

	private Class<? extends Component> jsonClass;

	private EntityFactoryManager(FileHandle jsonFile, Class<? extends Component> jsonClass, boolean debugCreation)
	{
		super();
		this.jsonFile = jsonFile;
		this.jsonClass = jsonClass;
		this.componentPackage = jsonClass.getPackage().getName();
		this.debugEntityCreation = debugCreation;
		notOfThisWorld = new Array<Entity>();
	}
	
	public static <T extends Component & JsonId> EntityFactoryManager from(FileHandle jsonFile, Class<T> jsonClass, boolean debugCreation)
	{
		return new EntityFactoryManager(jsonFile, jsonClass, debugCreation);
	}
	
	public static <T extends Component & JsonId> EntityFactoryManager from(FileHandle jsonFile, Class<T> jsonClass)
	{
		return from(jsonFile, jsonClass, false);
	}
	
	@Override @SuppressWarnings("unchecked")
	protected void initialize()
	{
		jsonKeyMapper = (ComponentMapper<? extends Component>)world.getMapper((Class<Component>)jsonClass);
	}
	
	public Entity create(Array<Component> components, String tag)
	{
		Entity entity;
		String id = getEntityTypeId(components);
		if (id != null) // creating entity w/ components from base type
			entity = create(id, tag);
		else
			entity = createEntity(tag);
		
		for (int i = 0, s = components.size; s > i; i++)
		{
			entity.addComponent(components.get(i));
		}
		
		return entity;
	}
	
	public Entity create(Array<Component> components)
	{
		return create(components, null);
	}
	
	public Entity create(String id)
	{
		return create(id, null);
	}
	
	public Entity create(String id, String tag)
	{
		Entity entity = createEntity(tag);
		
		entity.addComponent(createJsonKey(id));
		injectComponents(entity, id);
		
		return entity;
	}
	
	private Component createJsonKey(String id)
	{
		try
		{
			Component json = jsonClass.newInstance();
			return ((JsonId)json).name(id);
		}
		catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	private Entity createEntity(String tag)
	{
		Entity entity = world.createEntity();
		notOfThisWorld.add(entity);
		
		if (tag != null)
			world.getManager(TagManager.class).register(tag, entity);
		
		if (debugEntityCreation)
			Logger.log(TAG, "Created entity=%s tag=%s", getEntityIdString(entity), tag);
		
		return entity;
	}
	
	private String getEntityIdString(Entity e)
	{
		if (jsonKeyMapper.has(e))
			return ((JsonId)jsonKeyMapper.get(e)).name();
		else
			return String.valueOf(e.getId());
	}
	
	public void delayedAdd(Entity e)
	{
		notOfThisWorld.add(e);
	}
	
	/**
	 * When adding entities to the world during a process loop, managers and possibly systems
	 * might not get notified. Hence, call this method before {@link World#process()}.
	 */
	public void addNewToWorld()
	{
		if (notOfThisWorld.size == 0)
			return;
		
		if (debugEntityCreation)
			Logger.log(TAG, "Adding entities to world: %d", notOfThisWorld.size);
		
		for (int i = 0, s = notOfThisWorld.size; s > i; i++)
		{
			world.addEntity(notOfThisWorld.get(i));
		}
		notOfThisWorld.clear();
	}
	
	private static String getEntityTypeId(Array<Component> components)
	{
		for (int i = 0; components.size > i; i++)
		{
			if (components.get(i) instanceof JsonId)
				return ((JsonId)components.get(i)).name();
		}
		return null;
	}
	
	private Array<Component> injectComponents(Entity entity, String entityType)
	{
		FactoryInstance factory = JsonComponentFactory.from(jsonFile, componentPackage);
		
		Array<Component> parsed = factory.getComponents(entityType);
		for (int i = 0, s = parsed.size; s > i; i++)
		{
			entity.addComponent(parsed.get(i));
		}
		return parsed;
	}
}
