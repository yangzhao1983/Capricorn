package zy.Control.Logic;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import zy.Control.Info.LogicForm;
import zy.Control.Info.Utils.LogicException;
import zy.UI.ImportWord;

public class CommonLogic implements Logic {

	private LogicForm form;

	public CommonLogic(LogicForm form) {
		this.form = form;
	}

	@Override
	public void navigateTo(JFrame target) {
		if (target == null) {
			// [cancel] was clicked and exit frame with nothing done.
			form.getFrame()
					.dispatchEvent(
							new WindowEvent(form.getFrame(),
									WindowEvent.WINDOW_CLOSING));
		} else {
			// else
			// Display the window.
			target.pack();
			
	        target.setLocationRelativeTo(null);
	        
			target.setVisible(true);

			// if source is the word import frame
			if (form.getFrame() instanceof ImportWord) {
				form.getFrame().setVisible(false);
			} else {
				form.getFrame().dispose();
			}
		}
	}

	@Override
	public void storeInfo() throws LogicException{
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyUIInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUIInfo() throws LogicException{
	}

}
