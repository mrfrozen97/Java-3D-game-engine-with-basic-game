/*
It is used to handle the display of the 3D game...........


*/


package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int WIDTH = 1780;  // Width of display screen....
	private static final int HEIGHT = 980;   // Height of display screen......
	private static final int FPS_CAP = 120;   // Controls the speeed of the display........
	
	
	private static long lastFrameTime;                         //stores time of last frame.....................
	private static float delta;                                //holds time passed since previous frame......................

	
	
	///Method used to create display to run the game. It uses the functions of lwjgl to set and create display Screen...............................
	
	public static void createDisplay(){		
	
		ContextAttribs attribs = new ContextAttribs(3,2)   // Takes in the vesion of the opengl we want to use..................
		.withForwardCompatible(true)                          // just some optional settings...........................
		.withProfileCore(true);
	
		
		// Surrounded by a try catch block...........
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));     // Set the size of the display by passing it in a new Displaymode constructor.
			Display.create(new PixelFormat(), attribs);                // Create the display and set some settings for future use.....
			
			Display.setTitle("Java 3D game");                // This sets the title of the Game screen................
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0,0, WIDTH, HEIGHT);                    /// Tells opengl size in which to render the game. for our case it is entire screen.
        lastFrameTime = getCurrentTime();                   // get the current time..........................
	
	}
	
	public static void updateDisplay(){
		
		Display.sync(FPS_CAP);                          // Tells opengl the speed/fps we want to run the display.........
		Display.update();                              // Updates the display with above frame rate..........
		long currentFrameTime = getCurrentTime();                    //get the current frame time...............
		delta = (currentFrameTime - lastFrameTime)/1000f;               // get the time passed.............
		lastFrameTime = currentFrameTime;                              // set last time to previous time.......................
		
	}
	
	public static float getFrameTimeSeconds() {
		
		return delta;
		
	}
	
	public static void closeDisplay(){
		
		Display.destroy();                          /// Just closes the display...................................
		
	}
	
	
	
	//Method to return the current time.........................
	private static long getCurrentTime() {         
		
		return Sys.getTime()*1000/Sys.getTimerResolution();              //This function gives time in seconds which we multiply by 1000 for milliseconds..........
		
	}

}
