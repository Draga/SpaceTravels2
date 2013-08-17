package com.draga.SpaceTravels2.utility;

import android.content.Context;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
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
	private static final float MISSILE_ACCELERATION = 200;
	private static final float MISSILE_WIDTH = 16;
	private static final float MISSILE_HEIGHT = 32;
	private final Body mShipBody;
	private final ITexture mTexture;
	private final ITextureRegion mTextureRegion;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private Sound mMissileSound;
	private Scene mScene;
	private Sprite mShipSprite;
	private SoundManager mSoundManager;
	private Context mContext;

	public OnSceneTouchListener(Body pShipBody, Scene pScene, Sprite pShipSprite, ITexture pTexture, VertexBufferObjectManager pVertexBufferObjectManager, SoundManager pSoundManager, Context pContext) {
		this.mShipBody = pShipBody;
		this.mScene = pScene;
		this.mShipSprite = pShipSprite;
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
		this.mTexture = pTexture;
		this.mSoundManager = pSoundManager;
		this.mContext = pContext;
		this.mTextureRegion = TextureRegionFactory.extractFromTexture(mTexture);

		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.mMissileSound = SoundFactory.createSoundFromAsset(mSoundManager, mContext, "missileLaunch.mp3");
		} catch (final IOException e) {
			Debug.e(e);
		}
		this.mMissileSound.setVolume(0.5f);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (!pSceneTouchEvent.isActionDown()) return false;

		this.mMissileSound.play();
		//					final Sprite shipEntity = (Sprite) pScene.getChildByTag(TAGS.Ship.ordinal());
		final float[] weaponCoordinates = this.mShipSprite.convertLocalToSceneCoordinates(mShipSprite.getWidth() / 2, mShipSprite.getHeight() / 2);

		//					final FixtureDef missileFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.8f);
		//					final Body missileBody = PhysicsFactory.createBoxBody(mPhysicsWorld, missileSprite, BodyDef.BodyType.KinematicBody, missileFixtureDef);
		Sprite missileSprite = new Sprite(weaponCoordinates[0] - MISSILE_WIDTH / 2, weaponCoordinates[1] - MISSILE_HEIGHT, MISSILE_WIDTH, MISSILE_HEIGHT, mTextureRegion, mVertexBufferObjectManager);
		missileSprite.setRotation(mShipSprite.getRotation());

		final Vector2 missileSpeed = new Vector2(0, -(MISSILE_BASE_SPEED + mShipBody.getLinearVelocity().len()));
		missileSpeed.rotate(mShipSprite.getRotation());

		final Vector2 missileAcceleration = new Vector2(0, -MISSILE_ACCELERATION);
		missileAcceleration.rotate(mShipSprite.getRotation());

		final PhysicsHandler missilePhysicsHandler = new PhysicsHandler(missileSprite);
		missilePhysicsHandler.setVelocity(missileSpeed.x, missileSpeed.y);
		missilePhysicsHandler.setAcceleration(missileAcceleration.x, missileAcceleration.y);
		mScene.registerUpdateHandler(missilePhysicsHandler);

		mScene.attachChild(missileSprite);
		//					mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(missileSprite, missileBody, true, false));

		//TODO: remove sprite when out of sight

		return true;
	}
}
