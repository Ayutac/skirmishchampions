package org.abos.util.gui;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.JTextArea;

import org.abos.util.Utilities;

/**
 * A handler that puts log messages into an associated text area
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1
 * @see JTextArea
 */
public class TextAreaHandler extends Handler {
	
	/**
	 * if this handler is closed
	 * @see #close()
	 */
	protected boolean closed = false;
	
	/**
	 * the formatter for this handler
	 */
	protected Formatter formatter;
	
	/**
	 * the associated text area
	 */
	protected JTextArea textArea;
	
	/**
	 * Creates a new handler with the specified text area and formatter.
	 * @param textArea the text area for displaying the log messages, not <code>null</code>
	 * @param formatter The formatter for this handler. If <code>null</code>, a simplest formatter will be used
	 * @throws NullPointerException If <code>textArea</code> refers to <code>null</code>.
	 * @see Utilities#createSimplestFormatter()
	 */
	public TextAreaHandler(JTextArea textArea, Formatter formatter) {
		Utilities.requireNonNull(textArea, "textArea");
		this.textArea = textArea;
		if (formatter == null)
			this.formatter = Utilities.createSimplestFormatter();
		else
			this.formatter = formatter;
	}
	
	/**
	 * Creates a new handler with the specified text area and a simplest formatter.
	 * @param textArea the text area for displaying the log messages, not <code>null</code>
	 * @throws NullPointerException If <code>textArea</code> refers to <code>null</code>.
	 * @see #TextAreaHandler(JTextArea, Formatter)
	 * @see Utilities#createSimplestFormatter()
	 */
	public TextAreaHandler(JTextArea textArea) {
		this(textArea, null);
	}
	
	/**
	 * Publishes the specified record by appending it to the text area, unless this handler is closed.
	 * @param record the record to publish
	 * @throws NullPointerException Might happen if the record is <code>null</code> and the
	 * formatter cannot handle that.
	 * @see #close()
	 * @see Formatter#format(LogRecord)
	 * @see JTextArea#append(String)
	 */
	@Override
	public void publish(LogRecord record) {
		if (!closed)
			textArea.append(formatter.format(record));
	}
	
	/**
	 * The close method will perform a flush and then close the handler. 
	 * After this method has been called this handler should no longer be used. 
	 * Further method calls are silently ignored.
	 * @see #flush()
	 */
	@Override
	public void close() throws SecurityException {
		if (closed)
			return;
		flush();
		closed = true;
	}
	
	/**
	 * Only repaints the associated text area, as the records are immediatly added to the text area.
	 * @see #publish(LogRecord)
	 */
	@Override
	public void flush() {
		if (!closed)
			textArea.repaint();
	}
	
}
