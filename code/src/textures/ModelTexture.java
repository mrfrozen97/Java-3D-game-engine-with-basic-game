package textures;



/*
 This class manages the textures. It holds the texture id and other parameters for the texture id............... 
  
 */

public class ModelTexture {
	
	private int textureID;                               // It holds the texture id of the texture......................
	
	private float shineDamper = 1;                         //stores the dampning of the light......................
	private float reflectivity = 0;                        // stores reflectivity of the surface..........................
	private boolean hasTransparency = false;                //stores if model has transparency........................
	private boolean useFakeLighting = false;                //stores if model is using fake lighting........................
	
	private int numberOfRows = 1;                            //store number of rows................
	
	
	
	
	public int getNumberOfRows() {                                //get number of rows methos....................
		return numberOfRows;
	}

	
	
	// set number of rows in the texture........................................
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	
	
	
	//Constructor to take in and assign texture id.....................................................
	public ModelTexture(int texture){
		this.textureID = texture;
	}
	
	
	
	
	//get method of texture id......................................
	public int getID(){
		return textureID;                                                  // get texture id.....................
	}

	public float getShineDamper() {
		return shineDamper;                                              // get shine damper.....................
	} 

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;                                   // set sine damper.................
	}

	public float getReflectivity() {
		return reflectivity;                                             // get reflectivity.....................
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;                                 // set reflectivity.....................
	}

	public boolean isHasTransparency() {
		return hasTransparency;                                           // get has transparency..........................
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;                             // set has transparency..............................
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;                                                     // get weather to use fake lighting......................
	}

	public void setUseFakeLighting(boolean useFakeLighting) {                             
		this.useFakeLighting = useFakeLighting;                                          // set weather to use fake lighting.....................
	}
	
	
	

}
