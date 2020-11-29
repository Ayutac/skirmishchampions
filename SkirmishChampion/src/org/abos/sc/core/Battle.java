package org.abos.sc.core;

import java.util.Timer;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * Simulates a battle. Note that on construction the underlying timer will be started immediatly.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Battle extends Timer implements Runnable {
	
	/**
	 * If the timer has been cancelled. Usually means the battle has concluded.
	 * @see #isCancelled()
	 */
	protected boolean cancelled = false;

	/**
	 * First party, usually the player's.
	 * @see #party2
	 */
	protected BattleEncounter party1;
	
	/**
	 * Second party, usually the computer's.
	 * @see #party1
	 */
	protected BattleEncounter party2;
	
	protected Logger attackLogger;
	
	protected Handler attackHandler;

	/**
	 * Creates a new battle. The underlying timer will be started as a daemon immediatly upon creation, 
	 * but the attack tasks need to be sheduled seperately with {@link #run()}. 
	 * @param party1 the frst party, usually the player's
	 * @param party2 the second party, usually the computer's
	 * @param strategy1 the first party's strategy, usually the player's
	 * @param strategy2 the second party's strategy, usually the computers's
	 * @throws NullPointerException If any argument refers to <code>null</code>.
	 * @see #run()
	 */
	public Battle(BattleEncounter party1, BattleEncounter party2, Handler attackHandler) {
		super(true); // run as daemon
		Utilities.requireNonNull(party1, "party1");
		Utilities.requireNonNull(party2, "party2");
		this.party1 = party1;
		this.party2 = party2;
		this.attackHandler = attackHandler;
		attackLogger = Logger.getAnonymousLogger();
		attackLogger.setUseParentHandlers(false);
		if (attackHandler != null)
			attackLogger.addHandler(attackHandler);
	}
	
	/**
	 * Creates the attack tasks and shedules them. 
	 * 
	 * The order is: First the first's party, their first row and then the first column.
	 * @see AttackTask
	 */
	@Override
	public void run() {
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				if (party1.getCharacter(row, col) != null)
					scheduleAtFixedRate(new AttackTask(party1.getCharacter(row, col), party1.getTactic(row, col), party2.getFormation(), this,attackLogger), 
							party1.getCharacter(row, col).getAttackSpeed(), party1.getCharacter(row, col).getAttackSpeed());
		for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
			for (int col = 0; col < BattleFormation.COL_NUMBER; col++)
				if (party2.getCharacter(row, col) != null)
					scheduleAtFixedRate(new AttackTask(party2.getCharacter(row, col), party1.getTactic(row, col), party1.getFormation(), this,attackLogger), 
							party2.getCharacter(row, col).getAttackSpeed(), party2.getCharacter(row, col).getAttackSpeed());
	}
	
	/**
	 * Cancels this timer, causing all attack tasks to be discarded and thereby ends the battle. 
	 * This method is usually called by the attack tasks when there are no valid targets left, i.e. when the battle is over.
	 * @see #isCancelled()
	 * @see #waitForEnd()
	 * @see Timer#cancel()
	 * @see AttackTask#run()
	 */
	@Override
	public void cancel() {
		super.cancel();
		cancelled = true;
	}
	
	/**
	 * Returns <code>true</code> if the timer has been cancelled.
	 * @return <code>true</code> if the timer has been cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Waits for the battle to end, i.e. uses {@link Thread#onSpinWait()} in a loop until the timer has been cancelled.
	 * @see #cancel()
	 * @see #isCancelled()
	 * @see Thread#onSpinWait()
	 */
	public void waitForEnd() {
		while (!isCancelled()) {
			Thread.onSpinWait();
		}
		if (attackHandler != null)
			attackLogger.removeHandler(attackHandler);
	}
	
	public boolean party1Won() {
		return !party1.isDefeated() && party2.isDefeated();
	}
	
	public boolean party1Lost() {
		return party1.isDefeated() && !party2.isDefeated();
	}
	
	public boolean party2Won() {
		return party1Lost();
	}
	
	public boolean party2Lost() {
		return party1Won();
	}
	
	public boolean tieOccurred() {
		return party1.isDefeated() && party2.isDefeated();
	}
	
	public void restoreCombatants() {
		party1.getFormation().restoreAll();
		party1.getStrategy().reset();
		party2.getFormation().restoreAll();
		party2.getStrategy().reset();
	}
	
}
