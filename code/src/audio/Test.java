package audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public class Test {

	
	public static void main(String[] args) throws FileNotFoundException  {
		
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
		
		int buffer = AudioMaster.loadSound(new File("src/audio/abc.wav"));
		
		Source source = new Source();
		
		source.setLooping(true);
		source.play(buffer);
		char c = ' ';
		/*while(c != 'q') {
		source.play(buffer);
		try {
			c = (char)System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		}*/
	
		float xPos = 0;
		source.setPosition(xPos, 0, 0);
		
		while(xPos> -105) {
			xPos -= 0.04f;
			source.setPosition(xPos, 0, 0);
			System.out.println(xPos);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
		System.out.println("over");
		source.delete();
	    AudioMaster.cleanUp();
	  
	}

}
