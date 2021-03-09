package skyBox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import models.RawModel;
import renderEngine.Loader;

public class SkyboxRenderer {

private static final float SIZE = 1000f;                             //Size of our skybox..........
private static final float BLEND = 0.0f;                               // stores the blend factor of the game.....
	
	private static final float[] VERTICES = {                                         //positionn coordinate of skybox...........
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};

	
	
	//String array that stores the name of the images in the sky box1......................................
	private static String[] TEXTURE_FILES = {"skyBox/right", "skyBox/left", "skyBox/top", "skyBox/bottom", "skyBox/back", "skyBox/front"};  
	
	//String array that stores the name of the images in the sky box2......................................
    private static String[] TEXTURE_FILES2 = {"skyBox2/nightRight", "skyBox2/nightLeft", "skyBox2/nightTop", "skyBox2/nightBottom", "skyBox2/nightBack", "skyBox2/nightFront"}; 
	
	
	
	private RawModel cube;                                      //create a model cube........................
	private int texture;                                       // to store the id of the texture1................
	private SkyboxShader shader;                               // shader variable for texture................
	private int nightTexture;                                       // to store the id of the texture2...............
	
	     
	
	
	
	
	//Constructor to initialise all the skybox and load it and pass it to shader......................
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		
		cube = loader.loadToVAO(VERTICES, 3);                                               //load our cube to a vao..................................
		texture = loader.loadCubeMap(TEXTURE_FILES);                                       // load our texture1 files fro res........................
		nightTexture = loader.loadCubeMap(TEXTURE_FILES2);                                   // load our texture2 files fro res........................
		shader = new SkyboxShader();                                                         // object of shader...................................
		shader.start();                                                                       //start/prepare the shader to pass our parameters............
		shader.connectTextures();                                                                //tell shader which texture to pass.........
		shader.loadProjectionMatrix(projectionMatrix);                                    //method to load projection matrix to shader................
		shader.stop();                                                                   //stop the shader after load...................
		
	}
	
	
	
	//method to load View matrix to the shader...........................
	public void render(Camera camera, float r, float g, float b) {                           //rgb is fog color.........
		
		shader.start();                                                                    // start shader...........................
		shader.loadViewMatrix(camera);                                                 //load view matrix to shader.............................
		shader.loadFogColor(r, g, b);                                                 // load fog color to shader.................
		GL30.glBindVertexArray(cube.getVaoID());                                      // load our cube to the vao................
		GL20.glEnableVertexAttribArray(0);                                            // enable our attribute 0 of vao......................
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());              //draw the object on screen.............
		GL20.glDisableVertexAttribArray(0);                                          //disable our vao..................
		GL30.glBindVertexArray(0);                                                    // unbind vao....................
		shader.stop();                                                                //stop shader.............
	}
	
	
	
	//Method to bind the textures........................................................
	private void bindTextures() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);                           //Bind the texture1....................
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexture);                     // Bind the texture2...................
		shader.loadBlendFactor(BLEND);                                                   //load blend factor to the shader.................
	}
	
	
	
}
