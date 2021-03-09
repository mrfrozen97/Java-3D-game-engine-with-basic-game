package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;


/*
 This camera represent the virtual camera. It holds and updates all the camera parameters................................... 
 */

public class Camera {
	
	private float distanceFromPlayer = 12;                                              //store distance of camera from player......
	private float angleAroundPlayer  = 0;                                               //store the angle around player.......
	private float PLAYER_HEIGHT = 9;                                                  // stores the height of the player.............
	private float firstPersonZoomDistance = -1;                                            // stores zoom distance depends on the player model.........
	private boolean setFirstPerson = false;                                                   // stores weather first person is on........
	
	
	
	private Vector3f position = new Vector3f(100,55,50);                       // stores the camera position........................
	private float pitch = 10;                                                 // stores the camera pitch or rotation(how high or low camera is)...............
	private float yaw = 0 ;                                                      // stores the camera yaw or rotation(how left or right camera is)...............
	private float roll;                                                     // stores the camera roll or rotation(how tilted camera is)...............
	
	private Player player;
	
	
	
	
	public Camera(Player player){
		
		this.player = player;
		
	}
	
	
	
	public void setFirstPersoncameraON() {
		this.setFirstPerson =true;
	}
	public void setFirstPersoncameraOFF() {
		this.setFirstPerson =false;
	}
	
	
	
	//This decides the movement of the camera which is opposite of that of the movement of the game......................................
	public void move(Player player){
		
		
		if(!setFirstPerson) {
		calculateZoom();                                            // call zoom method......................
		calculateAngleAroundPlayer();                               // call angle around method......................
		calculatePitch();                                           // call pitch method......................
		}
		else {
			this.distanceFromPlayer = firstPersonZoomDistance;
			calculatePitchFirstPerson();                               // call angle around method......................
			
		}
		
		
		
		
		
		//This is method for first camera view................
		//move_camera();
		

		float horizontalDistance = calculateHorizontalDistance();           // call distance from camera method......................
		float verticalDistace =  calculateVerticalDistance();               // call height from camera method......................
		
		calculateCameraPosition(horizontalDistance,verticalDistace);               //calculate main camera distance calculate method............
		this.yaw = 180 - (player.getRotY()  + angleAroundPlayer);                   // calculate camera yaw of camera........
	}

	
	
	
	
	
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	
	
	
	
	
	//This is the getters to get the parameters of the camera..........................................
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
	
	
	
	
	//Method to calculate position of the camera.....................
	
	private void calculateCameraPosition(float horizontalDist, float verticalDist) {            //takes in horizontal and vertical distance......
		float theta = player.getRotY() + angleAroundPlayer;                                    // y rotation of the camera.......
		float offsetX = (float) ( horizontalDist * Math.sin(Math.toRadians(theta)));              // calculate offset x of camera...
		float offsetZ = (float) ( horizontalDist * Math.cos(Math.toRadians(theta)));             // calculate offset y of camera...
		
		position.x = player.getPosition().x -offsetX;                                 // cameras x,y,z positions........................
		position.z = player.getPosition().z -offsetZ;
		position.y = player.getPosition().y + verticalDist;
		
	}
	
	
	
	
	
	
	
	
	//This method checks how much mouse wheel is rotated.....................
	private void calculateZoom() {
		
		float zoomLevel = Mouse.getDWheel() * 0.02f;              //function to check mouse wheel movement..........................
		distanceFromPlayer -= zoomLevel;                          // increase/decrease distance from player......................
		
		if(distanceFromPlayer <-20) {
			distanceFromPlayer = -20;
		}
		
	}
	
	
	
	
	
	//Calculate horizontal distance from player...........................
	private float calculateHorizontalDistance() {
		
		return (float) (distanceFromPlayer  * Math.cos(Math.toRadians(pitch)));
		
	}
	//Calculate vertical distance from player...........................
   private float calculateVerticalDistance() {
		
		return (float) (distanceFromPlayer  * Math.sin(Math.toRadians(pitch)) + PLAYER_HEIGHT);
		
	}
	
   
   
   
   
   
   
   //This method calculate increase in pitch...................
	private void calculatePitch() {
		if(Mouse.isButtonDown(1) && (this.pitch >-10 && this.pitch <190)) {                 //checks if right mouse button is down.................... 
			
			float pitchChange = Mouse.getDY() * 0.1f;              //checks mouse movement.................
			pitch -= pitchChange;                              //changes pitch.................
			
		}
		else if(Mouse.isButtonDown(1)) {
			pitch = -9.9f;
		}
		
	}
	
	
	

	   //This method calculate increase in pitch for first person view...................
		private void calculatePitchFirstPerson() {
			if((this.pitch >-60 && this.pitch <100)) {                 //checks if right mouse button is down.................... 
				
				float pitchChange = Mouse.getDY() * 0.1f;              //checks mouse movement.................
				pitch -= pitchChange;                              //changes pitch.................
				
			}
			else {
				pitch = -59.99f;
			}
			
		}
	
	
	


	
	
	
	
	///This method is only to be used when we have to use first person camera
	public void move_camera(Player player) {
		
		float pitchChange = Mouse.getDY() * 0.05f;              //checks mouse movement.................
		pitch -= pitchChange;                              //changes pitch.................
		float angleChange = Mouse.getDX() * 0.15f;          //checks mouse movement.................            
		angleAroundPlayer -= angleChange;                    //changes pitch.................
	}
	
	
	
	
	
	
	 //This method calculate change in angle around player...................
	private void calculateAngleAroundPlayer() {
		
          if(Mouse.isButtonDown(0)) {                         //checks if left mouse button is down.................... 
			
			float angleChange = Mouse.getDX() * 0.3f;          //checks mouse movement.................            
			angleAroundPlayer -= angleChange;                    //changes pitch.................
			 
		}
	}
}








