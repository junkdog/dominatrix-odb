package net.onedaybeard.dominatrix.demo.event;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class CommandEventListener implements EventListener
{
	@Override
	public boolean handle(Event event)
	{
		if (!(event instanceof CommandEvent))
			return false;
		
		CommandEvent e = (CommandEvent)event;
		return accepts(e, e.getType()) ? onReceive(e, e.getType()) : false;
	}
	
	protected boolean accepts(CommandEvent event, CommandEvent.Type type)
	{
		return true;
	}
	
	protected abstract boolean onReceive(CommandEvent event, CommandEvent.Type type);
}
