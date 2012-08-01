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

	private static Camera mCamera;

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion mHelicopterTextureRegion;

	private AnimatedSprite helicopter;

	//Controls
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;

	private final Scene scene = new Scene();	

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

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 512, 256, TextureOptions.NEAREST);
		// this.mBitmapTextureAtlas = new
		// BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256,
		// TextureOptions.BILINEAR);

		this.mHelicopterTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"helicopter_tiled.png", 2, 2);

		//TODO: Verify that
		try {
			this.mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

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

		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		/* Continuously flying helicopter. */
		helicopter = new AnimatedSprite(320, 50, this.mHelicopterTextureRegion,
				this.getVertexBufferObjectManager());
		helicopter.animate(new long[] { 100, 100 }, 1, 2, true);

		// Amarra mCamera a entidade para segui-la
		mCamera.setChaseEntity(helicopter);

		helicopter.registerEntityModifier(new LoopEntityModifier(
				new MoveModifier(10f, helicopter.getX(), 0, helicopter.getY(),
						mCamera.getHeight()
								- this.mHelicopterTextureRegion.getHeight())));

		scene.attachChild(helicopter);
		
		final FPSCounter fpsCounter = new FPSCounter();
		this.mEngine.registerUpdateHandler(fpsCounter);	
		
		this.initOnScreenControls();

		return scene;

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

						// final Vector2 velocity = Vector2Pool.obtain(pValueX *
						// 5, pValueY * 5);
						// carBody.setLinearVelocity(velocity);
						// Vector2Pool.recycle(velocity);

						final float rotationInRad = (float) Math.atan2(
								-pValueX, pValueY);
						// carBody.setTransform(carBody.getWorldCenter(),
						// rotationInRad);

						MainActivity.this.helicopter.setRotation(MathUtils
								.radToDeg(rotationInRad));
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

		this.scene.setChildScene(analogOnScreenControl);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
