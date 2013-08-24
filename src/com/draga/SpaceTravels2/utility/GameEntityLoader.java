package com.draga.SpaceTravels2.utility;

import com.draga.SpaceTravels2.GameActivity;
import com.draga.SpaceTravels2.entity.Planet;
import com.draga.SpaceTravels2.entity.Ship;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.IEntityLoader;
import org.xml.sax.Attributes;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 09/08/13
 * Time: 21:37
 * To change this template use File | Settings | File Templates.
 */
public class GameEntityLoader implements IEntityLoader {
	//the y offset of the flame
	public static final float THRUSTER_OFFSET = -16;
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SHIP = "ship";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTH = "earth";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_JUPITER = "jupiter";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MARS = "mars";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_VENUS = "venus";

	public GameEntityLoader() {
	}

	@Override
	public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
		final ResourcesManager resourcesManager = ResourcesManager.getInstance();
		GameActivity gameActivity = resourcesManager.getActivity();
		FixedStepPhysicsWorld fixedStepPhysicsWorld = gameActivity.getFixedStepPhysicsWorld();
		VertexBufferObjectManager vertexBufferObjectManager = resourcesManager.getVertexBufferObjectManager();
		final Scene scene = resourcesManager.getActivity().getScene();

		final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
		final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
		final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X) - width / 2;
		final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y) - height / 2;
		final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

		final EntityTags entityTag = EntityTags.valueOf(type);
		switch (entityTag) {
			case Ship:
				final Ship ship = new Ship(x, y, width, height, resourcesManager.getShipTextureRegion(), vertexBufferObjectManager, gameActivity.mBoundCamera);
				//			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(shipSprite, shipBody, true, false));
				//			mScene.attachChild(shipSprite);
				gameActivity.mBoundCamera.setChaseEntity(ship);
				gameActivity.mBoundCamera.setBoundsEnabled(true);


				scene.registerUpdateHandler(new ShipPhysicsConnector(gameActivity.mBoundCamera, ship, fixedStepPhysicsWorld, ship.getBody(), resourcesManager.getThrusterMusic()));

				// Thruster
				final AnimatedSprite thrusterSprite = new AnimatedSprite(0, resourcesManager.getThrusterTiledTextureRegion().getHeight() / 2 + height / 2 + THRUSTER_OFFSET,
						resourcesManager.getThrusterTiledTextureRegion(), vertexBufferObjectManager);
				thrusterSprite.setTag(EntityTags.Thruster.ordinal());
				thrusterSprite.animate(50);

				ship.attachChild(thrusterSprite);
				// TODO: attach both to an entity and sort their Z index there
				//			thrusterSprite.setZIndex(0);
				//			shipSprite.setZIndex(1);

				thrusterSprite.setScaleCenter(thrusterSprite.getWidth() / 2, 0);

				thrusterSprite.registerUpdateHandler(new ThrusterUpdateHandler(thrusterSprite, gameActivity.getFixedStepPhysicsWorld()));
				return ship;
			case Venus:
				Sprite sprite = new Planet(x, y, width, height, resourcesManager.getVenusTextureRegion(), vertexBufferObjectManager, entityTag);
				return sprite;
			case Earth:
				sprite = new Planet(x, y, width, height, resourcesManager.getEarthTextureRegion(), vertexBufferObjectManager, entityTag);
				return sprite;
			case Jupiter:
				sprite = new Planet(x, y, width, height, resourcesManager.getJupiterTextureRegion(), vertexBufferObjectManager, entityTag);
				return sprite;
			case Mars:
				sprite = new Planet(x, y, width, height, resourcesManager.getMarsTextureRegion(), vertexBufferObjectManager, entityTag);
				return sprite;
			default:
				throw new IllegalArgumentException();
		}
		//			final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.8f);
		//			objectFixtureDef.shape = new CircleShape();
		//			final Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, planetSprite, BodyDef.BodyType.StaticBody, objectFixtureDef);

		//			body.setUserData(EntityTags.Ship);
		//			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(planetSprite, body, false, false));

		//						mScene.attachChild(planetSprite);
	}
}
