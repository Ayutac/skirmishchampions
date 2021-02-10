package org.abos.sc.gui;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Path;

import org.abos.sc.core.Character;
import org.abos.util.gui.GUIUtilities;
import org.abos.util.gui.ImagePanel;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 1.0
 */
public class CharacterImagePanel extends ImagePanel {

	/**
	 * The height of the character images.
	 */
	public static final int HEIGHT = 64;
	
	/**
	 * The width of the character images.
	 */
	public static final int WIDTH = 64;

	/**
	 * The preferred Dimension of character image panels.
	 */
	public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
	
	/**
	 * The path for a default character image.
	 */
	public static final Path DEFAULT_PATH = GUIUtilities.getCharacterImagesPath().resolve("default.png");
	
	/**
	 * The character to display the image of.
	 */
	protected Character character;
	
	/**
	 * 
	 */
	public CharacterImagePanel() {
		super(SIZE);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param path
	 * @throws IOException
	 */
	public CharacterImagePanel(Path path) throws IOException {
		super(path, SIZE);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns the character this image panel displays.
	 * @return the underlying character, can be <code>null</code>
	 */
	public Character getCharacter() {
		return character;
	}
	
	/**
	 * Sets the character for this image panel.
	 * @param character the new character of this image panel
	 */
	public void setCharacter(Character character) {
		this.character = character;
		if (character == null)
			setImage(null);
		else
			loadImageLazy(GUIUtilities.getCharacterImagesPath().resolve(character.getFandomId()).resolve(character.getId()+".png"), DEFAULT_PATH);
		repaint();
	}

}
