package com.draga.SpaceTravels2.utility;

import com.draga.SpaceTravels2.GameActivity;
import com.draga.SpaceTravels2.entity.Missile;
import com.draga.SpaceTravels2.entity.Ship;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 16/08/13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class SceneTouchListener implements IOnSceneTouchListener {
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private TiledTextureRegion mThrusterTiledTextureRegion;
	private Scene mScene;
	private Ship mShip;
	private GameActivity mGameActivity;
	private final static ResourcesManager resourcesManager = ResourcesManager.getInstance();

	public SceneTouchListener(Ship pShip) {
		this.mShip = pShip;
		this.mThrusterTiledTextureRegion = resourcesManager.getThrusterTiledTextureRegion();
		this.mVertexBufferObjectManager = resourcesManager.getVertexBufferObjectManager();
		this.mGameActivity = resourcesManager.getActivity();
		this.mScene = this.mGameActivity.getScene();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (!pSceneTouchEvent.isActionDown()) return false;

		//					final Sprite shipEntity = (Sprite) pScene.getChildByTag(TAGS.Ship.ordinal());
		//		final float[] missileCoordinates = mShip.convertLocalToSceneCoordinates(mShip.getWidth()/2-Missile.WIDTH/2, mShip.getHeight()/2-Missile.HEIGHT/2);

		//TODO:use spritebatch?
		Sprite missileSprite = new Missile(mShip);
		mScene.attachChild(missileSprite);
		ResourcesManager.getInstance().getScene().sortChildren();

		//		this.mScene.registerUpdateHandler(new MissileCollisionHandler(missileSprite, resourcesManager.getActivity().getScene().query(new IEntityMatcher() {
		//			@Override
		//			public boolean matches(IEntity pEntity) {
		//				if (pEntity.getTag() == EntityTags.Earth.ordinal() || pEntity.getTag() == EntityTags.Mars.ordinal() || pEntity.getTag() == EntityTags.Venus.ordinal() || pEntity.getTag() == EntityTags.Jupiter.ordinal())
		//					return true;
		//				return false;
		//			}
		//		})));

		return true;
	}
}
