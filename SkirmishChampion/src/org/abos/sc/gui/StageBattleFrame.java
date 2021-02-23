package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.logging.LogRecord;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import org.abos.sc.core.ChallengeRatable;
import org.abos.sc.core.Difficulty;
import org.abos.sc.core.Player;
import org.abos.sc.core.Stage;
import org.abos.sc.core.battle.Battle;
import org.abos.sc.core.battle.Conclusion;
import org.abos.sc.core.battle.Encounter;
import org.abos.util.gui.GUIUtilities;
import org.abos.util.gui.TextAreaHandler;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class StageBattleFrame extends JFrame {

	public static final String TITLE = "Stage Battle";
	
	protected Player player = null;
	
	protected Stage stage = null;
	
	protected Battle battle = null;
	
	protected Comparator<Encounter> crComparator = ChallengeRatable.createCRComparator();
	
	protected Runnable afterHiding = null;
	
	protected JLabel stageLabel;
	
	protected EncounterInfoPanel firstParty;
	
	protected EncounterInfoPanel secondParty;
	
	protected JEditorPane battleLog;
	
	protected JScrollPane battleLogWrapper;
	
	protected TextAreaHandler handler;
	
	protected JButton fightButton;
	
	protected JButton returnButton;
	
	/**
	 * @throws HeadlessException
	 */
	public StageBattleFrame() throws HeadlessException {
		super(TITLE);
		initComponents();
		initLayout();
		if (!GUIUtilities.LOGOS.isEmpty())
			setIconImages(GUIUtilities.LOGOS);
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
		if (firstParty != null)
			firstParty.setDifficulty(Difficulty.of(player));
		if (secondParty != null)
			secondParty.setDifficulty(Difficulty.of(player));
	}
	
	public Stage getStage() {
		return stage;
	}
	
	// note that setting the stage does NOT set the encounter of the second party
	public void setStage(Stage stage) {
		this.stage = stage;
		if (stage == null)
			stageLabel.setText("");
		else
			stageLabel.setText(stage.getName());
	}
	
	public void setFirstParty(Encounter encounter) {
		if (battle == null)
			this.firstParty.setEncounter(encounter);
	}
	
	public void setSecondParty(Encounter encounter) {
		if (battle == null)
			this.secondParty.setEncounter(encounter);
	}
	
	public void refreshHealth() {
		firstParty.refreshHealth();
		secondParty.refreshHealth();
	}
	
	public void commenceBattle() {
		Encounter be1 = firstParty.getEncounter();
		Encounter be2 = secondParty.getEncounter();
		// valid encounters required, as well as no battle going on
		if (be1 == null || be2 == null || battle != null)
			return;
		// maybe stop player from loosing
		if (Difficulty.of(player).warnWeakTeam() && be1.getChallengeRating()-be2.getChallengeRating() <= Difficulty.WEAK_TRESHOLD) {
			if (JOptionPane.showConfirmDialog(this, "Your team seems to be a bit weak against this encounter. Do you really want to fight?", 
					"You sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
				return;
		}
		// maybe stop player from steam rolling
		if (Difficulty.of(player).stopSteamrolling() && be1.getChallengeRating() > Difficulty.of(player).getChallengeRatingCap(be2.getChallengeRating())) {
			JOptionPane.showMessageDialog(this, "Your teams seems to be a bit too powerful against this encounter, doesn't it?"+System.lineSeparator()+"Let's not steamroll through the entire game, okay?", 
					"This is unfair to them", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// actually start the fight
		fightButton.setEnabled(false);
		returnButton.setEnabled(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		battle = new Battle(be1, be2, Difficulty.of(player), handler);
		battle.run();
		new Thread(new Runnable() {
			@Override public void run() {
				battle.waitForEnd();
				acknowledgeBattleResult();
				returnButton.setEnabled(true);
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
		}, "WaitsForBattleToEnd").start();
	}
	
	/**
	 * Acknowledges the result of the battle. Only call this method after {@link Battle#waitForEnd()}
	 * has returned, else the behaviour of this method is undefined.
	 */
	public void acknowledgeBattleResult() {
		StringBuilder message = new StringBuilder();
		if (battle.party1Won()) {
			stage.acknowledgeBattleResult(message, Conclusion.WON, player);
			JOptionPane.showMessageDialog(this, message.toString(), "You won!", JOptionPane.INFORMATION_MESSAGE);
		}
		else { // not having won is interpreted as a loss, even for ties
			stage.acknowledgeBattleResult(message, Conclusion.LOST, player);
			JOptionPane.showMessageDialog(this, message.toString(), "You lost...", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void returnFromBattle() {
		setVisible(false, false);
		if (battle != null)
			battle.restoreCombatants();
		battle = null;
		setFirstParty(null);
		setSecondParty(null);
		battleLog.setText(null);
	}
	
	/**
	 * @param afterHiding the action to execute
	 */
	public void setAfterHiding(Runnable afterHiding) {
		this.afterHiding = afterHiding;
	}
	
	
	public void setVisible(boolean b, boolean returnFromBattle) {
		if (!b) // re-enable on return only
			fightButton.setEnabled(true);
		super.setVisible(b);
		if (b)
			requestFocus();
		else {
			if (returnFromBattle)
				returnFromBattle();
			if (afterHiding != null)
				afterHiding.run();
		}
	}
	
	@Override
	public void setVisible(boolean b) {
		setVisible(b, true);
	}
	
	private void initComponents() {
		stageLabel = new JLabel();
		firstParty = new EncounterInfoPanel(true);
		secondParty = new EncounterInfoPanel(false);
		battleLog = GUIUtilities.createEmptyHtmlPane();
		battleLog.setEditable(false);
		// the follwing two lines enable autoscrolling down
		DefaultCaret caret = (DefaultCaret)battleLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		battleLog.setText(null); // needed so autoscrolling works on first time
		battleLogWrapper = new JScrollPane(battleLog);
		handler = new TextAreaHandler(battleLog) {
			@Override public void publish(LogRecord record) {
				super.publish(record);
				refreshHealth();
			}
		};
		fightButton = new JButton("Fight");
		fightButton.addActionListener(e -> commenceBattle());
		returnButton = new JButton("Return");
		returnButton.addActionListener(e -> returnFromBattle());
	}
	
	private void initLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		JPanel labelCenter = new JPanel();
		labelCenter.setPreferredSize(ContentComboBox.PREFERRED_SIZE);
		labelCenter.add(stageLabel, SwingConstants.CENTER);
		add(labelCenter, BorderLayout.PAGE_START);
		add(firstParty, BorderLayout.LINE_START);
		add(secondParty, BorderLayout.LINE_END);
		battleLogWrapper.setPreferredSize(firstParty.getPreferredSize());
		add(battleLogWrapper, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.add(fightButton);
		buttonPanel.add(returnButton);
		add(buttonPanel, BorderLayout.PAGE_END);
		//setPreferredSize(new Dimension(battleLogWrapper.getPreferredSize().width+2*ContentComboBox.WI, height));
		pack();
		setLocationRelativeTo(null);
	}

}
