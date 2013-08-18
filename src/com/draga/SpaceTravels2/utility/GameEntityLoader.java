package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.draga.SpaceTravels2.GameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.IEntityLoader;
import org.xml.sax.Attributes;

import java.io.IOException;

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
		GameActivity gameActivity = ResourcesManager.getInstance().mActivity;
		FixedStepPhysicsWorld fixedStepPhysicsWorld = gameActivity.getFixedStepPhysicsWorld();
		VertexBufferObjectManager vertexBufferObjectManager = ResourcesManager.getInstance().mVertexBufferObjectManager;
		final Scene scene = ResourcesManager.getInstance().mActivity.getScene();

		final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
		final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
		final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X) - width / 2;
		final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y) - height / 2;
		final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);


		if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SHIP)) {
			final Sprite shipSprite = loadSprite("gfx/ship64.png", x, y, width, height, vertexBufferObjectManager);
			shipSprite.setTag(EntityTags.Ship.ordinal());
			shipSprite.setZIndex(2);
			final FixtureDef shipFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.8f);
			final Body shipBody = PhysicsFactory.createBoxBody(fixedStepPhysicsWorld, shipSprite, BodyDef.BodyType.DynamicBody, shipFixtureDef);
			shipBody.setUserData(EntityTags.Ship);
			//			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(shipSprite, shipBody, true, false));
			//			mScene.attachChild(shipSprite);
			gameActivity.mBoundCamera.setChaseEntity(shipSprite);
			gameActivity.mBoundCamera.setBoundsEnabled(true);

			// Thruster sound
			MusicFactory.setAssetBasePath("mfx/");
			Music thrusterMusic;
			try {
				thrusterMusic = MusicFactory.createMusicFromAsset(gameActivity.getMusicManager(), gameActivity.getApplicationContext(), "rocketThruster.ogg");
			} catch (final IOException e) {
				Debug.e(e);
				return null;
			}
			thrusterMusic.setLooping(true);
			thrusterMusic.play();
			gameActivity.addMusic(thrusterMusic);

			scene.registerUpdateHandler(new ShipPhysicsConnector(gameActivity.mBoundCamera, shipSprite, fixedStepPhysicsWorld, shipBody, scene, thrusterMusic));

			// Thruster
			BuildableBitmapTextureAtlas thrusterBitmapTextureAtlas = new BuildableBitmapTextureAtlas(gameActivity.getTextureManager(), 384, 128, TextureOptions.NEAREST);
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			TiledTextureRegion thrusterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(thrusterBitmapTextureAtlas, gameActivity, "thruster.png", 6, 2);
			try {
				thrusterBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
				thrusterBitmapTextureAtlas.load();
			} catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
				Debug.e(e);
			}
			final AnimatedSprite thrusterSprite = new AnimatedSprite(0, thrusterTiledTextureRegion.getHeight() / 2 + height / 2 + THRUSTER_OFFSET, thrusterTiledTextureRegion, vertexBufferObjectManager);
			thrusterSprite.setTag(EntityTags.Thruster.ordinal());
			thrusterSprite.animate(50);

			// Missile
			scene.setOnSceneTouchListener(new OnSceneTouchListener(shipBody, shipSprite, loadTexture("gfx/missile16x32.png"), thrusterTiledTextureRegion));

			shipSprite.attachChild(thrusterSprite);
			// TODO: attach both to an entity and sort their Z index there
			//			thrusterSprite.setZIndex(0);
			//			shipSprite.setZIndex(1);

			thrusterSprite.setScaleCenter(thrusterSprite.getWidth() / 2, 0);

			thrusterSprite.registerUpdateHandler(new ThrusterUpdateHandler(thrusterSprite, gameActivity.getFixedStepPhysicsWorld()));
			return shipSprite;
		} else {//Planet
			final Sprite planetSprite;
			if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTH)) {
				planetSprite = loadSprite("gfx/earth.png", x, y, width, height, vertexBufferObjectManager);
				planetSprite.setTag(EntityTags.Earth.ordinal());
			} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_JUPITER)) {
				planetSprite = loadSprite("gfx/jupiter128.png", x, y, width, height, vertexBufferObjectManager);
				planetSprite.setTag(EntityTags.Jupiter.ordinal());
			} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MARS)) {
				planetSprite = loadSprite("gfx/mars.png", x, y, width, height, vertexBufferObjectManager);
				planetSprite.setTag(EntityTags.Mars.ordinal());
			} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_VENUS)) {
				planetSprite = loadSprite("gfx/venus64.png", x, y, width, height, vertexBufferObjectManager);
				planetSprite.setTag(EntityTags.Venus.ordinal());
			} else {
				throw new IllegalArgumentException();
			}
			//			final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.8f);
			//			objectFixtureDef.shape = new CircleShape();
			//			final Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, planetSprite, BodyDef.BodyType.StaticBody, objectFixtureDef);

			//			body.setUserData(EntityTags.Ship);
			//			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(planetSprite, body, false, false));

			//						mScene.attachChild(planetSprite);
			return planetSprite;
		}
	}

	private Sprite loadSprite(String assetPath, float x, float y, float width, float height, VertexBufferObjectManager vertexBufferObjectManager) {
		ITexture texture = loadTexture(assetPath);
		if (texture == null) return null;
		ITextureRegion textureRegion = TextureRegionFactory.extractFromTexture(texture);

		return new Sprite(x, y, width, height, textureRegion, vertexBufferObjectManager);
	}

	private ITexture loadTexture(String assetPath) {
		ITexture texture;
		try {
			texture = new AssetBitmapTexture(ResourcesManager.getInstance().mActivity.getTextureManager(), ResourcesManager.getInstance().mActivity.getAssets(), assetPath);
		} catch (IOException e) {
			Debug.e(e);
			return null;
		}
		texture.load();
		return texture;
	}
}
