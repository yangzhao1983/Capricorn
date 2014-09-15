package zy.Control.Info.doc.operation;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class BeforeDoTraverseDocCommand extends DocTraverse {

	public BeforeDoTraverseDocCommand(List<XWPFParagraph> paragraphList,
			ITraverseParagraphCommand command) {
		super(paragraphList, command);
	}

	@Override
	public boolean skipDoCommand(XWPFParagraph paragraph) {

		CustomXWPFDocument doc = (CustomXWPFDocument) paragraph.getDocument();
		// If the paragraph is between the start mark and end mark, then
		// get pictures from it.
		int pos = paragraph.getDocument().getPosOfParagraph(paragraph);
		if (pos < doc.getRemovedPos().getPosStart()
				|| pos >= doc.getRemovedPos().getPosEnd()) {
			return true;
		} else {
			return false;
		}
	}
}
