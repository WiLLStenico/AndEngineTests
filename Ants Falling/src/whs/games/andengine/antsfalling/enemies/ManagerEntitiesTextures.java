/**
 * 
 */
package whs.games.andengine.antsfalling.enemies;

import java.util.List;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * @author WiLLStenico
 * 
 */
public class ManagerEntitiesTextures {

	private final BaseGameActivity BGA;

	private EntityTiledTextureRegion SmurfTexturex = null;

	/**
	 * 
	 */
	public ManagerEntitiesTextures(BaseGameActivity bga) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.BGA = bga;
	}

	public void LoadResources(EntityType entityType) {
		switch (entityType) {
		case SMURF:
			SmurfTexturex = new EntityTiledTextureRegion(BGA, 515, 515, 4, 4,
					"smurf_sprite.png");
			break;

		default:
			break;
		}
	}
	
	public TiledTextureRegion getTextureRegion(EntityType entityType) {
//		switch (entityType) {
//		case SMURF:
//			return SmurfTexturex.getTextureRegion();	
//		default:
//			return null;
//		}
		return SmurfTexturex.getTextureRegion();
		
	}

}
