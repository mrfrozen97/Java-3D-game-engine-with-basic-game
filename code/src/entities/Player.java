package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;



//This class represents our player.......................
public class Player extends Entity{                        //Player extends entity because it is entity...............
	
	
	
	private List<TexturedModel> models = new ArrayList<TexturedModel>();
	private TexturedModel model;
	private int playerIndex = 0;
	private int delay = 0;
	private int playerSpeed = 1;
	private float playerRelativeToTerrainHeightAdjustment = 0;
	private int smooth = 35;
	
	private int limit_XPOS = -97;                                      //player movement range on x axis.....................................
	private int limit_ZPOS = -limit_XPOS;                                      //player movement range on z axis....................................
	private int limit_XPOS1 = 1981;                                      //player movement range on x axis.....................................
	private int limit_ZPOS1 = -1981;                                      //player movement range on z axis......................................
	private float bounceBackPlayer = limit_XPOS +  0.02f;                               // bounce bak player in if he tries to move out....................
	private float terrainHeight;
	
	
	
	private static final float RUN_SPEED = 50;                                // Store Running speed of the player.....................................
	private static final float TURN_SPEED = 120;                              // Store Turning speed of the player............................
	public static final float GRAVITY = -50;                                 // Store the gravity or the speed of the falling................
	private static final float JUMP_POWER  = 80;                              // Jump power is the speed of jump........................
	
	private static boolean isInAir = false;                                   // Check if the player is already in air.....................
 	
	private static final float TERRAIN_HEIGHT = 0;                            // Stores the terrain height to use for payer movement....................
	
	private float currentSpeed = 0;                                          // Stores the current speed........................
	private float currentTurnSpeed= 0;                                       // Stores the current rotation speed....................
	private float upwardSpeed = 0;                                           // stores the current upward or jumping speed....................
	

	
	//Constructor takes the model, position, rotation and scale inputs and pass it to the parent class..................................
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);                                  // pass parameters to the constructor of the parent class..........
		// TODO Auto-generated constructor stub
	}

	
	public Player(TexturedModel model, List<TexturedModel> models, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);                                  // pass parameters to the constructor of the parent class..........
		// TODO Auto-generated constructor stub
		this.models = models;
		this.model = model;
	}
	
	
	
	
	//Method to move player.................
	public void move(Terrain terrain){
		
		if(this.currentSpeed!=0 && (this.getPosition().y -this.terrainHeight) < 4 ) {
		if(this.delay == this.playerSpeed) {
		
		super.setModel(models.get(this.playerIndex));
		if(playerIndex == models.size() - 1) {
			this.playerIndex = 0;
		}
		else {
			this.playerIndex++;
		}
		this.delay = 0;
		}
		else {
			this.delay++;
		}
		}
		else if(!((this.getPosition().y -this.terrainHeight) < 4)) {
			super.setModel(models.get(39));
		}
		else {
			
			super.setModel(this.model);
		}
		
		
		checkInputs();                                                                                         //call check inputs method.....
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);                 //rotate object with current turn speed.......
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();                               // calculate distance to be moved by player........
		float dx;
		
		 dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));                         // calculate increase x........
		if((this.getPosition().x > limit_XPOS || dx > 0 ) && (this.getPosition().x < limit_XPOS1 || dx < 0)) {
		     }
		else {
		         dx = 0;
		}
		
		
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));                         // calculate z increase........  
		float temp1 = this.getPosition().z;
		if((temp1 < limit_ZPOS || dz < 0) && (temp1 > limit_ZPOS1 || dz > 0)) 
		{
	      }
	    else {
	           dz = 0;
	     }
		
		
		
		
		terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z) + playerRelativeToTerrainHeightAdjustment;
		Vector3f underWater = this.getPosition();
		if(underWater.x < 0 || underWater.z > 0) {
			//System.out.println("123testing..........");
			terrainHeight = Math.min(Math.max(underWater.x * 0.2f, -5), Math.max(-underWater.z * 0.2f, -5)) + playerRelativeToTerrainHeightAdjustment ;
			
		}
		
		
		
		super.increasePosition(dx, 0, dz);                                                                 //call increase position.............
		upwardSpeed += GRAVITY *  DisplayManager.getFrameTimeSeconds();                                   // decrease the player speed/ fall player........
	    super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);                  // add y movement in -ve direction........
	          
	    
	    if(super.getPosition().y<terrainHeight) {
	    	upwardSpeed = 0;                                                                               //upward speed is 0 if we reach bottom......
	    	super.getPosition().y = terrainHeight;                                   
	    	isInAir = false;                                                                              //player landed so is in air is false......
	    }
	}
    
	
	
	
	
	
	//Jump method just increase the upward speed of the player to the jump power.....................
	private void jump() {
		if(!isInAir) {                                 // Check if player is not in air...........................
		this.upwardSpeed+= JUMP_POWER;
		isInAir = true;                                    // After jumping assign is in air true.......................
	}
		}
	
	
	
	/// This method assigns current speed of the player according to the keyboard inputs..................................
	private void checkInputs() {
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {                             // If w key pressed current speed of player is speed we assign.............
			this.currentSpeed = RUN_SPEED;
			}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {                        // If s key pressed current speed of player is speed we assign.............
			this.currentSpeed = -RUN_SPEED;
		}
		else {
			this.currentSpeed = 0;                                          // if no key pressed speed is 0......................
		}
		
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {                    // If d key pressed current turning speed of player is speed we assign.............
			this.currentTurnSpeed = -TURN_SPEED;
			}
		else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {               // If a key pressed current turning speed of player is speed we assign.............
			this.currentTurnSpeed = TURN_SPEED;
			}
		else {
			this.currentTurnSpeed = 0;                                // if no key pressed turning speed is 0......................
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {               // If space key pressed call the jump method.............
			jump();                                                  
			}
		
	}
	
}
