package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.math.Vector2;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 06/08/13
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public class ShipUpdateHandler implements IUpdateHandler {
	private static final float TURN_DEGREES_PER_SEC = 540;

	private PhysicsWorld mPhysicsWorld;
	private Entity mScene;
	private BoundCamera mBoundCamera;

	public ShipUpdateHandler(Scene mScene, PhysicsWorld mPhysicsWorld, BoundCamera mBoundCamera) {
		this.mScene = mScene;
		this.mPhysicsWorld = mPhysicsWorld;
		this.mBoundCamera = mBoundCamera;
	}

	@Override
	public void reset() {
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		Vector2 accelerometer = mPhysicsWorld.getGravity();
		float thrustRotation = new Vector2(-accelerometer.y, accelerometer.x).angle();

		IEntity shipEntity = mScene.getChildByTag(GameLevelLoader.TAGS.Ship.ordinal());
		float diffRotation = thrustRotation - shipEntity.getRotation();
		// Avoid ship turning 360 when rotation close to 0 degrees
		if (diffRotation < -180) diffRotation += 360;
		else if (diffRotation > 180) diffRotation -= 360;
		// bring the rotation to the max if it's over it
		float maxTurn = TURN_DEGREES_PER_SEC * pSecondsElapsed * (Math.min(9.8f, accelerometer.len()) / 9.8f);
		if (Math.abs(diffRotation) > maxTurn) {
			diffRotation = diffRotation > 0 ? maxTurn : -maxTurn;
		}
		float finalRotation = shipEntity.getRotation() + diffRotation;
		if (finalRotation > 360) finalRotation %= 360;
		else if (finalRotation < 0) finalRotation += 360;
		shipEntity.setRotation(finalRotation);

		//updates the camera chasing the ship
		mBoundCamera.onUpdate(pSecondsElapsed);
		//		mBoundCamera.updateChaseEntity();
	}
}
