package entities;

import org.lwjgl.util.vector.Vector3f;



//This class represent the light in our game. It use fields such as color and position
public class Light {
	
	private Vector3f position;                                                         //Stores the position of the light source........................
	private Vector3f colour;                                                           // Stores the color of the light........................
	private Vector3f attenuation = new Vector3f(1,0,0);                                // Stores the attenuation value. For sun it is 0....................
	
	
	
	//This constructor takes in and assigns position and color..................................
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	
	
	//This constructor takes in and assigns position, color and attenuation value..................................
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}
	
	
	
	// Getter and setter methods to assign and get values.............................
	
	
	//get Attenuation.................
	public Vector3f getAttenuation() {
		return this.attenuation;
 	}
	
	//get position.................
	public Vector3f getPosition() {
		return position;
	}
	
	//set position.................
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	//get color.................
	public Vector3f getColour() {
		return colour;
	}

	//set color.................
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	

}
