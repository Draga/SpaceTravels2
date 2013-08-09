package com.draga.SpaceTravels2.utility;

import android.hardware.SensorManager;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 09/08/13
 * Time: 21:44
 * To change this template use File | Settings | File Templates.
 */
public class FlameUpdateHandler implements IUpdateHandler {
	private static final float FLAME_SIZE_MIN_SCALE = 0.5f;
	private static final float FLAME_ANIMATION_MAX_FRAME_DURATION = 200;
	private static final float FLAME_ANIMATION_MIN_FRAME_DURATION = 10;

	private PhysicsWorld mPhysicsWorld;
	private IEntity mFlameSprite;

	public FlameUpdateHandler(PhysicsWorld physicsWorld, IEntity flameSprite) {
		this.mPhysicsWorld = physicsWorld;
		this.mFlameSprite = flameSprite;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// Set max scale 1
		final float scale = Math.min(1, mPhysicsWorld.getGravity().len() / SensorManager.GRAVITY_EARTH);
		final float sizeScale = scale * (1 - FLAME_SIZE_MIN_SCALE) + FLAME_SIZE_MIN_SCALE;
		//							final float AnimationFrame = FLAME_ANIMATION_MAX_FRAME_DURATION-scale* (FLAME_ANIMATION_MAX_FRAME_DURATION - FLAME_ANIMATION_MIN_FRAME_DURATION);

		//							mFlameSprite.animate((long) AnimationFrame);
		mFlameSprite.setScale(sizeScale);
	}

	@Override
	public void reset() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
