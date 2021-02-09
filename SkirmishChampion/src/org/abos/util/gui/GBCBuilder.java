package org.abos.util.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import org.abos.util.Utilities;

/**
 * A builder for Grid Bag Constraints (GBC).
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.5
 */
public final class GBCBuilder implements Cloneable {
	
	/**
	 * The grid bag constraints given by an empty constructor.
	 */
	private final static GridBagConstraints DEFAULT = new GridBagConstraints();
	
	/**
	 * The internal grid bag constraints that are build.
	 * @see #get()
	 * @see #set(GridBagConstraints)
	 * @see #build()
	 */
	private GridBagConstraints c;
	
	private int gridxDefault;
	
	private int gridyDefault;
	
	private int gridwidthDefault;
	
	private int gridheightDefault;
	
	private double weightxDefault;
	
	private double weightyDefault;
	
	private int anchorDefault;
	
	private int fillDefault;
	
	private Insets insetsDefault;
	
	private int ipadxDefault;
	
	private int ipadyDefault;
	
	/**
	 * Creates a grid bag constraint builder with the given defaults.
	 * @param constraintDefaults the defaults for this builder
	 * @throws NullPointerException If <code>constraintDefaults</code> refers to <code>null</code>.
	 */
	public GBCBuilder(GridBagConstraints constraintDefaults) {
		Utilities.requireNonNull(constraintDefaults, "constraintDefaults");
		setDefault(constraintDefaults);
		c = getDefault();
	}
	
	/**
	 * Creates a grid bag constraint builder with the default grid bag constraints given by the empty constructor.
	 * @see GridBagConstraints#GridBagConstraints()
	 */
	public GBCBuilder() {
		this(DEFAULT);
	}
	
	/**
	 * Copy constructor.
	 * @param builder the builder to copy
	 * @throws NullPointerException if <code>builder</code> refers to <code>null</code>
	 */
	public GBCBuilder(GBCBuilder builder) {
		this(builder.getDefault()); // throws NPE
		set(builder.c);
	}
	
	/**
	 * Returns a copy of this builder's defaults as grid bag constraints
	 * @return a copy of this builder's defaults
	 * @see #setDefault(GridBagConstraints)
	 * @see #get()
	 */
	public GridBagConstraints getDefault() {
		return new GridBagConstraints(gridxDefault, gridyDefault, gridwidthDefault, gridheightDefault, weightxDefault, weightyDefault, 
				anchorDefault, fillDefault, (Insets)insetsDefault.clone(), ipadxDefault, ipadyDefault);
	}
	
	/**
	 * Sets this builder's defaults to the specified grid bag constraints.
	 * @param constraintDefaults the new defaults for this builder
	 * @return this instance
	 * @throws NullPointerException If <code>constraintDefaults</code> refers to <code>null</code>.
	 * @see #getDefault()
	 * @see #set(GridBagConstraints)
	 */
	public GBCBuilder setDefault(GridBagConstraints constraintDefaults) {
		Utilities.requireNonNull(constraintDefaults, "constraintDefaults");
		gridxDefault = constraintDefaults.gridx;
		gridyDefault = constraintDefaults.gridy;
		gridwidthDefault = constraintDefaults.gridwidth;
		gridheightDefault = constraintDefaults.gridheight;
		weightxDefault = constraintDefaults.weightx;
		weightyDefault = constraintDefaults.weighty;
		anchorDefault = constraintDefaults.anchor;
		fillDefault = constraintDefaults.fill;
		insetsDefault = (Insets)constraintDefaults.insets.clone();
		ipadxDefault = constraintDefaults.ipadx;
		ipadyDefault = constraintDefaults.ipady;
		return this;
	}
	
	/**
	 * Returns a copy of the current constraints of this builder <u>without</u> resetting them.
	 * @return a copy of the current constraints of this builder
	 * @see #set(GridBagConstraints)
	 * @see #build()
	 * @see #getDefault()
	 */
	public GridBagConstraints get() {
		return new GBCBuilder(c).c;
	}
	
	/**
	 * Sets the current constraints of this builder to a copy of the specified constraints without changing the defaults of this builder.
	 * @param constraints the constraints to set this builder's current constraints to
	 * @return this instance
	 * @throws NullPointerException If <code>constraints</code> refers to <code>null</code>.
	 * @see #get()
	 * @see #setDefault(GridBagConstraints)
	 */
	public GBCBuilder set(GridBagConstraints constraints) {
		Utilities.requireNonNull(constraints, "constraints");
		c = new GBCBuilder(constraints).c;
		return this;
	}
	
	/**
	 * Resets the current constraints of this builder to a copy of its default constraints. 
	 * @return this instance
	 */
	public GBCBuilder reset() {
		c = getDefault();
		return this;
	}
	
	/**
	 * Returns a copy of the current constraints of this builder <u>and</u> resets the builder.
	 * @return a copy of the current constraints of this builder
	 * @see #get()
	 * @see #reset()
	 */
	public GridBagConstraints build() {
		GridBagConstraints result = c;
		reset();
		return result;
	}
	
	public GBCBuilder gridxDefault(int gridxDefault) {
		this.gridxDefault = gridxDefault;
		return this;
	}
	
	public GBCBuilder gridyDefault(int gridyDefault) {
		this.gridyDefault = gridyDefault;
		return this;
	}
	
