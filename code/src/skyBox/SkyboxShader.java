package skyBox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;


//This class is same as our shader classes before....................................................

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";                                 // location of skybox vertex shader........
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";                              // location of skybox fragment shader........
	
	private int location_projectionMatrix;                                                                       //store location id of projection matrix......
	private int location_viewMatrix;                                                                            // store location id of view matrix............
	private int location_fogColor;                                                                               // store location id of fog color............
	private int location_cubeMap;                                                                               // store location id of cube map1............
	private int location_cubeMap2;                                                                               // store location id of cube map2............
	private int location_blendFactor;                                                                        // store location id of fog blend factor............
	
	private static final float ROTATION_SPEED = 0f;                                             //rotation speed of the skybox............
	
	private float rotation = 0;                                                            //current rotation of the skybox...................
	
	
	//constructor......................
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);                                                                      // call constructor of shader program class......
	}
	
	
	
	//Method to load projection matrix in shader code..........................
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	
	//Method to load view matrix in shader code..........................
	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);                    // create a new 4f matrix and calculate the view matrix.............
		matrix.m30 = 0 ;                                                     // enable translation.............
		matrix.m31 = 0 ;                                                     // enable translation.............
		matrix.m32 = 0 ;                                                     // enable translation.............
		rotation += ROTATION_SPEED * DisplayManager.getFrameTimeSeconds();           // increase the rotation...................
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);          // rotate the view matrix by the rotation..............
		
		super.loadMatrix(location_viewMatrix, matrix);                         // load view matrix to the shader...............
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColour");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void connectTextures() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}
	
	
	public void loadFogColor(float r, float g, float b) {
		
		super.loadVector(location_fogColor, new Vector3f(r,g,b));
		
	}
	
}
