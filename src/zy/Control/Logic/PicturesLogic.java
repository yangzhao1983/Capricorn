package zy.Control.Logic;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import zy.Control.Info.ImageArrayList;
import zy.Control.Info.ImageComment;
import zy.Control.Info.PicturesForm;
import zy.Control.Info.Utils.LogicException;
import zy.Control.Info.Utils.UtilsException;
import zy.Control.Info.Utils.WordUtil;
import zy.Control.Info.doc.operation.CustomXWPFDocument;
import zy.UI.UIConst;

public class PicturesLogic extends CommonLogic {

	private PicturesForm form;

	public PicturesLogic(PicturesForm form) {
		super(form);
		this.form = form;
	}

	@Override
	public void modifyUIInput() {

		if (form.isAdd()) {
			// 1. add pic
			form.getFrame().addPicturePanel(form.getSelPos(), null);
		} else {
			// 2. delete pic
			form.getFrame().removePicture(form.getSelPos());
		}
	}

	@Override
	public void storeInfo() throws LogicException {

		try {
			// 1. create a new word by copying the original word
			String target = WordUtil.getTmpPath(form.getPath());
			if (target != null && !target.trim().equals("")) {
				WordUtil.copyFile(form.getPath(), target);
			}

			// 2. delete the content scanned by mark
			removeContentBetweenMark(form.getPath(), UIConst.START_MARK,
					UIConst.END_MARK);

			// 3. add the generated pictures and comments to the new word
			writePictures2Word(UIConst.START_MARK, UIConst.END_MARK,
					form.getPath(), this.form.getFrame().getPictures());
		} catch (UtilsException e) {
			throw new LogicException("Found in " + this.getClass().toString()
					+ ".storeInfo", e);
		}
	}

	/**
	 * Remove the selected content, which has been marked.
	 * 
	 * @param path
	 * @param startMark
	 * @param endMark
	 */
	private void removeContentBetweenMark(String path, String startMark,
			String endMark) throws LogicException {

		try {
			CustomXWPFDocument doc = WordUtil.generateWord(startMark, endMark,
					path);

			FileOutputStream fopts = new FileOutputStream(
					WordUtil.getTmpPath(path));
			doc.write(fopts);
			fopts.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LogicException("Found in " + this.getClass().toString()
					+ ".removeContentBetweenMark", e);
		} catch (UtilsException e) {
			e.printStackTrace();
			throw new LogicException("Found in " + this.getClass().toString()
					+ ".removeContentBetweenMark", e);
		} catch (IOException e) {
			throw new LogicException("Found in " + this.getClass().toString()
					+ ".removeContentBetweenMark", e);
		}
	}

	/**
	 * Write pictures 2 doc.
	 * 
	 * @param startMark
	 * @param endMark
	 * @param path
	 * @param images
	 * @throws LogicException
	 */
	private void writePictures2Word(String startMark, String endMark,
			String path, ImageArrayList<ImageComment> images)
			throws LogicException {

		try {
			WordUtil.writeImages2Word(startMark, endMark, path, images);
		} catch (UtilsException e) {
			e.printStackTrace();
			throw new LogicException("Found in " + this.getClass().toString()
					+ ".writePictures2Word", e);
		}
	}
}
