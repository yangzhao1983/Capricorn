package zy.Control.Info.doc.operation;

import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.Control.Info.ImageComment;
import zy.Control.Info.Utils.ImageUtil;
import zy.UI.UIConst;

public class GetPicsFromPTraverseParagrahCommand implements
		ITraverseParagraphCommand {

	private List<XWPFRun> runs;
	private boolean hasSize = false;
	private CustomXWPFDocument doc;
	private boolean appendComments = false;
	private boolean firstPic = false;
	private StringBuilder sb = new StringBuilder();

	public GetPicsFromPTraverseParagrahCommand(CustomXWPFDocument doc) {
		this.doc = doc;
	}

	@Override
	public void doCommand() {
		if (runs == null || runs.size() == 0) {
			return;
		}
		ImageComment imgC;

		for (XWPFRun run : runs) {

			if (run.getEmbeddedPictures().size() > 0) {
				for (XWPFPicture pic : run.getEmbeddedPictures()) {
					if (!hasSize) {

						doc.getImgComments().setHeight(
								pic.getCTPicture().getSpPr().getXfrm().getExt()
										.getCy());
						doc.getImgComments().setWidth(
								pic.getCTPicture().getSpPr().getXfrm().getExt()
										.getCx());
						hasSize = true;

					}
					appendComments = true;
					if (!firstPic) {
						firstPic = true;
					} else {
						if (!sb.toString().trim().equals("")) {
							this.doc.setCommets4Image(
									this.doc.getImgComments(), sb.toString(),
									"’’∆¨");
							sb.delete(0, sb.length());
							firstPic = false;
						}
					}
					imgC = new ImageComment();
					try {
						imgC.setImage(ImageUtil.convertPicData2Image(pic
								.getPictureData()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.doc.getImgComments().add(imgC);
				}
			} else {
				if (appendComments) {
					sb.append(run.getText(0));
				}
			}
		}
		doc.setCommets4Image(doc.getImgComments(), sb.toString(), UIConst.PIC_COMMENTS_MARK);
	}

	@Override
	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}

}
