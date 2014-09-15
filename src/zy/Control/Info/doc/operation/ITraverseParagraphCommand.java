package zy.Control.Info.doc.operation;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFRun;


public interface ITraverseParagraphCommand {

	public void doCommand();
	public void setRuns(List<XWPFRun> runs);
}
