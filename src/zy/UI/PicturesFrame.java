package zy.UI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import zy.Control.Info.ImageArrayList;
import zy.Control.Info.ImageComment;
import zy.Control.Info.PicturesForm;
import zy.Control.Info.WordForm;
import zy.Control.Info.Utils.LogicException;
import zy.Control.Logic.Logic;
import zy.Control.Logic.PicturesLogic;

public class PicturesFrame extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// read the word file
	private JButton btnAdd;
	
	private WordForm form;
	
	private JScrollPane scrb;
	
	private GridBagConstraints c;
	
	public JScrollPane getScrb() {
		return scrb;
	}
	
	private List<PicturePanel> picPanels;
	
	/** pictures to display*/
	private ImageArrayList<ImageComment> pictures;
	
	public ImageArrayList<ImageComment> getPictures() {
		return pictures;
	}


	public void setPictures(ImageArrayList<ImageComment> pictures) {
		this.pictures = pictures;
	}

	private JPanel pPics;
	
	
	public WordForm getForm() {
		return form;
	}


	public void setForm(WordForm form) {
		this.form = form;
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			// TODO factory pattern?
			PicturesForm picsForm = new PicturesForm(this);
			Logic logic = new PicturesLogic(picsForm);

			if (e.getSource() == btnComplete) {
				
				picsForm.setPath(form.getPathWord());
			    //Create and set up the window.
				savePictures();
				logic.storeInfo();
			    
			} 
			else if (e.getSource() == btnCancel) {
				
			    logic.navigateTo(this.form.getImportWord());
			}else if (e.getSource() == btnAdd){
				
				picsForm.setAdd(true);
				picsForm.setSelPos(this.getPicPanels().size());
				logic.modifyUIInput();
			}
		} catch (LogicException e1) {

			e1.printStackTrace();
		}
	}

	// go to next frame
	private JButton btnComplete;
	
	// exit Capricorn
	private JButton btnCancel;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	       /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
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
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
                createAndShowGUI();
            }
        });
	}
	
	
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
    	
        //Create and set up the window.
    	PicturesFrame frame = new PicturesFrame(null);
        frame.init();
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    
    }
    
    /**
     * Save the pictures.
     * 
     */
    private void savePictures(){
    	
    	this.pictures.clear();
    	for(PicturePanel p :picPanels){
    		if (p.getPic()!=null){
    			pictures.add(p.getPic());
    		}
    	}
    }
    
    /**
     * Constructor, will take the pictures got from former frame. 
     * 
     * @param pictures
     */
    public PicturesFrame(ImageArrayList<ImageComment> pictures){
    	super(UIConst.FRAME_TITILE);
    	this.pictures = pictures;
    }

    public void init(){
    	
    	picPanels = new ArrayList<PicturePanel>();
    	
        //Create and set up the window.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setPreferredSize(new Dimension(700, 800));
        
        this.setResizable(false);
        
        //Set up the content pane.
        addComponentsToPane(this.getContentPane());
    }
    
    public void addComponentsToPane(Container pane) {
        
        
        //Make the center component big, since that's the
        //typical usage of BorderLayout.
      
        pPics = new JPanel(new GridBagLayout());
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        
        // initialize picture panels
        initPicPanel();
        
        scrb = new javax.swing.JScrollPane(pPics);
        
        JPanel pBtns = new JPanel();
        
        btnAdd = new JButton(UIConst.BTN_ADD_TITLE);        
        btnComplete = new JButton(UIConst.BTN_COMPLETE_TITLE);
        btnCancel= new JButton(UIConst.BTN_CANCEL_TITLE); 
        
        btnAdd.addActionListener(this);  
        btnComplete.addActionListener(this);  
        btnCancel.addActionListener(this);  
        
        pBtns.setMaximumSize(new Dimension(700, 60));
        GridLayout btnsLayout = new GridLayout(1,4);  
        pBtns.setLayout(btnsLayout);
        pBtns.add(btnAdd);
        pBtns.add(btnComplete);
        pBtns.add(btnCancel);

        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        
        pane.add(scrb);
        pane.add(pBtns);       
    }
    
    /**
     * Initialize picture panels 
     * 
     */
    private void initPicPanel(){
    	
        // add picture panels
        if (pictures== null || pictures.size()==0){
            addPicturePanel(0, null);
        }else{
        	this.addMultiPicturePanel(pictures);
        }
    }
    
    /**
     * Add pictures
     * 
     * @param pos
     */
    public void addPicturePanel(int pos, ImageComment pic){
    	
    	doAddPicPanel(pos, pic);
		
		reDisplayPictures();
		
//		getpPics().add(p,-1);
//		getpPics().revalidate();
    }
    
    private void doAddPicPanel(int pos, ImageComment pic){
    	
		PicturePanel p = new PicturePanel(this,pic);
		
		this.getPicPanels()
				.add(pos, p);
    }
    
    /**
     * Add multi-pictures
     * 
     * @param pics
     */
    public void addMultiPicturePanel(List<ImageComment> pics){
    	int i = 0;
    	
    	for(ImageComment pic : pics){
        	doAddPicPanel(i, pic);
    		i++;
    	}
    	
		reDisplayPictures();
    }
    
    public void removePicture(int pos){
    	if (pos>=0 && this.getPicPanels().size()>pos) {
			this.getPicPanels().remove(pos);
			this.reDisplayPictures();
		}
    }
    
    private void reDisplayPictures(){
    	int i = 0;
		getpPics().removeAll();
		
		getpPics().setVisible(false);
		
    	for( JPanel p : this.getPicPanels()){

    		c.gridx = (i%2==0)?0:1;
    		c.gridy = i/2;
    		i++;
    		getpPics().add(p,c);
    	}
    	
		getpPics().setVisible(true);
    	
  		getpPics().revalidate();
    }

	public JPanel getpPics() {
		return pPics;
	}


	public List<PicturePanel> getPicPanels() {
		return picPanels;
	}


	public void setPicPanels(List<PicturePanel> picPanels) {
		this.picPanels = picPanels;
	}
}
