package zy.Control.Info;

import zy.UI.ImportWord;

/**
 * To transfer the status from import word to pictures 
 * 
 * @author yangzhao
 *
 */
public class WordForm extends LogicForm{

	public WordForm(ImportWord frame) {
		super(frame);
		this.importWord = frame;
	}

	private String pathWord;

	private ImportWord importWord;
	
	public ImportWord getImportWord() {
		return importWord;
	}

	public String getPathWord() {
		return pathWord;
	}

	public void setPathWord(String pathWord) {
		this.pathWord = pathWord;
	}
}
