package zy.Control.Info.doc.operation;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;


public class Pos4AddPics {
	private int runPos;
	private XWPFParagraph paragraph;
	public int getRunPos() {
		return runPos;
	}
	public void setRunPos(int runPos) {
		this.runPos = runPos;
	}
	public XWPFParagraph getParagraph() {
		return paragraph;
	}
	public void setParagraph(XWPFParagraph paragraph) {
		this.paragraph = paragraph;
	}
}
