package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Player;
import renderEngine.DisplayManager;

//This class represent single particle in the space.................
public class Particle {

	private Vector3f velocity;
	private Vector3f position;
	private float gravityEffect;
	private float lifeLenght;
	private float roatation;
	private float scale;
	
	private ParticleTexture texture;
	
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;
	
	private float alive_Time = 0;
	private float distance;

	
	
	public Particle( Vector3f position, Vector3f velocity, float gravity, float lifeLenght, float roatation,
			float scale, ParticleTexture texture) {
		super();
		this.velocity = velocity;
		this.position = position;
		this.gravityEffect = gravity;
		this.texture = texture;
		this.lifeLenght = lifeLenght;
		this.roatation = roatation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}



	public Vector2f getTexOffset1() {
		return texOffset1;
	}



	public void setTexOffset1(Vector2f texOffset1) {
		this.texOffset1 = texOffset1;
	}



	public Vector2f getTexOffset2() {
		return texOffset2;
	}



	public float getDistance() {
		return distance;
	}



	public void setDistance(float distance) {
		this.distance = distance;
	}



	public void setTexOffset2(Vector2f texOffset2) {
		this.texOffset2 = texOffset2;
	}



	public float getBlend() {
		return blend;
	}



	public void setBlend(float blend) {
		this.blend = blend;
	}



	public float getGravityEffect() {
		return gravityEffect;
	}



	public ParticleTexture getTexture() {
		return texture;
	}



	public Vector3f getVelocity() {
		return velocity;
	}



	public Vector3f getPosition() {
		return position;
	}



	public float getGravity() {
		return gravityEffect;
	}



	public float getLifeLenght() {
		return lifeLenght;
	}



	public float getRoatation() {
		return roatation;
	}



	public float getScale() {
		return scale;
	}



	public float getAlive_Time() {
		return alive_Time;
	}
	
	
	protected boolean update(Camera camera) {
		
		velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		updateTextureCoordInfo();
		alive_Time += DisplayManager.getFrameTimeSeconds();
		return alive_Time < lifeLenght;
		
	}
	
	private void updateTextureCoordInfo() {
		
		float lifeFactor = alive_Time / lifeLenght;
		int stageCount = texture.getNumberofRows() * texture.getNumberofRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1+1 : index1;
		this.blend = atlasProgression %1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
		
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberofRows();
		int row = index /texture.getNumberofRows();
		offset.x = (float) column /texture.getNumberofRows();
		offset.y = (float) row /texture.getNumberofRows();
	}
	
	

}
