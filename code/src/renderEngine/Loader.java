package renderEngine;

/*
This class loads the models into the memory . It stores the positional data of the model in a VAO.

*/

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;
import textures.TextureData;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;



import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {
	
	// This is made to keep a track of the vaos and abos to delete it after our use to free memory.....
	
	private List<Integer> vaos = new ArrayList<Integer>();        // store the vaos we create.........
	private List<Integer> vbos = new ArrayList<Integer>();        // store the vbos we create..........
	private List<Integer> textures = new ArrayList<Integer>();     // stores the textues in texture list.....................
	
	
	
	
	
	
	
	/*
	 This  method takes positions to store the values in the vao and return the information of the VAO(length and id).
	 It manages the creation, loading and unbinding of a vao................
	 It takes in positions, texture coordinates, normals of coordinates and indices......................
	 
	 */
	public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals,int[] indices){
		int vaoID = createVAO();                         // call create vao method..........
		bindIndicesBuffer(indices);                             // call method to create a indices buffer.......................
		storeDataInAttributeList(0,3,positions);               //call store positions data in attribute list 0........
		storeDataInAttributeList(1,2,textureCoords);           //call store texture coordinates data in attribute list 1.......
		storeDataInAttributeList(2,3,normals);                 //call store data in attribute list 2.......
		unbindVAO();                                      // call unbind vao after the use............
		return new RawModel(vaoID,indices.length);         // return a rawmodel the information of the vao.......
	}
	
	
	
	
	
	
	
	
	public int loadToVAO(float[] positions,  float[] textureCoords) {
		int vaoID = createVAO();                                         // create vao method.......................
		storeDataInAttributeList(0,2,positions);               //call store positions data in attribute list 0........
		storeDataInAttributeList(1,2,textureCoords);           //call store texture coordinates data in attribute list 1.......
		unbindVAO();                                                  //unbind vao..........................
		return vaoID;                                                  //return vaoid.................
	}
	
	
	
	
	
	//This method is used to load guis on the screen.........................................
	public RawModel loadToVAO(float[] positions,int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/dimensions);
	}
	
	
	
	
	
	/*
	 This methods loads the texture in opengl so that we can us it. It takes in file name and returns id of the texture.................. 
	 */
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		
		try {                                                                      //This block checks if the file is present..............                                          
			texture = TextureLoader.getTexture("PNG",                   // this allows us to load a texture in the opengl and it takes a file path...........  
					new FileInputStream("res/" + fileName + ".png"));                     
		
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);                                                    //generate mip map.....................
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);      
			// tell opengl use mipmap when surface has less surface area.............................
			
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);    //Tell it to use it with parameter by passing bias for mip mapping......
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ".png , didn't work");           //print exeception......................
			System.exit(-1);
		}
		textures.add(texture.getTextureID());                                     // Add textures to texture array............      
		return texture.getTextureID();                                             // return the id of the texture.............
	}
	
	
	
	

	
	
	
	
	
	/// It just loops to vao and vbo list and deletes it........
	
	public void cleanUp(){
		
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);         // it deletes the vao from memory by taking in vao id............
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);           // it deletes the vbos and takes vbo id.............................
		}
		for(int texture:textures){
			GL11.glDeleteTextures(texture);        //It deletes the tuxture in textures array after game is over..........................
		}
	}
	
	
	
	
	
	
	
	
	/*
	 Create a new empty vao and returns the id of the vao 
	 */
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();              // creating empty vao using opengl function..........................
		vaos.add(vaoID);                                   // add vao to vao list when we create it.....................
		GL30.glBindVertexArray(vaoID);                    // activate the vao and it takes in the id of the vao................
		return vaoID;                                // return the id...............
	}
	
	
	
	
	
	
	
	
	
	/*
	 Stores the data in a attribute list........... 
	 It takes in attribute Number and also the data itself.
	 Coordinate six=ze for positions is 3 but for textures it is 2
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
		
		
		
		int vboID = GL15.glGenBuffers();                                    // creating the empty vbo....................
		vbos.add(vboID);                                                    // add the vboid in vbos.................
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);                     // bind the vbo buffer by passing type of vbo and id.............
		
		FloatBuffer buffer = storeDataInFloatBuffer(data);      
		// data stored in vbo in form of float buffer by calling store float buffer...................
		
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); 
		// now we can store our data in vbo using this opengl function.
		// It takes in the type of data, data, specify if it is static data or we are going to change it............
		
		GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);
		/// It loads the vbo in the vao in attribute list..................
		/// It takes in attribute  list number, length of each vertex(3d), type of data, is it normalised, distance between each vertex, offset
		//// offset means if it should start from beguinning(yes)...
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);       /// Unbind the vbo by passing the data as 0....................................
		
	}
	
	
	
	
	
	
	
	/*
	 It unbinds the vao. We have to unbind the vao after we have used it.
	*/
	private void unbindVAO(){
		GL30.glBindVertexArray(0);                   //we pass 0 in bind vao to unbind it.........................................
	}
	
	
	
	
	
	
	
	
	
	//This method takes in array of indices and binds it and stores to a vbo.........................................
	
	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();                               //create a vbo..........
		vbos.add(vboID);                                               // add to vbo list...............
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);          // bind a vbo by passing type as indices buffer and vboid...........
		IntBuffer buffer = storeDataInIntBuffer(indices);                    // call store method to store indices in a integer buffer.........
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);      // store our created int buffer in the vbo and 
	}
	
	
	
	
	
	
	
	
	// This stores the indices in a int buffer and return it........................................
	
	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);          // creates an empty int buffer...............
		buffer.put(data);                                                         // put data in the empty int buffer....................
		buffer.flip();                                                            // informs that edit is over.................
		return buffer;                                                             // return buffer.....................
	}
	
	
	
	
	
	
	
	
	//Method to convert float array data into a float buffer........
	private FloatBuffer storeDataInFloatBuffer(float[] data){      // takes in the array of data
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);    //create a float buffer object and pass size of array...............
		buffer.put(data);                                                  // store the data in the float buffer.......
		buffer.flip();                                                    // tell buffer that we have finished storing our data...........
		return buffer;                                                    /// return our float data buffer.............
	}
	
	
	
	
	
	
	
	//This methods loads our cube map..........................................................
	public int loadCubeMap(String[] textureFile) {
		
	    int texID = GL11.glGenTextures();                        //Generate empty texture...................
	    GL13.glActiveTexture(GL13.GL_TEXTURE0);                             //Activate the texture unit 0..................
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);                //Bind the texture and specify the type..........................
		
		for(int i=0; i<textureFile.length ; i++) {                                                  //loop through all images in the cube...........
			
			TextureData data = decodeTextureFile("res/" + textureFile[i] + ".png");                 // Load all the images to decode method..............
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
			//This function of opengl loads the our cube map.............
			//Parameters include which face of cube to load, start, type, width, height, start, type, byte type, data.......................
		}
	    
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);             //This method just makes our texture smooth...........
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);             //This method just makes our texture smooth...........
	    textures.add(texID);                                    //Add texid to textures so that it gets deleted during cleanup.............................
	    return texID;
	
	}
	
	
	
	
	
	
	
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}

}
