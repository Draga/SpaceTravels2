
package com.draga.SpaceTravels2;

import android.hardware.SensorManager;
import android.widget.Toast;
import com.badlogic.gdx.math.Vector2;
import com.draga.SpaceTravels2.utility.GameLevelLoader;
import com.draga.SpaceTravels2.utility.ShipUpdateHandler;
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

import java.io.IOException;

public class GameActivity extends BaseGameActivity implements IAccelerationListener {
	// Set by the MainActivity on StartIntent
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	public static final String EXTRA_TAG_LEVEL = "level";
	public BoundCamera mBoundCamera;
	private FixedStepPhysicsWorld mPhysicsWorld;
	private Scene mScene;
	private LevelLoader mGameLevelLoader;

	@Override
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Bring the ship home", Toast.LENGTH_SHORT).show();
		this.mBoundCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mBoundCamera);
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
//		this.mScene.setBackground(new Background(0, 0, 0));
		this.mPhysicsWorld = new FixedStepPhysicsWorld(50, new Vector2(0,
				SensorManager.GRAVITY_EARTH), true, 3, 2);

		this.mGameLevelLoader = GameLevelLoader.LoadLevel(this, mScene, this.mPhysicsWorld);

		this.mScene.registerUpdateHandler(this.mPhysicsWorld);

		//TODO: proper rotation handling
		mScene.registerUpdateHandler(new ShipUpdateHandler(mScene, mPhysicsWorld, mBoundCamera));

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		try {
			mGameLevelLoader.loadLevelFromAsset(this.getAssets(), String.format("level%d.lvl", getIntent().getExtras().getInt(EXTRA_TAG_LEVEL)));
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
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(),
				pAccelerationData.getY());
		this.mPhysicsWorld.setGravity(gravity);

		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerationSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerationSensor();
	}

//	private void addShip(float pX, float pY) {
//		final Sprite ship;
//		final Body body;
//
//		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
//
//		ship = new Sprite(pX, pY, this.mShipTextureRegion, this.getVertexBufferObjectManager());
//		body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, ship,
//				BodyType.DynamicBody,
//				objectFixtureDef);
//
//		this.mScene.attachChild(ship);
//		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(ship, body, true,
//				true));
//	}
}