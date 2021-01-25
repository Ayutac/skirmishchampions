package org.abos.sc.core;

import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Fandom extends FandomBase {
	
	protected final Registry<Region> regions = new Registry<>();

	/**
	 * 
	 */
	public Fandom(FandomBase base) {
		super(base);
	}
	
	/**
	 * @return the regions
	 */
	public Registry<Region> getRegions() {
		return regions;
	}
	
	public Companion getStartCompanion() {
		return new Companion(CharacterBase.CHARACTERS.lookup(startCompanionId));
	}
	
	public void updateRegions(Iterable<? extends Region> regions) {
		this.regions.clear();
		for (Region region : regions) {
			if (region.getFandomId().equals(id))
				this.regions.add(region);
		}
	}
	
	// TODO clone is missing
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		s.append(id);
	}
	
	public static Fandom parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		FandomBase base = FandomBase.FANDOMS.lookup(s);
		Fandom fandom = new Fandom(base);
		if (player != null)
			player.getFandoms().add(fandom);
		return fandom;
	}
	
}
