package com.draga.SpaceTravels2.utility;

import com.draga.SpaceTravels2.GameActivity;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 18/08/13
 * Time: 01:05
 * To change this template use File | Settings | File Templates.
 */
public class ResourcesManager {
	private static ResourcesManager ourInstance = new ResourcesManager();
	public Engine mEngine;
	public GameActivity mActivity;
	public BoundCamera mBoundCamera;
	public VertexBufferObjectManager mVertexBufferObjectManager;
	public FixedStepPhysicsWorld mFixedStepPhysicsWorld;

	private ResourcesManager() {
	}

	public static ResourcesManager getInstance() {
		return ourInstance;
	}
	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------

	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	/**
	 * @param engine
	 * @param pActivity
	 * @param pCamera
	 * @param pVertexBufferObjectManager <br><br>
	 *                                   We use this method at beginning of game loading, to prepare Resources Manager properly,
	 * @param pPhysicsWorld
	 */
	public static void prepareManager(Engine engine, GameActivity pActivity, BoundCamera pCamera, VertexBufferObjectManager pVertexBufferObjectManager, FixedStepPhysicsWorld pPhysicsWorld) {
		getInstance().mEngine = engine;
		getInstance().mFixedStepPhysicsWorld = pPhysicsWorld;
		getInstance().mActivity = pActivity;
		getInstance().mBoundCamera = pCamera;
		getInstance().mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
	}

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadMenuGraphics() {

	}

	private void loadMenuAudio() {

	}

	private void loadGameGraphics() {

	}

	private void loadGameFonts() {

	}

	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------

	private void loadGameAudio() {

	}

}
