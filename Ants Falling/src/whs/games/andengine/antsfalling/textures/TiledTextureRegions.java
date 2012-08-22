/**
 * 
 */
package whs.games.andengine.antsfalling.textures;

import org.andengine.opengl.texture.TextureManager;
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

import android.widget.Toast;

/**
 * @author WiLLStenico
 * 
 */
public class TiledTextureRegions {

//	private BitmapTextureAtlas mBitmapTextureAtlas;
//	private TiledTextureRegion mFaceTextureRegion;
	
	private SimpleBaseGameActivity contextGameActivity;

	/**
	 * 
	 */
	public TiledTextureRegions(SimpleBaseGameActivity contextGameActivity) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.contextGameActivity = contextGameActivity;
		
//		// Ball
//		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
//				contextGameActivity.getTextureManager(), 64, 32,
//				TextureOptions.BILINEAR);
//		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createTiledFromAsset(this.mBitmapTextureAtlas,
//						contextGameActivity, "face_circle_tiled.png", 0, 0, 2,
//						1);
//		this.mBitmapTextureAtlas.load();
	}
	public TiledTextureRegion GetBallTextureRegion(){
		// Ball
		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(
						contextGameActivity.getTextureManager(), 64, 32,
						TextureOptions.BILINEAR);
		TiledTextureRegion mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
						.createTiledFromAsset(mBitmapTextureAtlas,
								contextGameActivity, "face_circle_tiled.png", 0, 0, 2,
								1);
				mBitmapTextureAtlas.load();
		return mFaceTextureRegion;
		
	}
	
	public TiledTextureRegion GetSmurfTextureRegion(){
		// Ball
//		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(
//						contextGameActivity.getTextureManager(), 64, 64,
//						TextureOptions.NEAREST);
//		TiledTextureRegion mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
//						.createTiledFromAsset(mBitmapTextureAtlas,
//								contextGameActivity, "frog.png", 0, 0, 3,
//								1);
//				mBitmapTextureAtlas.load();
				
				
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				contextGameActivity.getTextureManager(), 515, 515, TextureOptions.BILINEAR);
				// this.mBitmapTextureAtlas = new
				// BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256,
				// TextureOptions.BILINEAR);

		
				TiledTextureRegion mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
						.createTiledFromAsset(mBitmapTextureAtlas, contextGameActivity,
								"smurf_sprite.png",4,4);
				//smurf_sprite.png
				try {
					mBitmapTextureAtlas
							.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
									0, 0, 1));
					mBitmapTextureAtlas.load();
				} catch (TextureAtlasBuilderException e) {
					Debug.e(e);
				}
				//mBitmapTextureAtlas.load();
		return mFaceTextureRegion;
		
	}
	
	public TiledTextureRegion GetMarioTextureRegion(){
		// Ball
//		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(
//						contextGameActivity.getTextureManager(), 64, 64,
//						TextureOptions.NEAREST);
//		TiledTextureRegion mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
//						.createTiledFromAsset(mBitmapTextureAtlas,
//								contextGameActivity, "frog.png", 0, 0, 3,
//								1);
//				mBitmapTextureAtlas.load();
				
				
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				contextGameActivity.getTextureManager(), 400, 800, TextureOptions.BILINEAR);
				// this.mBitmapTextureAtlas = new
				// BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256,
				// TextureOptions.BILINEAR);

		
				TiledTextureRegion mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
						.createTiledFromAsset(mBitmapTextureAtlas, contextGameActivity,
								"mario.png",10,20);
				//smurf_sprite.png
				try {
					mBitmapTextureAtlas
							.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
									0, 0, 1));
					mBitmapTextureAtlas.load();
				} catch (TextureAtlasBuilderException e) {
					Debug.e(e);
				}
				//mBitmapTextureAtlas.load();
		return mFaceTextureRegion;
		
	}
	
}
