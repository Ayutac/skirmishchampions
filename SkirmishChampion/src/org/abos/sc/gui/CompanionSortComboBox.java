package org.abos.sc.gui;

import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.StatsPrimary;
import org.abos.sc.core.StatsSecondary;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 1.0
 */
public class CompanionSortComboBox extends JComboBox<String> {
	
	public final static String NAME_CRITERIUM = "Name";
	
	protected boolean reversed = false;
	
	protected Comparator<Companion> idComparator = Utilities.createIdComparator();
	
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
		selectionComparator = new Comparator[1+StatsPrimary.SIZE+StatsSecondary.SIZE];
		selectionComparator[0] = Utilities.createNameComparator();
		int i = 1;
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
