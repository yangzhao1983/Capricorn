package zy.Control.Info;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import zy.Control.Info.doc.operation.CustomXWPFDocument;

/**
 * This class is the abstraction of pictures. One Object of this class will
 * contain the following properties:
 * 
 * width: width 
 * height: height 
 * picType: gif, jpg, and so on 
 * byteArray: the content of the picture should be transformed to byte format. 
 * pos: the position of the picture in the
 * paragraph
 * 
 * @author yangzhao
 * 
 */
public class PictureObj {

	public CustomXWPFDocument getDoc() {
		return doc;
	}

	public void setDoc(CustomXWPFDocument doc) {
		this.doc = doc;
	}

	public XWPFParagraph getParagraph() {
		return paragraph;
	}

	public void setParagraph(XWPFParagraph paragraph) {
		this.paragraph = paragraph;
	}

	private int width;
	private int height;
	private int picType;
	private byte[] byteArray;
	private int pos;
	private CustomXWPFDocument doc;
	private XWPFParagraph paragraph;
	
	public PictureObj(int width, int height, int picType, byte[] byteArray,int pos) {
		super();
		this.width = width;
		this.height = height;
		this.picType = picType;
		this.byteArray = byteArray;
		this.pos = pos;
	}

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

	public int getPicType() {
		return picType;
	}

	public void setPicType(int picType) {
		this.picType = picType;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
}
