package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;


//This class do all the important maths calculations in our game................

public class Maths {
	
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	
	
	
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	
    
	//This method takes in the 3d position, rotations and scale and returns a 4*4 transformation matrix.................
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();                                                             // create a new empty matrix..........
		matrix.setIdentity();                                                                        // set the matrix to identity matrix........
		Matrix4f.translate(translation, matrix, matrix);                                                 // translates matrix with our positions......
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);            // rotate matrix around x axis.............
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);             // rotate matrix around y axis.............
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);              // rotate matrix around z axis.............
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);                              // Scale our matrix in all directions......
		return matrix;                                                                                    // return transformation matrix.........
	}
	
	
	
	//This method takes in the camera object and creates a view matrix accordingly and returns the view matrix.....................................
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();                                                              // create a new empty matrix..........
		viewMatrix.setIdentity();                                                                            // set the matrix to identity matrix.......
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,           // rotate the camera according to pitch..........
				viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);      // rotate according to the yaw............
		Vector3f cameraPos = camera.getPosition();                                                                    // getting the camera position.........
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);                            // set camera positions opposite of the change...
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);                                           // load the view matrix with the values........
		return viewMatrix;                                                                          /// returns the view matrix...........................
	}

}
