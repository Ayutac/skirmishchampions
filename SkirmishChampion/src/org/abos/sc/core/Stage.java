package org.abos.sc.core;

import java.util.logging.Handler;

import javax.swing.JOptionPane;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Stage extends StageBase {
	
	protected boolean accessable;
	
	protected boolean cleared;
	
	protected BattleEncounter encounter = null;
	
	/**
	 * 
	 * @param base
	 * @param accessable
	 * @param cleared
	 */
	public Stage(StageBase base, boolean accessable, boolean cleared) {
		super(base); // throws NPE
		this.accessable = accessable;
		this.cleared = cleared;
	}
	
	/**
	 * 
	 * @param base
	 */
	public Stage(StageBase base) {
		this(base, true, false); // throws NPE
	}
	
	/**
	 * @return the accessable
	 */
	public boolean isAccessable() {
		return accessable;
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

	public void engageStage() {
		encounter = createEncounter();
	}
	
	public void disengageStage() {
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
	
	public Stage[] rewardStages(BattleConclusion conclusion) {
		Utilities.requireNonNull(conclusion, "conclusion");
		if (encounter == null) 
			throw new IllegalStateException("Encounter cannot be gone before the battle is resolved!");
		if (conclusion == BattleConclusion.WON) {
			Stage[] reward = new Stage[nextStages.length];
			for (int i = 0; i < reward.length; i++)
				reward[i] = new Stage(STAGES.lookup(nextStages[i]));
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
		if (extraGold > 0)
			message.append(String.format("You recieved %d gold!", extraGold));
		else 
			message.append(String.format("You lost %d gold...", Math.abs(extraGold)));
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
		Stage[] rewardStages = rewardStages(conclusion);
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
		acknowledgeCompanionChange(message, conclusion, player);
		acknowledgeStageChange(message, conclusion, player);
		acknowledgeRegionChange(message, conclusion, player);
		acknowledgeFandomChange(message, conclusion, player);
		if (player != null) {
			player.updateRegionStages();
			player.updateFandomRegions();
		}
		if (conclusion == BattleConclusion.WON)
			setCleared(true);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		if (accessable)
			s.append('?');
		s.append(id);
		if (cleared)
			s.append('!');
	}
	
	public static Stage parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		int start = s.startsWith("?") ? 1 : 0;
		int end = s.endsWith("!") ? s.length() - 1 : s.length();
		String id = s.substring(start, end);
		StageBase base = StageBase.STAGES.lookup(id);
		if (base == null)
			throw new IllegalArgumentRangeException(String.format("Unknown stage ID %s!", id));
		Stage stage = new Stage(base, start != 0, end != s.length());
		if (player != null)
			player.getStages().add(stage);
		return stage;
	}

}
