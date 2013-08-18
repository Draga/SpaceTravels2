package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 17/08/13
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
public class GameContactListener implements ContactListener {
	@Override
	public void beginContact(Contact contact) {
		final Body bodyA = contact.getFixtureA().getBody();
		final Body bodyB = contact.getFixtureB().getBody();
		if (!(bodyA.getUserData() instanceof EntityTags) || !(bodyB.getUserData() instanceof EntityTags)) return;
		final EntityTags bodyAUserData = (EntityTags) bodyA.getUserData();

		switch (bodyAUserData) {
			case Ship:
				break;
			case Missile:
				switch ((EntityTags) bodyB.getUserData()) {
					case Earth:
					case Jupiter:
					case Mars:
					case Venus:
						break;
					default:
						break;
				}
				break;
			case Earth:
				break;
			case Jupiter:
			case Mars:
			case Venus:
				break;
			default:
				break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
