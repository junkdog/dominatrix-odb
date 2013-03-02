package net.onedaybeard.dominatrix.demo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

@Data @Accessors(fluent=true) @EqualsAndHashCode(callSuper=false)
public final class Position extends Component
{
	private final Vector2 point;

	public Position(float x, float y)
	{
		point = new Vector2(x, y);
	}

	public Position(Vector2 position)
	{
		point = position.cpy();
	}
	
	public Position()
	{
		this(0, 0);
	}

	public float x()
	{
		return point.x;
	}

	public float y()
	{
		return point.y;
	}

	public Vector2 set(Vector2 v)
	{
		return point.set(v);
	}

	public Vector2 set(float x, float y)
	{
		return point.set(x, y);
	}
}
