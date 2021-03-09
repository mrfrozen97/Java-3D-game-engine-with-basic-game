package audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;




public class AudioMaster {

	
	private static List<Integer> buffers = new ArrayList<Integer>();
	
	
	public static void init() {
		try {
			
			
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);	
		}
	
	
	
	
	public static int loadSound(File file) throws FileNotFoundException {
		
		
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
		
		WaveData waveFile = WaveData.create(is);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
		
	}
	
	
	public static void cleanUp() {
		for(int buffer: buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
		
	}
}
