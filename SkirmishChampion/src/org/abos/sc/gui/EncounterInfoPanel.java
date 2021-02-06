package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.BattleEncounter;
import org.abos.sc.core.BattleFormation;
import org.abos.sc.core.Character;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class EncounterInfoPanel extends JPanel {

	protected boolean atLineStart;
	
	protected BattleEncounter encounter;
	
	protected CharacterBattlePanel[][] formation;
	
	protected JLabel challengeRatingLabel;
	
	/**
	 * 
	 */
	public EncounterInfoPanel(boolean atLineStart) {
		this.atLineStart = atLineStart;
		initComponents();
		initLayout();
	}

	/**
	 * @param isDoubleBuffered
	 */
	public EncounterInfoPanel(boolean atLineStart, boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.atLineStart = atLineStart;
		initComponents();
		initLayout();
	}
	
	public BattleEncounter getEncounter() {
		return encounter;
	}

	public void setEncounter(BattleEncounter encounter) {
		this.encounter = encounter;
		refreshEncounter();
	}
	
	public void refreshEncounter() {
		if (encounter == null) {
			for (int row = 0; row < formation.length; row++)
				for (int col = 0; col < formation[0].length; col++)
					formation[row][col].setCharacter(null);
			challengeRatingLabel.setText("");
			return;
		}
		for (int row = 0; row < formation.length; row++)
			for (int col = 0; col < formation[0].length; col++)
				formation[row][col].setCharacter(encounter.getCharacter(row, col));
		challengeRatingLabel.setText(String.format("Challenge Rating: %d", encounter.getChallengeRating()));
		repaint();
	}
	
	public void refreshHealth() {
		if (encounter == null) {
			return;
		}
		for (int row = 0; row < formation.length; row++)
			for (int col = 0; col < formation[0].length; col++)
				formation[row][col].refreshCharacter(true);
		repaint();
	}

	/**
	 * Initializes the components.
	 * @see #initLayout()
	 */
	private void initComponents() {
		formation = new CharacterBattlePanel[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		for (int row = 0; row < formation.length; row++)
			for (int col = 0; col < formation[0].length; col++) {
				formation[row][col] = new CharacterBattlePanel();
			}
		challengeRatingLabel = new JLabel();
	}
	
	protected GridBagConstraints createConstraint(int gridx, int gridy) {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = gridx;
		c.gridy = gridy;
		return c;
	}
	
	/**
	 * Initializes the layout. Only call directly after {@link #initComponents()} has been called.
	 * @see #initComponents()
	 */
	private void initLayout() {
		// note that grid is rotated
		//GridLayout layout = new GridLayout(BattleFormation.COL_NUMBER, BattleFormation.ROW_NUMBER);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		if (atLineStart) {
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) {
					add(formation[row][col], createConstraint(BattleFormation.ROW_NUMBER-1-row, col));
				}
		}
		else {
			for (int col = BattleFormation.COL_NUMBER - 1; col >= 0; col--)
				for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) {
					add(formation[row][col], createConstraint(row, col));
				}
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = BattleFormation.COL_NUMBER;
		c.gridwidth = BattleFormation.ROW_NUMBER;
		c.anchor = GridBagConstraints.CENTER;
		add(challengeRatingLabel, c);
		setPreferredSize(new Dimension(
				BattleFormation.ROW_NUMBER*CharacterBattlePanel.PREFERRED_WIDTH, 
				BattleFormation.COL_NUMBER*CharacterBattlePanel.PREFERRED_HEIGHT+ContentComboBox.PREFERRED_HEIGHT));
	}

}
