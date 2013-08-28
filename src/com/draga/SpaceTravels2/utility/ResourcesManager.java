package com.draga.SpaceTravels2.utility;

import android.graphics.Color;
import android.graphics.Typeface;
import com.draga.SpaceTravels2.GameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 18/08/13
 * Time: 01:05
 * To change this template use File | Settings | File Templates.
 */
public class ResourcesManager {
	private static ResourcesManager ourInstance = new ResourcesManager();
	private static Scene mScene;
	private Engine mEngine;
	private GameActivity mActivity;
	private BoundCamera mBoundCamera;
	private VertexBufferObjectManager mVertexBufferObjectManager;
	private FixedStepPhysicsWorld mFixedStepPhysicsWorld;
	private ITextureRegion mShipTextureRegion;
	private BuildableBitmapTextureAtlas mBuildableTextureAtlas;
	private TextureRegion mEarthTextureRegion;
	private TextureRegion mVenusTextureRegion;
	private TextureRegion mMarsTextureRegion;
	private TextureRegion mJupiterTextureRegion;
	private TextureRegion mBackgroundTextureRegion;
	private TiledTextureRegion mThrusterTiledTextureRegion;
	private TiledTextureRegion mExplosionTiledTextureRegion;
	private String mBackgroundPath = "background4.jpg";
	private Music mThrusterMusic;
	private Music mMusic;
	private String mMusicPath;
	private TextureRegion mMissileTextureRegion;
	private Sound mMissileSound;
	private Font mFont;

	private ResourcesManager() {
	}

	/**
	 * @param engine
	 * @param pActivity
	 * @param pCamera
	 * @param pVertexBufferObjectManager <br><br>
	 *                                   We use this method at beginning of game loading, to prepare Resources Manager properly,
	 * @param pPhysicsWorld
	 * @param pScene
	 */
	public static void prepareManager(Engine engine, GameActivity pActivity, BoundCamera pCamera, VertexBufferObjectManager pVertexBufferObjectManager,
									  FixedStepPhysicsWorld pPhysicsWorld, Scene pScene) {
		getInstance().mEngine = engine;
		getInstance().mFixedStepPhysicsWorld = pPhysicsWorld;
		getInstance().mActivity = pActivity;
		getInstance().mBoundCamera = pCamera;
		getInstance().mVertexBufferObjectManager = pVertexBufferObjectManager;
		getInstance().mScene = pScene;
	}
	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------

	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	public static ResourcesManager getInstance() {
		return ourInstance;
	}

	public static Scene getScene() {
		return mScene;
	}

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
	}

	private void loadMenuAudio() {

	}

	private void loadMenuGraphics() {

	}

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadGameAudio() {
		// Thruster sound
		MusicFactory.setAssetBasePath("mfx/");
		SoundFactory.setAssetBasePath("mfx/");
		try {
			mMissileSound = SoundFactory.createSoundFromAsset(mActivity.getSoundManager(), mActivity, "missileLaunch.mp3");
			mThrusterMusic = MusicFactory.createMusicFromAsset(mActivity.getMusicManager(), mActivity.getApplicationContext(), "rocketThruster.ogg");
			if (mMusicPath != null) {
				mMusic = MusicFactory.createMusicFromAsset(mActivity.getEngine().getMusicManager(), mActivity, mMusicPath);
			}
		} catch (final IOException e) {
			Debug.e(e);
			return;
		}
		mMissileSound.setVolume(0.2f);

		mThrusterMusic.setLooping(true);
		if (mMusic != null) {
			mMusic.setLooping(true);
		}
	}

	private void loadGameFonts() {

	}

	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mBuildableTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);

		mShipTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTextureAtlas, mActivity, "ship64.png");
		mEarthTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTextureAtlas, mActivity, "earth.png");
		mVenusTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTextureAtlas, mActivity, "venus64.png");
		mMarsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTextureAtlas, mActivity, "mars.png");
		mJupiterTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTextureAtlas, mActivity, "jupiter128.png");
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTextureAtlas, mActivity, mBackgroundPath);
		mMissileTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableTextureAtlas, mActivity, "missile16x32.png");

		mThrusterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBuildableTextureAtlas, mActivity, "thruster.png", 6, 2);
		mExplosionTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBuildableTextureAtlas, mActivity, "explosion32.png", 8, 6);

		mFont = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), (int) mBoundCamera.getWidth(), (int) mBoundCamera.getHeight(),
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 64, true, Color.WHITE);
		mFont.load();

		try {
			mBuildableTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			mBuildableTextureAtlas.load();
		} catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void playMusic() {
		mThrusterMusic.play();
		if (mMusicPath != null) {
			mMusic.play();
		}
	}

	public void pauseMusic() {
		mThrusterMusic.pause();
		if (mMusicPath != null) {
			mMusic.pause();
		}
	}

	public ITextureRegion getShipTextureRegion() {
		return mShipTextureRegion;
	}

	public TiledTextureRegion getThrusterTiledTextureRegion() {
		return mThrusterTiledTextureRegion;
	}

	public TextureRegion getJupiterTextureRegion() {
		return mJupiterTextureRegion;
	}

	public TextureRegion getMarsTextureRegion() {
		return mMarsTextureRegion;
	}

	public TextureRegion getVenusTextureRegion() {
		return mVenusTextureRegion;
	}

	public TextureRegion getEarthTextureRegion() {
		return mEarthTextureRegion;
	}

	public TextureRegion getBackgroundTextureRegion() {
		return mBackgroundTextureRegion;
	}

	public void setBackgroundPath(String mBackgroundPath) {
		this.mBackgroundPath = mBackgroundPath;
	}

	public Music getThrusterMusic() {
		return mThrusterMusic;
	}

	public void setMusicPath(String mMusicPath) {
		this.mMusicPath = mMusicPath;
	}

	public TextureRegion getMissileTextureRegion() {
		return mMissileTextureRegion;
	}

	public Engine getEngine() {
		return mEngine;
	}

	public GameActivity getActivity() {
		return mActivity;
	}

	public BoundCamera getBoundCamera() {
		return mBoundCamera;
	}

	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return mVertexBufferObjectManager;
	}

	public FixedStepPhysicsWorld getFixedStepPhysicsWorld() {
		return mFixedStepPhysicsWorld;
	}

	public Sound getMissileSound() {
		return mMissileSound;
	}

	public TiledTextureRegion getExplosionTiledTextureRegion() {
		return mExplosionTiledTextureRegion;
	}

	public Font getFont() {
		return mFont;
	}
}
