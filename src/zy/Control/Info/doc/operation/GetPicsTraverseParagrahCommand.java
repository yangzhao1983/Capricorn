package zy.Control.Info.doc.operation;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.Control.Info.ImageComment;
import zy.Control.Info.Utils.ImageUtil;

/**
 * Traverse every runs to get the pictures.
 * 
 * @author yangzhao
 * 
 */
public class GetPicsTraverseParagrahCommand implements
		ITraverseParagraphCommand {

	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}

	private List<XWPFRun> runs;
	private CustomXWPFDocument doc;
	private boolean toSetSize;

	@Override
	public void doCommand() {

		ImageComment imgC;

		if (runs == null || runs.size() == 0) {
			return;
		}

		for (XWPFRun run : runs) {

			if (run.getEmbeddedPictures().size() > 0) {

				imgC = new ImageComment();

				XWPFPicture pic = run.getEmbeddedPictures().get(0);

				if (toSetSize) {

					doc.getImgComments().setHeight(
							pic.getCTPicture().getSpPr().getXfrm().getExt()
									.getCy());
					doc.getImgComments().setWidth(
							pic.getCTPicture().getSpPr().getXfrm().getExt()
									.getCx());
				}

				try {
					imgC.setImage(ImageUtil.convertPicData2Image(pic
							.getPictureData()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				doc.getImgComments().add(imgC);
			}
		}
	}

	public GetPicsTraverseParagrahCommand(CustomXWPFDocument doc,
			boolean toSetSize) {
		this.doc = doc;
		this.toSetSize = toSetSize;
	}
}
