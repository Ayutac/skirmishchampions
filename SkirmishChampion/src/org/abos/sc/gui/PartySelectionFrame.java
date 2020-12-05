package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.abos.sc.core.BattleFormation;
import org.abos.sc.core.Companion;
import org.abos.util.Registry;
import org.abos.util.Utilities;
import org.abos.util.gui.GUIUtilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class PartySelectionFrame extends JFrame {

	public static String TITLE = "Party Selection";
	
	protected Registry<Companion> companionPool;
	
	protected Runnable afterHiding = null;
	
	protected CompanionSortComboBox sortComboBox;
	
	protected JRadioButton ascButton;
	
	protected JRadioButton descButton;
	
	protected PartySelectionPanel selectionPanel;
	
	protected JButton confirmButton;
	
	protected JButton cancelButton;
	
	/**
	 * @throws HeadlessException
	 */
	public PartySelectionFrame(Registry<Companion> companionPool) throws HeadlessException {
		super(TITLE);
		Utilities.requireNonNull(companionPool, "companionPool");
		this.companionPool = companionPool;
		initComponents();
		initLayout();
		if (!GUIUtilities.LOGOS.isEmpty())
			setIconImages(GUIUtilities.LOGOS);
	}
	
	public BattleFormation getFormation() {
		return selectionPanel.getFormation();
	}
	
	public void setFormation(BattleFormation formation) {
		selectionPanel.setFormation(formation);
	}
	
	/**
	 * @param companionPool the companionPool to set
	 */
	public void setCompanionPool(Registry<Companion> companionPool) {
		Utilities.requireNonNull(companionPool, "companionPool");
		this.companionPool = companionPool;
		selectionPanel.setCompanionPool(companionPool);
	}
	
	/**
	 * @param afterHiding the action to execute
	 */
	public void setAfterHiding(Runnable afterHiding) {
		this.afterHiding = afterHiding;
	}
	
	public void setVisible(boolean b, boolean cancel) {
		super.setVisible(b);
		if (b) {
			selectionPanel.refreshSelectors();
			requestFocus();
		}
		else {
			if (cancel)
				cancelFormation(false);
			if (!b && afterHiding != null)
				afterHiding.run();
		}
	}
	
	@Override
	public void setVisible(boolean b) {
		setVisible(b, true); // true because the windows close operation calls this
	}
	
	public void confirmFormation() {
		if (selectionPanel.validateFormation()) {
			selectionPanel.acceptFormation();
			setVisible(false, false);
		}
		else {
			JOptionPane.showMessageDialog(this, 
					"The party must contain at least one member and "+System.lineSeparator()+
					"no character can be used several times!", 
					"Invalid party!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void cancelFormation(boolean hide) {
		if (hide)
			setVisible(false, false);
		selectionPanel.resetFormation();
	}
	
	private void initComponents() {
		sortComboBox = new CompanionSortComboBox();
		ascButton = new JRadioButton("Asc", true);
		descButton = new JRadioButton("Desc", false);
		ButtonGroup orderSelectionGroup = new ButtonGroup();
		orderSelectionGroup.add(ascButton);
		orderSelectionGroup.add(descButton);
		selectionPanel = new PartySelectionPanel(companionPool);
		sortComboBox.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selectionPanel.setComparator(sortComboBox.getComparator());
				}
			}
		});
		ascButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				sortComboBox.setReversed(false);
				selectionPanel.setComparator(sortComboBox.getComparator());
			}
		});
		descButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				sortComboBox.setReversed(true);
				selectionPanel.setComparator(sortComboBox.getComparator());
			}
		});
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(e -> confirmFormation());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> cancelFormation(true));
	}
	
	private void initLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		JPanel sortPanel = new JPanel();
		sortPanel.add(new JLabel("Sort by:"));
		sortPanel.add(sortComboBox);
		sortPanel.add(ascButton);
		sortPanel.add(descButton);
		add(sortPanel, BorderLayout.PAGE_START);
		add(selectionPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

}
