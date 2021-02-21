package org.abos.sc.core;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Comparator;

import org.abos.util.IllegalArgumentRangeException;
import org.abos.util.ParsedIdFoundException;
import org.abos.util.ParsedIdNotFoundException;
import org.abos.util.Utilities;

/**
 * 
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see StageBase
 */
public class Stage extends StageBase {
	
	protected boolean cleared;
	
	protected boolean showChallengeRating;
	
	protected BattleEncounter encounter = null;
	
	/**
	 * 
	 * @param base
	 * @param accessable
	 * @param cleared
	 */
	public Stage(StageBase base, boolean cleared, boolean showChallengeRating) {
		super(base); // throws NPE
		this.cleared = cleared;
		this.showChallengeRating = showChallengeRating;
	}
	
	/**
	 * 
	 * @param base
	 */
	public Stage(StageBase base, boolean showChallengeRating) {
		this(base, false, showChallengeRating); // throws NPE
	}
	
	/**
	 * @return the cleared
	 */
	public boolean isCleared() {
		return cleared;
	}
	
	/**
	 * @param cleared the cleared to set
	 */
	public void setCleared(boolean cleared) {
		this.cleared = cleared;
	}
	
	/**
	 * @return the encounter
	 */
	public BattleEncounter getEncounter() {
		return encounter;
	}
	
	@Override
	public BattleEncounter createEncounter() {
		BattleEncounter encounter = super.createEncounter();
		return encounter;
	}
	
	/**
	 * Returns the challenge rating of this stage. If this stage currently has an encounter stored, its challenge rating
	 * is returned, else the challenge rating from the base class.
	 * @return the challenge rating of this stage
	 * @see StageBase#getChallengeRating()
	 */
	@Override
	public int getChallengeRating() {
		if (encounter != null) 
			return encounter.getChallengeRating();
		return super.getChallengeRating();
	}

	public boolean isEngaged() {
		return encounter != null;
	}
	
	public void engageStage() {
		if (encounter != null)
			throw new IllegalStateException("Cannot engage already engaged stage!");
		encounter = createEncounter();
	}
	
	public void disengageStage() {
		if (encounter == null)
			throw new IllegalStateException("Cannot disengage unengaged stage!");
		encounter = null;
	}
	
	public int rewardMoney(BattleConclusion conclusion) {
		Utilities.requireNonNull(conclusion, "conclusion");
		if (encounter == null) 
			throw new IllegalStateException("Encounter cannot be gone before the battle is resolved!");
		switch (conclusion) {
		case WON: 
			if (!cleared)
				return 10+2*encounter.getSize();
			return 2;
		case LOST:
			return -encounter.getSize();
		default:
			return 0;
		}
	}
	
	public Companion[] rewardCompanions(BattleConclusion conclusion) {
		Utilities.requireNonNull(conclusion, "conclusion");
		if (encounter == null) 
			throw new IllegalStateException("Encounter cannot be gone before the battle is resolved!");
		if (conclusion == BattleConclusion.WON) {
			Companion[] reward = new Companion[encounter.getSize()];
			int i = 0;
			for (int row = 0; row < BattleFormation.ROW_NUMBER; row++)
				for (int col = 0; col < BattleFormation.COL_NUMBER; col++) {
					if (encounter.getCharacter(row, col) != null) {
						reward[i] = new Companion(encounter.getCharacter(row, col));
						i++;
					}
				}
			return reward;
		}
		return new Companion[0];
	}
	
	public Stage[] rewardStages(BattleConclusion conclusion, boolean showChallengeRating) {
		Utilities.requireNonNull(conclusion, "conclusion");
		if (encounter == null) 
			throw new IllegalStateException("Encounter cannot be gone before the battle is resolved!");
		if (conclusion == BattleConclusion.WON) {
			Stage[] reward = new Stage[nextStages.length];
			// FIXME if there are unkown stages as nextStages, the lookup will result in an NPE
			// System.out.println(Arrays.toString(nextStages));
			for (int i = 0; i < reward.length; i++)
				reward[i] = new Stage(STAGES.lookup(nextStages[i]), showChallengeRating);
			return reward;
		}
		return new Stage[0];
	}
	
