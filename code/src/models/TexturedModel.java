package models;

import textures.ModelTexture;




//This class holds data for both a model and the texture of the model..........................................

public class TexturedModel {
	
	private RawModel rawModel;                                             //Create a raw model .......................
	private ModelTexture texture;                                           // create a texture..........................
    private boolean ifCalling = false;                                                 
	
	
	//Constructor to add a model and a texture and assign it  to the above variables.....................................
	public TexturedModel(RawModel model, ModelTexture texture){                        
		this.rawModel = model;
		this.texture = texture;
		this.ifCalling = false;
	}

	//Constructor to add a model and a texture and assign it  to the above variables.....................................
		public TexturedModel(RawModel model, ModelTexture texture, boolean ifCalling){                        
			this.rawModel = model;
			this.texture = texture;
			this.ifCalling = ifCalling;
		}

		
		
	
	public boolean isIfCalling() {
			return ifCalling;
		}

	/// Getter to get a rawmodel if needed......................................................
	public RawModel getRawModel() {
		return rawModel;
	}
	
	
	
    //Geeter to get Texture if needed.....................................................................
	public ModelTexture getTexture() {
		return texture;
	}

}
