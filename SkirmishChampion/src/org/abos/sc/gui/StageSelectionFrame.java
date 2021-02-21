package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.abos.sc.core.Difficulty;
import org.abos.sc.core.Player;
import org.abos.sc.core.Stage;
import org.abos.sc.core.battle.Encounter;
import org.abos.sc.core.battle.Formation;
import org.abos.sc.core.battle.Strategy;
import org.abos.util.Utilities;
import org.abos.util.gui.GUIUtilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class StageSelectionFrame extends JFrame {

	public static final String TITLE = "Stage Selection";
	
	protected Player player;
	
	protected Runnable afterHiding = null;
	
	protected boolean leftToRight;
	
	protected StageSelectionPanel selectionPanel;
	
	protected PartySelectionFrame partySelectionFrame;
	
	protected JButton partySelectionButton;
	
	protected StageBattleFrame stageBattleFrame;
	
	protected JButton stageBattleButton;
	
	protected JButton returnButton;
	
	/**
	 * @throws HeadlessException
	 */
	public StageSelectionFrame(Player player, boolean leftToRight) throws HeadlessException {
		super(TITLE);
		Utilities.requireNonNull(player, "player");
		this.player = player;
		this.leftToRight = leftToRight;
		initComponents();
		initLayout();
		if (!GUIUtilities.LOGOS.isEmpty())
			setIconImages(GUIUtilities.LOGOS);
	}
	
	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		Utilities.requireNonNull(player, "player");
		if (player.equals(this.player))
			return;
		this.player = player;
		selectionPanel.setPlayer(player);
		partySelectionFrame.setCompanionPool(player.getCompanions());
		stageBattleFrame.setPlayer(player);
	}
	
	/**
	 * @param afterHiding the action to execute
	 */
	public void setAfterHiding(Runnable afterHiding) {
		this.afterHiding = afterHiding;
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b)
			requestFocus();
		else {
			if (afterHiding != null)
				afterHiding.run();
		}
	}
	
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		if (b)
			requestFocus();
	}
	
	@Override
	public void dispose() {
		setEnabled(false);
		partySelectionFrame.dispose();
		stageBattleFrame.dispose();
		super.dispose();
	}
	
	public void showSubframe(JFrame subframe) {
		if (subframe == null)
			throw new NullPointerException("subframe must be specified!");
		this.setEnabled(false);
		subframe.setVisible(true);
	}
	
	public void showPartySelectionFrame() {
		partySelectionFrame.setFormation(player.getParty());
		if (player.getDifficulty().stopSteamrolling())
			partySelectionFrame.setStageChallengeRatingCap(Difficulty.of(player).getChallengeRatingCap(selectionPanel.getStage().getChallengeRating()));
		else
			partySelectionFrame.setStageChallengeRatingCap(null);
		showSubframe(partySelectionFrame);
	}
	
	public void afterHidingSubframe() {
		setEnabled(true);
	}
	
	public void afterHidingPartySelectionFrame() {
		player.setParty(partySelectionFrame.getFormation());
		afterHidingSubframe();
	}
	
	public void afterBattle() {
		if (selectionPanel.getStage() != null && selectionPanel.getStage().isEngaged())
			selectionPanel.getStage().disengageStage();
		selectionPanel.refreshSelectors();
		afterHidingSubframe();
	}
	
	public void prepareBattle() {
		Stage stage = selectionPanel.getStage();
		stageBattleFrame.setStage(stage);
		stage.engageStage();
		stageBattleFrame.setFirstParty(new Encounter((Formation)player.getParty().clone(), Strategy.createConcentratedAssault()));
		stageBattleFrame.setSecondParty(stage.getEncounter());
		showSubframe(stageBattleFrame);
	}
	
	private void initComponents() {
		selectionPanel = new StageSelectionPanel(player,leftToRight);
		partySelectionFrame = new PartySelectionFrame(player.getCompanions());
		partySelectionFrame.setAfterHiding(() -> afterHidingPartySelectionFrame());
		partySelectionButton = new JButton("Party");
		partySelectionButton.addActionListener(e -> showPartySelectionFrame());
		stageBattleFrame = new StageBattleFrame();
		stageBattleFrame.setPlayer(player);
		stageBattleFrame.setAfterHiding(() -> afterBattle());
		stageBattleButton = new JButton("Fight");
		stageBattleButton.addActionListener(e -> prepareBattle());
		returnButton = new JButton("Return");
		returnButton.addActionListener(e -> setVisible(false));
	}
	
	private void initLayout() {
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.add(partySelectionButton);
		buttonPanel.add(stageBattleButton);
		buttonPanel.add(returnButton);
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		add(selectionPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

}
