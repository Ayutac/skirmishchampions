package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.BattleEncounter;
import org.abos.sc.core.BattleFormation;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class EncounterInfoPanel extends JPanel {

	protected boolean atLineStart;
	
	protected BattleEncounter encounter;
	
	protected JLabel[][] formation;
	
	protected JLabel[][] formationHealth;
	
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
				for (int col = 0; col < formation[0].length; col++) {
					formation[row][col].setText("");
					formationHealth[row][col].setText("");
				}
			repaint();
			return;
		}
		org.abos.sc.core.Character character;
		for (int row = 0; row < formation.length; row++)
			for (int col = 0; col < formation[0].length; col++) {
				character = encounter.getCharacter(row, col);
				if (character == null) {
					formation[row][col].setText("");
					formationHealth[row][col].setText("");
				}
				else {
					formation[row][col].setText(character.toString());
					formationHealth[row][col].setText(character.healthToString());
				}
			}
		repaint();
	}
	
	public void refreshHealth() {
		if (encounter == null) {
			return;
		}
		org.abos.sc.core.Character character;
		for (int row = 0; row < formation.length; row++)
			for (int col = 0; col < formation[0].length; col++) {
				character = encounter.getCharacter(row, col);
				if (character != null) {
					formationHealth[row][col].setText(character.healthToString());
				}
			}
		repaint();
	}

	private void initComponents() {
		formation = new JLabel[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		formationHealth = new JLabel[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		for (int row = 0; row < formation.length; row++)
			for (int col = 0; col < formation[0].length; col++) {
				formation[row][col] = new JLabel();
				formationHealth[row][col] = new JLabel();
			}
	}
	
	private void initLayout() {
		// note that grid is rotated
		GridLayout layout = new GridLayout(2 * BattleFormation.COL_NUMBER, BattleFormation.ROW_NUMBER);
		setLayout(layout);
		if (atLineStart) {
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				for (int row = BattleFormation.ROW_NUMBER - 1; row >= 0; row--) {
					add(formation[row][col]);
				}
				for (int row = BattleFormation.ROW_NUMBER - 1; row >= 0; row--) {
					add(formationHealth[row][col]);
				}
			}
		}
		else {
			for (int col = BattleFormation.COL_NUMBER - 1; col >= 0; col--) {
				for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) {
					add(formation[row][col]);
				}
				for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) {
					add(formationHealth[row][col]);
				}
			}
		}
		setPreferredSize(new Dimension(
				BattleFormation.ROW_NUMBER*ContentComboBox.PREFERRED_WIDTH, 
				2*BattleFormation.COL_NUMBER*ContentComboBox.PREFERRED_HEIGHT));
	}

}
