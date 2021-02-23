package org.abos.sc.gui;

import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.ChallengeRatable;
import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.StatsPrimary;
import org.abos.sc.core.StatsSecondary;
import org.abos.util.AbstractNamedComparator;
import org.abos.util.Id;
import org.abos.util.Name;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 1.0
 */
public class CompanionSortComboBox extends JComboBox<AbstractNamedComparator<? extends CharacterBase>> {
	
	protected boolean reversed = false;
	
	protected AbstractNamedComparator<Companion> idComparator = Id.createIdComparator();
	
	/**
	 * 
	 */
	public CompanionSortComboBox() {
		addItem(Name.createNameComparator());
		addItem(ChallengeRatable.createCRComparator());
		for (StatsPrimary primary : StatsPrimary.values())
			addItem(CharacterBase.createPrimaryComparator(primary));
		for (StatsSecondary secondary : StatsSecondary.values())
			addItem(CharacterBase.createSecondaryComparator(secondary));
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
		Comparator<Companion> comp = ((AbstractNamedComparator<Companion>)getSelectedItem()).thenComparing(idComparator);
		if (reversed)
			return comp.reversed();
		return comp;
	}

}
