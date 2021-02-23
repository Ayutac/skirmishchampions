package org.abos.sc.core.battle;

import java.time.Instant;
import java.util.Timer;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.abos.sc.core.Difficulty;
import org.abos.util.Utilities;

/**
 * Simulates a battle. Note that the underlying timer will be started immediatly upon creation. 
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see #Battle(Encounter, Encounter, Handler)
 * @see #run()
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
	protected Encounter party1;
	
	/**
	 * Second party, usually the computer's.
	 * @see #party1
	 */
	protected Encounter party2;
	
	/**
	 * The difficulty for this battle.
	 */
	protected Difficulty difficulty;
	
	/**
	 * logger for the battle
	 */
	protected Logger battleLogger;
	
	/**
	 * The primary handler for the logger. The handler is saved internally so it
	 * can be removed once the battle is over.
	 * @see #Battle(Encounter, Encounter, Handler)
	 * @see #waitForEnd()
	 */
	protected Handler battleHandler;

	/**
	 * Creates a new battle. The underlying timer will be started as a daemon immediatly upon creation, 
	 * but the attack tasks need to be sheduled seperately with {@link #run()}. 
	 * @param party1 the first party, usually the player's
	 * @param party2 the second party, usually the computer's
	 * @param difficulty the difficulty for this battle
	 * @param battleHandler the primary handler for this battle's logger
	 * @throws NullPointerException If <code>party1</code>, <code>party2</code> or <code>difficulty</code> refers to <code>null</code>.
	 * @see #run()
	 */
	public Battle(Encounter party1, Encounter party2, Difficulty difficulty, Handler battleHandler) {
		super("Battle created "+Instant.now().toString(), true); // run as daemon
		Utilities.requireNonNull(party1, "party1");
		Utilities.requireNonNull(party2, "party2");
		Utilities.requireNonNull(difficulty, "difficulty");
		this.party1 = party1;
		this.party2 = party2;
		this.difficulty = difficulty;
		this.battleHandler = battleHandler;
		battleLogger = Logger.getAnonymousLogger();
		battleLogger.setUseParentHandlers(false);
		if (battleHandler != null)
			battleLogger.addHandler(battleHandler);
	}
	
	/**
	 * Creates the attack tasks and shedules them. 
	 * 
	 * The order is: First the first's party, their first row and then the first column.
	 * @see AttackTask
	 * @see #cancel()
	 * @see #waitForEnd()
	 */
	@Override
	public void run() {
		for (int row = 0; row < Formation.ROW_NUMBER; row++)
			for (int col = 0; col < Formation.COL_NUMBER; col++)
				if (party1.getCharacter(row, col) != null)
					scheduleAtFixedRate(new AttackTask(party1.getCharacter(row, col), party1.getTactic(row, col), party2.getFormation(), this, battleLogger, difficulty, true), 
							party1.getCharacter(row, col).getAttackSpeed(), party1.getCharacter(row, col).getAttackSpeed());
		for (int row = 0; row < Formation.ROW_NUMBER; row++)
			for (int col = 0; col < Formation.COL_NUMBER; col++)
				if (party2.getCharacter(row, col) != null)
					scheduleAtFixedRate(new AttackTask(party2.getCharacter(row, col), party2.getTactic(row, col), party1.getFormation(), this, battleLogger, difficulty, false), 
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
	 * @see #cancel()
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Waits for the battle to end, i.e. uses {@link Thread#onSpinWait()} in a loop until the timer has been cancelled.
	 * @see #run()
	 * @see #cancel()
	 * @see #isCancelled()
	 * @see Thread#onSpinWait()
	 */
	public void waitForEnd() {
		while (!isCancelled()) {
			Thread.onSpinWait();
		}
		if (battleHandler != null)
			battleLogger.removeHandler(battleHandler);
	}
	
	/**
	 * Returns <code>true</code> if the first party is not defeated but the second party is.
	 * @return <code>true</code> if the first party is not defeated but the second party is, else <code>false</code>.
	 * @see #party1Lost()
	 * @see #party2Won()
	 * @see #party2Lost()
	 * @see #tieOccurred()
	 */
	public boolean party1Won() {
		return !party1.isDefeated() && party2.isDefeated();
	}
	
	/**
	 * Returns <code>true</code> if the first party is defeated but the second party is not.
	 * @return <code>true</code> if the first party is defeated but the second party is not, else <code>false</code>.
	 * @see #party1Won()
	 * @see #party2Won()
	 * @see #party2Lost()
	 * @see #tieOccurred()
	 */
	public boolean party1Lost() {
		return party1.isDefeated() && !party2.isDefeated();
	}
	
	/**
	 * Returns <code>true</code> if the second party is not defeated but the first party is.
	 * @return <code>true</code> if the second party is not defeated but the first party is, else <code>false</code>.
	 * @see #party1Won()
	 * @see #party1Lost()
	 * @see #party2Lost()
	 * @see #tieOccurred()
	 */
	public boolean party2Won() {
		return party1Lost();
	}
	
	/**
	 * Returns <code>true</code> if the second party is defeated but the first party is not.
	 * @return <code>true</code> if the second party is defeated but the first party is not, else <code>false</code>.
	 * @see #party1Won()
	 * @see #party1Lost()
	 * @see #party2Won()
	 * @see #tieOccurred()
	 */
	public boolean party2Lost() {
		return party1Won();
	}
	
	/**
	 * Returns <code>true</code> if both parties are defeated.
	 * @return <code>true</code> if both parties are defeated, else <code>false</code>.
	 * @see #party1Won()
	 * @see #party1Lost()
	 * @see #party2Won()
	 * @see #party2Lost()
	 */
	public boolean tieOccurred() {
		return party1.isDefeated() && party2.isDefeated();
	}
	
	/**
	 * Restores the formations and resets the strategies.
	 * @see Formation#restoreAll()
	 * @see Strategy#reset()
	 */
	public void restoreCombatants() {
		party1.getFormation().restoreAll();
		party1.getStrategy().reset();
		party2.getFormation().restoreAll();
		party2.getStrategy().reset();
	}
	
}
