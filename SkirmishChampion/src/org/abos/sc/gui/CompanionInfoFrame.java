package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.abos.sc.core.Companion;
import org.abos.sc.core.Player;
import org.abos.util.Name;
import org.abos.util.Utilities;
import org.abos.util.gui.GUIUtilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class CompanionInfoFrame extends JFrame {
	
	public static final String TITLE = "Companion Info";
	
	protected Player player;
	
	protected ContentComboBox<Companion> companionSelection;
	
	protected CompanionStatInfoPanel companionStatInfoPanel;
	
	protected JButton closeButton;

	/**
	 * @throws HeadlessException
	 */
	public CompanionInfoFrame(Player player) throws HeadlessException {
		super(TITLE);
		Utilities.requireNonNull(player, "player");
		this.player = player;
		initComponents();
		initLayout();
		if (!GUIUtilities.LOGOS.isEmpty())
			setIconImages(GUIUtilities.LOGOS);
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b) {
			companionStatInfoPanel.refreshCompanion();
		}
		super.setVisible(b);
	}
	
	public void setInfoToSelected() {
		if (companionSelection.getSelectedIndex() == -1)
			companionStatInfoPanel.setCompanion(null);
		else
			companionStatInfoPanel.setCompanion((Companion)companionSelection.getSelectedItem());
	}
	
	private void initComponents() {
		companionSelection = new ContentComboBox<>(player.getCompanions(), Name.createNameComparator());
		companionStatInfoPanel = new CompanionStatInfoPanel();
		setInfoToSelected();
		companionSelection.addActionListener(e -> setInfoToSelected());
		closeButton = new JButton("Close");
		closeButton.addActionListener(e -> setVisible(false));
	}
	
	private void initLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		Container pane = getContentPane();
		pane.add(companionSelection, BorderLayout.PAGE_START);
		pane.add(companionStatInfoPanel, BorderLayout.CENTER);
		pane.add(closeButton, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

}
