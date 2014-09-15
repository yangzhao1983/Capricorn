package zy.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zy.Control.Info.ImageComment;
import zy.Control.Info.PicturesForm;
import zy.Control.Logic.Logic;
import zy.Control.Logic.PicturesLogic;

public class PicturePanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageComment getPic(){
		ImageComment imgComment = this.jpPic.getImage();
		imgComment.setComment(this.jtfComments.getText().trim());
		return imgComment;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		PicturesForm picsForm = new PicturesForm(this.parent);
		
		int pos = this.parent.getPicPanels().indexOf(this);
		
		picsForm.setAdd(true);

		
        if (e.getSource() == btnDel) {
            //Create and set up the window.
    		
    		picsForm.setAdd(false);
    		picsForm.setSelPos(pos);

        } else if (e.getSource() == btnAddAfter){
    		picsForm.setSelPos(pos+1);
        } else{
    		picsForm.setSelPos(pos);
        }
        Logic logic = new PicturesLogic(picsForm);
		logic.modifyUIInput();
	}

	private JTextField jtfComments;
	
	private PicPanel jpPic;
	
	private JButton btnDel;
	
	private JButton btnAddAfter;
	
	private JButton btnAddBefore;
	
	private PicturesFrame parent;
	
	private ImageComment picture;
	
	/**
	 * Constructor, taking 2 parameters.
	 * 
	 * @param parent the parent component.
	 * @param image the image to be displayed; if it is null, then an empty picPanel will be created. 
	 */
	public PicturePanel(PicturesFrame parent, ImageComment image){
		this.parent = parent;
		this.picture = image;
		init();
	}
	
	public void init(){
    	
        this.setPreferredSize(new Dimension(300, 350));
        
        this.setMaximumSize(new Dimension(300, 350));
        
        this.setMinimumSize(new Dimension(300, 350));
        
        this.setLayout(new BorderLayout());
        
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        //Set up the content pane.
        addComponentsToPane();
	}
	
    public void addComponentsToPane() {
        
        //Make the center component big, since that's the
        //typical usage of BorderLayout.
//        Border paneEdge = BorderFactory.createEmptyBorder(10,10,10,10);
        
        jtfComments = new JTextField();
        jtfComments.setPreferredSize(new Dimension(200, 20));
        if (this.picture != null) {
			jtfComments.setText(this.picture.getComment());
		}
		jpPic = new PicPanel(this.picture);

        jpPic.setPreferredSize(new Dimension(200, 280));
    
        
        btnDel = new JButton(UIConst.BTN_DELETE_TITLE);
        btnDel.setPreferredSize(new Dimension(200, 30));
        btnDel.addActionListener(this);
        
        btnAddAfter = new JButton(UIConst.BTN_ADD_AFTER_TITLE);
        btnAddAfter.setPreferredSize(new Dimension(200, 30));
        btnAddAfter.addActionListener(this);
        
        btnAddBefore = new JButton(UIConst.BTN_ADD_BEFORE_TITLE);
        btnAddBefore.setPreferredSize(new Dimension(200, 30));
        btnAddBefore.addActionListener(this);
        
        GridLayout btnLayout = new GridLayout(0,3);
        JPanel p1 = new JPanel();
        p1.setLayout(btnLayout);
        
        p1.add(btnDel);
        p1.add(btnAddAfter);
        p1.add(btnAddBefore);
        
        JPanel panel = new JPanel();
        
        BoxLayout experimentLayout = new BoxLayout(panel,BoxLayout.PAGE_AXIS);
        panel.setLayout(experimentLayout);
//      panel.setBorder(paneEdge);

        panel.add(jtfComments);
        panel.add(p1);        
        panel.add(jpPic); 
        
        this.add(panel, BorderLayout.CENTER);        
    }
}
