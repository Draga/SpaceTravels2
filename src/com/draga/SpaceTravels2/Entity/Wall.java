package com.draga.SpaceTravels2.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.draga.SpaceTravels2.utility.EntityTags;
import com.draga.SpaceTravels2.utility.ResourcesManager;
import com.draga.SpaceTravels2.utility.Utility;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import static java.util.Arrays.asList;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 25/08/13
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class Wall extends Rectangle {
	private static final short MASK_BIT = Utility.getMaskBit(asList(EntityTags.Missile, EntityTags.Ship));
	private final static FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.5f, false, Utility.tagCategoryBit(EntityTags.Wall), MASK_BIT, (short) 0);

	public Wall(int pX, int pY, int pWidth, int pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

		createPhysics(ResourcesManager.getInstance().getFixedStepPhysicsWorld());
	}

	private void createPhysics(PhysicsWorld physicsWorld) {
		PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.StaticBody, wallFixtureDef);
	}
}