	public GBCBuilder gridwidthDefault(int gridwidthDefault) {
		this.gridwidthDefault = gridwidthDefault;
		return this;
	}
	
	public GBCBuilder gridheightDefault(int gridheightDefault) {
		this.gridheightDefault = gridheightDefault;
		return this;
	}
	
	public GBCBuilder weightxDefault(double weightxDefault) {
		this.weightxDefault = weightxDefault;
		return this;
	}
	
	public GBCBuilder weightyDefault(double weightyDefault) {
		this.weightyDefault = weightyDefault;
		return this;
	}
	
	public GBCBuilder anchorDefault(int anchorDefault) {
		this.anchorDefault = anchorDefault;
		return this;
	}
	
	public GBCBuilder fillDefault(int fillDefault) {
		this.fillDefault = fillDefault;
		return this;
	}
	
	public GBCBuilder insetsDefault(Insets insetsDefault) {
		this.insetsDefault = (Insets)insetsDefault.clone();
		return this;
	}
	
	public GBCBuilder ipadxDefault(int ipadxDefault) {
		this.ipadxDefault = ipadxDefault;
		return this;
	}
	
	public GBCBuilder ipadyDefault(int ipadyDefault) {
		this.ipadyDefault = ipadyDefault;
		return this;
	}
	
	public GBCBuilder gridx(int gridx) {
		c.gridx = gridx;
		return this;
	}
	
	public GBCBuilder gridy(int gridy) {
		c.gridy = gridy;
		return this;
	}
	
	public GBCBuilder gridwidth(int gridwidth) {
		c.gridwidth = gridwidth;
		return this;
	}
	
	public GBCBuilder gridheight(int gridheight) {
		c.gridheight = gridheight;
		return this;
	}
	
	public GBCBuilder weightx(double weightx) {
		c.weightx = weightx;
		return this;
	}
	
	public GBCBuilder weighty(double weighty) {
		c.weighty = weighty;
		return this;
	}
	
	public GBCBuilder anchor(int anchor) {
		c.anchor = anchor;
		return this;
	}
	
	public GBCBuilder fill(int fill) {
		c.fill = fill;
		return this;
	}
	
	public GBCBuilder insets(Insets insets) {
		c.insets = (Insets)insets.clone();
		return this;
	}
	
	public GBCBuilder ipadx(int ipadx) {
		c.ipadx = ipadx;
		return this;
	}
	
	public GBCBuilder ipady(int ipady) {
		c.ipady = ipady;
		return this;
	}
	
	/**
	 * Returns a deep copy of this grid bag constraints builder by calling the copy constructor.
	 * @see #GBCBuilder(GBCBuilder)
	 */
	@Override
	public Object clone() {
		return new GBCBuilder(this);
	}

	/**
	 * Generates a hash code for this instance, taking into account all fields. It's guarantied that
	 * the hash codes of two builders are equal if the builders are equal.
	 * @return a hash code for this builder
	 * @see #equals(Object)
	 * @see GUIUtilities#hashCodeGBC(GridBagConstraints)
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anchorDefault;
		result = prime * result + ((c == null) ? 0 : GUIUtilities.hashCodeGBC(c));
		result = prime * result + fillDefault;
		result = prime * result + gridheightDefault;
		result = prime * result + gridwidthDefault;
		result = prime * result + gridxDefault;
		result = prime * result + gridyDefault;
		result = prime * result + ((insetsDefault == null) ? 0 : insetsDefault.hashCode());
		result = prime * result + ipadxDefault;
		result = prime * result + ipadyDefault;
		long temp;
		temp = Double.doubleToLongBits(weightxDefault);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(weightyDefault);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Checks if the specified object is the same as this grid bag constraints builder, i.e. the other object
	 * must be a {@link GBCBuilder} and all its fields must be the same as the fields of this builder
	 * to return <code>true</code>. The current constraints of the builders are compared by values, not reference, as well.
	 * If the other object is equal to this builder, then their hash codes return the same number.
	 * @param obj the object to check
	 * @return <code>true</code> if this builder is equal to the object, else <code>false</code>.
	 * @see #hashCode()
	 * @see GUIUtilities#equalsGBC(GridBagConstraints, GridBagConstraints)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GBCBuilder other = (GBCBuilder) obj;
		if (anchorDefault != other.anchorDefault)
			return false;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!GUIUtilities.equalsGBC(c, other.c))
			return false;
		if (fillDefault != other.fillDefault)
			return false;
		if (gridheightDefault != other.gridheightDefault)
			return false;
		if (gridwidthDefault != other.gridwidthDefault)
			return false;
		if (gridxDefault != other.gridxDefault)
			return false;
		if (gridyDefault != other.gridyDefault)
			return false;
		if (insetsDefault == null) {
			if (other.insetsDefault != null)
				return false;
		} else if (!insetsDefault.equals(other.insetsDefault))
			return false;
		if (ipadxDefault != other.ipadxDefault)
			return false;
		if (ipadyDefault != other.ipadyDefault)
			return false;
		if (Double.doubleToLongBits(weightxDefault) != Double.doubleToLongBits(other.weightxDefault))
			return false;
		if (Double.doubleToLongBits(weightyDefault) != Double.doubleToLongBits(other.weightyDefault))
			return false;
		return true;
	}

}
