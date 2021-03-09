package collisiondetection;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Player;

public class Collision {
	
	
	
	public List<Entity> checkProximity(List<Entity> entities, Player player){
		List<Entity> newEntities =  new ArrayList<Entity>();
		float x = player.getPosition().x;
		float z = player.getPosition().z;
		for(Entity entity: entities) {
			
			float x1 = entity.getPosition().x;
			float z1 = entity.getPosition().z;
			if(x1 > x-50 && x1< x+50) {
				if(z1 > z-50 && z1 < z+50) {
					newEntities.add(entity);
				}
			}
		}
		
	
		
		return newEntities;
	}
	
	
	
	
	
	
	

	public boolean collision_detection(List<Entity> entities, Player player) {
		float x = player.getPosition().x;
		float z = player.getPosition().z;
		float y = player.getPosition().y;
		float roty = player.getRotY();
		float difx;
		float difz;
		
		//System.out.println(roty);
		
		if(roty > 180 && roty<270){
			difx = 0.05f;
			difz = -0.05f;
		}
		else if (roty > 90 && roty<180) {
			
			difx = -0.05f;
			difz = 0.05f;
		}
		else if(roty>270 && roty<360) {
			
			difx = 0.05f;
			difz = -0.05f;
			
			
		}
		else {
			difx = -0.05f;
			difz = -0.05f;
		}
		
		if(entities!= null) {
		for(Entity entity: entities) {
			
			float x1 = entity.getPosition().x;
			float z1 = entity.getPosition().z;
			
			if(x1-4 < x  && x < x1+4 && (y<20)) {
				if(z1+3 > z && z> z1-6) {
				player.setPosition(new Vector3f(x+difx ,y, z+difz));
				return true;
			}
				}
			
		}}
		return false;
	}
	
	
	
	
	
	
	

	public boolean collision_detection(List<Entity> entities, Player player, boolean override) {
		float x = player.getPosition().x;
		float z = player.getPosition().z;
		float y = player.getPosition().y;
		float roty = player.getRotY();
		float difx;
		float difz;
		
		//System.out.println(roty);
		
		if(roty > 180 && roty<270){
			difx = 0.05f;
			difz = -0.05f;
		}
		else if (roty > 90 && roty<180) {
			
			difx = -0.05f;
			difz = 0.05f;
		}
		else if(roty>270 && roty<360) {
			
			difx = 0.05f;
			difz = -0.05f;
			
			
		}
		else {
			difx = -0.05f;
			difz = -0.05f;
		}
		
		if(entities!= null) {
		for(Entity entity: entities) {
			
			float x1 = entity.getPosition().x;
			float z1 = entity.getPosition().z;
			
			if(x1-(1.3*entity.getScale()) < x  && x < x1+(1.3*entity.getScale()) && (y<15*entity.getScale())) {
				if(z1+(1.3*entity.getScale()) > z && z> z1-(1.3*entity.getScale())) {
				player.setPosition(new Vector3f(x+difx ,y, z+difz));
				return true;
			}
				}
			
		}}
		return false;
	}
	
	
	
	
	
}
