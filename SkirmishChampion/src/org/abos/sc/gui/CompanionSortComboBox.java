package org.abos.sc.gui;

import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.StatsPrimary;
import org.abos.sc.core.StatsSecondary;
import org.abos.util.Id;
import org.abos.util.Name;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 1.0
 */
public class CompanionSortComboBox extends JComboBox<String> {
	
	public final static String NAME_CRITERIUM = "Name";
	
	public final static String CR_CRITERIUM = "CR";
	
	protected boolean reversed = false;
	
	protected Comparator<Companion> idComparator = Id.createIdComparator();
	
	protected Comparator<Companion>[] selectionComparator;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public CompanionSortComboBox() {
		addItem(NAME_CRITERIUM);
		for (StatsPrimary primary : StatsPrimary.values())
			addItem(primary.getCapitalizedName());
		for (StatsSecondary secondary : StatsSecondary.values())
			addItem(secondary.getCapitalizedName());
		addItem(CR_CRITERIUM);
		selectionComparator = new Comparator[2+StatsPrimary.SIZE+StatsSecondary.SIZE];
		selectionComparator[0] = Name.createNameComparator();
		selectionComparator[1] = CharacterBase.createChallengeRatingComparator();
		int i = 2;
		for (StatsPrimary primary : StatsPrimary.values())
			selectionComparator[i++] = CharacterBase.createPrimaryComparator(primary);
		for (StatsSecondary secondary : StatsSecondary.values())
			selectionComparator[i++] = CharacterBase.createSecondaryComparator(secondary);
	}
	
	/**
	 * @return the reverse
	 */
	public boolean isReversed() {
		return reversed;
	}
	
	/**
	 * @param reversed the reversed to set
	 */
	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
	
	public Comparator<Companion> getComparator() {
		Comparator<Companion> comp = (Comparator<Companion>)selectionComparator[getSelectedIndex()].thenComparing(idComparator);
		if (reversed)
			return comp.reversed();
		return comp;
	}

}
