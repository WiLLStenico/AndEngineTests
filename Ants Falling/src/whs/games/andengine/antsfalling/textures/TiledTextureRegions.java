/**
 * 
 */
package whs.games.andengine.antsfalling.textures;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * @author WiLLStenico
 * 
 */
public class TiledTextureRegions {

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mFaceTextureRegion;
	
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
	public TiledTextureRegion GetBallTextureRegionBall(){
		// Ball
				this.mBitmapTextureAtlas = new BitmapTextureAtlas(
						contextGameActivity.getTextureManager(), 64, 32,
						TextureOptions.BILINEAR);
				this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
						.createTiledFromAsset(this.mBitmapTextureAtlas,
								contextGameActivity, "face_circle_tiled.png", 0, 0, 2,
								1);
				this.mBitmapTextureAtlas.load();
		return mFaceTextureRegion;
		
	}

}
