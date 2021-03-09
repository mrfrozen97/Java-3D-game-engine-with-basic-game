package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import shaders.StaticShader;
import shaders.TerrainShader;
import shadows.ShadowMapMasterRenderer;
import skyBox.SkyboxRenderer;
import terrains.Terrain;
import entities.Camera;
import entities.Entity;
import entities.Light;


//This class manages all the rendering in our game...............................
public class MasterRenderer {
	
	public static final float FOV = 70;                                   //The field view angle of the frustum..........................
	public static final float NEAR_PLANE = 0.1f;                         // stores the near plane variable of the screen..................
	public static final float FAR_PLANE = 2000;                          // Stores how far we can see in the screen.........
	private static final float RED  = 0.0f;                              // Store the sky red...............
	private static final float GREEN = 0.93f;                             // Store the sky green...............
	private static final float BLUE = 1.4f;                             // Store the sky blue...............
	
	private Matrix4f projectionMatrix;                                     // stores the projection matrix.................
	
	private StaticShader shader = new StaticShader();                      // create a object of static shader................
	private EntityRenderer renderer;                                       // stores the renderer of the entity renderer.............................
	
	private TerrainRenderer terrainRenderer;                              //Create a variable of terrain renderer.......................
	private TerrainShader terrainShader = new TerrainShader();             //create a object of terrain shader class..............
	
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();              //Hash map that contains lot of models............
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	
	
	private SkyboxRenderer skyboxRenderer;                                                   //skybox renderer variable................
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	
	
	
	
	
	//Constructor of the Master renderer..........
	public MasterRenderer(Loader loader, Camera cam){
		enableCulling();                                                                  // call enable calling.......................
		createProjectionMatrix();                                                         // loads up the projection matrix..........................
		renderer = new EntityRenderer(shader,projectionMatrix);                       // pass the shader and projection matrix in the entity renderer class.......
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);   // pass the terrain shader object and projection to terrain renderer constructor......
		
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);                    //call the constructor of the skybox renderer class..................
		this.shadowMapRenderer = new ShadowMapMasterRenderer(cam);
	}
	
	
	
	
	
	//This method is called if model do not have transparency.....................
	//this method enables calling which basically allows opengl to not render faces which are pointing away from our vision...............
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);                  // this function tells opengl to enable culling................
		GL11.glCullFace(GL11.GL_BACK);                       // this function tells which faces to cull...........
	}
	
	
	
	
	
	//Method to disable calling........................................
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);                                //this function tells opengl to disable calling.................                      
	}
	
	
	
	
	
	
	//This method renders the parameters of each entity into the shader ..................................
	public void render(List<Light> lights,Camera camera){
		
		prepare();                                             //prepares the screen before rendering........................
		shader.start();                                        // start the shader........................
		shader.loadSkyColour(RED, GREEN, BLUE);                //load the sky color to the shader......................
		shader.loadLights(lights);                            // load lights to the shader..........
		shader.loadViewMatrix(camera);                        //load the camera to the shader......................
		renderer.render(entities);                            //render all entities on the screen...............    
		shader.stop();                                           //stop the shader.........................
		terrainShader.start();                                   // start the terrain shader........................
		terrainShader.loadSkyColour(RED, GREEN, BLUE);           //load the sky color to the terrain shader......................
		terrainShader.loadLight(lights);                         // load lights to the terrain shader..........
		terrainShader.loadViewMatrix(camera);                    //load the camera to the terrain shader......................
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());                         //render all terrains on the screen.....................
		terrainShader.stop();                                      //stop the terrain shader.........................    
		skyboxRenderer.render(camera, RED, GREEN, BLUE);                //call method to render the skybox on the screen....................
		terrains.clear();                                                //clear terrains after rendering...........................
		entities.clear();                                               // clear our entities.........................
	}
	
	
	
	///Method takes in terrain to add  the terrain to the list of the terrain...........................................
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);                                   //add terrain to list......................
	}
	
	
	
	
	//Method to put entity in the hash map........................................................
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();                             // create a entity model...............................
		List<Entity> batch = entities.get(entityModel);                          // get a batch from hashmap........................  
		if(batch!=null){                                                           //add entity to existing batch if it is not null...............
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();                       //create a new batch.......................
			newBatch.add(entity);                                                 // add entity to the new batch........................
			entities.put(entityModel, newBatch);		                          // add this batch and model to the hashmap................
		}
	}
	
	
	
	
	
	//clean up method to clean up shader after use........
	public void cleanUp(){
		shader.cleanUp();                      // call clean up shader.................
		terrainShader.cleanUp();
	    shadowMapRenderer.cleanUp();
	}
	
	public int renderShadowMap(List<Entity> entityList, Light sun) {
		
		for(Entity entity: entityList) {
			processEntity(entity);
		}
		shadowMapRenderer.render(entities, sun);
		entities.clear();
		
		return shadowMapRenderer.getShadowMap();
	}
	
	
	public int getShadowMaptexture() {
		return shadowMapRenderer.getShadowMap();
	}
	
	
	//method to get projection matrix..............................
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}





	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}





	public StaticShader getShader() {
		return shader;
	}





	public EntityRenderer getRenderer() {
		return renderer;
	}





	public TerrainRenderer getTerrainRenderer() {
		return terrainRenderer;
	}





	public TerrainShader getTerrainShader() {
		return terrainShader;
	}





	public Map<TexturedModel, List<Entity>> getEntities() {
		return entities;
	}





	public List<Terrain> getTerrains() {
		return terrains;
	}





	public SkyboxRenderer getSkyboxRenderer() {
		return skyboxRenderer;
	}





	//prepares the screen before we refresh the screen.................................
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);                           // test for depth to check overlapping triangles and render in depth order......
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);       //need to cleear depth buffer every frame.........
		GL11.glClearColor(RED, GREEN, BLUE, 1);                             // clear screen.............................
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMaptexture());
	}
	
	
	
	
	
	
	
	
	
	///This method stores the projection matrix elements in the projection matrix based on its maths.......................
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f)));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	

}
