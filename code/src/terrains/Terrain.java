package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexturePack;
import textures.TerrainTextures;
import toolbox.Maths;



//This class represents all the terrains in our game.......................................................
public class Terrain {
	
	private static final float SIZE = 2048;                                          //Stores the size of our terrain(square)..............
	private static float MAX_HEIGHT = 10;                                     //Stores the maximum height terrain can have.................
	private static final float MAX_PIXEL_COLOUR = 256*256*256;                      //set the max color pixel.........................
	private float [][] heights;
	//private static final int VERTEX_COUNT = 128;                                  //Stores the number of vertices in each side of terrain...................
	
	private float x;                                                                //Stores the x position in the world.........
	private float z;                                                                //Stores the y position in the world.........
	private RawModel model;                                                         //Stores the terrain model.................
	private TerrainTexturePack texturePack;                                        //stores the pack of terrain textures............
	private TerrainTextures blendMap;                                              
	
    boolean HeightZero = false;
	
	
	
	
	//This constructor takes the x, y indices of grids of the model , a loader object, textues pack of the textures
	// and also the blend map and height map of the terrains..............................................
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTextures blendMap, String heightMap, boolean heightZero){
		if(heightZero) {
			this.MAX_HEIGHT =0; 
		}
		else {
			this.MAX_HEIGHT =40;
		}
		
		
		this.texturePack  = texturePack;                                   //assign the texture pack of the terrain............
		this.blendMap = blendMap;                                          //assign the blend map of the terrain.............                                        
		this.x = gridX * SIZE;                                             //assign the x position of the terrain...........        
		this.z = gridZ * SIZE;                                             //assign the y position of the terrain...........
		this.model = generateTerrain(loader, heightMap);                   //call generate terrain models get the terrain model.....................
	}
	
	
	
	
	
	//Getter methods to get these parameters if required......................
	public float getX() {
		return x;
	}



	public float getZ() {
		return z;
	}



	public RawModel getModel() {
		return model;
	}




	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}



	public void setTexturePack(TerrainTexturePack texturePack) {
		this.texturePack = texturePack;
	}



	public TerrainTextures getBlendMap() {
		return blendMap;
	}



	public void setBlendMap(TerrainTextures blendMap) {
		this.blendMap = blendMap;
	}
    
	
	
	//This method gives height for any x and z position................................................
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;                                     
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float)heights.length - 1);
		int gridX = (int) Math.floor(terrainX/ gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ/ gridSquareSize);
		
		if(gridX >= heights.length -1 || gridZ >= heights.length- 1|| gridX < 0|| gridZ <0) {
			return 0;
		}
		
		float xCoords = (terrainX % gridSquareSize)/gridSquareSize;
		float zCoords = (terrainZ % gridSquareSize)/gridSquareSize;
		float answer;
		if(xCoords <1-zCoords) {
			
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ+1], 1)
					, new Vector2f(xCoords, zCoords));
			
		}
		else {
			
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX+1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ+1], 1), new Vector3f(0, heights[gridX][gridZ+1], 1)
					, new Vector2f(xCoords, zCoords));
			
		}
		return answer;
		
	}


	
	
	
	
	
	
	
	//This method below just generates a terrain.
	//It takes in the loader, height map for the terrain............................................................
	//It return vao which has all the positions and stuff loaded in it.........................
	private RawModel generateTerrain(Loader loader, String heightMap){
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("res/" + heightMap  + ".png"));                         //load height map.........................
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int VERTEX_COUNT = image.getHeight();                                       // gives total number of vertex............................
		heights = new float [VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height ;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	
	
	
	
	
	
	
	
	///Mathematical method to calculate the normal of of the terrain.............. 
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getHeight(x-1, z, image);
		float heightR = getHeight(x+1, z, image);
		float heightD = getHeight(x, z-1, image);
		float heightU = getHeight(x, z+1, image);
		Vector3f normalVector = new Vector3f(heightL-heightR, 2f, heightD-heightU);
		normalVector.normalise();
		
		return normalVector;                         // return normal vector..................
		
	}
	
	
	
	
	public float getSize() {
		return SIZE;
	}





	public static float getMaxHeight() {
		return MAX_HEIGHT;
	}





	public static float getMaxPixelColour() {
		return MAX_PIXEL_COLOUR;
	}





	public float[][] getHeights() {
		return heights;
	}



	
	public void setHeightZero() {
		this.MAX_HEIGHT =0;
	}


	//This method returns the height of the map according to the pixel........................
	 private float getHeight(int x, int z, BufferedImage image) {
		 
		
		 if(x<0 || x>=image.getHeight() || z<0 || z>=image.getHeight()) {
			 return 0;
		 }
		 
		 float height = image.getRGB(x, z);         //this function returns a value between -max pixel and 0........
		 height += MAX_PIXEL_COLOUR/2f;
		 height /= MAX_PIXEL_COLOUR/2f;
		 height *= MAX_HEIGHT;
		 
		 return height;
		 
		 
	 }
	
}
