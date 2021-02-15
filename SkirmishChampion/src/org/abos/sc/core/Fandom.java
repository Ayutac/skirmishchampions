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
public class Fandom extends FandomBase {
	
	protected final int allRegionsCount;
	
	protected boolean cleared;
	
	protected boolean clearedFlag = false;
	
	protected final Registry<Region> regions;

	public Fandom(FandomBase base, Registry<Region> regions) {
		super(base);
		Utilities.requireNonNull(regions, "regions");
		this.regions = regions;
		allRegionsCount = collectAssociatedRegionIds().size();
	}
	
	public Fandom(FandomBase base) {
		this(base, new Registry<>());
	}
	
	/**
	 * @return the cleared
	 */
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
		if (allRegionsCount != regions.size()) {
			cleared = false;
		}
		else {
			boolean check = true;
			for (Region region : regions)
				check &= region.isCleared();
			cleared = check;
		}
		clearedFlag = oldCleared != cleared;
		return cleared;
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
		checkCleared();
	}
	
	@Override
	public Object clone() {
		return new Fandom(this, Registry.deepClone(regions));
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
	
	public static Fandom parse(String s, Player player) {
		Utilities.requireNonNull(s, "s");
		FandomBase base = FandomBase.FANDOMS.lookup(s);
		if (base == null)
			throw new ParsedIdNotFoundException("Fandom ID + "+s+" couldn't be found in the fandom registry!");
		Fandom fandom = new Fandom(base);
		if (player != null) {
			if (player.getFandoms().containsId(fandom.getId()))
				throw new ParsedIdFoundException("Fandom "+fandom.getId()+" already registered with this player!");
			player.getFandoms().add(fandom);
		}
		return fandom;
	}
	
}
