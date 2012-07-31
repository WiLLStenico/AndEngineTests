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

	private static Camera camera;
	
	private boolean teste;

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion mSnapdragonTextureRegion;

	private TiledTextureRegion mHelicopterTextureRegion;

	private TiledTextureRegion mBananaTextureRegion;

	private TiledTextureRegion mFaceTextureRegion;

	private TiledTextureRegion mFrogTextureRegion;

	// TESTES
	AnimatedSprite helicopter;
	
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	
	private final Scene scene = new Scene();

	private Font mFont;

	// ==============

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.andengine.ui.IGameInterface#onCreateEngineOptions()
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {

		// Get Display Metrics
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		final int CAMERA_WIDTH = dm.widthPixels;
		final int CAMERA_HEIGHT = dm.heightPixels;

		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.andengine.ui.activity.SimpleBaseGameActivity#onCreateResources()
	 */
	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 512, 256, TextureOptions.NEAREST);
		// this.mBitmapTextureAtlas = new
		// BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256,
		// TextureOptions.BILINEAR);

		this.mSnapdragonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"snapdragon_tiled.png", 4, 3);
		this.mHelicopterTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"helicopter_tiled.png", 2, 2);
		this.mBananaTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"banana_tiled.png", 4, 2);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"face_box_tiled.png", 2, 1);
		this.mFrogTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"frog.png", 3, 1);

		try {
			this.mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
		
		//Controles
		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();
		

		// Propriedade Texto
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 10);
		this.mFont.load();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.andengine.ui.activity.SimpleBaseGameActivity#onCreateScene()
	 */
	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		//scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		/* Quickly twinkling face. */
		final AnimatedSprite face = new AnimatedSprite(100, 50,
				this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		face.animate(100);
		scene.attachChild(face);

		/* Continuously flying helicopter. */
		helicopter = new AnimatedSprite(320, 50, this.mHelicopterTextureRegion,
				this.getVertexBufferObjectManager());
		helicopter.animate(new long[] { 100, 100 }, 1, 2, true);

		//Amarra camera a entidade para segui-la		
		camera.setChaseEntity(helicopter);
		
		helicopter.registerEntityModifier(new LoopEntityModifier(
				new MoveModifier(10f, helicopter.getX(), 0, helicopter.getY(),
						camera.getHeight()
								- this.mHelicopterTextureRegion.getHeight())));

		
		

		
		scene.attachChild(helicopter);

		/* Snapdragon. */
		final AnimatedSprite snapdragon = new AnimatedSprite(300, 200,
				this.mSnapdragonTextureRegion,
				this.getVertexBufferObjectManager());
		snapdragon.animate(100);
		scene.attachChild(snapdragon);

		final AnimatedSprite snapdragon2 = new AnimatedSprite(400, 200,
				this.mSnapdragonTextureRegion,
				this.getVertexBufferObjectManager());
		snapdragon2.animate(50);
		scene.attachChild(snapdragon2);

		/* Funny banana. */
		final AnimatedSprite banana = new AnimatedSprite(100, 220,
				this.mBananaTextureRegion, this.getVertexBufferObjectManager());
		banana.animate(100);

		banana.registerEntityModifier(new LoopEntityModifier(
				new MoveModifier(5f, banana.getX(), helicopter.getX(),
						banana.getY(), helicopter.getY())));
		
//		banana.registerEntityModifier(new MoveModifier(5f, banana.getX(), helicopter.getX(),
//				banana.getY(), helicopter.getY()));

		scene.attachChild(banana);

		/* frog */
		final AnimatedSprite frog = new AnimatedSprite(100, 250,
				this.mFrogTextureRegion, this.getVertexBufferObjectManager());
		frog.animate(500);
		scene.attachChild(frog);

		final FPSCounter fpsCounter = new FPSCounter();
		this.mEngine.registerUpdateHandler(fpsCounter);

		final Text elapsedText = new Text(0, 0, this.mFont, "Seconds elapsed:",
				"Seconds elapsed: XXXXX".length(),
				this.getVertexBufferObjectManager());
		final Text fpsText = new Text(camera.getWidth()/2, 0,
				this.mFont, "FPS:", "FPS: XXXXX".length(),
				this.getVertexBufferObjectManager());
		
		

		scene.attachChild(elapsedText);
		scene.attachChild(fpsText);
		
//		helicopter.attachChild(elapsedText);
//		helicopter.attachChild(fpsText);
		
		
		scene.registerUpdateHandler(new TimerHandler(1 / 20.0f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {

						elapsedText.setText("Seconds elapsed: "
								+ String.format("%.3f",
										MainActivity.this.mEngine
												.getSecondsElapsedTotal()));
						fpsText.setText("FPS: "
								+ String.format("%.3f", fpsCounter.getFPS()));
					}
				}));

		
		
		this.initOnScreenControls();
		
		return scene;

	}
	// /* (non-Javadoc)
	// * @see
	// org.andengine.input.sensor.acceleration.IAccelerationListener#onAccelerationChanged(org.andengine.input.sensor.acceleration.AccelerationData)
	// */
	// @Override
	// public void onAccelerationChanged(final AccelerationData
	// pAccelerationData) {
	//
	// helicopter.registerEntityModifier(new MoveModifier(10f, helicopter
	// .getX(), 0, helicopter.getY(), camera.getHeight()
	// - this.mHelicopterTextureRegion.getHeight()));
	//
	// helicopter.setRotation(-1);
	//
	//
	//
	// }

	// ===========================================================
	// Methods
	// ===========================================================
	private void initOnScreenControls() {
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(
				0, this.camera.getHeight()
						- this.mOnScreenControlBaseTextureRegion.getHeight(), this.camera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				//final Body carBody = RacerGameActivity.this.mCarBody;

				//final Vector2 velocity = Vector2Pool.obtain(pValueX * 5, pValueY * 5);
				//carBody.setLinearVelocity(velocity);
				//Vector2Pool.recycle(velocity);

				final float rotationInRad = (float)Math.atan2(-pValueX, pValueY);
				//carBody.setTransform(carBody.getWorldCenter(), rotationInRad);

				MainActivity.this.helicopter.setRotation(MathUtils.radToDeg(rotationInRad));
				//MainActivity.this.scene.setRotation(MathUtils.radToDeg(rotationInRad));
			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
				/* Nothing. */
			}
		});
		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		//		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		//		analogOnScreenControl.getControlBase().setScale(0.75f);
		//		analogOnScreenControl.getControlKnob().setScale(0.75f);
		analogOnScreenControl.refreshControlKnobPosition();

		this.scene.setChildScene(analogOnScreenControl);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
