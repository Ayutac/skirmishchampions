package org.abos.sc.core.cards;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.abos.sc.core.Valuable;
import org.abos.util.AbstractNamedComparator;
import org.abos.util.Id;
import org.abos.util.Name;
import org.abos.util.Utilities;

/**
 * The interface for cards.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.7
 */
public interface Card extends Id, Name, Valuable {
	
	/**
	 * How many identical cards are usually needed to get a single card of higher rarity. 
	 */
	public final static int DEFAULT_COMBINATION_SIZE = 5;

	/**
	 * Returns the rarity of this card.
	 * @return the rarity of this card
	 */
	public Rarity getRarity();
	
	/**
	 * Returns the flavour text of this card.
	 * @return the flavour text of this card
	 */
	public String getFlavourText();
	
	/**
	 * Returns the gold value of this card, by default the gold value of its rarity.
	 * @see Rarity#getValueGold()
	 */
	@Override
	public default int getValueGold() {
		return getRarity().getValueGold();
	}
	
	/**
	 * Returns the diamond value of this card, by default the diamond value of its rarity.
	 * @see Rarity#getValueDiamonds()
	 */
	@Override
	public default int getValueDiamonds() {
		return getRarity().getValueDiamonds();
	}
	
	/**
	 * Returns a comparator that sorts cards by their rarity starting with the most common one.
	 * <code>null</code> is permitted for both objects to compare and names of the objects to compare.
	 * <code>null</code> is smaller than any other card and an object with 
	 * a <code>null</code> rarity is smaller than any other non <code>null</code> enum instance,
	 * except of course in both cases if both objects respectively their rarities are <code>null</code>,
	 * in which this comparator evaluates them as equal.  
	 * @param <T> the class extending {@link Card}
	 * @return A comparator for <code>T</code>.
	 */
	public static <T extends Card> AbstractNamedComparator<T> createRarityComparator() {
		return new AbstractNamedComparator<T>(Rarity.DISPLAY_NAME) {
			@Override
			public int compare(T o1, T o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				if (o1.getRarity() == o2.getRarity())
					return 0;
				if (o1.getRarity() == null)
					return Integer.MIN_VALUE;
				if (o2.getRarity() == null)
					return Integer.MAX_VALUE;
				return o1.getRarity().compareTo(o2.getRarity());
			}
		};
	}
	
	public static <T extends Card> Map<Rarity, Collection<T>> toRarityMap(Iterable<T> cards) {
		Utilities.requireNonNull(cards, "cards");
		Map<Rarity, Collection<T>> map = new EnumMap<>(Rarity.class);
		map.put(null, new LinkedList<>());
		for (Rarity rarity : Rarity.values()) {
			map.put(rarity, new LinkedList<>());
		}
		// add all cards
		Iterator<T> it = cards.iterator();
		T current = null;
		while (it.hasNext()) {
			current = it.next();
			if (current == null)
				throw new NullPointerException("All entries of cards must be non null!");
			map.get(current.getRarity()).add(current);
			map.get(null).add(current);
		}
		return map;
	}
	
}
