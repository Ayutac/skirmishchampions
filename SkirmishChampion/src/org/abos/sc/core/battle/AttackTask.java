package org.abos.sc.core.battle;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.abos.sc.core.Character;
import org.abos.sc.core.Difficulty;
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
	protected Tactic tactic;
	
	/**
	 * The group of enemies to attack.
	 */
	protected Formation enemies;
	
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
	 * The difficulty setting for this task.
	 */
	protected Difficulty difficulty;
	
	protected boolean challenger;

	/**
	 * Creates a new attack task with the given specifications.
	 * @param character the character which attacks, not <code>null</code>
	 * @param tactic the tactic the character employs, not <code>null</code>
	 * @param enemies the group of enemies to attack, not <code>null</code>
	 * @param timer The associated timer to stop once this character runs out of targets/enemies. If <code>null</code>, this task will not stop by itself.
	 * @param attackLogger a logger to record the attacks occuring here, not <code>null</code>
	 * @param difficulty the difficulty setting for this task
	 * @throws NullPointerException If any parameter except <code>timer</code> refers to <code>null</code>.
	 */
	public AttackTask(Character character, Tactic tactic, Formation enemies, Timer timer, Logger attackLogger, Difficulty difficulty, boolean challenger) {
		Utilities.requireNonNull(character, "character");
		Utilities.requireNonNull(tactic, "tactic");
		Utilities.requireNonNull(enemies, "enemies");
		Utilities.requireNonNull(attackLogger, "attackLogger");
		Utilities.requireNonNull(difficulty, "difficulty");
		this.character = character;
		this.tactic = tactic;
		this.enemies = enemies;
		this.timer = timer;
		this.attackLogger = attackLogger;
		this.difficulty = difficulty;
		this.challenger = challenger;
	}
	
	// TODO put this somewhere else
	public String getLineColor() {
		return challenger ? "green" : "red";
	}
	
	public String getLineColorDefeat() {
		return challenger ? "#006400" : "#8B0000";
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
		// TODO put the style somewhere else
		attackLogger.info(() -> difficulty.showCharacterHealth()
			? String.format("<span style=\"color:%s\">%s %s %s and dealt <b>%d</b> damage.</span>", 
				getLineColor(), character.getName(), character.getDamageStat().getAttackVerb(), enemy.getName(), character.getAttackPower())
			: String.format("<span style=\"color:%s\">%s %s %s.</span>", 
				getLineColor(), character.getName(), character.getDamageStat().getAttackVerb(), enemy.getName()));
		if (enemy.isDefeated())
			attackLogger.info(() -> String.format("<span style=\"color:%s;text-decoration:underline\">%s was defeated!</span>", 
				getLineColorDefeat(), enemy.getName()));
	}

}
