package org.abos.sc.gui;

import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.StatsPrimary;
import org.abos.sc.core.StatsSecondary;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class CompanionStatInfoPanel extends JPanel {
	
	protected Companion companion;
	
	protected JLabel levelDesc;
	
	protected JLabel level;
	
	protected JLabel[] primaryStatsDesc;
	
	protected JLabel[] primaryStats;
	
	protected JLabel[] secondaryStatsDesc;
	
	protected JLabel[] secondaryStats;

	public CompanionStatInfoPanel() {
		initComponents();
		initLayout();
	}

	public CompanionStatInfoPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		initComponents();
		initLayout();
	}
	
	/**
	 * @return the companion
	 */
	public Companion getCompanion() {
		return companion;
	}
	
	/**
	 * @param companion the companion to set
	 */
	public void setCompanion(Companion companion) {
		this.companion = companion;
		refreshCompanion();
	}
	
	public void refreshCompanion() {
		if (companion == null) {
			level.setText("");
			for (int i = 0; i < primaryStats.length; i++)
				primaryStats[i].setText("");
			for (int i = 0; i < secondaryStats.length; i++)
				secondaryStats[i].setText("");
			return;
		}
		level.setText(String.valueOf(companion.getLevel()));
		for (int i = 0; i < primaryStats.length; i++)
			primaryStats[i].setText(String.valueOf(companion.getPrimaryStat(i)));
		for (int i = 0; i < secondaryStats.length; i++)
			secondaryStats[i].setText(String.valueOf(companion.getSecondaryStat(i)));
		repaint();
	}
	
	private void initComponents() {
		levelDesc = new JLabel("Level:");
		level = new JLabel();
		primaryStatsDesc = new JLabel[StatsPrimary.SIZE];
		primaryStats = new JLabel[StatsPrimary.SIZE];
		StatsPrimary[] valsP = StatsPrimary.values();
		for (int i = 0; i < primaryStats.length; i++) {
			primaryStatsDesc[i] = new JLabel(valsP[i].getName()+":");
			primaryStats[i] = new JLabel();
		}
		secondaryStatsDesc = new JLabel[StatsSecondary.SIZE];
		secondaryStats = new JLabel[StatsSecondary.SIZE];
		StatsSecondary[] valsS = StatsSecondary.values();
		for (int i = 0; i < secondaryStats.length; i++) {
			secondaryStatsDesc[i] = new JLabel(valsS[i].getName()+":");
			secondaryStats[i] = new JLabel();
		}
	}
	
	private void initLayout() {
		GridLayout layout = new GridLayout(0, 4);
		setLayout(layout);
		for (int i = 0; i < primaryStats.length; i++) {
			add(primaryStatsDesc[i]);
			add(primaryStats[i]);
		}
		for (int i = 0; i < secondaryStats.length; i++) {
			add(secondaryStatsDesc[i]);
			add(secondaryStats[i]);
		}
		add(levelDesc);
		add(level);
	}

}
