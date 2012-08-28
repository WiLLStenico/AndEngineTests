/**
 * 
 */
package whs.games.andengine.antsfalling;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * @author WiLL
 *
 */
public final class Ball extends AnimatedSprite {
	private final PhysicsHandler mPhysicsHandler;

	public float DEMO_VELOCITY = 100.0f;
	/**
	 * @return the dEMO_VELOCITY
	 */
	public float getDEMO_VELOCITY() {
		return DEMO_VELOCITY;
	}

	/**
	 * @param dEMO_VELOCITY the dEMO_VELOCITY to set
	 */
	public void setDEMO_VELOCITY(float dEMO_VELOCITY) {
		if(dEMO_VELOCITY > 0){
		DEMO_VELOCITY = dEMO_VELOCITY;
		}
	}

	private final Camera mCamera;
	
	public Ball(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, Camera camera) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mCamera = camera;
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.mPhysicsHandler.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);
		
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mX < 0) {
			this.mPhysicsHandler.setVelocityX(DEMO_VELOCITY);
		} else if(this.mX + this.getWidth() > mCamera.getWidth()) {
			this.mPhysicsHandler.setVelocityX(-DEMO_VELOCITY);
		}else{
			//this.mPhysicsHandler.setVelocityX(-DEMO_VELOCITY);
		}

		if(this.mY < 0) {
			this.mPhysicsHandler.setVelocityY(DEMO_VELOCITY);
		} else if(this.mY + this.getHeight() > mCamera.getHeight()) {
			this.mPhysicsHandler.setVelocityY(-DEMO_VELOCITY);
		}else{
			//this.mPhysicsHandler.setVelocityY(-DEMO_VELOCITY);
		}

		super.onManagedUpdate(pSecondsElapsed);
	}
}