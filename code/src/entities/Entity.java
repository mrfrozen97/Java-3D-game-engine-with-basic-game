package entities;

import models.TexturedModel;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

/*
 This class hold  a entity of any object to be rendered on screen.
 It holds the positions, rotations and the scale of the object........................................
 */

public class Entity {

	
	private List<TexturedModel> models;               
	
	private TexturedModel model;                   // stores the model in a variable of type in texture...........
	private Vector3f position;                      // stores 3d positions of the vector................
	private float rotX, rotY, rotZ;                  // stores the rotation in each direction..............
	private float scale;                             // stores the scale of the object....................
	
	private int textureIndex = 0;

	
	//Constructor which takes in and assigns all the required parameters..............
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	
	public Entity(List<TexturedModel> models, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.models = models;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	

	
	
	
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale, int textureIndex) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.textureIndex = textureIndex;
	}

	
	
	
	//This method increases the position of the entity in the world by the values we pass in.............................................
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;                                  // add the x,y and z values to current position......................
		this.position.z += dz;
	}

	
	
	
	//This method increases the rotation of the entity in the world by the values we pass in.............................................
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;                                       // add the x,y and z values to current rotation......................
		this.rotZ += dz;
	}
	
	
	
	
	public float getTextureXOffset() {
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}

	public float getTextureYOffset() {
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}

	
	
	
	
	
	
	
	
	////All the  methods below are getters and setters to get or set values whenever required........................
	
	
	
	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	
	
	
}
