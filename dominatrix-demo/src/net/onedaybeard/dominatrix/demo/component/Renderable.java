package net.onedaybeard.dominatrix.demo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.onedaybeard.dominatrix.artemis.ExcludeFromJson;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

@Data @EqualsAndHashCode(callSuper=false) @ExcludeFromJson
public final class Renderable extends Component
{
	private Sprite sprite;
	
	public Renderable(Sprite sprite)
	{
		this.sprite = sprite;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s(sprite=%s)",
			getClass().getSimpleName(), formatSprite(sprite));
	}
	
	private static String formatSprite(Sprite sprite)
	{
		if (sprite == null)
			return "null";
		
		String scale = sprite.getScaleX() == sprite.getScaleY()
			? String.format("%.2f", sprite.getScaleX())
			: String.format("%.2fx%.2f", sprite.getScaleX(), sprite.getScaleY());
		
		return String.format("(pos=%.2fx%.2f, size=%.2fx%.2f, offset=%.2fx%.2f, scale=%s)",
				sprite.getX(), sprite.getY(),
				sprite.getWidth(), sprite.getHeight(),
				sprite.getOriginX(), sprite.getOriginY(),
				scale);
	}
}
