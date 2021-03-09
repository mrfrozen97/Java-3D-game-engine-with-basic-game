package shaders;

import java.io.BufferedReader;


import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;




/*
 It represent a generic shader program containing all the attributes and methods of shaders.
 It creates shader program and attaches vertex and fragment shader to it........ 
 */

public abstract class ShaderProgram {
	
	private int programID;                    // stores the Program id........
	private int vertexShaderID;              // stores the vertex id...............
	private int fragmentShaderID;             // stores the fragment id...................
	
	
	
	//Create a new matrix buffer to store in the location......................................
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	
	
	//Constructor of the class which executes all required methods in it.................................................
	
	public ShaderProgram(String vertexFile,String fragmentFile){
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);             //create vertex shader in opengl and get its id.................
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);      //create fragment shader in opengl and get its id................
		programID = GL20.glCreateProgram();                                    // creates a new program and returns program id........
		GL20.glAttachShader(programID, vertexShaderID);                        // adds vertex shader to program........
		GL20.glAttachShader(programID, fragmentShaderID);                      // adds fragment shader to program........
		bindAttributes();                                                          // call bind attributes         
		GL20.glLinkProgram(programID);                                          // links both shaders.......
		GL20.glValidateProgram(programID);                                     // validate both the shaders...........
		getAllUniformLocations();                                                       
	}
	
	protected abstract void getAllUniformLocations();                          // it makes sure all classes in shader program have this method............
	
	
	
	
	
	//This method takes in the location of uniform variable by taking the name of the variable.........................
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID,uniformName);      // This takes in the program id and the name of the uniform and return the location id.....       
	}
	
	
	
	
	
	//When we want to use program we have to start it................................
	public void start(){
		GL20.glUseProgram(programID);                 //starts the program.................
	}
	
	
	// stops the program...................................................................
	public void stop(){
		GL20.glUseProgram(0);                          // stops it..........................
	}
	
	
	
	//Detach and delete shaders after use........................................
	
	public void cleanUp(){
		stop();                                                   // calls stop..................
		GL20.glDetachShader(programID, vertexShaderID);              //detach vertex shader...........
		GL20.glDetachShader(programID, fragmentShaderID);            // detach fragment shader..........
		GL20.glDeleteShader(vertexShaderID);                          // delete vertex shader............
		GL20.glDeleteShader(fragmentShaderID);                       // delete fragment shader...........
		GL20.glDeleteProgram(programID);                             //delete program........
	}
	
	
	// binds one of the attributes list from vao...............
	protected abstract void bindAttributes();
	
	
	
	//Bind the attributes . IT takes attribute to bind and variable name and program id................
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);         // opengl function to bind attribute............
	}
	
	
	
	
	
	//This method loads the float value in the uniform variable location................................
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);                          //loads value in that location.....................
	}
	
	
	
	
	//This method loads the int value in the uniform variable location................................
	protected void loadInt(int location, int value) {
		
		GL20.glUniform1i(location, value);                               //loads value in that location...................
		
	}
	
	
	
	//This method loads the 3d vector value in the uniform variable location................................
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location,vector.x,vector.y,vector.z);              //loads values in that location...................
	}
	
	
	
	protected void loadVector(int location, Vector4f vector){
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);;              //loads values in that location...................
	}
	
	
	
	//This method loads the 2d vector value in the uniform variable location................................
	protected void load2DVector(int location, Vector2f vector){
		GL20.glUniform2f(location,vector.x,vector.y);                         //loads values in that location...................
	}
	
	
	
	
	
	//This method loads the boolean value in the uniform variable location................................
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);                                      //loads value in that location...................
	}
	
	
	
	
	
	
	//This method loads the float value in the uniform variable location................................
	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);                                         // add our value to the matrix buffer.......................
		matrixBuffer.flip();                                               // tell it we are done with editing.......................
		GL20.glUniformMatrix4(location, false, matrixBuffer);             //loads value in that location...................
		//It take in the location, if we want to transpose, a matrix buffer.....................
	}
	
	
	
	
	
	/*
	 This method connects the txt file in a long string . And compiles it and returns id of new shader................... 
	 */
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();                                // Creates the empty string to store later.........
		try{                                                                             // check the shader file and catch if file is present.....
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);                                    // create a new shader and get the shaderid for it............
		GL20.glShaderSource(shaderID, shaderSource);                               // Combines the shader code and prepares it for compiling......
		GL20.glCompileShader(shaderID);                                                  //Compile the shader programs.......
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){                  /// Print compiler error if any...........
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;                                                            // return the shader id......................
	}

}
