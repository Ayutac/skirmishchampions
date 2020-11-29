package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class TextAreaFrame extends JFrame {
	
	public final static Dimension TEXT_AREA_DIMENSION = new Dimension(220, 220);
	
	protected Path textFilePath = null;

	protected JTextArea textArea;
	
	protected JScrollPane textAreaWrapper;
	
	protected JButton closeButton;
	
	/**
	 * @throws HeadlessException
	 */
	public TextAreaFrame(String title) throws HeadlessException {
		super(title);
		initComponents();
		initLayout();
	}
	
	public String getText() {
		return textArea.getText();
	}
	
	public void setText(String text) {
		textArea.setText(text);
	}
	
	/**
	 * @param textFileSupplier the textFileSupplier to set
	 */
	public void setTextPath(Path textFilePath) {
		this.textFilePath = textFilePath;
	}
	
	public boolean isEmpty() {
		return getText().isEmpty();
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b && isEmpty() && textFilePath != null) {
			try {
				setText(Files.readString(textFilePath));
			}
			catch (IOException ex) {
				StringBuilder message = new StringBuilder(textFilePath.toString());
				message.append(System.lineSeparator());
				message.append("couldn't be loaded! Error:");
				message.append(System.lineSeparator());
				message.append(ex.getMessage() == null ? "-no error message-" : ex.getMessage());
				message.append(System.lineSeparator());
				message.append("For more information see console.");
				JOptionPane.showMessageDialog(null, message.toString(), getTitle(), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		super.setVisible(b);
		if (b)
			requestFocus();
	}
	
	private void initComponents() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		textAreaWrapper = new JScrollPane(textArea);
		closeButton = new JButton("Close");
		closeButton.addActionListener(e -> setVisible(false));
	}
	
	private void initLayout() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		textAreaWrapper.setPreferredSize(TEXT_AREA_DIMENSION);
		add(textAreaWrapper, BorderLayout.CENTER);
		add(closeButton, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
	}

}
