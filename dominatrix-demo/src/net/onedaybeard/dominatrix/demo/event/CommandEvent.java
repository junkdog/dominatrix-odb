package net.onedaybeard.dominatrix.demo.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.badlogic.gdx.scenes.scene2d.Event;

@Data @EqualsAndHashCode(callSuper=false)
public final class CommandEvent extends Event
{
	public static enum Type
	{
		ENTITY_SELECTED,
		HOVERED_ENTITY,
		NO_HOVERED_ENTITY,
		PROPERTIES_INJECTED;
	}

	private Type type;
	private int value;
	
	@Override
	public void reset()
	{
		type = null;
		value = 0;
		super.reset();
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public void setValue(float value)
	{
		this.value = Float.floatToIntBits(value);
	}
	
	public float getFloatValue()
	{
		return Float.intBitsToFloat(value);
	}
}
