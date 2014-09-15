package zy.UI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import zy.Control.Info.ImageArrayList;
import zy.Control.Info.ImageComment;
import zy.Control.Info.WordForm;
import zy.Control.Info.Utils.LogicException;
import zy.Control.Info.Utils.WordUtil;
import zy.Control.Logic.ImportWordLogic;
import zy.Control.Logic.Logic;

public class ImportWord extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WordForm form;

	private ImageArrayList<ImageComment> images;

	public void setImages(ImageArrayList<ImageComment> images) {
		this.images = images;
	}

	public JButton getBtnNext() {
		return btnNext;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO factory pattern?

		try {
			Logic logic = new ImportWordLogic(form);
			String wordPath = "";

			if (e.getSource() == btnReadTmplate) {
				wordPath = getPath();
				if (!WordUtil.isEmptyString(wordPath)) {
					btnNext.setEnabled(false);
					form.setPathWord(wordPath);
					logic.setUIInfo();
				}
			} else if (e.getSource() == btnCancel) {
				logic.navigateTo(null);
			} else if (e.getSource() == btnNext) {
				// Create and set up the window.
				PicturesFrame frame = new PicturesFrame(images);
				frame.init();

				frame.setForm(form);

				logic.navigateTo(frame);
			}
		} catch (LogicException e1) {
			e1.printStackTrace();
		}
	}

	// read the word file
	private JButton btnReadTmplate;

	// go to next frame
	private JButton btnNext;

	// exit Capricorn
	private JButton btnCancel;

	private JFileChooser fc;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				createAndShowGUI();
			}
		});
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {

		// Create and set up the window.
		ImportWord frame = new ImportWord();
		frame.init();
		
		// Display the window.
		frame.pack();
		// set frame in the center of the window
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
	}

	/**
	 * Get the path of selected image.
	 *  
	 * @return
	 */
	private String getPath() {
		int returnVal = fc.showOpenDialog(ImportWord.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file.getAbsolutePath();

			// This is where a real application would open the file.
		} else {
			return "";
		}
	}

	public ImportWord() {
		super(UIConst.FRAME_TITILE);
	}

	public void init() {

		this.form = new WordForm(this);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		
		// Create and set up the window.
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(this.getContentPane());
	}

	public void addComponentsToPane(Container pane) {

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		// Make the center component big, since that's the
		// typical usage of BorderLayout.
		Border paneEdge = BorderFactory.createEmptyBorder(50, 10, 50, 10);

		btnReadTmplate = new JButton(UIConst.BTN_IMPORT_WORD_TITLE);
		btnReadTmplate.setPreferredSize(new Dimension(200, 100));
		btnReadTmplate.addActionListener(this);
		
		// Create a file chooser
		fc = new JFileChooser();

		btnNext = new JButton(UIConst.BTN_NEXT_TITLE);
		btnNext.setPreferredSize(new Dimension(200, 100));
		btnNext.addActionListener(this);

		btnCancel = new JButton(UIConst.BTN_CANCEL_TITLE);
		btnCancel.setPreferredSize(new Dimension(200, 100));
		btnCancel.addActionListener(this);

		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();

		p1.add(btnReadTmplate);
		p2.add(btnNext);
		p3.add(btnCancel);

		JPanel panel = new JPanel();

		GridLayout experimentLayout = new GridLayout(0, 1);

		panel.setLayout(experimentLayout);
		panel.setBorder(paneEdge);

		panel.add(p1);
		panel.add(p2);
		panel.add(p3);

		pane.add(panel, BorderLayout.CENTER);
	}
}
