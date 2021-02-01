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
	
	protected final Registry<Stage> stages;

	private Region(RegionBase base, Registry<Stage> stages) {
		super(base);
		this.stages = stages;
	}
	
	public Region(RegionBase base) {
		this(base, new Registry<>());
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
	public Object clone() {
		return new Region(this, Registry.deepClone(stages));
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
