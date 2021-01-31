package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.abos.util.gui.GUIUtilities;
import org.abos.util.gui.ImagePanel;
import org.abos.sc.core.Character;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.4
 */
public class CharacterBattlePanel extends JPanel {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = 5266823928536695642L;

	/**
	 * the height of the character battle images
	 */
	public static final int IMAGE_HEIGHT = 64;
	
	/**
	 * the width of the character battle images
	 */
	public static final int IMAGE_WIDTH = 64;
	
	/**
	 * the preferred height of the character battle panel
	 */
	public static final int PREFERRED_HEIGHT = Math.max(IMAGE_HEIGHT, 2*ContentComboBox.PREFERRED_HEIGHT);
	
	/**
	 * the preferred width of the character battle panel
	 */
	public static final int PREFERRED_WIDTH = IMAGE_WIDTH + ContentComboBox.PREFERRED_WIDTH;
	
	/**
	 * the preferred size of the character battle panel
	 */
	public static final Dimension PREFERRED_SIZE = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
	
	/**
	 * the character this battle panel is for
	 */
	protected Character character;
	
	/**
	 * the panel for the image
	 */
	protected ImagePanel imagePanel;
	
	/**
	 * the label containing the character name
	 */
	protected JLabel characterLabel;
	
	/**
	 * the label containing the remaining chara
	 */
	protected JLabel characterHealthLabel;
	
	public CharacterBattlePanel(Character character) {
		initComponents();
		initLayout();
		setCharacter(character);
	}
	
	public CharacterBattlePanel() {
		this(null);
	}
	
	/**
	 * Returns the character this battle panel displays.
	 * @return the underlying character, can be <code>null</code>
	 */
	public Character getCharacter() {
		return character;
	}
	
	/**
	 * Sets the character
	 * @param character the character to set
	 */
	public void setCharacter(Character character) {
		this.character = character;
		refreshCharacter(false);
	}
	
	public void refreshCharacter(boolean healthOnly) {
		if (healthOnly) {
			if (character == null)
				characterHealthLabel.setText("");
			else
				characterHealthLabel.setText(character.healthToString());
			characterHealthLabel.repaint();
			return;
		}
		if (character == null) {
			imagePanel.setImage(null);
			characterLabel.setText("");
			characterHealthLabel.setText("");
		}
		else {
			imagePanel.loadImageLazy(GUIUtilities.getCharacterImagesPath().resolve(character.getId()+".png"));
			characterLabel.setText(character.toString());
			characterHealthLabel.setText(character.healthToString());
		}
		repaint();
	}
	
	/**
	 * Initializes the components.
	 * @see #initLayout()
	 */
	private void initComponents() {
		imagePanel = new ImagePanel(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		characterLabel = new JLabel();
		characterHealthLabel = new JLabel();
	}
	
	/**
	 * Initializes the layout. Only call directly after {@link #initComponents()} has been called.
	 * @see #initComponents()
	 */
	private void initLayout() {
		//BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		add(characterLabel, c);
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(characterHealthLabel, c);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.LINE_START;
		add(imagePanel, c);
		setPreferredSize(PREFERRED_SIZE);
		
//		GridBagLayout layout = new GridBagLayout();
//		setLayout(getLayout());
//		JPanel labelPanel = new JPanel();
//		labelPanel.setLayout(new GridLayout(2, 1));
//		labelPanel.add(characterLabel);
//		labelPanel.add(characterHealthLabel);
//		GridBagConstraints c = new GridBagConstraints();
//		c.gridx = 0;
//		add(imagePanel, c);
//		c.gridx = 1;
//		add(labelPanel, c);
//		setPreferredSize(PREFERRED_SIZE);
	}
	
}