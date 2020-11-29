package org.abos.sc.gui;

import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.Fandom;
import org.abos.sc.core.Player;
import org.abos.sc.core.Region;
import org.abos.sc.core.Stage;
import org.abos.sc.core.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class StageSelectionPanel extends JPanel {
	
	protected Player player;
	
	protected boolean leftToRight;
	
	protected JLabel fandomLabel;
	
	protected ContentComboBox<Fandom> fandomSelector;
	
	protected JLabel regionLabel;
	
	protected ContentComboBox<Region> regionSelector;
	
	protected JLabel stageLabel;
	
	protected ContentComboBox<Stage> stageSelector; 
	
	/**
	 * @throws HeadlessException
	 */
	public StageSelectionPanel(Player player, boolean leftToRight) throws HeadlessException {
		if (player == null)
			throw new NullPointerException("player must be specified!");
		this.player = player;
		this.leftToRight = leftToRight;
		initComponents();
		initLayout();
	}
	
	public Fandom getFandom() {
		return (Fandom)fandomSelector.getSelectedItem();
	}
	
	public Region getRegion() {
		return (Region)regionSelector.getSelectedItem();
	}
	
	public Stage getStage() {
		return (Stage)stageSelector.getSelectedItem();
	}
	
	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		Utilities.requireNonNull(player, "player");
		this.player = player;
		fandomSelector.setContent(player.getFandoms());
		fandomSelector.setSelectedIndex(0);
		refreshSelectors();
	}
	
	public void refreshStages(Stage oldStage) {
		stageSelector.setContent(getRegion().getStages());
		if (stageSelector.containsItem(oldStage))
			stageSelector.setSelectedItem(oldStage);
		else
			stageSelector.setSelectedIndex(0);
	}
	
	public void refreshStages(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED && ((JComboBox)e.getSource()).isPopupVisible()) {
			refreshStages(getStage());
		}
	}
	
	public void refreshRegions(Region oldRegion, Stage oldStage) {
		regionSelector.setContent(getFandom().getRegions());
		if (regionSelector.containsItem(oldRegion))
			regionSelector.setSelectedItem(oldRegion);
		else
			regionSelector.setSelectedIndex(0);
		refreshStages(oldStage);
	}
	
	public void refreshRegions(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED && ((JComboBox)e.getSource()).isPopupVisible())
			refreshRegions(getRegion(), getStage());
	}
	
	public void refreshFandoms(Fandom oldFandom, Region oldRegion, Stage oldStage) {
		fandomSelector.refreshContent();
		if (fandomSelector.containsItem(oldFandom))
			fandomSelector.setSelectedItem(oldFandom);
		else
			fandomSelector.setSelectedIndex(0);
		refreshRegions(oldRegion, oldStage);
	}
	
//	public void refreshFandoms() {
//		refreshFandoms(getFandom(), getRegion(), getStage());
//	}
	
	public void refreshSelectors() {
		refreshFandoms(getFandom(), getRegion(), getStage());
	}
	
	private void initComponents() {
		fandomLabel = new JLabel("Fandom:");
		fandomSelector = new ContentComboBox<>(player.getFandoms(), Utilities.createNameComparator());
		fandomSelector.addItemListener(e -> refreshRegions(e));
		regionLabel = new JLabel("Region:");
		regionSelector = new ContentComboBox<>(getFandom().getRegions(), Utilities.createNameComparator());
		regionSelector.addItemListener(e -> refreshStages(e));
		stageLabel = new JLabel("Stage:");
		stageSelector = new ContentComboBox<>(getRegion().getStages(), Utilities.createNameComparator());
		//stageSelector.addItemListener(e -> refreshStages());
	}
	
	private void initLayout() {
		GridLayout layout = new GridLayout(0, 2);
		setLayout(layout);
		if (leftToRight) {
			add(fandomLabel);
			add(fandomSelector);
			add(regionLabel);
			add(regionSelector);
			add(stageLabel);
			add(stageSelector);
		}
		else {
			add(fandomSelector);
			add(fandomLabel);
			add(regionSelector);
			add(regionLabel);
			add(stageSelector);
			add(stageLabel);
		}
	}

}
