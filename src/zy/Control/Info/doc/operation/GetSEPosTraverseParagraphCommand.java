package zy.Control.Info.doc.operation;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * For every run, get the positions of start Mark and end Mark.
 * 
 * @author yangzhao
 * 
 */
public class GetSEPosTraverseParagraphCommand implements
		ITraverseParagraphCommand {

	private boolean isMarked = false;

	private String startMark;

	private String endMark;

	private List<XWPFRun> runs;

	private CustomXWPFDocument doc;

	public GetSEPosTraverseParagraphCommand(String startMark, String endMark,
			CustomXWPFDocument doc) {
		this.startMark = startMark;
		this.endMark = endMark;
		this.doc = doc;
	}

	@Override
	public void doCommand() {
		if (runs == null || runs.size() == 0) {
			return;
		}
		for (XWPFRun run : runs) {
			String text = run.getText(0);

			if (!isMarked) {
				if (startMark.equals(text)) {
					int posStart = doc.getPosOfParagraph(run.getParagraph());
					isMarked = true;
					doc.getRemovedPos().setPosStart(posStart);
				}
			} else {
				if (endMark.equals(text)) {

					run.setText("", 0);
					isMarked = false;
					int posEnd = doc.getPosOfParagraph(run.getParagraph());
					doc.getRemovedPos().setPosEnd(posEnd);
				}
			}
		}
	}

	@Override
	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}

}
