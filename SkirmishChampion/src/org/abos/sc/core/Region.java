package org.abos.sc.core;

import org.abos.util.ParsedIdFoundException;
import org.abos.util.ParsedIdNotFoundException;
import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Region extends RegionBase {
	
	/**
	 * The number of all stages associated with this region.
	 */
	protected final int allStagesCount;
	
	protected boolean cleared;
	
	protected boolean clearedFlag = false;
	
	protected final Registry<Stage> stages;

	private Region(RegionBase base, boolean cleared, Registry<Stage> stages) {
		super(base);
		Utilities.requireNonNull(stages, "stages");
		this.cleared = cleared;
		this.stages = stages;
		allStagesCount = collectAssociatedStageIds().size();
	}
	
	public Region(RegionBase base) {
		this(base, false, new Registry<>());
	}
	
	public boolean isCleared() {
		return cleared;
	}
	
	public boolean hasBeenCleared() {
		if (!clearedFlag)
			return false;
		clearedFlag = false;
		return true;
	}
	
	public boolean checkCleared() {
		boolean oldCleared = cleared;
		if (allStagesCount != stages.size()) {
			cleared = false;
		}
		else {
			boolean check = true;
			for (Stage stage : stages)
				check &= stage.isCleared();
			cleared = check;
		}
		clearedFlag = oldCleared != cleared;
		return cleared;
	}
	
	/**
	 * @return the stages
	 */
	public Registry<Stage> getStages() {
		return stages;
	}
	
	public void updateStages(Iterable<? extends Stage> stages) {
		this.stages.clear();
		for (Stage stage : stages) {
			if (stage.getRegionId().equals(id))
				this.stages.add(stage);
		}
		checkCleared();
	}
	
	@Override
	public Object clone() {
		return new Region(this, cleared, Registry.deepClone(stages));
	}
	
	@Override
	public String toString() {
		return cleared ? getName() + " ✓" : getName();
	}
	
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		s.append(id);
	}
	
	public static Region parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		RegionBase base = RegionBase.REGIONS.lookup(s);
		if (base == null)
			throw new ParsedIdNotFoundException(String.format("Unknown region ID %s!", s));
		Region region = new Region(base);
		if (player != null) {
			if (player.getRegions().containsId(region.getId()))
				throw new ParsedIdFoundException("Region "+region.getId()+" already registered with this player!");
			player.getRegions().add(region);
		}
		return region;
	}
	
}
