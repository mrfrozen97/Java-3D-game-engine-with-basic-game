package textures;



//This class just keeps the record of all the textures .It takes in texture id.............................

public class TerrainTextures {
	
 	private int textureID;                           // Stores the texture id..............................

 	
 	//Constructor that takes in and assigns the id...............................
	public TerrainTextures(int terrainID) {
		
		this.textureID = terrainID;
		
	}

    // getter for texture id........................................
	public int getTextureID() {
		return textureID;
	}

	
}
