package zy.Control.Info;

import zy.UI.PicturesFrame;

public class PicturesForm extends LogicForm{

	public PicturesForm(PicturesFrame frame) {
		super(frame);
		this.frame = frame;
	}
	private String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	private PicturesFrame frame;

	private int selPos;
	
	private boolean isAdd;

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public int getSelPos() {
		return selPos;
	}

	public void setSelPos(int selPos) {
		this.selPos = selPos;
	}

	public PicturesFrame getFrame() {
		return frame;
	}
}
