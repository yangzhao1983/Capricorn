package zy.Control.Info.Utils;

public class DocException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Exception e;
	
	
	private String msg;
	
	public DocException(String msg, Exception e){
		super(msg);
		this.msg = msg;
		this.e = e;
	}
	
	public Exception getE() {
		return e;
	}



	public String getMsg() {
		return msg;
	}
}
