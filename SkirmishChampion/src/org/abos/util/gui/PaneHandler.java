package org.abos.util.gui;

import java.awt.Component;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1
 */
public class PaneHandler extends Handler {
	
	protected boolean closed = false;
	
	protected Formatter formatter;
	
	protected JTextArea textArea;
	
	/**
	 * 
	 */
	public PaneHandler(JTextArea textArea, Formatter formatter) {
		if (textArea == null)
			throw new NullPointerException("textArea must be specified!");
		this.textArea = textArea;
		if (formatter == null)
			this.formatter = Utilities.createSimplestFormatter();
		else
			this.formatter = formatter;
	}
	
	/**
	 * 
	 */
	public PaneHandler(JTextArea textArea) {
		this(textArea, null);
	}
	
	@Override
	public void publish(LogRecord record) {
		if (!closed)
			textArea.append(formatter.format(record));
	}
	
	@Override
	public void close() throws SecurityException {
		closed = true;
	}
	
	@Override
	public void flush() {
		textArea.repaint();
	}
	
}