	public Region[] rewardRegions(BattleConclusion conclusion) {
		Utilities.requireNonNull(conclusion, "conclusion");
		if (encounter == null) 
			throw new IllegalStateException("Encounter cannot be gone before the battle is resolved!");
		if (conclusion == BattleConclusion.WON) {
			Region[] reward = new Region[nextRegions.length];
			for (int i = 0; i < reward.length; i++)
				reward[i] = new Region(RegionBase.REGIONS.lookup(nextRegions[i]));
			return reward;
		}
		return new Region[0];
	}
	
	public Fandom[] rewardFandoms(BattleConclusion conclusion) {
		Utilities.requireNonNull(conclusion, "conclusion");
		if (encounter == null) 
			throw new IllegalStateException("Encounter cannot be gone before the battle is resolved!");
		if (conclusion == BattleConclusion.WON) {
			Fandom[] reward = new Fandom[nextFandoms.length];
			for (int i = 0; i < reward.length; i++)
				reward[i] = new Fandom(FandomBase.FANDOMS.lookup(nextFandoms[i]));
			return reward;
		}
		return new Fandom[0];
	}
	
	protected void acknowledgeMoneyChange(StringBuilder message, BattleConclusion conclusion, Player player) {
		Utilities.requireNonNull(message, "message");
		Utilities.requireNonNull(conclusion, "conclusion");
		int extraGold = rewardMoney(conclusion);
		if (player == null)
			return;
		player.addMoney(extraGold);
		message.append(System.lineSeparator());
		if (extraGold > 0) {
			message.append("You recieved ");
			message.append(extraGold);
			message.append(" gold!");
		}
		else {
			message.append("You lost ");
			message.append(Math.abs(extraGold));
			message.append(" gold...");
		}
	}
	
	protected void acknowledgeExtraPointChange(StringBuilder message, BattleConclusion conclusion, Player player) {
		Utilities.requireNonNull(message, "message");
		Utilities.requireNonNull(conclusion, "conclusion");
		if (player == null)
			return;
		// base amount is stage CR + difference of stage CR - party CR
		int amount = Utilities.addWithoutOverflow(Utilities.multWithoutOverflow(2, getChallengeRating()), -player.getParty().getChallengeRating());
		amount = Math.max(0, amount); // ensure nonnegative
		if (conclusion != BattleConclusion.WON) { // if WON no division will happen
			if (conclusion == BattleConclusion.TIE)
				amount /= 2;
			else
				amount /= 100; // unexpected cases will be interpreted as loss
		}
		if (amount > 0) {
			player.addExtraPointsToParty(amount);
			message.append(System.lineSeparator());
			message.append("Party recieved ");
			message.append(amount);
			message.append(" extra points!");
		}
	}
	
	protected void acknowledgeCompanionChange(StringBuilder message, BattleConclusion conclusion, Player player) {
		Utilities.requireNonNull(message, "message");
		Utilities.requireNonNull(conclusion, "conclusion");
		Companion[] rewardCompanions = rewardCompanions(conclusion);
		if (player == null)
			return;
		if (rewardCompanions.length > 0) {
			for (Companion companion : rewardCompanions) {
				if (!player.getCompanions().contains(companion)) {
					player.getCompanions().add(companion);
					message.append(System.lineSeparator());
					message.append("Companion ");
					message.append(companion.getName());
					message.append(" joins you!");
				}
			}
		}
	}
	
	protected void acknowledgeStageChange(StringBuilder message, BattleConclusion conclusion, Player player) {
		Utilities.requireNonNull(message, "message");
		Utilities.requireNonNull(conclusion, "conclusion");
		Stage[] rewardStages = rewardStages(conclusion, Difficulty.of(player).showChallengeRatings());
		if (player == null)
			return;
		if (rewardStages.length > 0) {
			for (Stage stage : rewardStages) {
				if (!player.getStages().contains(stage)) {
					player.getStages().add(stage);
					message.append(System.lineSeparator());
					message.append("Stage ");
					message.append(stage.getName());
					message.append(" unlocked!");
				}
			}
		}
	}
	
	protected void acknowledgeRegionChange(StringBuilder message, BattleConclusion conclusion, Player player) {
		Utilities.requireNonNull(message, "message");
		Utilities.requireNonNull(conclusion, "conclusion");
		Region[] rewardRegions = rewardRegions(conclusion);
		if (player == null)
			return;
		if (rewardRegions.length > 0) {
			for (Region region : rewardRegions) {
				if (!player.getRegions().contains(region)) {
					player.getRegions().add(region);
					message.append(System.lineSeparator());
					message.append("Region ");
					message.append(region.getName());
					message.append(" unlocked!");
				}
			}
		}
	}
	
