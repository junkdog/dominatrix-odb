package net.onedaybeard.dominatrix.demo.manager;


import net.onedaybeard.dominatrix.demo.Assets;
import net.onedaybeard.dominatrix.demo.component.AssetReference;
import net.onedaybeard.dominatrix.demo.component.Renderable;
import net.onedaybeard.dominatrix.demo.component.Size;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.graphics.g2d.Sprite;


public final class RenderableResolverManager extends Manager
{
	private ComponentMapper<AssetReference> assetReferenceMapper;
	@Override
	protected void initialize()
	{
		assetReferenceMapper = world.getMapper(AssetReference.class);
	}

	@Override
	public void added(Entity e)
	{
		AssetReference reference = assetReferenceMapper.getSafe(e);
		if (reference == null)
			return;
		
		Sprite sprite = Assets.allocateSprite(reference.getPath());
//		if (reference.getFrame() != null)
//			sprite = Assets.allocateSprites(reference.getAtlas(), reference.getFrame()).first();
//		else
//			sprite = Assets.allocateSprite(reference.getAtlas());
		
		
//		SpriteFlip flip = reference.getFlip();
//		if (flip == null)
//			flip = SpriteFlip.NO_FLIPPING;
		
//		sprite.flip(flip.flipX(), flip.flipY());
		
//		if (reference.isAnimated())
//		{
//			Array<Sprite> sprites = Assets.allocateSprites(reference.getAtlas(), reference.getFrame());
//			Animation animation = new Animation(sprites, reference.getDuration());
//			
//			for (int i = 0, s = sprites.size; s > i; i++)
//				sprites.get(i).flip(flip.flipX(), flip.flipY());
//			
//			e.addComponent(animation);
//		}
		
		e.addComponent(new Renderable(sprite));
		e.addComponent(new Size(sprite.getWidth(), sprite.getHeight()));
		e.changedInWorld();
	}
}
