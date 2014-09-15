package zy.Control.Info.doc.operation;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class GetAddPosDoTraverseDocCommand extends DocTraverse {

	public GetAddPosDoTraverseDocCommand(List<XWPFParagraph> paragraphList,
			ITraverseParagraphCommand command) {
		super(paragraphList, command);
	}

	@Override
	protected boolean breakDoCommand() {
		GetPicPosTraverseParagraphCommand cmd = (GetPicPosTraverseParagraphCommand) this.command;
		if (cmd == null || cmd.getDoc().isFoundMark()) {
			return true;
		}
		return super.breakDoCommand();
	}
}
