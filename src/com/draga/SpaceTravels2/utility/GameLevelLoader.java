package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.draga.SpaceTravels2.GameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 05/08/13
 * Time: 20:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class GameLevelLoader extends LevelLoader {
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_BACKGROUND = "background";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SHIP = "ship";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTH = "earth";
	private static final String TAG_ENTITY_ATTRIBUTE_MUSIC = "music";
	public static Music mMusic;

	public static enum TAGS {Ship, Earth}

	public static LevelLoader LoadLevel(final GameActivity gameActivity, final Scene scene, final PhysicsWorld physicsWorld) {
		final LevelLoader levelLoader = new LevelLoader();
		final ITextureRegion mShipTextureRegion;
		final ITextureRegion mEarthTextureRegion;
		final ITexture mShipTexture;
		final ITexture mEarthTexture;
		try {
			mShipTexture = new AssetBitmapTexture(gameActivity.getTextureManager(), gameActivity.getAssets(), "gfx/ship64.png");
			mEarthTexture = new AssetBitmapTexture(gameActivity.getTextureManager(), gameActivity.getAssets(), "gfx/earth.png");
			//			mEarthTexture = new BitmapTexture(gameActivity.getTextureManager(),
			//					new IInputStreamOpener() {
			//						@Override
			//						public InputStream open() throws IOException {
			//							return gameActivity.getAssets().open("gfx/earth.png");
			//						}
			//					});
		} catch (IOException e) {
			Debug.e(e);
			return null;
		}

		mShipTexture.load();
		mShipTextureRegion = TextureRegionFactory.extractFromTexture(mShipTexture);
		mEarthTexture.load();
		mEarthTextureRegion = TextureRegionFactory.extractFromTexture(mEarthTexture);
		levelLoader.setAssetBasePath("level/");

		levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new IEntityLoader() {
			@Override
			public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				gameActivity.mBoundCamera.setBounds(0, 0, width, height);

				final Rectangle ground = new Rectangle(0, height - 2, width, 2, gameActivity.getVertexBufferObjectManager());
				final Rectangle roof = new Rectangle(0, 0, width, 2, gameActivity.getVertexBufferObjectManager());
				final Rectangle left = new Rectangle(0, 0, 2, height, gameActivity.getVertexBufferObjectManager());
				final Rectangle right = new Rectangle(width - 2, 0, 2, height, gameActivity.getVertexBufferObjectManager());

				final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
				PhysicsFactory.createBoxBody(physicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
				PhysicsFactory.createBoxBody(physicsWorld, roof, BodyDef.BodyType.StaticBody, wallFixtureDef);
				PhysicsFactory.createBoxBody(physicsWorld, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
				PhysicsFactory.createBoxBody(physicsWorld, right, BodyDef.BodyType.StaticBody, wallFixtureDef);

				scene.attachChild(ground);
				scene.attachChild(roof);
				scene.attachChild(left);
				scene.attachChild(right);

				final ITextureRegion backgroundTextureRegion;
				final ITexture backgroundTexture;
				final String backgroundFile = SAXUtils.getAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_BACKGROUND, null);
				if (backgroundFile != null) {
					try {
						backgroundTexture = new AssetBitmapTexture(gameActivity.getTextureManager(), gameActivity.getAssets(), "gfx/" + backgroundFile);
					} catch (IOException e) {
						Debug.e(e);
						return null;
					}
					backgroundTexture.load();
					backgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
					final Sprite backgroundSprite = new Sprite(0, 0, width, height, backgroundTextureRegion, gameActivity.getVertexBufferObjectManager());
					scene.attachChild(backgroundSprite);
				}

				// Music
				final String musicFile = SAXUtils.getAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_MUSIC, null);
				if (musicFile != null) {
					MusicFactory.setAssetBasePath("mfx/");
					try {
						mMusic = MusicFactory.createMusicFromAsset(gameActivity.getEngine().getMusicManager(), gameActivity, musicFile);
					} catch (final IOException e) {
						Debug.e(e);
					}
					mMusic.setLooping(true);
					mMusic.play();
				}
				return scene;
			}
		});

		levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader() {
			@Override
			public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
				final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X) - width / 2;
				final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y) - height / 2;
				final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

				final VertexBufferObjectManager vertexBufferObjectManager = gameActivity.getVertexBufferObjectManager();

				final Sprite sprite;
				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SHIP)) {

					sprite = new Sprite(x, y, width, height, mShipTextureRegion, vertexBufferObjectManager);
					sprite.setTag(TAGS.Ship.ordinal());
					final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.2f, 0.8f);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyDef.BodyType.DynamicBody, objectFixtureDef);
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, false));

					gameActivity.mBoundCamera.setChaseEntity(sprite);
					gameActivity.mBoundCamera.setBoundsEnabled(true);
					//TODO: proper rotation handling
					sprite.registerUpdateHandler(new ShipUpdateHandler(scene, physicsWorld, gameActivity.mBoundCamera));
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTH)) {

					sprite = new Sprite(x, y, width, height, mEarthTextureRegion, vertexBufferObjectManager);
					sprite.setTag(TAGS.Earth.ordinal());
					final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.2f, 0.8f);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyDef.BodyType.StaticBody, objectFixtureDef);
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, false, false));
				} else {
					throw new IllegalArgumentException();
				}
				return sprite;
			}
		});
		return levelLoader;
	}
}
