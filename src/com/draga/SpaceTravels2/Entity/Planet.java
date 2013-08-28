package com.draga.SpaceTravels2.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.draga.SpaceTravels2.utility.EntityTags;
import com.draga.SpaceTravels2.utility.ResourcesManager;
import com.draga.SpaceTravels2.utility.Utility;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TextureRegion;

import static java.util.Arrays.asList;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 23/08/13
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public class Planet extends Sprite {
	private static final short MASK_BIT = Utility.getMaskBit(asList(EntityTags.Ship, EntityTags.Missile));
	private static final ResourcesManager resourcesManager = ResourcesManager.getInstance();
	private Body mBody;
	private float mMass = 1;

	public Planet(int pX, int pY, int pWidth, int pHeight, TextureRegion pTextureRegion, EntityTags pTag, float pMass) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, resourcesManager.getVertexBufferObjectManager());
		mMass = pMass;

		this.setTag(pTag.ordinal());
		createPhysics();
	}

	private void createPhysics() {
		PhysicsWorld physicsWorld = resourcesManager.getFixedStepPhysicsWorld();
		final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0, false, Utility.tagCategoryBit(EntityTags.Planet), MASK_BIT, (short) 0);
		mBody = PhysicsFactory.createCircleBody(physicsWorld, this, BodyDef.BodyType.StaticBody, fixtureDef);

		mBody.setUserData(this);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, mBody, true, true));
	}

	public float getMass() {
		return mMass;
	}
}
