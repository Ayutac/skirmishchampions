package org.abos.sc.gui;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.abos.sc.core.Difficulty;
import org.abos.sc.core.Fandom;
import org.abos.sc.core.FandomBase;
import org.abos.util.Name;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public class NewGameDialog extends JDialog {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = 4401381164058326765L;

	public final static String TITLE = "New Game";
	
	protected boolean started = false;
	
	protected JLabel fandomChooseLabel;
	
	protected ContentComboBox<Fandom> fandomSelector;
	
	protected JLabel difficultyChooseLabel;
	
	protected JRadioButton[] difficultyButtons;
	
	protected JButton startButton;
	
	protected JButton returnButton;

	/**
	 * 
	 * @throws IllegalStateException If {@link FandomBase#FANDOMS} is empty, which shouldn't happen. Either they have been loaded
	 * successfully already, or the inability to load them should have ended the application already.
	 */
	public NewGameDialog(Window owner) {
		super(owner);
		setTitle(TITLE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		initComponents();
		initLayout();
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b)
			started = false;
		super.setVisible(b);
	}
	
	public void start() {
		started = true;
		setVisible(false);
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	/**
	 * 
	 * @return
	 * @throws IllegalStateException If there was no radio button selected. This should never happen.
	 */
	public Difficulty getDifficulty() {
		for (int i = 0; i < difficultyButtons.length; i++) {
			if (difficultyButtons[i].isSelected())
				return Difficulty.values()[i];
		}
		throw new IllegalStateException("There was no selected difficulty!");
	}
	
	/**
	 * 
	 * @return
	 * @throws IllegalStateException If the base of the selected fandom isn't in {@link FandomBase#FANDOMS} anymore.
	 */
	public FandomBase getFandom() {
		FandomBase result = FandomBase.FANDOMS.lookup(((Fandom)fandomSelector.getSelectedItem()).getId());
		if (result == null)
			throw new IllegalStateException("Selected fandom not found anymore!");
		return result;
	}
	
	/**
	 * 
	 * @throws IllegalStateException If {@link FandomBase#FANDOMS} is empty, which shouldn't happen. Either they have been loaded
	 * successfully already, or the inability to load them should have ended the application already.
	 */
	private void initComponents() {
		if (FandomBase.FANDOMS.isEmpty())
			throw new IllegalStateException("No fandoms found!");
		fandomChooseLabel = new JLabel("Start Fandom:");
		List<Fandom> fandoms = FandomBase.FANDOMS.stream()
				.map(base -> new Fandom(base)).collect(Collectors.toList());
		fandomSelector = new ContentComboBox<>(fandoms, Name.createNameComparator());
		FandomBase defaultFandom = FandomBase.getDefaultFandom();
		if (defaultFandom != null) {
			// sorting is required so we get the correct index
			Collections.sort(fandoms, Name.createNameComparator());
			fandomSelector.setSelectedIndex(fandoms.indexOf(new Fandom(defaultFandom)));
		}
		Difficulty[] difficulties = Difficulty.values();
		difficultyChooseLabel = new JLabel("What is at steak?");
		difficultyButtons = new JRadioButton[difficulties.length];
		ButtonGroup difficultyGroup = new ButtonGroup(); 
		for (int i = 0; i < difficulties.length; i++) {
			difficultyButtons[i] = new JRadioButton(difficulties[i].getName(), difficulties[i] == Difficulty.MEDIUM);
			difficultyButtons[i].setToolTipText(difficulties[i].getCapitalizedName());
			difficultyGroup.add(difficultyButtons[i]);
		}
		startButton = new JButton("Start");
		startButton.addActionListener(e -> start());
		returnButton = new JButton("Return");
		returnButton.addActionListener(e -> setVisible(false));
	}
	
	private void initLayout() {
		GridLayout layout = new GridLayout(0, 1);
		setLayout(layout);
		JPanel centeredLabelPanel = new JPanel();
		centeredLabelPanel.add(fandomChooseLabel);
		add(centeredLabelPanel);
		add(fandomSelector);
		centeredLabelPanel = new JPanel();
		centeredLabelPanel.add(difficultyChooseLabel);
		add(centeredLabelPanel);
		for (JRadioButton difficultyButton : difficultyButtons) {
			add(difficultyButton);
		}
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.add(startButton);
		buttonPanel.add(returnButton);
		add(buttonPanel);
		pack();
		setLocationRelativeTo(null);
	}

}
