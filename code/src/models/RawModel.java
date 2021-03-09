package models;

// Classs to handel the VAO , VBO

public class RawModel {
	
	private int vaoID;     // stores the id of the vao.........
	private int vertexCount;  // it holds the count of the vertex present in the
	
	
	// Constructor of the class to take in id and vertex count.....................................................
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	
	
	///// Getters to get the information if needed......................................................

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	

}
