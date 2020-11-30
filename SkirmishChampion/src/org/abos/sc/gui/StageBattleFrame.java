package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import org.abos.sc.core.Battle;
import org.abos.sc.core.BattleConclusion;
import org.abos.sc.core.BattleEncounter;
import org.abos.sc.core.Player;
import org.abos.sc.core.Stage;

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
	
	protected Runnable afterHiding = null;
	
	protected JLabel stageLabel;
	
	protected EncounterInfoPanel firstParty;
	
	protected EncounterInfoPanel secondParty;
	
	protected JTextArea battleLog;
	
	protected JScrollPane battleLogWrapper;
	
	protected PaneHandler handler;
	
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
	
	public void setFirstParty(BattleEncounter encounter) {
		if (battle == null)
			this.firstParty.setEncounter(encounter);
	}
	
	public void setSecondParty(BattleEncounter encounter) {
		if (battle == null)
			this.secondParty.setEncounter(encounter);
	}
	
	public void refreshHealth() {
		firstParty.refreshHealth();
		secondParty.refreshHealth();
	}
	
	public void commenceBattle() {
		if (firstParty.getEncounter() == null || secondParty.getEncounter() == null || battle != null)
			return;
		fightButton.setEnabled(false);
		returnButton.setEnabled(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		battle = new Battle(firstParty.getEncounter(), secondParty.getEncounter(), handler);
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
			stage.acknowledgeBattleResult(message, BattleConclusion.WON, player);
			JOptionPane.showMessageDialog(this, message.toString(), "You won!", JOptionPane.INFORMATION_MESSAGE);
		}
		else { // not having won is interpreted as a loss, even for ties
			stage.acknowledgeBattleResult(message, BattleConclusion.LOST, player);
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
		battleLog = new JTextArea() {
			private static final long serialVersionUID = 2598439816814944789L;
			@Override public void append(String str) {
				super.append(str);
				refreshHealth();
			};
		};
		battleLog.setEditable(false);
		// the follwing two lines enable autoscrolling down
		DefaultCaret caret = (DefaultCaret)battleLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		battleLogWrapper = new JScrollPane(battleLog);
		handler = new PaneHandler(battleLog);
		fightButton = new JButton("Fight");
		fightButton.addActionListener(e -> commenceBattle());
		returnButton = new JButton("Return");
		returnButton.addActionListener(e -> returnFromBattle());
	}
	
	private void initLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		JPanel labelCenter = new JPanel();
		labelCenter.add(stageLabel, SwingConstants.CENTER);
		add(labelCenter, BorderLayout.PAGE_START);
		add(firstParty, BorderLayout.LINE_START);
		add(secondParty, BorderLayout.LINE_END);
		add(battleLogWrapper, BorderLayout.CENTER);
		battleLogWrapper.setPreferredSize(firstParty.getPreferredSize());
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.add(fightButton);
		buttonPanel.add(returnButton);
		add(buttonPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

}
