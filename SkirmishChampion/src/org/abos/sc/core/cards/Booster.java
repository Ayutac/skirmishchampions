package org.abos.sc.core.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
	
	@Override
	public String getId() {
		throw new UnsupportedOperationException("Not yet implemented!");
	}
	
	@Override
	public String getName() {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	/**
	 * Returns the cards available in this booster as a mapping from a rarity to the booster cards of that rarity.
	 * <code>null</code> is a valid key and the union of all the other collections.
	 * @return A mapping from a rarity to the booster cards of that rarity. The map and the mapped collections are immutable.
	 */
	public Map<Rarity, Collection<T>> getAvailableCards() {
		throw new UnsupportedOperationException("Not yet implemented!");
	}
	
	/**
	 * The distribution for each place in a booster pack.
	 * @return distribution for each place in a booster pack
	 */
	public Collection<Map<Rarity, Integer>> getDistributions() {
		throw new UnsupportedOperationException("Not yet implemented!");
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
	
}
