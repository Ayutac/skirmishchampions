package org.abos.sc.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;

import org.abos.util.Id;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class ContentComboBox<T extends Id> extends JComboBox<T> {
	
	public static final int PREFERRED_HEIGHT = 25;
	
	public static final int PREFERRED_WIDTH = 162; // should be divisible by 6 for GUI reasons
	
	public static final Dimension PREFERRED_SIZE = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);

	protected Collection<T> content;
	
	protected Comparator<T> comparator;
	
	/**
	 * 
	 */
	public ContentComboBox(Collection<T> content, Comparator<T> comparator) {
		if (content == null)
			throw new NullPointerException("content must be specified!");
		this.content = content;
		this.comparator = comparator;
		setPreferredSize(PREFERRED_SIZE);
		refreshContent();
	}
	
	public ContentComboBox(Collection<T> content) {
		this(content, null);
	}
	
	public boolean containsItem(Object item) {
		return content.contains(item);
	}
	
	public void setContent(Collection<T> content) {
		Utilities.requireNonNull(content, "content");
		this.content = content;
		refreshContent();
	}
	
	public void refreshContent() {
		removeAllItems();
		if (comparator == null)
			for (T item : content)
				addItem(item);
		else {
			List<T> input = new ArrayList<>(content);
			Collections.sort(input, comparator);
			for (T item : input)
				addItem(item);
		}
		repaint();
	}
	
	/**
	 * @return the comparator
	 */
	public Comparator<T> getComparator() {
		return comparator;
	}
	
	/**
	 * @param comparator the comparator to set
	 */
	public void setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
		refreshContent();
	}

}
