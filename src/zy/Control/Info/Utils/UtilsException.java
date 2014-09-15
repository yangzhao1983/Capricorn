package zy.Control.Info.Utils;

@SuppressWarnings("serial")
public class UtilsException extends Exception{

	private String msg;
	
	public UtilsException(String msg, Throwable t) {
		super(msg,t);
		// TODO Auto-generated constructor stub
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}
}
