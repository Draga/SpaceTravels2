package com.draga.SpaceTravels2;

import android.hardware.SensorManager;
import android.widget.Toast;
import com.badlogic.gdx.math.Vector2;
import com.draga.SpaceTravels2.utility.GameEntityLoader;
import com.draga.SpaceTravels2.utility.GameLevelLoader;
import com.draga.SpaceTravels2.utility.ResourcesManager;
import org.andengine.audio.music.Music;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GameActivity extends BaseGameActivity implements IAccelerationListener {
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	public static final String EXTRA_TAG_LEVEL = "level";
	private static final String TAG_ENTITY = "entity";
	public BoundCamera mBoundCamera;
	private FixedStepPhysicsWorld mFixedStepPhysicsWorld;
	private Scene mScene;
	private LevelLoader mLevelLoader;
	private ArrayList<Music> mMusics;
	//	private AccelerometerHelper mAccelerometerHelper;

	//	public GameActivity() {
	//		mAccelerometerHelper = AccelerometerHelper.getInstance();
	//	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Bring the ship home", Toast.LENGTH_SHORT).show();
		this.mBoundCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mBoundCamera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		mMusics = new ArrayList<Music>();
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();

		this.mFixedStepPhysicsWorld = new FixedStepPhysicsWorld(50, new Vector2(0, SensorManager.GRAVITY_EARTH), true, 3, 2);

		ResourcesManager.prepareManager(this.getEngine(), this, mBoundCamera, this.getVertexBufferObjectManager(), this.mFixedStepPhysicsWorld);

		// TODO: create resources in appropriate method
		this.mLevelLoader = new LevelLoader();
		mLevelLoader.setAssetBasePath("level/");

		mLevelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new GameLevelLoader());

		mLevelLoader.registerEntityLoader(TAG_ENTITY, new GameEntityLoader());

		this.mScene.registerUpdateHandler(this.mFixedStepPhysicsWorld);

		//		mFixedStepPhysicsWorld.setContactListener(new GameContactListener());

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		try {
			mLevelLoader.loadLevelFromAsset(this.getAssets(), String.format("level%d.lvl", getIntent().getExtras().getInt(EXTRA_TAG_LEVEL)));
		} catch (final IOException e) {
			Debug.e(e);
			throw e;
		}
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {

	}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
		this.mFixedStepPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();
		Iterator musicsIterator = mMusics.iterator();
		while (musicsIterator.hasNext()) ((Music) musicsIterator.next()).resume();

		this.enableAccelerationSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();
		Iterator musicsIterator = mMusics.iterator();
		while (musicsIterator.hasNext()) ((Music) musicsIterator.next()).pause();

		this.disableAccelerationSensor();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (this.isGameLoaded()) System.exit(0);
	}

	public FixedStepPhysicsWorld getFixedStepPhysicsWorld() {
		return mFixedStepPhysicsWorld;
	}

	public Scene getScene() {
		return mScene;
	}

	public void addMusic(Music music) {
		this.mMusics.add(music);
	}

	//	private void addShip(float pX, float pY) {
	//		final Sprite ship;
	//		final Body body;
	//
	//		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
	//
	//		ship = new Sprite(pX, pY, this.mShipTextureRegion, this.getVertexBufferObjectManager());
	//		body = PhysicsFactory.createBoxBody(this.mFixedStepPhysicsWorld, ship,
	//				BodyType.DynamicBody,
	//				objectFixtureDef);
	//
	//		this.mScene.attachChild(ship);
	//		this.mFixedStepPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(ship, body, true,
	//				true));
	//	}
}