package zy.Control.Info.Utils;

/**
 * Exception for logic level.
 * 
 * @author yangzhao
 *
 */
public class LogicException extends Exception{

	private Exception e;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String msg;
	
	public LogicException(String msg, Exception e){
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
