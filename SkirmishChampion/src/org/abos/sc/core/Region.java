package org.abos.sc.core;

import org.abos.util.ParsedIdFoundException;
import org.abos.util.ParsedIdNotFoundException;
import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * This subclass of {@link RegionBase} saves additional information of a region that is only relevant as
 * a collectable for the player, e.g. if it has been cleared.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Region extends RegionBase {
	
	/**
	 * The number of all stages associated with this region.
	 */
	protected final int allStagesCount;
	
	/**
	 * If this region has been cleared.
	 * @see #isCleared()
	 */
	protected boolean cleared;
	
	/**
	 * A flag for notification that this region has been cleared.
	 * @see #isCleared()
	 * @see #hasBeenCleared()
	 */
	protected boolean clearedFlag = false;
	
	/**
	 * The stages accessable via this region.
	 * @see #getStages()
	 */
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
	
	/**
	 * Tells if this region is cleared / completed.
	 * @return <code>true</code> if this region is cleared, else <code>false</code>.
	 * @see #hasBeenCleared()
	 * @see #checkCleared()
	 */
	public boolean isCleared() {
		return cleared;
	}
	
	/**
	 * Tells if this region has recently been cleared / completed. This method can only return
	 * <code>true</code> if {@link #checkCleared()} cleared has been called and after calling this method
	 * once, any subsequent calls will return <code>false</code>.
	 * @return <code>true</code> if this region has recently been cleared, else false
	 * @see #isCleared()
	 * @see #checkCleared()
	 */
	public boolean hasBeenCleared() {
		if (!clearedFlag)
			return false;
		clearedFlag = false;
		return true;
	}
	
	/**
	 * Checks if this region has been completed, i.e. if all of its stages have been cleared.
	 * Only after this method has been called can {@link #hasBeenCleared()} return <code>true</code>.
	 * @return <code>true</code> if this region has been cleared, else <code>false</code>. 
	 * The return value of this method can also be called with {@link #isCleared()}.
	 * @see #isCleared()
	 * @see #hasBeenCleared()
	 */
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
	
	/**
	 * Changes the stages associated to this region to all of those of the
	 * specified that belong to this region. Afterwards checks if this region has been cleared.
	 * @param stages the stages to consider for adding
	 * @throws NullPointerException If <code>regions</code> refers to <code>null</code>.
	 * @see #checkCleared()
	 */
	public void updateStages(Iterable<? extends Stage> stages) {
		Utilities.requireNonNull(stages, "stages");
		this.stages.clear();
		for (Stage stage : stages) {
			if (stage.getRegionId().equals(id))
				this.stages.add(stage);
		}
		checkCleared();
	}
	
	/**
	 * Returns a deep copy of this region, including cloning the associated stages.
	 */
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
