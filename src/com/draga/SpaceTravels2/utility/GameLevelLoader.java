package com.draga.SpaceTravels2.utility;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.draga.SpaceTravels2.GameActivity;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.color.Color;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

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
	private static final float SPEEDBAR_HEIGHT = 10;
	private static final float SPEEDBAR_MARGINS = 10;
	private static final float SPEEDBAR_WIDTH = 70;
	private static final float SPEEDBAR_PADDING = 2;
	private final ResourcesManager resourcesManager;
	private GameActivity mGameActivity;
	private FixedStepPhysicsWorld mFixedStepPhysicsWorld;
	private BoundCamera mBoundCamera;
	private Scene mScene;
	private VertexBufferObjectManager mVertexBufferObjectManager;

	public GameLevelLoader() {
		resourcesManager = ResourcesManager.getInstance();
		mGameActivity = resourcesManager.getActivity();
		this.mFixedStepPhysicsWorld = resourcesManager.getFixedStepPhysicsWorld();
		this.mScene = mGameActivity.getScene();
		this.mVertexBufferObjectManager = resourcesManager.getVertexBufferObjectManager();
		this.mBoundCamera = resourcesManager.getBoundCamera();
	}

	@Override
	public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
		final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
		final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

		mBoundCamera.setBounds(0, 0, width, height);

		final Rectangle ground = new Rectangle(0, height - 2, width, 2, mVertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, width, 2, mVertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, height, mVertexBufferObjectManager);
		final Rectangle right = new Rectangle(width - 2, 0, 2, height, mVertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0.5f, false, (short) EntityTags.Wall.ordinal(), (short) EntityTags.Wall.ordinal(), (short) 0);
		PhysicsFactory.createBoxBody(mFixedStepPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mFixedStepPhysicsWorld, roof, BodyDef.BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mFixedStepPhysicsWorld, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mFixedStepPhysicsWorld, right, BodyDef.BodyType.StaticBody, wallFixtureDef);

		mScene.attachChild(ground);
		mScene.attachChild(roof);
		mScene.attachChild(left);
		mScene.attachChild(right);

		// Background
		final String backgroundFile = SAXUtils.getAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_BACKGROUND, null);
		if (backgroundFile != null) {
			resourcesManager.setBackgroundPath(backgroundFile);
			final Sprite backgroundSprite = new Sprite(0, 0, width, height, resourcesManager.getBackgroundTextureRegion(), mVertexBufferObjectManager);
			mGameActivity.getScene().attachChild(backgroundSprite);
		}

		// Music
		final String musicFile = SAXUtils.getAttribute(pAttributes, TAG_ENTITY_ATTRIBUTE_MUSIC, null);
		if (musicFile != null) resourcesManager.setMusicPath(musicFile);

		// Speed bar
		final Rectangle speedBar = new Rectangle(SPEEDBAR_MARGINS, SPEEDBAR_MARGINS, SPEEDBAR_WIDTH, SPEEDBAR_HEIGHT, mVertexBufferObjectManager);
		final Rectangle speedBarBackground = new Rectangle(SPEEDBAR_MARGINS - SPEEDBAR_PADDING, SPEEDBAR_MARGINS - SPEEDBAR_PADDING, SPEEDBAR_WIDTH + SPEEDBAR_PADDING * 2,
				SPEEDBAR_HEIGHT + SPEEDBAR_PADDING * 2, mVertexBufferObjectManager);

		speedBar.setColor(Color.GREEN);
		speedBarBackground.setColor(Color.WHITE);

		speedBar.setScaleCenterX(0);

		speedBar.setTag(EntityTags.SpeedBar.ordinal());
		HUD speedBarHUD = new HUD();
		speedBarHUD.attachChild(speedBarBackground);
		speedBarHUD.attachChild(speedBar);
		//		speedBarHUD.setTag(EntityTags.HUD.ordinal());
		mBoundCamera.setHUD(speedBarHUD);

		return mScene;
	}
}
