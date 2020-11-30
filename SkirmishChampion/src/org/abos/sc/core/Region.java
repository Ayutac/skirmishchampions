package org.abos.sc.core;

import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Region extends RegionBase {
	
	protected final Registry<Stage> stages = new Registry();

	public Region(RegionBase base) {
		super(base);
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
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		s.append(id);
	}
	
	public static Region parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		RegionBase base = RegionBase.REGIONS.lookup(s);
		Region region = new Region(base);
		if (player != null)
			player.getRegions().add(region);
		return region;
	}
	
}
