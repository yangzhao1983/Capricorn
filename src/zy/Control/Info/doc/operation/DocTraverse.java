package zy.Control.Info.doc.operation;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * Use this class to traverse the doc, and for every run do the Traversal
 * command.
 * 
 * @author yangzhao
 * 
 */
public class DocTraverse implements ITraverseDocCommand {

	protected boolean skipDoCommand(XWPFParagraph paragraph) {
		return false;
	}

	protected boolean breakDoCommand(){
		return false;
	}
	
	@Override
	public void doCommand() {
		if (command == null) {
			return;
		}

		if (paragraphList != null && paragraphList.size() > 0) {
			for (XWPFParagraph paragraph : paragraphList) {
				if(skipDoCommand(paragraph)){
					continue;
				}
				
				List<XWPFRun> runs = paragraph.getRuns();
				command.setRuns(runs);
				command.doCommand();
				if(breakDoCommand()){
					break;
				}
			}
		}
	}

	private List<XWPFParagraph> paragraphList;

	protected ITraverseParagraphCommand command;

	public DocTraverse(List<XWPFParagraph> paragraphList,
			ITraverseParagraphCommand command) {
		this.paragraphList = paragraphList;
		this.command = command;
	}
}
