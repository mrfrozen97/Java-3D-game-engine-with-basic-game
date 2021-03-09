package fontMeshCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * During the loading of a text this represents one word in the text.
 * @author Karl
 *
 */
public class Word {
	
	private List<Character> characters = new ArrayList<Character>();
	private double width = 0;
	private double fontSize;
	
	/**
	 * Create a new empty word.
	 * @param fontSize - the font size of the text which this word is in.
	 */
	protected Word(double fontSize){
		this.fontSize = fontSize;
	}
	
	/**
	 * Adds a character to the end of the current word and increases the screen-space width of the word.
	 * @param character - the character to be added.
	 */
	protected void addCharacter(Character character){
		characters.add(character);
		width += character.getxAdvance() * fontSize;
	}
	
	/**
	 * @return The list of characters in the word.
	 */
	protected List<Character> getCharacters(){
		return characters;
	}
	
	/**
	 * @return The width of the word in terms of screen size.
	 */
	protected double getWordWidth(){
		return width;
	}

}
