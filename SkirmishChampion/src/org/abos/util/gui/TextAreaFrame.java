package org.abos.util.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * A frame that only contains a text area and a button to hide the frame.
 * Text can be accessed directly over the frame with {@link #getText()}, {@link #setText(String)}
 * and a link to a text file can be provided via {@link #setTextFilePath(Path)} 
 * to get automatically loaded into this frame's text area.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1
 * @see JTextArea
 */
public class TextAreaFrame extends JFrame {
	
	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = -5742624375522499707L;

	/**
	 * default size for the text area
	 */
	public final static Dimension TEXT_AREA_SIZE = new Dimension(220, 220);
	
	/**
	 * path to the text file
	 * @see #setTextFilePath(Path)
	 */
	protected Path textFilePath = null;

	/**
	 * this frame's text area component
	 * @see #getText()
	 * @see #setText(String)
	 */
	protected JTextArea textArea;
	
	/**
	 * a scroll pane wrapper for the text area
	 */
	protected JScrollPane textAreaWrapper;
	
	/**
	 * the button to hide this frame again
	 */
	protected JButton closeButton;
	
	/**
	 * Creates a new {@link TextAreaFrame} with the specified title.
	 * @param title the title for the frame
	 * @throws HeadlessException If {@link GraphicsEnvironment#isHeadless()} returns <code>true</code>.
	 */
	public TextAreaFrame(String title) throws HeadlessException {
		super(title);
		initComponents();
		initLayout();
		if (!GUIUtilities.LOGOS.isEmpty())
			setIconImages(GUIUtilities.LOGOS);
	}
	
	/**
	 * Returns the text of this frame's text area.
	 * @return the text of this frame's text area
	 * @throws NullPointerException If this method is called before the components of this frame are
	 * properly initialized.
	 */
	public String getText() {
		return textArea.getText();
	}
	
	/**
	 * Sets the text of this frame's text area.
	 * @param text the text for the text area
	 * @throws NullPointerException If this method is called before the components of this frame are
	 * properly initialized.
	 */
	public void setText(String text) {
		textArea.setText(text);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setDot(0);
	}
	
	/**
	 * Checks if the underlying text area is empty, i.e. its text equals <code>""</code>.
	 * @return <code>true</code> if the underlying text area is empty, <code>false</code> otherwise.
	 * @throws NullPointerException If this method is called before the components of this frame are
	 * properly initialized.
	 */
	public boolean isEmpty() {
		return getText().isEmpty();
	}
	
	/**
	 * Sets the path to a text file to load.
	 * @param textFilePath The path to the text file to load. <code>null</code> is permitted
	 * and means that there is no text file to load.
	 */
	public void setTextFilePath(Path textFilePath) {
		this.textFilePath = textFilePath;
	}
	
	/**
	 * Shows or hides this frame depending on the value of parameter <code>b</code>.
	 * <br/><br/>
	 * If <code>b == true</code>, the text area is empty and the text file path is set to a non <code>null</code>
	 * path, this frame will try to load the specified text file into its area. If that fails,
	 * this frame will not become visible, instead an error message is shown.
	 * <br/><br/>
	 * If this frame is successfully shown, it will request the focus.
	 * @param b if <code>true</code>, makes the frame visible, otherwise hides the frame
	 * @see #isEmpty()
	 * @see #setTextFilePath(Path)
	 * @see #requestFocus()
	 * @see Window#setVisible(boolean)
	 * @see GUIUtilities#errorMessage(String, String, Exception)
	 */
	@Override
	public void setVisible(boolean b) {
		if (b && isEmpty() && textFilePath != null) {
			try {
				setText(Files.readString(textFilePath));
			}
			catch (IOException ex) {
				GUIUtilities.errorMessage(getTitle(), textFilePath.toString()+" couldn't be loaded!", ex);
				return;
			}
		}
		super.setVisible(b);
		if (b)
			requestFocus();
	}
	
	/**
	 * Initializes this container's components.
	 */
	private void initComponents() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textAreaWrapper = new JScrollPane(textArea);
		closeButton = new JButton("Close");
		closeButton.addActionListener(e -> setVisible(false));
	}
	
	/**
	 * Initializes this container's layout.
	 */
	private void initLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		textAreaWrapper.setPreferredSize(TEXT_AREA_SIZE);
		add(textAreaWrapper, BorderLayout.CENTER);
		add(closeButton, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

}
