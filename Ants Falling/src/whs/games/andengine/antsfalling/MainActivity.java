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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
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

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion mSnapdragonTextureRegion;

	private TiledTextureRegion mHelicopterTextureRegion;

	private TiledTextureRegion mBananaTextureRegion;

	private TiledTextureRegion mFaceTextureRegion;

	private TiledTextureRegion mFrogTextureRegion;

	// TESTES
	AnimatedSprite helicopter;

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

		// Propriedade Texto
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20);
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

		final Scene scene = new Scene();
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

		banana.registerEntityModifier(new MoveModifier(5f, banana.getX(), 0,
				banana.getY(), 0));

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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
