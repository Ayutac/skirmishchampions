package org.abos.sc.core.cards;

import org.abos.util.Name;

/**
 * The interface for cards.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.7
 */
public interface Card extends Name {

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
	
}
