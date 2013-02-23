package net.onedaybeard.dominatrix.experimental.artemis;


import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

import net.onedaybeard.dominatrix.annotation.Sloppy;
import net.onedaybeard.dominatrix.artemis.ComponentNameComparator;
import net.onedaybeard.dominatrix.util.Tree;

@Sloppy("refuctor")
public final class CommandUtils
{
	public static Tree<ObjectNode> feedComponents(Tree<ObjectNode> systemTree, Entity e)
	{
		if (systemTree == null)
			systemTree = new Tree<ObjectNode>();
		
		Tree<ObjectNode> entityNode = systemTree.addNode(new EntityNode(e));
		Bag<Component> components = e.getComponents(new Bag<Component>());
		components.sort(new ComponentNameComparator());
		
		for (int i = 0, s = components.size(); s > i; i++)
		{
			Component component = components.get(i);
			entityNode.addNode(ComponentNode.from(component));
		}
		
		return systemTree;
	}
	
	public static StringBuilder formatTree(Tree<ObjectNode> tree, StringBuilder buffer)
	{
		if (buffer == null)
			buffer = new StringBuilder();
		
		
		ObjectNode node = tree.getValue();
		if (node != null)
		{
			buffer.append(treeFormatNode(tree));
			buffer.append(node.format());
		}
		
		for (Tree<ObjectNode> childNode : tree.getChildNodes())
		{
			formatTree(childNode, buffer);
		}
		
		return buffer;
	}
	
	private static String treeFormatNode(Tree<ObjectNode> tree)
	{
		StringBuilder sb = new StringBuilder();
		
		Tree<ObjectNode> treeCopy = tree;
		if (!treeCopy.getParent().isRootNode())
		{
			sb.append(treeCopy.isLastNode() ? "\\-- " : "|-- ");
		}
		else
		{
			sb.append( "=-- " );
		}
		
		while ((treeCopy = treeCopy.getParent()) != null)
		{
			if (treeCopy.isRootNode()) // there's no root to visualize
				break;
			
			if (treeCopy.isLastNode() || treeCopy.getParent().isRootNode())
				sb.insert(0, "    ");
			else
				sb.insert(0, "|   ");
		}
		
		return sb.toString();
	}
	
	static Tree<ObjectNode> getSystems(ImmutableBag<EntitySystem> systems)
	{
		Tree<ObjectNode> systemTree = new Tree<ObjectNode>();
		
		Tree<ObjectNode> automatic = systemTree.addNode(new LabelNode("automatic"));
		Tree<ObjectNode> passive = systemTree.addNode(new LabelNode("passive"));
		
		for (int i = 0, s = systems.size(); s > i; i++)
		{
			EntitySystem system = systems.get(i);
			
			if (system.isPassive())
				passive.addNode(SystemNode.from(system));
			else
				automatic.addNode(SystemNode.from(system));
		}
		
		return systemTree;
	}
	
	static Entity getEntity(World world, String entity)
	{
		if (entity.matches("^\\d*$"))
			return world.getEntity(Integer.parseInt(entity));
		else
			return world.getManager(TagManager.class).getEntity(entity);
	}
	
	public static interface ObjectNode
	{
		String format();
	}
	
	private static class LabelNode implements ObjectNode
	{
		private String label;

		public LabelNode(String label)
		{
			this.label = label + "\n";
		}
		
		@Override
		public String format()
		{
			return label;
		}
	}
	
	static class SystemNode implements ObjectNode
	{
		private EntitySystem system;
		private static int longestName;
		
		public SystemNode(EntitySystem system)
		{
			int nameLength = system.getClass().getSimpleName().length();
			if (longestName < nameLength)
				longestName = nameLength;
			
			this.system = system;
		}
		
		public static SystemNode from(EntitySystem system)
		{
			return new SystemNode(system);
		}

		@Override
		public String format()
		{
			String format = system.isEnabled() 
				? "%-" + longestName + "s entities: %d\n"
				: "(%-" + longestName + "s entities: %d)\n";
			
			return String.format(format,
				system.getClass().getSimpleName(),
				system.getActives().size());
		}
	}
	
	static class EntityNode implements ObjectNode
	{
		private Entity entity;

		public EntityNode(Entity e)
		{
			this.entity = e;
		}
		
		@Override
		public String format()
		{
			return String.format("Entity[%d] (%s)\n", entity.getId(), entity.getUuid().toString());
		}
	}
	
	private static class ComponentNode implements ObjectNode
	{
		private static int longestName;
		private Component component;
		
		private ComponentNode(Component component)
		{
			int nameLength = component.getClass().getSimpleName().length();
			if (longestName < nameLength)
				longestName = nameLength;
			
			this.component = component;
		}
		
		public static ComponentNode from(Component component)
		{
			return new ComponentNode(component);
		}

		@Override
		public String format()
		{
			return String.format("%s\n", component);
		}
	}
}
