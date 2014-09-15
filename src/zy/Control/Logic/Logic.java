package zy.Control.Logic;

import javax.swing.JFrame;

import zy.Control.Info.Utils.LogicException;

/**
 * This interface will used by Action/Listener to do the tasked invoked by the
 * event.
 * 
 * 
 * @author yangzhao
 * 
 */
public interface Logic {

	/**
	 * Navigate from one frame to another frame
	 * 
	 * @author yangzhao
	 * 
	 */
	public void navigateTo(JFrame target);

	/**
	 * Store the information
	 * 
	 */
	public void storeInfo() throws LogicException;
	
	/**
	 * Modify the input info of the UI
	 * 
	 */
	public void modifyUIInput();
	
	/**
	 * Set UI Info
	 */
	public void setUIInfo() throws LogicException;
}
