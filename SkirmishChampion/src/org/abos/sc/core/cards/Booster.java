package org.abos.sc.core.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.abos.util.Id;
import org.abos.util.Name;
import org.abos.util.Utilities;

/**
 * Booster packs for getting more cards.
 * @author Sebastian Koch
 * @version %I%
 * @param <T> the card type this booster is for
 * @since 0.7
 */
public class Booster<T extends Card> implements Id, Name {
	
	protected final String id;
	
	protected final String name;
	
	protected final Map<Rarity, Collection<T>> availableCards;
	
	protected final Collection<Map<Rarity, Integer>> distributions;
	
	public Booster(String id, String name, Map<Rarity, Collection<T>> availableCards, Collection<Map<Rarity, Integer>> distributions) {
		// TODO exceptions
		this.id = id;
		this.name = name;
		Map<Rarity, Collection<T>> acCopy = new EnumMap<>(Rarity.class);
		for (Entry<Rarity, Collection<T>> entry : availableCards.entrySet())
			acCopy.put(entry.getKey(), Collections.unmodifiableCollection(entry.getValue()));
		this.availableCards = Collections.unmodifiableMap(acCopy);
		Collection<Map<Rarity, Integer>> dCopy = new LinkedList<>();
		for (Map<Rarity, Integer> distribution : distributions)
			dCopy.add(Collections.unmodifiableMap(distribution));
		this.distributions = Collections.unmodifiableCollection(dCopy);
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the cards available in this booster as a mapping from a rarity to the booster cards of that rarity.
	 * <code>null</code> is a valid key and the union of all the other collections.
	 * @return A mapping from a rarity to the booster cards of that rarity. The map and the mapped collections are immutable.
	 */
	public Map<Rarity, Collection<T>> getAvailableCards() {
		return availableCards;
	}
	
	/**
	 * The distribution for each place in a booster pack.
	 * @return distribution for each place in a booster pack
	 */
	public Collection<Map<Rarity, Integer>> getDistributions() {
		return distributions;
	}
	
	/**
	 * Returns the number of cards in a booster pack. Should always return the same as
	 * <code>getDistributions().size()</code>.
	 * @return the number of cards in a booster pack (greater than zero)
	 * @see #getDistributions()
	 * @see #openPack()
	 */
	public int packSize() {
		return getDistributions().size();
	}
	
	/**
	 * Opens a booster pack and returns a list of the cards within. The content of the list can change with each call,
	 * but the size is always {@link #packSize()}. The cards in the list are a selection from the cards in
	 * <code>availableCards().get(null)</code>.
	 * @return a list of the cards within a booster pack, not empty and not containing <code>null</code>.
	 * @see #packSize()
	 * @see #getAvailableCards()
	 */
	public List<T> openPack() {
		ArrayList<T> result = new ArrayList<>(packSize());
		Random rng = new Random();
		for (Map<Rarity, Integer> distribution : getDistributions()) {
			result.add(Utilities.randomlyChoose(getAvailableCards().get(Utilities.randomlyChoose(distribution, rng)), rng));
		}
		return result;
	}
	
	public static Collection<Map<Rarity, Integer>> createSingletonDistribution(int packSize, Map<Rarity, Integer> rareDistribution) {
		// TODO exceptions
		Collection<Map<Rarity, Integer>> distributions = new ArrayList<>(packSize);
		Map<Rarity, Integer> commonConstant = new EnumMap<>(Rarity.class);
		commonConstant.put(Rarity.COMMON, 1);
		for (int i = 1; i < packSize; i++) {
			distributions.add(commonConstant);
		}
		distributions.add(rareDistribution);
		return distributions;
	}
	
	public static Collection<Map<Rarity, Integer>> createSingleDistribution(int packSize, Map<Rarity, Integer> rareDistribution) {
		// TODO exceptions
		return Collections.nCopies(packSize, rareDistribution);
	}
	
}
