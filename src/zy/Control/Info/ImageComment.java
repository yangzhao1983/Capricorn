package zy.Control.Info;

import java.awt.Image;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 * Add a comment to image.
 * 
 * 
 * @author yangzhao
 *
 */
public class ImageComment{

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private XWPFParagraph paragraph;
	
	public XWPFParagraph getParagraph() {
		return paragraph;
	}

	public void setParagraph(XWPFParagraph paragraph) {
		this.paragraph = paragraph;
	}

	private String comment;
	
	private Image image;
	
	private int type;
	
	private int width;
	
	private int height;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
