package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.draga.SpaceTravels2.GameActivity;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 16/08/13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class OnSceneTouchListener implements IOnSceneTouchListener {
	private static final float MISSILE_BASE_SPEED = 300;
	private static final float MISSILE_ACCELERATION = 100;
	private static final float MISSILE_WIDTH = 16;
	private static final float MISSILE_HEIGHT = 32;
	private static final float MISSILE_THRUSTER_HEIGHT = 16;
	private static final float MISSILE_THRUSTER_WIDTH = 16;
	private final Body mShipBody;
	private final ITexture mTexture;
	private final ITextureRegion mTextureRegion;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private TiledTextureRegion mThrusterTiledTextureRegion;
	private Sound mMissileSound;
	private Scene mScene;
	private Sprite mShipSprite;
	private SoundManager mSoundManager;
	private GameActivity mGameActivity;

	public OnSceneTouchListener(Body pShipBody, Sprite pShipSprite, ITexture pTexture, TiledTextureRegion pThrusterTiledTextureRegion) {
		this.mShipBody = pShipBody;
		this.mShipSprite = pShipSprite;
		this.mThrusterTiledTextureRegion = pThrusterTiledTextureRegion;
		this.mVertexBufferObjectManager = ResourcesManager.getInstance().mVertexBufferObjectManager;
		this.mTexture = pTexture;
		this.mSoundManager = ResourcesManager.getInstance().mActivity.getSoundManager();
		this.mGameActivity = ResourcesManager.getInstance().mActivity;
		this.mScene = this.mGameActivity.getScene();
		this.mTextureRegion = TextureRegionFactory.extractFromTexture(mTexture);

		// Sound
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.mMissileSound = SoundFactory.createSoundFromAsset(mSoundManager, mGameActivity, "missileLaunch.mp3");
		} catch (final IOException e) {
			Debug.e(e);
		}
		this.mMissileSound.setVolume(0.2f);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (!pSceneTouchEvent.isActionDown()) return false;

		this.mMissileSound.play();
		//					final Sprite shipEntity = (Sprite) pScene.getChildByTag(TAGS.Ship.ordinal());
		final float[] MissileCoordinates = this.mShipSprite.convertLocalToSceneCoordinates(mShipSprite.getWidth() / 2 - MISSILE_WIDTH / 2, mShipSprite.getHeight() / 2);

		//TODO:use spritebatch?
		Sprite missileSprite = new Sprite(MissileCoordinates[0], MissileCoordinates[1], MISSILE_WIDTH, MISSILE_HEIGHT, mTextureRegion, mVertexBufferObjectManager);
		missileSprite.setZIndex(1);
		missileSprite.setRotation(mShipSprite.getRotation());

		//		final FixtureDef missileFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.8f);
		//		final Body missileBody = PhysicsFactory.createBoxBody(mPhysicsWorld, missileSprite, BodyDef.BodyType.KinematicBody, missileFixtureDef);
		//		missileBody.setUserData(EntityTags.Missile);
		//		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(missileSprite, missileBody, true, true));

		final Vector2 missileVelocity = new Vector2(0, -(MISSILE_BASE_SPEED + mShipBody.getLinearVelocity().len()));
		missileVelocity.rotate(mShipSprite.getRotation());

		final Vector2 missileAcceleration = new Vector2(0, -MISSILE_ACCELERATION);
		missileAcceleration.rotate(mShipSprite.getRotation());


		final PhysicsHandler missilePhysicsHandler = new PhysicsHandler(missileSprite);
		missilePhysicsHandler.setVelocity(missileVelocity.x, missileVelocity.y);
		missilePhysicsHandler.setAcceleration(missileAcceleration.x, missileAcceleration.y);
		mScene.registerUpdateHandler(missilePhysicsHandler);

		mScene.attachChild(missileSprite);

		AnimatedSprite thrusterSprite = new AnimatedSprite(0, MISSILE_THRUSTER_HEIGHT + MISSILE_HEIGHT / 2, MISSILE_THRUSTER_WIDTH, MISSILE_HEIGHT, mThrusterTiledTextureRegion, mVertexBufferObjectManager);
		thrusterSprite.setTag(EntityTags.MissileThruster.ordinal());
		thrusterSprite.setZIndex(1);
		thrusterSprite.animate(50);
		missileSprite.attachChild(thrusterSprite);

		this.mScene.registerUpdateHandler(new MissileCollisionHandler(missileSprite, ResourcesManager.getInstance().mActivity.getScene().query(new IEntityMatcher() {
			@Override
			public boolean matches(IEntity pEntity) {
				if (pEntity.getTag() == EntityTags.Earth.ordinal() || pEntity.getTag() == EntityTags.Mars.ordinal() || pEntity.getTag() == EntityTags.Venus.ordinal() || pEntity.getTag() == EntityTags.Jupiter.ordinal())
					return true;
				return false;
			}
		})));

		//TODO: remove sprite when out of sight

		return true;
	}
}
