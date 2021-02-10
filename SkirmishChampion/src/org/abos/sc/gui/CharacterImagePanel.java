package org.abos.sc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.nio.file.Path;

import org.abos.sc.core.Character;
import org.abos.util.gui.GUIUtilities;
import org.abos.util.gui.ImagePanel;

/**
 * 
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public class CharacterImagePanel extends ImagePanel {

	/**
	 * The width of the character images.
	 */
	public static final int WIDTH = 64;

	/**
	 * The height of the character images.
	 */
	public static final int HEIGHT = 64;
	
	/**
	 * The preferred Dimension of character image panels.
	 */
	public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
	
	/**
	 * The path for a default character image.
	 */
	public static final Path DEFAULT_PATH = GUIUtilities.getCharacterImagesPath().resolve("default.png");
	
	/**
	 * How long damage should be displayed on characters, in milliseconds.
	 */
	public static final long DAMAGE_DISPLAY_DURATION = 500;
	
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
	
	protected boolean paintDamage() {
		return (character != null && !character.isDefeated() && character.getLastDamageMoment() != null &&
				System.currentTimeMillis() - character.getLastDamageMoment() <= DAMAGE_DISPLAY_DURATION);
	}
	
	protected boolean paintDefeated() {
		return (character != null && character.isDefeated());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color old = g.getColor();
		if (paintDamage()) {
			g.setColor(Color.RED);
			g.drawRect(1, 1, WIDTH-3, HEIGHT-3);
		}
		else if (paintDefeated()) {
			g.setColor(Color.BLACK);
			// draw X
			g.drawLine(3, 1, WIDTH-2, HEIGHT-4);
			g.drawLine(2, 1, WIDTH-2, HEIGHT-3);
			g.drawLine(1, 1, WIDTH-2, HEIGHT-2);
			g.drawLine(1, 2, WIDTH-3, HEIGHT-2);
			g.drawLine(1, 3, WIDTH-4, HEIGHT-2);
			g.drawLine(WIDTH-4, 1, 1, HEIGHT-4);
			g.drawLine(WIDTH-3, 1, 1, HEIGHT-3);
			g.drawLine(WIDTH-2, 1, 1, HEIGHT-2);
			g.drawLine(WIDTH-2, 2, 2, HEIGHT-2);
			g.drawLine(WIDTH-2, 3, 3, HEIGHT-2);
		}
		g.setColor(old);
	}

}
