package zy.Control.Info.doc.operation;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFRun;

public class GetPicPosTraverseParagraphCommand implements
		ITraverseParagraphCommand {
	private String startMark;
	private Pos4AddPics pos;

	private List<XWPFRun> runs;

	public CustomXWPFDocument getDoc() {
		return doc;
	}

	private CustomXWPFDocument doc;

	public GetPicPosTraverseParagraphCommand(String startMark, Pos4AddPics pos,
			CustomXWPFDocument doc) {
		this.startMark = startMark;
		this.pos = pos;
		this.doc = doc;
	}

	@Override
	public void doCommand() {

		if (pos == null) {
			return;
		}
		for (XWPFRun run : runs) {
			String text = run.getText(0);

			if (!doc.isFoundMark()) {
				if (startMark.equals(text)) {
					doc.setFoundMark(true);

					run.setText("", 0);

					pos.setParagraph(run.getParagraph());
					pos.setRunPos(runs.indexOf(run));
					break;
				}
			}
		}
	}

	@Override
	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}

}
