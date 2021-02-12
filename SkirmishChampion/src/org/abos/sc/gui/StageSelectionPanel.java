package org.abos.sc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.Fandom;
import org.abos.sc.core.Player;
import org.abos.sc.core.Region;
import org.abos.sc.core.Stage;
import org.abos.util.Name;
import org.abos.util.Utilities;
import org.abos.util.gui.GBCBuilder;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class StageSelectionPanel extends JPanel {
	
	public static final int PREFERRED_CB_HEIGHT = ContentComboBox.PREFERRED_HEIGHT;
	
	public static final int PREFERRED_CB_WIDTH = 2 * ContentComboBox.PREFERRED_WIDTH + 1;
	
	public static final Dimension PREFERRED_CB_SIZE = new Dimension(PREFERRED_CB_WIDTH, PREFERRED_CB_HEIGHT);
	
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
		fandomSelector.refreshContent(true);
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
		fandomSelector = new ContentComboBox<>(player.getFandoms(), Name.createNameComparator());
		fandomSelector.addItemListener(e -> refreshRegions(e));
		regionLabel = new JLabel("Region:");
		regionSelector = new ContentComboBox<>(getFandom().getRegions(), Name.createNameComparator());
		regionSelector.addItemListener(e -> refreshStages(e));
		stageLabel = new JLabel("Stage:");
		stageSelector = new ContentComboBox<>(getRegion().getStages(), Name.createNameComparator());
	}
	
	private void initLayout() {
		fandomSelector.setPreferredSize(PREFERRED_CB_SIZE);
		regionSelector.setPreferredSize(PREFERRED_CB_SIZE);
		stageSelector.setPreferredSize(PREFERRED_CB_SIZE);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GBCBuilder labelBuilder = new GBCBuilder().
				anchorDefault(GridBagConstraints.LINE_END).insetsDefault(new Insets(0, 1, 0, 2));
		GBCBuilder selectorBuilder = new GBCBuilder().
				anchorDefault(GridBagConstraints.LINE_END).weightxDefault(0.7).fillDefault(GridBagConstraints.HORIZONTAL);
		labelBuilder.gridx(leftToRight ? 0 : 1).reset();
		selectorBuilder.gridx(leftToRight ? 1 : 0).reset();
		add(fandomLabel, labelBuilder.gridy(0).get());
		add(fandomSelector, selectorBuilder.gridy(0).get());
		add(regionLabel, labelBuilder.gridy(1).get());
		add(regionSelector, selectorBuilder.gridy(1).get());
		add(stageLabel, labelBuilder.gridy(2).get());
		add(stageSelector, selectorBuilder.gridy(2).get());
	}

}
