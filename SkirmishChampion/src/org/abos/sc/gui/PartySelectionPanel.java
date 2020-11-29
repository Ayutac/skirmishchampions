package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.abos.sc.core.BattleFormation;
import org.abos.sc.core.Character;
import org.abos.sc.core.Companion;
import org.abos.sc.core.Fandom;
import org.abos.sc.core.Region;
import org.abos.sc.core.Registry;
import org.abos.sc.core.Stage;
import org.abos.sc.core.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class PartySelectionPanel extends JPanel {
	
	protected BattleFormation formation;
	
	protected boolean facesLineEnd;
	
	protected Registry<Companion> companionPool;

	protected JCheckBox[][] positionCheckBox;
	
	protected ContentComboBox<Companion>[][] positionSelector;
	
	/**
	 * 
	 */
	public PartySelectionPanel(Registry<Companion> companionPool, boolean facesLineEnd) {
		Utilities.requireNonNull(companionPool, "companionPool");
		this.companionPool = companionPool;
		this.facesLineEnd = facesLineEnd;
		initComponents();
		initLayout();
	}
	
	public PartySelectionPanel(Registry<Companion> companionPool) {
		this(companionPool, true);
	}
	
	public BattleFormation getFormation() {
		return formation;
	}
	
	public void setFormation(BattleFormation formation) {
		this.formation = formation;
		resetFormation();
	}
	
	/**
	 * @param companionPool the companionPool to set
	 */
	public void setCompanionPool(Registry<Companion> companionPool) {
		Utilities.requireNonNull(companionPool, "companionPool");
		this.companionPool = companionPool;
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				positionSelector[row][col].setContent(companionPool);
			}
		refreshSelectors();
		deselectAlmostAll();
		assert validateFormation();
		acceptFormation();
	}
	
	public void deselectAlmostAll() {
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				if (row == 0 && col == 0)
					positionCheckBox[row][col].setSelected(true);
				else
					positionCheckBox[row][col].setSelected(false);
			}
	}
	
	/**
	 * Changes the GUI to display the internal formation. Unused spaces will make the corresponding combobox
	 * display its first item.
	 */
	public void resetFormation() {
		Character currentCharacter = null;
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				currentCharacter = formation.getCharacter(row, col);
				if (currentCharacter == null) {
					positionCheckBox[row][col].setSelected(false);
					positionSelector[row][col].setSelectedIndex(0);
				}
				else {
					positionCheckBox[row][col].setSelected(true);
					assert positionSelector[row][col].containsItem(currentCharacter);
					positionSelector[row][col].setSelectedItem(currentCharacter);
					
				}
			}
	}
	
	/**
	 * Checks if the formation given by the GUI is a valid one. 
	 * If this method returns <code>true</code>, {@link #acceptFormation()} is guarantied to succeed.
	 * @return <code>true</code> if at least one character is selected and no character is doubled, else <code>false</code>.
	 * @see #acceptFormation()
	 */
	public boolean validateFormation() {
		boolean anySelected = false;
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				anySelected |= positionCheckBox[row][col].isSelected();
		if (!anySelected)
			return false;
		Set<Character> selectedChars = new HashSet<>(BattleFormation.MAX_CHAR_NUMBER+1,1f);
		Character currentChar = null;
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				if (positionCheckBox[row][col].isSelected()) {
					currentChar = (Character)positionSelector[row][col].getSelectedItem();
					if (selectedChars.contains(currentChar))
						return false;
					selectedChars.add(currentChar);
				}
			}
		return true;
	}
	
	/**
	 * Changes the internal battle formation to the one displayed by the GUI.
	 * Note that this method might throw exceptions if {@link #validateFormation()} didn't
	 * return <code>true</code> directly before calling this method. Also note that these two
	 * methods are not thread-safe.
	 * @throws RuntimeException Possibly if {@link #validateFormation()} didn't returned <code>true</code> before calling this method.
	 * @see #validateFormation()
	 */
	public void acceptFormation() {
		Character[][] characters = new Character[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				if (positionCheckBox[row][col].isSelected())
					characters[row][col] = (Character)positionSelector[row][col].getSelectedItem();
			}
		formation = new BattleFormation(characters);
	}
	
	public void refreshSelectors() {
		Companion companion = null;
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				companion = (Companion)positionSelector[row][col].getSelectedItem();
				positionSelector[row][col].refreshContent();
				if (positionSelector[row][col].containsItem(companion))
					positionSelector[row][col].setSelectedItem(companion);
				else
					positionSelector[row][col].setSelectedIndex(0);
			}
	}
	
	private void initComponents() {
		positionCheckBox = new JCheckBox[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		positionSelector = new ContentComboBox[BattleFormation.ROW_NUMBER][BattleFormation.COL_NUMBER];
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				positionCheckBox[row][col] = new JCheckBox(String.format("Position (%d,%d) ",row,col), row == 0 && col == 0);
				// enable toggle selection with right mouse click just like for the combobox
				positionCheckBox[row][col].addMouseListener(new MouseAdapter() {
					@Override public void mouseClicked(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON3)
							((JCheckBox)e.getSource()).setSelected(!((JCheckBox)e.getSource()).isSelected());
					}
				});
				positionSelector[row][col] = new ContentComboBox<>(companionPool, Utilities.createNameComparator());
				// automatically change to selected if combobox is used
				positionSelector[row][col].addItemListener(new ItemListener() {
					@Override public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED && ((JComboBox)e.getSource()).isPopupVisible())
							for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
								for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
									if (e.getSource() == positionSelector[row][col])
										positionCheckBox[row][col].setSelected(true);
					}
				});
				// also enable toggle selection with right mouse click
				positionSelector[row][col].addMouseListener(new MouseAdapter() {
					@Override public void mouseClicked(MouseEvent e) {
						for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
							for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
								if (e.getSource() == positionSelector[row][col]) {
									if (e.getButton() == MouseEvent.BUTTON3)
										positionCheckBox[row][col].setSelected(!positionCheckBox[row][col].isSelected());
									return;
								}
					}
				});
			}
		assert validateFormation();
		acceptFormation();
	}
	
	private void initLayout() {
		// note that the grid is rotated
		GridLayout layout = new GridLayout(BattleFormation.COL_NUMBER, 2 * BattleFormation.ROW_NUMBER);
		setLayout(layout);
		if (facesLineEnd) {
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				for (int row = BattleFormation.ROW_NUMBER - 1; row >= 0; row--) {
					add(positionCheckBox[row][col]);
					add(positionSelector[row][col]);
				}
		}
		else {
			for (int col = BattleFormation.COL_NUMBER - 1; col >= 0; col--)
				for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) {
					add(positionCheckBox[row][col]);
					add(positionSelector[row][col]);
				}
		}
		// positionCheckBox[0][0].requestFocusInWindow();
		setPreferredSize(new Dimension(
				2*BattleFormation.ROW_NUMBER*ContentComboBox.PREFERRED_WIDTH, 
				BattleFormation.COL_NUMBER*ContentComboBox.PREFERRED_HEIGHT));
	}
	
	
}
