package zy.Control.Logic;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;

import zy.Control.Info.ImageArrayList;
import zy.Control.Info.ImageComment;
import zy.Control.Info.WordForm;
import zy.Control.Info.Utils.LogicException;
import zy.Control.Info.doc.operation.CustomXWPFDocument;
import zy.UI.UIConst;

public class ImportWordLogic extends CommonLogic {

	private WordForm form;

	public ImportWordLogic(WordForm form) {
		super(form);
		this.form = form;
	}

	@Override
	public void storeInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyUIInput() {
		// TODO Auto-generated method stub

	}

	// Background task for loading images.
	SwingWorker<ImageArrayList<ImageComment>, Void> worker = new SwingWorker<ImageArrayList<ImageComment>, Void>() {
		@Override
		public ImageArrayList<ImageComment> doInBackground(){

			ImageArrayList<ImageComment> imgs = null;

			try {
				OPCPackage pack = POIXMLDocument
						.openPackage(form.getPathWord());
				CustomXWPFDocument doc = new CustomXWPFDocument(pack);
				doc.initializeds(UIConst.START_MARK, UIConst.END_MARK);
				imgs = doc.getImgComments();
			} catch (IOException e) {
				e.printStackTrace();
//				throw new LogicException(this.getClass()+".doInBackground:" + e.getClass(), e); 
			}

			if (imgs == null) {
				return new ImageArrayList<ImageComment>();
			} else {
				return imgs;
			}

		}

		@Override
		public void done() {
			form.getImportWord().getBtnNext().setEnabled(true);
		}
	};

	@Override
	public void setUIInfo() throws LogicException{
		try {
			worker.execute();
			form.getImportWord().setImages(worker.get());
		} catch (InterruptedException e) {
			throw new LogicException(this.getClass()+".setUIInfo:" + e.getClass(), e); 
		} catch (ExecutionException e) {
			throw new LogicException(this.getClass()+".setUIInfo:" + e.getClass(), e); 
		}
	}

}
