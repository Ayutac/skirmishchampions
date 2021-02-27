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
	
	/**
	 * The number of all regions associated with this fandom.
	 */
	protected final int allRegionsCount;
	
	/**
	 * If this fandom has been cleared.
	 * @see #isCleared()
	 */
	protected boolean cleared;
	
	/**
	 * A flag for notification that this fandom has been cleared.
	 * @see #isCleared()
	 * @see #hasBeenCleared()
	 */
	protected boolean clearedFlag = false;
	
	/**
	 * The regions accessable via this fandom.
	 * @see #getRegions()
	 */
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
	 * Tells if this fandom is cleared / completed.
	 * @return <code>true</code> if this fandom is cleared, else <code>false</code>.
	 * @see #hasBeenCleared()
	 * @see #checkCleared()
	 */
	public boolean isCleared() {
		return cleared;
	}
	
	/**
	 * Tells if this fandom has recently been cleared / completed. This method can only return
	 * <code>true</code> if {@link #checkCleared()} cleared has been called and after calling this method
	 * once, any subsequent calls will return <code>false</code>.
	 * @return <code>true</code> if this fandom has recently been cleared, else false
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
	 * Checks if this fandom has been completed, i.e. if all of its regions have been cleared.
	 * Only after this method has been called can {@link #hasBeenCleared()} return <code>true</code>.
	 * @return <code>true</code> if this fandom has been cleared, else <code>false</code>. 
	 * The return value of this method can also be called with {@link #isCleared()}.
	 * @see #isCleared()
	 * @see #hasBeenCleared()
	 */
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
	
	/**
	 * Returns a new companion from the character base of this fandom's start companion.
	 * @return a new companion from the character base of this fandom's start companion
	 * @throws IllegalStateException If the start companion ID of this fandom isn't registered as a character.
	 * @see #getStartCompanionId()
	 */
	public Companion getStartCompanion() {
		CharacterBase cb = CharacterBase.CHARACTERS.lookup(getStartCompanionId());
		if (cb == null)
			throw new IllegalStateException(String.format("Character base %s is not registered!", getStartCompanionId()));
		return new Companion(cb);
	}
	
	/**
	 * Changes the regions associated to this fandom to all of those of the
	 * specified that belong to this fandom. Afterwards checks if this fandom has been cleared.
	 * @param regions the regions to consider for adding
	 * @throws NullPointerException If <code>regions</code> refers to <code>null</code>.
	 * @see #checkCleared()
	 */
	public void updateRegions(Iterable<? extends Region> regions) {
		Utilities.requireNonNull(regions, "regions");
		this.regions.clear();
		for (Region region : regions) {
			if (region.getFandomId().equals(id))
				this.regions.add(region);
		}
		checkCleared();
	}
	
	/**
	 * Returns a deep copy of this fandom, including cloning the associated regions.
	 */
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
