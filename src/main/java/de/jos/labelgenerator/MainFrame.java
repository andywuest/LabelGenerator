package de.jos.labelgenerator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainFrame extends JFrame  {

	private JTextArea sourceTextarea = null;
	private JScrollPane sourceScrollPane = null;

	private JTextArea targetTextarea = null;
	private JScrollPane targetScrollPane = null;

	private JPanel mainPanel = null;
	private JButton createStringButton = null;
	private JLabel levelNumberLabel = null;
	private JTextField levelNumberTextfield = null;
	private JLabel additionalBlanksLabel = null;
	private JTextField additionalBlanksTextField = null;
	private JButton deleteSourceButton = null;
	private JButton quit = null;

	private JCheckBox harmonizeStringLenghCb = null;
	private JCheckBox replaceQuotes = null;

	public MainFrame() {
		setTitle("Stringify");
		setSize(680, 510);

		initComponents();
	}

	private void initComponents() {

		int height = 5;
		int width = 3;

		mainPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(height, width);
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);
		
		
		mainPanel.setLayout(gridLayout);
		getContentPane().add(mainPanel);
		
		final JFrame frame = this;

		for (int i = 0; i < height * width; i++) {
			JButton button = new JButton(" Button " + i);
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
		//			AboutDialog aboutDialog = new AboutDialog(frame, "bla", "blubb");
      //					aboutDialog.setVisible(true);
					JDialog jDialog = new JDialog();
					jDialog.setVisible(true);
				}
			});

			mainPanel.add(button);
		}

	}

	public static void main(final String[] args) {
		final MainFrame simple = new MainFrame();
		simple.setVisible(true);
	}

	private static class AboutDialog extends JDialog implements ActionListener {

		public AboutDialog(JFrame parent, String title, String message) {
			super(parent, title, true);
			if (parent != null) {
				Dimension parentSize = parent.getSize();
				Point p = parent.getLocation();
				setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
			}
			JPanel messagePane = new JPanel();
			messagePane.add(new JLabel(message));
			getContentPane().add(messagePane);
			JPanel buttonPane = new JPanel();
			JButton button = new JButton("OK");
			buttonPane.add(button);
			button.addActionListener(this);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			pack();
			setVisible(true);
		}

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			dispose();
		}
	}
}
