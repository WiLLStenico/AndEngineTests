/**
 * 
 */
package whs.games.andengine.antsfalling;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import whs.games.andengine.antsfalling.enemies.BaseEnemy;
import whs.games.andengine.antsfalling.textures.TiledTextureRegions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.opengl.GLES20;
import android.util.DisplayMetrics;

/**
 * @author WiLL
 * 
 */
public class MainActivity extends SimpleBaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================
	
	

	// ===========================================================
	// Fields
	// ===========================================================

//	private BitmapTextureAtlas mBitmapTextureAtlas;
//	private TiledTextureRegion mFaceTextureRegion;

	private static Camera mCamera;	
	
	//Controls
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;

	private final Scene mScene = new Scene();	

	// ===========================================================
	// Entities
	// ===========================================================
	 //Ball ball = null;
	 
	 private TiledTextureRegions testeTexture;
	 
	 //Car
	 private static final int CAR_SIZE = 16;
	
	 private BitmapTextureAtlas mVehiclesTexture;
		private TiledTextureRegion mVehiclesTextureRegion;
	 
	 private PhysicsWorld mPhysicsWorld;	 
	 
		private Body mCarBody;
		private TiledSprite mCar;
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {

		// Get Display Metrics
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		final int CAMERA_WIDTH = dm.widthPixels;
		final int CAMERA_HEIGHT = dm.heightPixels;

		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		//Car
		this.mVehiclesTexture = new BitmapTextureAtlas(this.getTextureManager(), 128, 16, TextureOptions.BILINEAR);
		this.mVehiclesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mVehiclesTexture, this, "vehicles.png", 0, 0, 6, 1);
		this.mVehiclesTexture.load();

		
//		//Ball
//		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 32, TextureOptions.BILINEAR);
//		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 0, 2, 1);
//		this.mBitmapTextureAtlas.load();
		
		testeTexture = new TiledTextureRegions(this);
		
		// Controls
		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();

	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		//PhysicsWorld
		this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		
//		final float centerX = (this.mCamera.getWidth() - this.mFaceTextureRegion.getWidth()) / 2;
//		final float centerY = (this.mCamera.getHeight() - this.mFaceTextureRegion.getHeight()) / 2;
		
		final float centerX = (this.mCamera.getWidth()) / 2;
		final float centerY = (this.mCamera.getHeight()) / 2;
		//ball = new Ball(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager(), mCamera);

//		BaseEnemy testeEnemy = new BaseEnemy(centerX - 30, centerY, testeTexture.GetBallTextureRegion(), this.getVertexBufferObjectManager());
//		testeEnemy.animate(200);
		
		BaseEnemy testeSmurf = new BaseEnemy(centerX, centerY, testeTexture.GetSmurfTextureRegion(), this.getVertexBufferObjectManager());
		testeSmurf.animate(50);
		mScene.attachChild(testeSmurf);
		
		BaseEnemy testeMario = new BaseEnemy(centerX - 30, centerY, testeTexture.GetMarioTextureRegion(), this.getVertexBufferObjectManager());
		
		//testeSmurf.setScale(2,2);
		testeMario.setScale(2,2);
		
		testeMario.animate(new long[]{200,200 }, new int[] { 1,4}, true);

		mScene.attachChild(testeMario);
		
		
		
BaseEnemy testeMario2 = new BaseEnemy(centerX - 80, centerY, testeTexture.GetMarioTextureRegion(), this.getVertexBufferObjectManager());
		
		//testeSmurf.setScale(2,2);
		testeMario2.setScale(2,2);
		
		testeMario2.animate(new long[]{200,200,200,200 }, new int[] { 11,4,12,4}, true);

		mScene.attachChild(testeMario2);
		
		
//		mScene.attachChild(testeEnemy);
		
		
		//mScene.attachChild(ball);
				
		this.initCar();
		this.initOnScreenControls();

		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
		
		return mScene;

	}

	// ===========================================================
	// Methods
	// ===========================================================
	private void initOnScreenControls() {
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(
				0, this.mCamera.getHeight()
						- this.mOnScreenControlBaseTextureRegion.getHeight(),
				this.mCamera, this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f,
				this.getVertexBufferObjectManager(),
				new IAnalogOnScreenControlListener() {
					@Override
					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY) {
						// final Body carBody = RacerGameActivity.this.mCarBody;

						 final Vector2 velocity2 = Vector2Pool.obtain(pValueX *
						 50, pValueY * 50);
						 
				//		 ball.setDEMO_VELOCITY(ball.getDEMO_VELOCITY() + velocity2.y);
												
						final Body carBody = MainActivity.this.mCarBody;

						final Vector2 velocity = Vector2Pool.obtain(pValueX * 5, pValueY * 5);
						carBody.setLinearVelocity(velocity);
						Vector2Pool.recycle(velocity);

						final float rotationInRad = (float)Math.atan2(-pValueX, pValueY);
						carBody.setTransform(carBody.getWorldCenter(), rotationInRad);

						MainActivity.this.mCar.setRotation(MathUtils.radToDeg(rotationInRad));
					
						
						// MainActivity.this.scene.setRotation(MathUtils.radToDeg(rotationInRad));
					}

					@Override
					public void onControlClick(
							final AnalogOnScreenControl pAnalogOnScreenControl) {
						/* Nothing. */
					}
				});
		analogOnScreenControl.getControlBase().setBlendFunction(
				GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		// analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		// analogOnScreenControl.getControlBase().setScale(0.75f);
		// analogOnScreenControl.getControlKnob().setScale(0.75f);
		analogOnScreenControl.refreshControlKnobPosition();

		this.mScene.setChildScene(analogOnScreenControl);
	}
	
	private void initCar() {
		this.mCar = new TiledSprite(20, 20, CAR_SIZE, CAR_SIZE, this.mVehiclesTextureRegion, this.getVertexBufferObjectManager());
		this.mCar.setCurrentTileIndex(0);

		final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		this.mCarBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, this.mCar, BodyType.DynamicBody, carFixtureDef);

		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mCar, this.mCarBody, true, false));

		this.mScene.attachChild(this.mCar);
	}

	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
