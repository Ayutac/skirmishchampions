package org.abos.sc.core;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.abos.util.Utilities;

/**
 * A timer task to manage attacks of a single character.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see #run()
 * @see Battle#run()
 */
public class AttackTask extends TimerTask {
	
	/**
	 * The character which attacks.
	 */
	protected Character character;
	
	/**
	 * The tactic the character employs.
	 */
	protected BattleTactic tactic;
	
	/**
	 * The group of enemies to attack.
	 */
	protected BattleFormation enemies;
	
	/**
	 * The associated timer to stop once this character runs out of targets/enemies.
	 * @see #run()
	 */
	protected Timer timer;
	
	/**
	 * A logger to record the attacks occuring here.
	 */
	protected Logger attackLogger;

	/**
	 * Creates a new attack task with the given specifications.
	 * @param character the character which attacks, not <code>null</code>
	 * @param tactic the tactic the character employs, not <code>null</code>
	 * @param enemies the group of enemies to attack, not <code>null</code>
	 * @param timer The associated timer to stop once this character runs out of targets/enemies. If <code>null</code>, this task will not stop by itself.
	 * @param attackLogger a logger to record the attacks occuring here, not <code>null</code>
	 * @throws NullPointerException If any parameter except <code>timer</code> refers to <code>null</code>.
	 */
	public AttackTask(Character character, BattleTactic tactic, BattleFormation enemies, Timer timer, Logger attackLogger) {
		Utilities.requireNonNull(character, "character");
		Utilities.requireNonNull(tactic, "tactic");
		Utilities.requireNonNull(enemies, "enemies");
		Utilities.requireNonNull(attackLogger, "attackLogger");
		this.character = character;
		this.tactic = tactic;
		this.enemies = enemies;
		this.timer = timer;
		this.attackLogger = attackLogger;
	}

	/**
	 * Executes one attack. If the attacking character is defeated or no valid targets are left, returns without dealing damage.
	 * If no targets are left, the associated timer is stopped as well, if one was provided.
	 * @see #timer
	 * @see Character#getAttackPower()
	 * @see Character#getDamageStat()
	 * @see Character#dealDamage(int, StatsSecondary)
	 */
	@Override
	public void run() {
		if (character.isDefeated()) {
			cancel();
			return;
		}
		while (tactic.hasTarget() && enemies.isDefeated(tactic.getCurrentTargetRow(), tactic.getCurrentTargetCol())) {
			tactic.nextTarget();
		}
		if (!tactic.hasTarget()) {
			if (timer != null)
				timer.cancel();
			return;
		}
		Character enemy = enemies.getCharacter(tactic.getCurrentTargetRow(), tactic.getCurrentTargetCol());
		enemy.dealDamage(character.getAttackPower(), character.getDamageStat());
		attackLogger.info(() -> character.getName()+" attacked "+enemy.getName()+" and did "+character.getAttackPower()+" damage.");
	}

}
