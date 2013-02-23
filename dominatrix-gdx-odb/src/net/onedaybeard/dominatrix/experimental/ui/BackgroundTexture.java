package net.onedaybeard.dominatrix.experimental.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public final class BackgroundTexture
{
	private BackgroundTexture()
	{
		
	}
	
	private static Texture texture;

	public static TiledDrawable getDrawable()
	{
		if (texture == null)
		{
			Pixmap newsBackground = new Pixmap(20, 20, Format.RGBA4444);
			newsBackground.setColor(new Color(0.15f, 0.15f, 0.320f, 0.6f));
			newsBackground.fill();
			texture = new Texture(newsBackground);
			newsBackground.dispose();
		}
		return new TiledDrawable(new TextureRegion(texture));
	}
}
