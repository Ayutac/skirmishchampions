package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.abos.sc.core.BattleFormation;
import org.abos.sc.core.ChallengeRatable;
import org.abos.sc.core.Character;
import org.abos.sc.core.Companion;
import org.abos.util.Registry;
import org.abos.util.Utilities;
import org.abos.util.gui.GBCBuilder;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class PartySelectionPanel extends JPanel implements ChallengeRatable {
	
	protected BattleFormation formation;
	
	protected Comparator<Companion> comparator = null;
	
	protected boolean facesLineEnd;
	
	protected Registry<Companion> companionPool;
	
	protected Integer scrc; // stage challenge rating cap

	protected JCheckBox[][] positionCheckBox;
	
	protected ContentComboBox<Companion>[][] positionSelector;
	
	protected JLabel challengeRatingTextLabel;
	
	protected JLabel challengeRatingValueLabel;
	
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
	
	public void setComparator(Comparator<Companion> comparator) {
		if (this.comparator == comparator)
			return;
		this.comparator = comparator;
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				positionSelector[row][col].setComparator(comparator, positionCheckBox[row][col].isSelected());
			}
	}
	
	/**
	 * @return the comparator
	 */
	public Comparator<Companion> getComparator() {
		return comparator;
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
	
	/**
	 * @return the stageChallengeRatingCap
	 */
	public Integer getStageChallengeRatingCap() {
		return scrc;
	}
	
	/**
	 * @param stageChallengeRatingCap the stageChallengeRatingCap to set
	 */
	public void setStageChallengeRatingCap(Integer stageChallengeRatingCap) {
		this.scrc = stageChallengeRatingCap;
		refreshChallengeRating();
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
					characters[row][col] = (Character)((Character)positionSelector[row][col].getSelectedItem()).clone();
			}
		formation = new BattleFormation(characters);
	}
	
	public void refreshSelectors() {
		Companion companion = null;
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
				companion = (Companion)positionSelector[row][col].getSelectedItem();
				positionSelector[row][col].refreshContent(true);
			}
	}
	
	/**
	 * Returns the summed challenge rating of the currently selected characters, NOT the challege rating
	 * of {@link #getFormation()}.
	 * @return the summed challenge rating of the currently selected characters
	 */
	@Override
	public int getChallengeRating() {
		int sum = 0;
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				if (positionCheckBox[row][col].isSelected())
					sum += ((Character)positionSelector[row][col].getSelectedItem()).getChallengeRating();
		return sum;
	}
	
	public void refreshChallengeRating() {
		int cr = getChallengeRating();
		if (scrc == null)
			challengeRatingValueLabel.setText(Integer.toString(cr));
		else
			challengeRatingValueLabel.setText(String.format("%d (%s %d)", 
				cr, cr <= scrc ? "<=" : ">", scrc));
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
				// refresh CR on change
				positionCheckBox[row][col].addChangeListener(new ChangeListener() {
					@Override public void stateChanged(ChangeEvent e) {
						refreshChallengeRating();
					}
				});
				positionSelector[row][col] = new ContentComboBox<>(companionPool, Utilities.createNameComparator());
				// automatically change to selected if combobox is used, also refresh CR
				positionSelector[row][col].addItemListener(new ItemListener() {
					@SuppressWarnings("rawtypes")
					@Override public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							// refresh the tooltip
							((JComponent)e.getSource()).setToolTipText(((Companion)e.getItem()).toHintString());
							// check the appropiate checkbox
							if (((JComboBox)e.getSource()).isPopupVisible())
								for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
									for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
										if (e.getSource() == positionSelector[row][col])
											positionCheckBox[row][col].setSelected(true);
							refreshChallengeRating();
						}
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
		challengeRatingTextLabel = new JLabel("Challenge Rating:");
		challengeRatingValueLabel = new JLabel(Integer.toString(((Character)positionSelector[0][0].getSelectedItem()).getChallengeRating()));
		assert validateFormation();
		acceptFormation();
	}
	
	private void initLayout() {
		// note that the grid is rotated
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GBCBuilder checkBoxBuilder = new GBCBuilder().
				anchorDefault(GridBagConstraints.LINE_START).reset();
		GBCBuilder selectorBuilder = new GBCBuilder().
				anchorDefault(GridBagConstraints.LINE_START).weightxDefault(1d/BattleFormation.ROW_NUMBER).fillDefault(GridBagConstraints.HORIZONTAL).reset();
		if (facesLineEnd) {
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				for (int row = BattleFormation.ROW_NUMBER - 1; row >= 0; row--) {
					add(positionCheckBox[row][col], checkBoxBuilder.gridx(2*(BattleFormation.ROW_NUMBER-1-row)).gridy(col).get());
					add(positionSelector[row][col], selectorBuilder.gridx(2*(BattleFormation.ROW_NUMBER-1-row)+1).gridy(col).get());
				}
		}
		else {
			for (int col = BattleFormation.COL_NUMBER - 1; col >= 0; col--)
				for (int row = 0; row < BattleFormation.ROW_NUMBER; row++) {
					add(positionCheckBox[row][col], checkBoxBuilder.gridx(2*row+1).gridy(col).get());
					add(positionSelector[row][col], selectorBuilder.gridx(2*row).gridy(col).get());
				}
		}
		GBCBuilder crBuilder =  new GBCBuilder().gridyDefault(BattleFormation.COL_NUMBER+1).gridwidthDefault(BattleFormation.ROW_NUMBER).reset();
		add(challengeRatingTextLabel, crBuilder.anchor(GridBagConstraints.EAST).get());
		add(challengeRatingValueLabel, crBuilder.anchor(GridBagConstraints.WEST).
				gridx(BattleFormation.ROW_NUMBER).insets(new Insets(0, 3, 0, 0)).get());
		// positionCheckBox[0][0].requestFocusInWindow();
		setPreferredSize(new Dimension(
				2*BattleFormation.ROW_NUMBER*ContentComboBox.PREFERRED_WIDTH, 
				(BattleFormation.COL_NUMBER+1)*ContentComboBox.PREFERRED_HEIGHT));
	}
	
	
}
