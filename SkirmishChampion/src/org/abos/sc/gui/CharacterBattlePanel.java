package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.Character;
import org.abos.util.gui.GBCBuilder;
import org.abos.util.gui.GUIUtilities;
import org.abos.util.gui.ImagePanel;

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
	 * The preferred height of the character battle panel.
	 */
	public static final int PREFERRED_HEIGHT = CharacterImagePanel.HEIGHT + 2*ContentComboBox.PREFERRED_HEIGHT;
	
	/**
	 * The preferred width of the character battle panel.
	 */
	public static final int PREFERRED_WIDTH = Math.max(CharacterImagePanel.WIDTH,ContentComboBox.PREFERRED_WIDTH);
	
	/**
	 * The preferred size of the character battle panel.
	 */
	public static final Dimension PREFERRED_SIZE = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
	
	/**
	 * The character this battle panel is for.
	 */
	protected Character character;
	
	/**
	 * The panel for the image.
	 */
	protected CharacterImagePanel imagePanel;
	
	/**
	 * The label containing the character name.
	 */
	protected JLabel characterLabel;
	
	/**
	 * The label containing the remaining character health.
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
	 * Sets the character for this battle panel.
	 * @param character the new character of this image panel
	 */
	public void setCharacter(Character character) {
		this.character = character;
		imagePanel.setCharacter(character);
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
			characterLabel.setText("");
			characterLabel.setToolTipText(null);
			characterHealthLabel.setText("");
		}
		else {
			characterLabel.setText(character.toString());
			characterLabel.setToolTipText(character.toHintString());
			characterHealthLabel.setText(character.healthToString());
		}
		repaint();
	}
	
	/**
	 * Initializes the components.
	 * @see #initLayout()
	 */
	private void initComponents() {
		imagePanel = new CharacterImagePanel();
		characterLabel = new JLabel();
		characterHealthLabel = new JLabel();
	}
	
	/**
	 * Initializes the layout. Only call directly after {@link #initComponents()} has been called.
	 * @see #initComponents()
	 */
	private void initLayout() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GBCBuilder builder = new GBCBuilder();
		add(imagePanel, builder.weighty(0.5).anchor(GridBagConstraints.PAGE_END).build());
		add(characterLabel, builder.gridy(1).anchor(GridBagConstraints.CENTER).build());
		add(characterHealthLabel, builder.gridy(2).weighty(0.5).anchor(GridBagConstraints.PAGE_START).build());
		setPreferredSize(PREFERRED_SIZE);
	}
	
}
