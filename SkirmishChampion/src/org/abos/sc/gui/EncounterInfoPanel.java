package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.Difficulty;
import org.abos.sc.core.battle.Encounter;
import org.abos.sc.core.battle.Formation;
import org.abos.util.Utilities;
import org.abos.util.gui.GBCBuilder;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class EncounterInfoPanel extends JPanel {

	protected boolean atLineStart;
	
	protected Encounter encounter;
	
	protected Difficulty difficulty = Difficulty.DEFAULT;
	
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
	
	public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
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
			challengeRatingLabel.setToolTipText(String.format("<html>The challenge rating of your team is only allowed to be<br><b>%d</b> at most for this encounter.</html>",
					difficulty.getChallengeRatingCap(encounter.getChallengeRating())));
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
			repaint();
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
		formation = new CharacterBattlePanel[Formation.ROW_NUMBER][Formation.COL_NUMBER];
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
			for (int col = 0; col < Formation.COL_NUMBER; col++)
				for (int row = 0; row < Formation.ROW_NUMBER; row++) {
					add(formation[row][col], builder.gridx(Formation.ROW_NUMBER-1-row).gridy(col).get());
				}
		}
		else {
			for (int col = Formation.COL_NUMBER - 1; col >= 0; col--)
				for (int row = 0; row < Formation.ROW_NUMBER; row++) {
					add(formation[row][col], builder.gridx(row).gridy(col).get());
				}
		}
		add(challengeRatingLabel, builder.gridx(0).gridy(Formation.COL_NUMBER).gridwidth(Formation.ROW_NUMBER).anchor(GridBagConstraints.CENTER).build());
		setPreferredSize(new Dimension(
				Formation.ROW_NUMBER*CharacterBattlePanel.PREFERRED_WIDTH, 
				Formation.COL_NUMBER*CharacterBattlePanel.PREFERRED_HEIGHT+ContentComboBox.PREFERRED_HEIGHT));
	}

}
