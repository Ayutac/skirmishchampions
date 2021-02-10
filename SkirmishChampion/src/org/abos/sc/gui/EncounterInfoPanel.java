package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.BattleEncounter;
import org.abos.sc.core.BattleFormation;
import org.abos.sc.core.Difficulty;
import org.abos.util.Utilities;
import org.abos.util.gui.GBCBuilder;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class EncounterInfoPanel extends JPanel {

	protected boolean atLineStart;
	
	protected BattleEncounter encounter;
	
	protected Difficulty difficulty = Difficulty.of(null);
	
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
	
	/**
	 * @return the difficulty
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(Difficulty difficulty) {
		Utilities.requireNonNull(difficulty, "difficulty");
		this.difficulty = difficulty;
	}
	
	public void refreshDifficulty() {
		for (int row = 0; row < formation.length; row++)
			for (int col = 0; col < formation[0].length; col++) {
				formation[row][col].setHealthVisible(difficulty.showCharacterHealth());
				formation[row][col].setStatsHintVisible(difficulty.showCharacterStats());
			}
		if (difficulty.stopSteamrolling()) {
			challengeRatingLabel.setToolTipText(String.format("<html>The challenge rating of your team is only allowed to be at most %.2f times<br> higher than the challenge rating of the opposing team.",Difficulty.STEAMROLL_FACTOR));
		}
		else {
			challengeRatingLabel.setToolTipText(null);
		}
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
		refreshDifficulty();
		repaint();
	}
	
	public void refreshHealth() {
		if (encounter == null || !difficulty.showCharacterHealth()) {
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
	
	/**
	 * Initializes the layout. Only call directly after {@link #initComponents()} has been called.
	 * @see #initComponents()
	 */
	private void initLayout() {
		// note that grid is rotated
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GBCBuilder builder = new GBCBuilder().anchor(GridBagConstraints.LINE_START).reset();
		if (atLineStart) {
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) {
					add(formation[row][col], builder.gridx(BattleFormation.ROW_NUMBER-1-row).gridy(col).get());
				}
		}
		else {
			for (int col = BattleFormation.COL_NUMBER - 1; col >= 0; col--)
				for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) {
					add(formation[row][col], builder.gridx(row).gridy(col).get());
				}
		}
		add(challengeRatingLabel, builder.gridx(0).gridy(BattleFormation.COL_NUMBER).gridwidth(BattleFormation.ROW_NUMBER).anchor(GridBagConstraints.CENTER).build());
		setPreferredSize(new Dimension(
				BattleFormation.ROW_NUMBER*CharacterBattlePanel.PREFERRED_WIDTH, 
				BattleFormation.COL_NUMBER*CharacterBattlePanel.PREFERRED_HEIGHT+ContentComboBox.PREFERRED_HEIGHT));
	}

}
