package net.onedaybeard.dominatrix.demo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

@Data @Accessors(fluent=true) @EqualsAndHashCode(callSuper=false)
public final class Velocity extends Component
{
	private final Vector2 vector;

	public Velocity(float x, float y)
	{
		vector = new Vector2(x, y);
	}

	public Velocity(Vector2 position)
	{
		vector = position.cpy();
	}
	
	public Velocity()
	{
		this(0, 0);
	}

	public Vector2 get()
	{
		return vector;
	}
	
	public float x()
	{
		return vector.x;
	}

	public float y()
	{
		return vector.y;
	}

	public Vector2 set(float x, float y)
	{
		return vector.set(x, y);
	}
}
