package com.draga.SpaceTravels2.utility;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.Shape;
import org.andengine.util.adt.list.ListUtils;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 17/08/13
 * Time: 23:17
 * To change this template use File | Settings | File Templates.
 */
public class MissileCollisionHandler implements IUpdateHandler {

	//	private final ICollisionCallback mCollisionCallback;
	private final IEntity mCheckShape;
	private final ArrayList<? extends IEntity> mTargetStaticEntities;

	public MissileCollisionHandler(final IEntity pCheckShape, final IEntity pTargetShape) throws IllegalArgumentException {
		this(pCheckShape, ListUtils.toList(pTargetShape));
	}

	public MissileCollisionHandler(final IEntity pCheckShape, final ArrayList<? extends IEntity> pTargetStaticEntities) throws IllegalArgumentException {
		if (pCheckShape == null) {
			throw new IllegalArgumentException("pCheckShape must not be null!");
		}
		if (pTargetStaticEntities == null) {
			throw new IllegalArgumentException("pTargetStaticEntities must not be null!");
		}

		this.mCheckShape = pCheckShape;
		this.mTargetStaticEntities = pTargetStaticEntities;
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		final Shape checkShape = (Shape) this.mCheckShape;
		final ArrayList<? extends IEntity> staticEntities = this.mTargetStaticEntities;
		final int staticEntityCount = staticEntities.size();

		for (int i = 0; i < staticEntityCount; i++) {
			final Shape otherShape = (Shape) staticEntities.get(i);
			if (checkShape.collidesWith(otherShape)) {
				//				final boolean proceed = this.mCollisionCallback.onCollision(checkShape, otherShape);
				//				if (!proceed) {
				switch (EntityTags.values()[otherShape.getTag()]) {
					case Earth:
					case Mars:
					case Venus:
					case Jupiter:
						explode();
						break;
					default:
						break;
				}
				//				}
			}
		}
	}

	private void explode() {
		ResourcesManager.getInstance().getActivity().getScene().detachChild(mCheckShape);
	}

	@Override
	public void reset() {
	}
}
