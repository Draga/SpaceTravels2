package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.draga.SpaceTravels2.entity.Planet;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 25/08/13
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class Utility {
	public static short tagCategoryBit(EntityTags tag) {
		return (short) Math.pow(2, tag.ordinal());
	}

	public static Vector2 getPlanetsGravity(final Body pBody) {
		Vector2 forceAcceleration = new Vector2();
		for (Iterator<Body> iterator = ResourcesManager.getInstance().getFixedStepPhysicsWorld().getBodies(); iterator.hasNext(); ) {
			Body currentBody = iterator.next();
			if (!(currentBody.getUserData() instanceof Planet))
				continue;
			Planet planet = (Planet) currentBody.getUserData();
			Vector2 dir = currentBody.getPosition().sub(pBody.getPosition()); //get the direction from the mass to the body
			float dist = dir.len2(); //gives the distance squared (uses less processing, and it would be squared later anyway)
			dir = dir.nor(); //normalize the direction
			float forceMagnitude = pBody.getMass() * planet.getMass() / dist;
			forceAcceleration = forceAcceleration.add(dir.mul(forceMagnitude));
		}
		return forceAcceleration;
	}

	public static short getMaskBit(List<EntityTags> entityTags) {
		short bit = 0;
		for (Iterator<EntityTags> i = entityTags.iterator(); i.hasNext(); ) {
			bit += tagCategoryBit(i.next());
		}
		return bit;
	}
}
