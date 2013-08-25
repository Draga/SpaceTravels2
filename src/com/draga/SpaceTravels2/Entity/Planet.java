package com.draga.SpaceTravels2.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.draga.SpaceTravels2.utility.EntityTags;
import com.draga.SpaceTravels2.utility.ResourcesManager;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 23/08/13
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public class Planet extends Sprite {
	private Body mBody;

	public Planet(int pX, int pY, int pWidth, int pHeight, TextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, EntityTags pTag, float pMass) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);

		this.setTag(pTag.ordinal());
		createPhysics(pTag, 100, 100);
	}

	private void createPhysics(EntityTags tag, float pMass, float pGravityScale) {
		PhysicsWorld physicsWorld = ResourcesManager.getInstance().getFixedStepPhysicsWorld();
		final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0, false, (short) tag.ordinal(), (short) tag.ordinal(), (short) 0);
		mBody = PhysicsFactory.createCircleBody(physicsWorld, this, BodyDef.BodyType.StaticBody, fixtureDef);
		final MassData massData = new MassData();
		massData.mass = pMass;
		mBody.setMassData(massData);
		mBody.setGravityScale(pGravityScale);

		mBody.setUserData(tag);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, mBody, false, false));
	}
}
