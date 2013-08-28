package com.draga.SpaceTravels2.entity;

import com.draga.SpaceTravels2.utility.EntityTags;
import com.draga.SpaceTravels2.utility.ResourcesManager;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 25/08/13
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public abstract class GameSprite extends Sprite {
	public static final ResourcesManager resourceManager = ResourcesManager.getInstance();
	public final EntityTags entityTag;
	protected boolean mIsDisposing;

	public GameSprite(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, EntityTags pEntityTag) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, resourceManager.getVertexBufferObjectManager());
		entityTag = pEntityTag;
	}
}
