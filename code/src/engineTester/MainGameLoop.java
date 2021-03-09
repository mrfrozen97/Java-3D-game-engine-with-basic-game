package engineTester;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import models.RawModel;
import models.TexturedModel;
import particles.Particle;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import audio.AudioMaster;
import audio.Source;
import collisiondetection.Collision;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexturePack;
import textures.TerrainTextures;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {
	
	
	
	public float max(float a, float b) {
		if(a>b) {
			return a;
		}
		else {
			return b;
		}
	}
	
	public static Vector2f getMapPosition(Player player,Terrain terrain) {
		
		float posx = 0.6985f + ((0.2765f * player.getPosition().x )/ terrain.getSize()); 
		float posy = 0.483f + ((0.472f * -1 * player.getPosition().z)/ terrain.getSize());
		
		return new Vector2f(posx, posy);
	}
	
	

	public static void main(String[] args) {
		
		
	   int border_Adjustment = 100;
		
	   boolean quitGame = false;
	   while(!quitGame) {
		
         
		DisplayManager.createDisplay();      // creates the display from method in display manager class............
		Loader loader = new Loader();       // create a object of loader class which loads the data in a vao...............
		MainGameLoop game = new MainGameLoop();
		
		
		
		
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
        int menuMusic = 0;
        int gameMusic1 = 0; 
		
        try {
			 menuMusic = AudioMaster.loadSound(new File("src/audio/mainMenuMusic.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
        try {
        	gameMusic1 = AudioMaster.loadSound(new File("src/audio/gameMusic1.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Source source = new Source();
		source.setLooping(true);
		
		
		
		//Text stuff on screen.............................
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTexture("fonts/harrington"), new File("res/fonts/harrington.fnt"));
		GUIText text = new GUIText("Souls Borne", 2, font, new Vector2f(0,0), 1, true);
		text.setColour(1, 1, 0);
		GUIText text3 = new GUIText("", 3.5f, font, new Vector2f(0,0.2f), 1, true);
		text3.setColour(0, 0.2f, 1);
		GUIText text4 = new GUIText("", 2, font, new Vector2f(0,0.45f), 1, true);
		text4.setColour(1, 0.80f, 0);
		GUIText text5 = new GUIText("", 2, font, new Vector2f(0,0.55f), 1, true);
		text5.setColour(1, 0.80f, 0);
		FontType font1 = new FontType(loader.loadTexture("fonts/sans"), new File("res/fonts/sans.fnt"));
		GUIText text1 = new GUIText("100", 2, font1, new Vector2f(-0.425f,0.935f), 1, true);
		text1.setColour(1, 0, 0);
		
		
		//This part creates 4 objects of the Terrain texture class that stores and return the id for textures.......... 
		// We pass loader.loadTexture which loads the texture in the opengl.....................
		TerrainTextures backgroundTexture = new TerrainTextures(loader.loadTexture("TerrainTextures/grass2"));                 // For grass texture........................
		TerrainTextures rTexture = new TerrainTextures(loader.loadTexture("TerrainTextures/dryGrass"));                          // For dirt texture.....................
		TerrainTextures gTexture = new TerrainTextures(loader.loadTexture("TerrainTextures/sand"));                            // For mud texture.......................
		TerrainTextures bTexture = new TerrainTextures(loader.loadTexture("TerrainTextures/path1"));                         // For path/road texture............... 
		
		
		//Object of the Texture pack to assign the 4 textures we have to the terrain of our game..........................................
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		//Now we load our blender map in the opengl...................................................
		TerrainTextures blendMap = new TerrainTextures(loader.loadTexture("map/blendMap3"));
		
		
		
		//Create a object of rawmodel to load our tree  model in opengl and returns the id of the model..........
		RawModel model = OBJLoader.loadObjModel("trees/pine", loader);
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("trees/pine")));
		
       //Stores the credentials in the teture model class.......
		
		
		
		
		TerrainTextures oceanT1 = new TerrainTextures(loader.loadTexture("TerrainTextures/ocean2"));
		TerrainTextures oceanT2 = new TerrainTextures(loader.loadTexture("TerrainTextures/ocean1"));
		TerrainTextures oceanT3 = new TerrainTextures(loader.loadTexture("TerrainTextures/sand"));
		TerrainTextures oceanMap = new TerrainTextures(loader.loadTexture("map/black"));
		TerrainTextures oceanMap1 = new TerrainTextures(loader.loadTexture("map/black_inverted"));
		TerrainTexturePack oceanPack = new TerrainTexturePack(oceanT1, oceanT2, oceanT3, oceanT1);
		
		
		
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		ModelTexture fernt = new ModelTexture(loader.loadTexture("fern1"));
		fernt.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),fernt);
		fern.getTexture().setHasTransparency(true);
		TexturedModel lowPolyTree = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),new ModelTexture(loader.loadTexture("flower")));
		lowPolyTree.getTexture().setHasTransparency(true);
		TexturedModel PolyTree = new TexturedModel(OBJLoader.loadObjModel("cherry", loader),new ModelTexture(loader.loadTexture("cherry")));
		PolyTree.getTexture().setHasTransparency(true);
	
		
		
		
		
		
		
		
		
		
		
		Terrain ocean = new Terrain(-1,-1,loader,oceanPack,oceanMap, "map/black", true);
		Terrain ocean1 = new Terrain(-1,0,loader,oceanPack,oceanMap, "map/black", true);
		Terrain ocean2 = new Terrain(0,0,loader,oceanPack,oceanMap1, "map/black_inverted", true);
		
		
		Terrain terrain = new Terrain(0,-1,loader,texturePack,blendMap, "map/heightmap", false);
		Terrain terrain2 = new Terrain(-1,-1,loader,texturePack, blendMap, "map/heightMap", false);
		
		
		
		List<Entity> entities = new ArrayList<Entity>();               // This creates the list of entities(objects) in our game.........................
		List<Entity> collisionEntities = new ArrayList<Entity>();               // This creates the list of entities(objects) in our game.........................
		List<Entity> collisionEntities1 = new ArrayList<Entity>();
		
		
		Random random = new Random();  //Creating the object of the random class to give us random terrain objects in our terrain........................
		
		
		BufferedImage blendImage = null;
		String blendMapImage = "map/blendMap3edit";
		try {
			blendImage = ImageIO.read(new File("res/" + blendMapImage  + ".png"));                         //load height map.........................
		} catch (IOException e) {
			e.printStackTrace();
		}
		int blendImageHeight = blendImage.getHeight();
		
		System.out.println(blendImageHeight/ terrain.getSize());
		//float divisionFactor = 
		
		//terrain entities like fern, plants , grass, shrubs and cherry blossom
		int adjustterrain = 60;
		//
		
		entities.add(new Entity(fern, new Vector3f(0,0,0),0,0,0,0.4f, random.nextInt(4)));
		
		
		
		
		
		
		for(int i=0;i<900;i++){
			int x = (int) (random.nextFloat()*(terrain.getSize() - border_Adjustment - adjustterrain) + border_Adjustment);// - 400;
			int z = (int) (random.nextFloat() * -(terrain.getSize() - border_Adjustment - adjustterrain) - border_Adjustment);
			float y = terrain.getHeightOfTerrain(x, z);
			
			int xb = (int) (x * (blendImageHeight/ terrain.getSize()));
			int zb =  blendImageHeight - (-1 * ((int) ( (z )* (blendImageHeight / terrain.getSize()))));
			
			System.out.print(xb  + " " +  zb + " "+ x + " "+ -1*z+ ": ");
			Color blendPixelColor = new Color(blendImage.getRGB(xb, zb));
			System.out.println(blendPixelColor.getBlue() + " ");
			
			if(blendPixelColor.getBlue() < 1) {
			float scaleTree =  game.max(random.nextFloat(), 0.3f);
			Entity tree1 = new Entity(staticModel, new Vector3f(x,y,z),0,0,0,2 *scaleTree);
			entities.add(tree1);
			collisionEntities1.add(tree1);
			}
		//	if(scaleTree >= 1) {
				
			//}
			
			
		    x = (int) (random.nextFloat()*(terrain.getSize()  - border_Adjustment- adjustterrain) + border_Adjustment);
			z = (int) (random.nextFloat() * -(terrain.getSize() - border_Adjustment- adjustterrain) - border_Adjustment);
			y = terrain.getHeightOfTerrain(x, z);
			 xb = (int) (x * (blendImageHeight/ terrain.getSize()));
			 zb = blendImageHeight -( -1 * ((int) ( (z )* (blendImageHeight / terrain.getSize()))));
			 Color blendPixelColor5 = new Color(blendImage.getRGB(xb, zb));
			 if(blendPixelColor5.getBlue() <1) {
			entities.add(new Entity(grass, new Vector3f(x,y,z),0,0,0,0.5f));
			 }
			
			
			 x = (int) (random.nextFloat()*(terrain.getSize() - border_Adjustment- adjustterrain) + border_Adjustment) ;
			 z = (int) (random.nextFloat() * -(terrain.getSize() - border_Adjustment- adjustterrain) - border_Adjustment);
			 y = terrain.getHeightOfTerrain(x, z);
			
			 xb = (int) (x * (blendImageHeight/ terrain.getSize()));
			 zb = blendImageHeight -( -1 * ((int) ( (z )* (blendImageHeight / terrain.getSize()))));
			 Color blendPixelColor1 = new Color(blendImage.getRGB(xb, zb));
			 
			 if(blendPixelColor1.getBlue()<1) {
			 entities.add(new Entity(fern, new Vector3f(x,y,z),0,0,0,0.8f, random.nextInt(4)));	
			 }
			
			
			x = (int) (random.nextFloat()*(terrain.getSize() - border_Adjustment- adjustterrain) + border_Adjustment);
			 z = (int) (random.nextFloat() * -(terrain.getSize() - border_Adjustment- adjustterrain) - border_Adjustment);
			 y = terrain.getHeightOfTerrain(x, z);
			 xb = (int) (x * (blendImageHeight/ terrain.getSize()));
			 zb = blendImageHeight -( -1 * ((int) ( (z )* (blendImageHeight / terrain.getSize()))));
			 Color blendPixelColor2 = new Color(blendImage.getRGB(xb, zb));
			 if(blendPixelColor2.getBlue()<1) {
			entities.add(new Entity(lowPolyTree, new Vector3f(x,y,z),0,0,0,1));
			 }
			
			if(i%11 == 0) {
			x = (int) (random.nextFloat()*(terrain.getSize() - border_Adjustment - adjustterrain) + border_Adjustment);
			z = (int) (random.nextFloat() * -(terrain.getSize() - border_Adjustment - adjustterrain) - border_Adjustment);
			y = terrain.getHeightOfTerrain(x, z);
			
			xb = (int) (x * (blendImageHeight/ terrain.getSize()));
			 zb = blendImageHeight -( -1 * ((int) ( (z )* (blendImageHeight / terrain.getSize()))));
			 Color blendPixelColor3 = new Color(blendImage.getRGB(xb, zb));
			 
			 if(blendPixelColor3.getBlue()<1) {
		    Entity lpt = new Entity(PolyTree, new Vector3f(x,y,z),0,0,0,3.6f);
			entities.add(lpt);
			collisionEntities.add(lpt);
		
			 }
			}
		}
		
		
		
		
		
		TexturedModel house = new TexturedModel(OBJLoader.loadObjModel("WoodenCabinObj", loader),new ModelTexture(loader.loadTexture("WoodCabinDif")));
		
		
		
		ModelTexture lampt = new ModelTexture(loader.loadTexture("lamp"));
		lampt.setUseFakeLighting(true);
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),lampt);
		//Light light = new Light(new Vector3f(0,10000,-7000),new Vector3f(1,1,1));
		List<Light> lights = new ArrayList<Light>();
	//	lights.add(light);
		Light sun = new Light(new Vector3f(0,800000,-700000), new Vector3f(1.2f,1.2f,1.2f));
		lights.add(sun);
		lights.add(new Light(new Vector3f(185,10,-293), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.0002f)));
		lights.add(new Light(new Vector3f(370,17,-300), new Vector3f(0,0,2), new Vector3f(1, 0.01f, 0.0002f)));
		lights.add(new Light(new Vector3f(293,7,-305), new Vector3f(0,2,0), new Vector3f(1, 0.01f, 0.0002f)));
		
		entities.add(new Entity(lamp, new Vector3f(180, terrain.getHeightOfTerrain(180, -293), -293), 0,0,0,1));
		entities.add(new Entity(lamp, new Vector3f(370, terrain.getHeightOfTerrain(370, -293), -293), 0,0,0,1));
		entities.add(new Entity(lamp, new Vector3f(293, terrain.getHeightOfTerrain(293, -293), -300), 0,0,0,1));
		entities.add(new Entity(house, new Vector3f(30, -2, -30), 0,180,0,0.4f));
		
		float h;
		for(int i=0; i<blendImageHeight; i++) {
			for(int j=0; j<blendImageHeight; j++) {
				 Color blendPixelColorlamp = new Color(blendImage.getRGB(i, j));
				 
				
				 if(blendPixelColorlamp.getGreen() > 150 && blendPixelColorlamp.getRed() > 150 && blendPixelColorlamp.getBlue() > 150) {
		                                
					 System.out.println("white" + i/2 + " "+ (j - blendImageHeight)/2);
					 
					 h = terrain.getHeightOfTerrain(i/2, (j - blendImageHeight)/2);
					 float scaleTree1 =  game.max(random.nextFloat(), 0.3f);
						Entity tree12 = new Entity(staticModel, new Vector3f(i/2,h, (j - blendImageHeight)/2),0,0,0,2 *scaleTree1);
						entities.add(tree12);
						collisionEntities1.add(tree12);
				 }
				 
				 else if(blendPixelColorlamp.getGreen() > 100) {
						System.out.println(i/2 + " "+ (j - blendImageHeight)/2);
						 h = terrain.getHeightOfTerrain(i/2, (j - blendImageHeight)/2);
						entities.add(new Entity(lamp, new Vector3f(i/2, h, (j - blendImageHeight)/2), 0,0,0,1));
						
						}
			}
			
		}
		
		
		
		
		
		
		//coin
		ModelTexture coin1m = new ModelTexture(loader.loadTexture("cube"));
		coin1m.setShineDamper(1.5f);
		coin1m.setReflectivity(0.6f);
		coin1m.setUseFakeLighting(true);
		TexturedModel coin1 = new TexturedModel(OBJLoader.loadObjModel("models/coin1", loader), coin1m);
		coin1.getTexture().setHasTransparency(true);
		
		Entity coin1e  = new Entity(coin1, new Vector3f(60, 3, -60), 0,0,0,20);	
		
		//entities.add(coin1e);
		
		
		
		
		

		//fence
		ModelTexture fenceT = new ModelTexture(loader.loadTexture("models/fence"));
		fenceT.isUseFakeLighting();
		TexturedModel fence = new TexturedModel(OBJLoader.loadObjModel("models/fence1", loader),fenceT);
		fence.getTexture().setHasTransparency(true);
	
		for(int i=1; i<102; i++) {
	//	entities.add(new Entity(fence, new Vector3f(-100, -3, -21 * i + 100), 0,-90,0,0.05f));
		}
		for(int i=0; i<100; i++) {
//		entities.add(new Entity(fence, new Vector3f(21 * i - 100, -3, 100), 0,0,0,0.05f));
		}
		for(int i=0; i<6; i++) {
			entities.add(new Entity(fence, new Vector3f(1991, -3, 100 - 21*i), 0,90,0,0.05f));
				}
		for(int i=1; i<7; i++) {
			entities.add(new Entity(fence, new Vector3f(-100 + 21*i, -3, -1991), 0,180,0,0.05f));
				}
		
		
		
		
		
		
		TexturedModel boat = new TexturedModel(OBJLoader.loadObjModel("models/boat", loader),new ModelTexture(loader.loadTexture("models/wood")));
		Entity boate = new Entity(boat, new Vector3f(-50, 0, 0),0 ,90,0,800);
		entities.add(boate);
		Player boatp = new Player(boat, new Vector3f(50, 0, -50),0 ,0,0,500f);
		
        RawModel bunny = OBJLoader.loadObjModel("models/goblins/goblin2", loader);
        
		TexturedModel stanbunny = new TexturedModel(bunny, new ModelTexture(loader.loadTexture("models/goblins/goblinTexture")));
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		 Just add each of the model in below models...... 
		 And also add it to goblins list ........................
		*/
		
		
		List<TexturedModel> goblins = new ArrayList<TexturedModel>();
		
		String player_texture =  "models/trex/trex";
		/*
		TexturedModel goblin1 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000001", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin2 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000002", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin3 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000003", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin4 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000004", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin5 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000005", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin6 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000006", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin7 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000007", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin8 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000008", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin9 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000009", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin10 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000010", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin11 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000011", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin12 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000012", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin13 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000013", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin14 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000014", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin15 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000015", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin16 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000016", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin17 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000017", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin18 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000018", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin19 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000019", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin20 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000020", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin21 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000021", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin22 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000022", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin23 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000023", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin24 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000024", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin25 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000025", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin26 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000026", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin27 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000027", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin28 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000028", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin29 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000029", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin30 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000030", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin31 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000031", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin32 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000032", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin33 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000033", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin34 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000034", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin35 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000035", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin36 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000036", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin37 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000037", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin38 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000038", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin39 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000039", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin40 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000040", loader), new ModelTexture(loader.loadTexture(player_texture)));
		TexturedModel goblin41 = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000041", loader), new ModelTexture(loader.loadTexture(player_texture)));
		*/
		TexturedModel trex_standing = new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_standing", loader), new ModelTexture(loader.loadTexture(player_texture))); 
		
		/*
		goblins.add(goblin1);
		goblins.add(goblin2);
		goblins.add(goblin3);
		goblins.add(goblin4);
		goblins.add(goblin5);
		goblins.add(goblin6);
		goblins.add(goblin7);
		goblins.add(goblin8);
		goblins.add(goblin9);
		goblins.add(goblin10);
		goblins.add(goblin11);
		goblins.add(goblin12);
		goblins.add(goblin13);
		goblins.add(goblin14);
		goblins.add(goblin15);
		goblins.add(goblin16);
		goblins.add(goblin17);
		goblins.add(goblin18);
	    goblins.add(goblin19);
	 	goblins.add(goblin20);
		goblins.add(goblin21);
		goblins.add(goblin22);
		goblins.add(goblin23);
		goblins.add(goblin24);
		goblins.add(goblin25);
		goblins.add(goblin26);
		goblins.add(goblin27);
		goblins.add(goblin28);
		goblins.add(goblin29);
		goblins.add(goblin30);
		goblins.add(goblin31);
		goblins.add(goblin32);
		goblins.add(goblin33);
		goblins.add(goblin34);
		goblins.add(goblin35);
		goblins.add(goblin36);
		goblins.add(goblin37);
		goblins.add(goblin38);
		goblins.add(goblin39);
		goblins.add(goblin40);
		goblins.add(goblin41);
		*/
		//65
		for(int i = 1; i<65; i++) {
		//	System.out.println(i);
			if(i<10) {
				goblins.add(new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_00000" + i, loader), new ModelTexture(loader.loadTexture(player_texture))));
			}
			else if(i<100){
				goblins.add(new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_0000" + i, loader), new ModelTexture(loader.loadTexture(player_texture))));
			}
			else {
				goblins.add(new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_000" + i, loader), new ModelTexture(loader.loadTexture(player_texture))));
			}
			
		}
		/*for(int i = 49; i>0; i--) {
			if(i>=10) {
				goblins.add(new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_0000" + i, loader), new ModelTexture(loader.loadTexture(player_texture))));
			}
			else {
				goblins.add(new TexturedModel(OBJLoader.loadObjModel("models/trex/trex_00000" + i, loader), new ModelTexture(loader.loadTexture(player_texture))));
			}
			
		}*/
		
		//goblins.add(goblin28);
		//goblins.add(goblin28);
		//goblins.add(goblin28);

		
		
		
		
		
		
		//Player player = new Player(stanbunny, new Vector3f(50, 0, -50),0 ,100,0,3.0f);
		Player player = new Player(trex_standing, goblins, new Vector3f(50, 0, -50),0 ,180,0,1.5f);
		//player.setModel(goblin2);
		
		Camera camera = new Camera(player);
		
		
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		
		ParticleMaster.init(loader,renderer.getProjectionMatrix());
		
      
		
		//TexturedModel stanbunny = new TexturedModel(bunny, new ModelTexture(loader.loadTexture("image")));
		//Player player = new Player(stanbunny, new Vector3f(50, 0, -50),0 ,100,0,1.5f);
		
		
		
		//Create the new object of camera object and pass our player in the camera...............
		//Camera camera = new Camera(player);
		
		
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("map/game_map2"), new Vector2f(0.86f, 0.63f), new Vector2f(0.175f, 0.30f));
		
		GuiTexture gui1 = new GuiTexture(loader.loadTexture("map/pointer"), new Vector2f(0.71f, 0.51f), new Vector2f(0.02f, 0.04f));
		GuiTexture gui2 = new GuiTexture(loader.loadTexture("map/life"), new Vector2f(-0.94f, -0.92f), new Vector2f(0.05f, 0.08f));
		GuiTexture gui4 = new GuiTexture(loader.loadTexture("map/sword"), new Vector2f(-0.94f, -0.75f), new Vector2f(0.05f, 0.08f));
		//gui1.setPosition(new Vector2f(0.69855f, 0.483f));      //0.6985
		//gui1.setPosition(new Vector2f(0.975f, 0.483f));                              //0.975,0.955
		
		List<GuiTexture> guisMenu = new ArrayList<GuiTexture>();
		GuiTexture gui3 = new GuiTexture(loader.loadTexture("map/background6"), new Vector2f(-0.00f, -0.60f), new Vector2f(0.9f, 1.6f));
		
		guisMenu.add(gui3);
		
		guis.add(gui4);
		guis.add(gui2);
		guis.add(gui1);
		guis.add(gui);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		
		
		
		
		
		
	    		
		
		
		
		
		Collision col = new Collision();
		
		
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particles/cosmic"), 4);
		ParticleSystem system = new ParticleSystem(particleTexture ,150, 50, 0.15f, 4,2 );
		
		List<Entity> newEntities = col.checkProximity(collisionEntities, player);
		List<Entity> newEntities1 = col.checkProximity(collisionEntities1, player);
		int chance= 0, i1 = 0;
		
		
	//	camera.setFirstPersoncameraON();
		/// Main game loop which is running during the game....................................
		byte flag999 = 0;
		int life = 100;
		
		int mouseX;
		int mouseY;
		
		
		
		
		
		
		
		
		//////Main game loop
		//boolean quitGame = false;
		
	//	while(!quitGame) {
		
			
			
			//DisplayManager.createDisplay();
			
		/////Reset variables.....................................................................................................................	
			
	    life = 100;
	    player.setPosition(new Vector3f(50, 0, -50));
	   // DisplayManager.updateDisplay();
		quitGame = false;
		boolean startFlag = false;	
		boolean gameOver = false;
		source.play(menuMusic);
		text3.setString("");
		text4.setString("");
		text5.setString("");
		//GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		//Display.update();
		//DisplayManager.updateDisplay();
		
		
	   while(!startFlag && !quitGame) {
		   
		//   GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			//Display.update();
		   guiRenderer.render(guisMenu);
		   
		   DisplayManager.updateDisplay();                     /// Calls the method to update display in display manager class................
		   
		// System.out.println(Mouse.getX() + " " + Mouse.getY());
		 
		  mouseX = Mouse.getX();
		  mouseY = Mouse.getY();
		 
		      if(((mouseX>394 && mouseX<484)&&(mouseY<733 && mouseY>700)) && Mouse.isButtonDown(0)) {
			                          startFlag = true;
		       }
		
		      if(((mouseX>400 && mouseX<465)&&(mouseY<593 && mouseY>555)) && Mouse.isButtonDown(0)) {
			                          quitGame = true;
		       }
		      if(Display.isCloseRequested()) {
		                         	 quitGame = true;
		      }
		   
	   }
	   
	   
	   
	   
	   
		source.stop();
		
		source.play(gameMusic1);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		if(startFlag) {
		
		while(!quitGame && !gameOver){
			
			Vector2f mapPointer = getMapPosition(player, terrain);
			//System.out.println(mapPointer);
			gui1.setPosition(mapPointer);
			//GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			
			if(life<1) {
				gameOver = true;
			}
			
			if(Display.isCloseRequested()) {
            	 quitGame = true;
             }
			
			camera.move(player);                                             // call move method to move our view according to the keyboard inputs.........
		//	boatp.move(terrain);
		
			if(!col.collision_detection(newEntities, player) && !col.collision_detection(newEntities1, player, true)) {
			player.move(terrain);
			flag999 = 1;
			}
			else if(flag999 == 1) {
				life--;
				text1.setString(String.valueOf(life));
				flag999 = 0;
			}
			if(chance>20) {
				newEntities = col.checkProximity(collisionEntities, player);
				newEntities1 = col.checkProximity(collisionEntities1, player);
				chance = 0;
			}
			else {
				chance++;
			}
			
			ParticleMaster.update(camera);
			renderer.renderShadowMap(entities, sun);
			renderer.processEntity(player);
		
			
			if(i1<40) {
	        system.generateParticles(new Vector3f(100,10,-100));
			i1++;
			}
	       
			renderer.processTerrain(ocean);
			renderer.processTerrain(ocean1);
			renderer.processTerrain(ocean2);
			renderer.processTerrain(terrain);
			for(Entity entity:entities){
				renderer.processEntity(entity);                   // call process entity renderer for every entity.......................
			}
			renderer.render(lights, camera);                       // load light and camera to the shaders.........................
	
			
			
			
			ParticleMaster.renderParticle(camera);                //load particle to the screen......
		
			
			TextMaster.render();
		    guiRenderer.render(guis);
			DisplayManager.updateDisplay();                     /// Calls the method to update display in display manager class................
		}
		source.stop();
		
		
		
		
		boolean backToMenu = false;
		while(!quitGame && !backToMenu) {
			if(Display.isCloseRequested()) {
           	 quitGame = true;
            }
			mouseX = Mouse.getX();
			mouseY = Mouse.getY();
			if(((mouseX>774 && mouseX<1007)&&(mouseY<416 && mouseY>390)) && Mouse.isButtonDown(0)) {
				 backToMenu = true;
            }
			
		//	System.out.println(Mouse.getX() + " " + Mouse.getY());
			
			text3.setString("Game Over");
			text4.setString("Score = ");
			text5.setString("Main Menu");
			TextMaster.render();
			DisplayManager.updateDisplay();
			
		}
		
		

		
		}
		
		
		
		
		
	
			goblins.clear();
	
		
		
		
		source.delete();
	    AudioMaster.cleanUp();
		guiRenderer.cleanUp();                       
		ParticleMaster.cleanUp();                    //clean up the particles master class containing all the particles...........
		TextMaster.cleanUp();                          //clean up the text part of the text master.................
		renderer.cleanUp();                          // clean up the renderer..............................
		loader.cleanUp();                            // clean up the loader once our game is over............................
		DisplayManager.closeDisplay();              /// Calls the method to close display in display manager class.................

	   }
	   }

}
