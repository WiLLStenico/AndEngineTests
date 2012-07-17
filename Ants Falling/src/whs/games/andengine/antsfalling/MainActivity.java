/**
 * 
 */
package whs.games.andengine.antsfalling;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.DisplayMetrics;

/**
 * @author WiLL
 * 
 */
public class MainActivity extends SimpleBaseGameActivity implements SensorEventListener  {

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
	
	//TESTES
	AnimatedSprite helicopter;
	
	//==============

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
		helicopter = new AnimatedSprite(320, 50,
				this.mHelicopterTextureRegion,
				this.getVertexBufferObjectManager());
		helicopter.animate(new long[] { 100, 100 }, 1, 2, true);

//		helicopter.registerEntityModifier(new LoopEntityModifier(new MoveModifier(10f, helicopter
//				.getX(), 0, helicopter.getY(), camera.getHeight()
//				- this.mHelicopterTextureRegion.getHeight())));

		helicopter.registerEntityModifier(new MoveModifier(10f, helicopter
				.getX(), 0, helicopter.getY(), camera.getHeight()
				- this.mHelicopterTextureRegion.getHeight()));
		

		
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
		
		//Text t = new Text(0, 0, font, "Score:0123456789", vbo);
		
		 HUD hud = new HUD();
		 hud.attachChild(new Helicopter());
		 
		 camera.setHUD(hud);

		 
		return scene;
		
		
	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// check sensor type
				if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

					// assign directions
					float x=event.values[0];
					float y=event.values[1];
					float z=event.values[2];

					helicopter.setRotation(x);
				}
		
	}

	

//	/* (non-Javadoc)
//	 * @see org.andengine.input.sensor.acceleration.IAccelerationListener#onAccelerationChanged(org.andengine.input.sensor.acceleration.AccelerationData)
//	 */
//	@Override
//	 public void onAccelerationChanged(final AccelerationData pAccelerationData) {
//	 
//		helicopter.registerEntityModifier(new MoveModifier(10f, helicopter
//				.getX(), 0, helicopter.getY(), camera.getHeight()
//				- this.mHelicopterTextureRegion.getHeight()));
//				
//		helicopter.setRotation(-1);
//		
//		
//	       
//	 }
	
	

	// ===========================================================
	// Methods
	// ===========================================================

	
	
	
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
