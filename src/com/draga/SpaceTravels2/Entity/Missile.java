package com.draga.SpaceTravels2.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Transform;
import com.draga.SpaceTravels2.utility.EntityTags;
import com.draga.SpaceTravels2.utility.ResourcesManager;
import com.draga.SpaceTravels2.utility.Utility;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 24/08/13
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public class Missile extends Sprite {
	public static final int WIDTH = 16;
	public static final int HEIGHT = 32;
	public static final int Z_INDEX = 1;
	private static final float BASE_SPEED = 0;
	private static final float ACCELERATION = 18;
	private static final float THRUSTER_HEIGHT = 16;
	private static final float THRUSTER_WIDTH = 16;
	//	private static final float FLAME_ANIMATION_MAX_FRAME_DURATION = 200;
	//	private static final float FLAME_ANIMATION_MIN_FRAME_DURATION = 10;
	private static final Vector2 mMissileAcceleration = new Vector2(0, -ACCELERATION);
	private final static ResourcesManager resourcesManager = ResourcesManager.getInstance();
	private static final short MASK_BIT = 0;
	private final FixedStepPhysicsWorld mFixedStepPhysicsWorld;
	private final AnimatedSprite mThrusterSprite;
	private Body mBody;

	public Missile(Ship pShip) {
		super(pShip.getX(), pShip.getY(), WIDTH, HEIGHT, resourcesManager.getMissileTextureRegion(), resourcesManager.getVertexBufferObjectManager());
		mFixedStepPhysicsWorld = resourcesManager.getFixedStepPhysicsWorld();
		final EntityTags missileTag = EntityTags.Missile;


		createPhysics(mFixedStepPhysicsWorld, pShip);

		setZIndex(Z_INDEX);
		setTag(missileTag.ordinal());

		mThrusterSprite = new AnimatedSprite(0, THRUSTER_HEIGHT + Missile.HEIGHT / 2, THRUSTER_WIDTH, Missile.HEIGHT, resourcesManager.getThrusterTiledTextureRegion(),
				resourcesManager.getVertexBufferObjectManager());
		mThrusterSprite.setTag(EntityTags.MissileThruster.ordinal());
		mThrusterSprite.animate(50);
		attachChild(mThrusterSprite);
	}

	private void createPhysics(PhysicsWorld physicsWorld, Ship ship) {
		final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(Z_INDEX, 0, 0.8f, false, Utility.tagCategoryBit(EntityTags.Missile), MASK_BIT, (short) 0);
		mBody = PhysicsFactory.createBoxBody(physicsWorld, mX, mY, mWidth, mHeight, ship.getRotation(), BodyDef.BodyType.KinematicBody, fixtureDef);
		mBody.setUserData(EntityTags.Missile);
		final Transform transform = ship.getBody().getTransform();
		mBody.setTransform(transform.getPosition(), transform.getRotation());
		final Vector2 missileVelocity = new Vector2(0, -(BASE_SPEED + ship.getBody().getLinearVelocity().len()));
		missileVelocity.rotate(ship.getRotation());
		mBody.setLinearVelocity(missileVelocity);

		//				final PhysicsHandler missilePhysicsHandler = new PhysicsHandler(mBody);
		//				missilePhysicsHandler.setVelocity(missileVelocity.x, missileVelocity.y);
		//				missilePhysicsHandler.setAcceleration(mMissileAcceleration.x, mMissileAcceleration.y);
		//				mScene.registerUpdateHandler(missilePhysicsHandler);


		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, mBody, true, true));
		resourcesManager.getEngine().registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				Vector2 missileAcceleration = mMissileAcceleration.cpy().rotate(getRotation()).mul(pSecondsElapsed);
				mBody.setLinearVelocity(mBody.getLinearVelocity().add(missileAcceleration));
				//TODO: set max velocity
			}

			@Override
			public void reset() {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
	}
}
