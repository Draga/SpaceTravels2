package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.draga.SpaceTravels2.GameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.SAXUtils;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 09/08/13
 * Time: 21:20
 * To change this template use File | Settings | File Templates.
 */
public class GameLevelLoader implements IEntityLoader {
	private static final String TAG_ENTITY_ATTRIBUTE_MUSIC = "music";
	private static final String TAG_ENTITY_ATTRIBUTE_BACKGROUND = "background";
	private GameActivity mGameActivity;
	private FixedStepPhysicsWorld mFixedStepPhysicsWorld;
	private BoundCamera mBoundCamera;
	private Scene mScene;

	public GameLevelLoader(GameActivity gameActivity) {
		this.mGameActivity = gameActivity;
		this.mBoundCamera = mGameActivity.mBoundCamera;
		this.mFixedStepPhysicsWorld = mGameActivity.getFixedStepPhysicsWorld();
		this.mScene = mGameActivity.getScene();
	}

	@Override
	public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
		final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
		final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

		mBoundCamera.setBounds(0, 0, width, height);

		final Rectangle ground = new Rectangle(0, height - 2, width, 2, mGameActivity.getVertexBufferObjectManager());
		final Rectangle roof = new Rectangle(0, 0, width, 2, mGameActivity.getVertexBufferObjectManager());
		final Rectangle left = new Rectangle(0, 0, 2, height, mGameActivity.getVertexBufferObjectManager());
		final Rectangle right = new Rectangle(width - 2, 0, 2, height, mGameActivity.getVertexBufferObjectManager());

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
		PhysicsFactory.createBoxBody(mFixedStepPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mFixedStepPhysicsWorld, roof, BodyDef.BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mFixedStepPhysicsWorld, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mFixedStepPhysicsWorld, right, BodyDef.BodyType.StaticBody, wallFixtureDef);

		mScene.attachChild(ground);
		mScene.attachChild(roof);
		mScene.attachChild(left);
		mScene.attachChild(right);

		// Background
		final ITextureRegion backgroundTextureRegion;
		final ITexture backgroundTexture;
		final String backgroundFile = SAXUtils.getAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_BACKGROUND, null);
		if (backgroundFile != null) {
			try {
				backgroundTexture = new AssetBitmapTexture(mGameActivity.getTextureManager(), mGameActivity.getAssets(), "gfx/" + backgroundFile);
			} catch (IOException e) {
				Debug.e(e);
				return null;
			}
			backgroundTexture.load();
			backgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
			final Sprite backgroundSprite = new Sprite(0, 0, width, height, backgroundTextureRegion, mGameActivity.getVertexBufferObjectManager());
			mScene.attachChild(backgroundSprite);
		}

		// Music
		final String musicFile = SAXUtils.getAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_MUSIC, null);
		if (musicFile != null) {
			MusicFactory.setAssetBasePath("mfx/");
			final Music music;
			try {
				music = MusicFactory.createMusicFromAsset(mGameActivity.getEngine().getMusicManager(), mGameActivity, musicFile);
			} catch (final IOException e) {
				Debug.e(e);
				return null;
			}
			music.setLooping(true);
			music.play();
			mGameActivity.setMusic(music);
		}
		return mScene;
	}
}
