package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.andengine.audio.music.Music;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 06/08/13
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public class ShipPhysicsConnector extends PhysicsConnector {
	private static final float TURN_DEGREES_PER_SEC = 720;
	private static final float MAX_SPEED = 15;
	private final PhysicsWorld mPhysicsWorld;
	//		private PhysicsWorld mPhysicsWorld;
	private BoundCamera mBoundCamera;
	private Sprite mSprite;
	private Scene mScene;
	private Music mThrusterMusic;

	public ShipPhysicsConnector(BoundCamera pBoundCamera, Sprite pSprite, PhysicsWorld pPhysicsWorld, Body pBody, Scene mScene, Music pThrusterMusic) {
		super(pSprite, pBody);
		this.mPhysicsWorld = pPhysicsWorld;
		this.mBoundCamera = pBoundCamera;
		this.mSprite = pSprite;
		this.mScene = mScene;
		this.mThrusterMusic = pThrusterMusic;
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		// Sets max speed
		maxVelocity();

		updateRotation(pSecondsElapsed);

		super.onUpdate(pSecondsElapsed);

		applyPlanetsGravity(pSecondsElapsed);

		//updates the camera chasing the ship
		mBoundCamera.onUpdate(pSecondsElapsed);
	}

	private void maxVelocity() {
		final Vector2 linearVelocity = this.mBody.getLinearVelocity();
		float speed = linearVelocity.len();
		if (speed > MAX_SPEED) {
			final float exceeded = speed / MAX_SPEED;
			this.mBody.setLinearVelocity(linearVelocity.div(exceeded));
		}

		final IEntity speedBar = mBoundCamera.getHUD().getChildByTag(EntityTags.SpeedBar.ordinal());
		final float speedOnMax = this.mBody.getLinearVelocity().len() / MAX_SPEED;
		speedBar.setScaleX(speedOnMax);
		speedBar.setColor(speedOnMax, 1 - speedOnMax, 0);

		mThrusterMusic.setVolume(speedOnMax);
	}

	private void applyPlanetsGravity(float pSecondsElapsed) {
		for (int i = 0; i < EntityTags.values().length; i++) {
			final EntityTags entityTag = EntityTags.values()[i];
			IEntity entity = mScene.getChildByTag(entityTag.ordinal());
			switch (entityTag) {
				case Earth:
					break;
			}
		}
	}

	private void updateRotation(float pSecondsElapsed) {
		float thrustRotation = new com.badlogic.gdx.math.Vector2(-mPhysicsWorld.getGravity().y, mPhysicsWorld.getGravity().x).angle();
		final float accelerometerRatio = Math.min(9.8f, mPhysicsWorld.getGravity().len()) / 9.8f;

		double diffRotation = thrustRotation - mBody.getAngle() * MathUtils.radDeg;
		// Avoid ship turning 360 when rotation close to 0 degrees
		if (diffRotation < -180) diffRotation += 360;
		else if (diffRotation > 180) diffRotation -= 360;
		// bring the rotation to the max if it's over it
		float maxTurn = TURN_DEGREES_PER_SEC * pSecondsElapsed * accelerometerRatio;
		if (Math.abs(diffRotation) > maxTurn) {
			diffRotation = diffRotation > 0 ? maxTurn : -maxTurn;
		}
		double finalRotation = mBody.getAngle() * MathUtils.radDeg + diffRotation;
		//Brings the finalRotation between 0 and 360
		if (finalRotation > 360) finalRotation %= 360;
		else if (finalRotation < 0) finalRotation += 360;

		mBody.setTransform(mBody.getWorldCenter(), (float) finalRotation * MathUtils.degRad);
	}

	@Override
	public void reset() {
		super.reset();
	}
}