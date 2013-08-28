package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 26/08/13
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public interface IEntityContactListener {
	public void onContact(Contact contact, Body otherBody);
}
