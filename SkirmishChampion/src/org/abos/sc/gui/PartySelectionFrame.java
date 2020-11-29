package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.abos.sc.core.BattleFormation;
import org.abos.sc.core.Companion;
import org.abos.sc.core.Registry;
import org.abos.sc.core.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class PartySelectionFrame extends JFrame {

	public static String TITLE = "Party Selection";
	
	protected Registry<Companion> companionPool;
	
	protected Runnable afterHiding = null;
	
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
		selectionPanel = new PartySelectionPanel(companionPool);
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(e -> confirmFormation());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> cancelFormation(true));
	}
	
	private void initLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		add(selectionPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

}