	protected void acknowledgeFandomChange(StringBuilder message, BattleConclusion conclusion, Player player) {
		Utilities.requireNonNull(message, "message");
		Utilities.requireNonNull(conclusion, "conclusion");
		Fandom[] rewardFandoms = rewardFandoms(conclusion);
		if (player == null)
			return;
		if (rewardFandoms.length > 0) {
			for (Fandom fandom : rewardFandoms) {
				if (!player.getFandoms().contains(fandom)) {
					player.getFandoms().add(fandom);
					message.append(System.lineSeparator());
					message.append("Region ");
					message.append(fandom.getName());
					message.append(" unlocked!");
				}
			}
		}
	}
	
	protected void acknowledgeRegionCompletion(StringBuilder message, Player player) {
		Utilities.requireNonNull(message, "message");
		if (player == null)
			return;
		for (Region region : player.getRegions()) {
			if (region.hasBeenCleared()) {
				message.append(System.lineSeparator());
				message.append("Region ");
				message.append(region.getName());
				message.append(" completed!");
			}
		}
	}
	
	protected void acknowledgeFandomCompletion(StringBuilder message, Player player) {
		Utilities.requireNonNull(message, "message");
		if (player == null)
			return;
		for (Fandom fandom : player.getFandoms()) {
			if (fandom.hasBeenCleared()) {
				message.append(System.lineSeparator());
				message.append("Fandom ");
				message.append(fandom.getName());
				message.append(" completed!");
			}
		}
	}
	
	protected void acknowledgeRuntimeAndDiff(StringBuilder message, Player player) {
		Utilities.requireNonNull(message, "message");
		if (player == null || !player.speedrunActive())
			return;
		Duration runDuration = player.getRunDuration();
		message.append(System.lineSeparator());
		message.append("Time: ");
		message.append(runDuration.toHours());
		message.append("h ");
		message.append(runDuration.toMinutesPart());
		message.append("min ");
		message.append(runDuration.toSecondsPart());
		message.append("s (");
		message.append(player.getDifficulty().getCapitalizedName());
		message.append(')');
	}
	
	/**
	 * Acknowledges the result of the battle by rewarding the player and building an
	 * acknowledging string.
	 */
	public void acknowledgeBattleResult(StringBuilder message, BattleConclusion conclusion, Player player) {
		if (conclusion == BattleConclusion.WON)
			message.append("You have won this battle!");
		else // not having won is interpreted as a loss, even for ties
			message.append("You have lost this battle...");
		acknowledgeMoneyChange(message, conclusion, player);
		acknowledgeExtraPointChange(message, conclusion, player);
		acknowledgeCompanionChange(message, conclusion, player);
		acknowledgeStageChange(message, conclusion, player);
		acknowledgeRegionChange(message, conclusion, player);
		acknowledgeFandomChange(message, conclusion, player);
		if (conclusion == BattleConclusion.WON)
			setCleared(true);
		if (player != null) { // update and remember which ones have been cleared
			player.updateRegionStages(false); 
			player.updateFandomRegions(false);
		}
		acknowledgeRegionCompletion(message, player);
		acknowledgeFandomCompletion(message, player);
		acknowledgeRuntimeAndDiff(message, player);
	}
	
	@Override
	public Object clone() {
		Stage clone = new Stage(this, cleared, showChallengeRating);
		if (encounter != null)
			clone.encounter = (BattleEncounter)encounter.clone();
		return clone;
	}
	
	// TODO hashCode and equals
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(getName());
		if (showChallengeRating) {
			s.append(" (CR: ");
			s.append(getChallengeRating());
			s.append(")");
		}
		if (cleared)
			s.append(" ✓");
		return s.toString();
	}
	
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		s.append(id);
		if (cleared)
			s.append('!');
	}
	
	public static Stage parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		int end = s.endsWith("!") ? s.length() - 1 : s.length();
		String id = s.substring(0, end);
		StageBase base = StageBase.STAGES.lookup(id);
		if (base == null)
			throw new ParsedIdNotFoundException(String.format("Unknown stage ID %s!", id));
		Stage stage = new Stage(base, end != s.length(), Difficulty.of(player).showChallengeRatings());
		if (player != null) {
			if (player.getStages().containsId(stage.getId()))
				throw new ParsedIdFoundException("Stage "+stage.getId()+" already registered with this player!");
			player.getStages().add(stage);
		}
		return stage;
	}

}
