package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.draga.SpaceTravels2.GameActivity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
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
	public static final float FLAME_OFFSET = -16;
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
	private PhysicsWorld mPhysicsWorld;
	private GameActivity mGameActivity;

	public static enum TAGS {Ship, Flame, Jupiter, Mars, Venus, Earth}

	public GameEntityLoader(GameActivity gameActivity) {
		this.mGameActivity = gameActivity;
		this.mPhysicsWorld = gameActivity.getFixedStepPhysicsWorld();
	}

	@Override
	public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
		final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
		final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
		final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X) - width / 2;
		final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y) - height / 2;
		final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

		final VertexBufferObjectManager vertexBufferObjectManager = mGameActivity.getVertexBufferObjectManager();

		final Sprite sprite;

		if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SHIP)) {
			sprite = loadSprite("gfx/ship64.png", x, y, width, height, vertexBufferObjectManager);
			sprite.setTag(TAGS.Ship.ordinal());
			final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.8f);
			final Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, sprite, BodyDef.BodyType.DynamicBody, objectFixtureDef);
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, false));

			mGameActivity.mBoundCamera.setChaseEntity(sprite);
			mGameActivity.mBoundCamera.setBoundsEnabled(true);
			// TODO: proper rotation handling
			sprite.registerUpdateHandler(new ShipUpdateHandler(mPhysicsWorld, mGameActivity.mBoundCamera, sprite));

			// Missile
			//			mGameActivity.getScene().setOnSceneTouchListener(new IOnSceneTouchListener() {
			//				@Override
			//				public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
			//					Sprite missileSprite = loadSprite("gfx/particle_point.png", sprite.getX(), sprite.getY() + sprite.getHeight() / 2, 10, 10, vertexBufferObjectManager);
			//					//					missileSprite.setRed(1);
			//					final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.8f);
			//					objectFixtureDef.shape = new CircleShape();
			//					final Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, missileSprite, BodyDef.BodyType.StaticBody, objectFixtureDef);
			//					mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(missileSprite, body, false, false));
			//					return true;
			//				}
			//			});

			// Flame
			BuildableBitmapTextureAtlas flameBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mGameActivity.getTextureManager(), 384, 128, TextureOptions.NEAREST);
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			TiledTextureRegion flameTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(flameBitmapTextureAtlas, mGameActivity, "flame.png", 6, 2);
			try {
				flameBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
				flameBitmapTextureAtlas.load();
			} catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
				Debug.e(e);
			}
			final AnimatedSprite flameSprite = new AnimatedSprite(0, flameTiledTextureRegion.getHeight() / 2 + height / 2 + FLAME_OFFSET, flameTiledTextureRegion, vertexBufferObjectManager);
			flameSprite.setTag(TAGS.Flame.ordinal());
			flameSprite.animate(50);

			sprite.attachChild(flameSprite);
			flameSprite.setZIndex(3);
			sprite.setZIndex(2);

			flameSprite.setScaleCenter(flameSprite.getWidth() / 2, 0);

			flameSprite.registerUpdateHandler(new FlameUpdateHandler(mPhysicsWorld, flameSprite));
		} else {//Planet
			if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTH)) {
				sprite = loadSprite("gfx/earth.png", x, y, width, height, vertexBufferObjectManager);
				sprite.setTag(TAGS.Earth.ordinal());
			} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_JUPITER)) {
				sprite = loadSprite("gfx/jupiter128.png", x, y, width, height, vertexBufferObjectManager);
				sprite.setTag(TAGS.Jupiter.ordinal());
			} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MARS)) {
				sprite = loadSprite("gfx/mars.png", x, y, width, height, vertexBufferObjectManager);
				sprite.setTag(TAGS.Mars.ordinal());
			} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_VENUS)) {
				sprite = loadSprite("gfx/venus64.png", x, y, width, height, vertexBufferObjectManager);
				sprite.setTag(TAGS.Venus.ordinal());
			} else {
				throw new IllegalArgumentException();
			}
			final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.8f);
			objectFixtureDef.shape = new CircleShape();
			final Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, sprite, BodyDef.BodyType.StaticBody, objectFixtureDef);
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, false, false));
		}
		return sprite;
	}

	private Sprite loadSprite(String assetPath, float x, float y, float width, float height, VertexBufferObjectManager vertexBufferObjectManager) {
		ITexture texture;
		try {
			texture = new AssetBitmapTexture(mGameActivity.getTextureManager(), mGameActivity.getAssets(), assetPath);
		} catch (IOException e) {
			Debug.e(e);
			return null;
		}
		texture.load();
		ITextureRegion textureRegion = TextureRegionFactory.extractFromTexture(texture);

		return new Sprite(x, y, width, height, textureRegion, vertexBufferObjectManager);
	}
}
