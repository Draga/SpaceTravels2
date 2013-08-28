package com.draga.SpaceTravels2.entity;

import com.draga.SpaceTravels2.utility.ResourcesManager;
import org.andengine.entity.sprite.AnimatedSprite;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 26/08/13
 * Time: 00:04
 * To change this template use File | Settings | File Templates.
 */
public class Explosion extends AnimatedSprite {
	private static final ResourcesManager resourceManager = ResourcesManager.getInstance();

	public Explosion(float pX, float pY, float pHeight, float pWidth) {
		super(pX - pWidth / 2, pY - pHeight / 2, pWidth, pHeight, resourceManager.getExplosionTiledTextureRegion(), resourceManager.getVertexBufferObjectManager());
		animate(50, false, new ExplosionAnimationListener());
	}

	private class ExplosionAnimationListener implements IAnimationListener {

		@Override
		public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
		}

		@Override
		public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
		}

		@Override
		public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
			//				detachSelf();
		}

		@Override
		public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
			resourceManager.getEngine().runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					detachSelf();
				}
			});
		}
	}
}