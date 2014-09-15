package zy.Control.Info.doc.operation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

import zy.Control.Info.ImageArrayList;
import zy.Control.Info.ImageComment;
import zy.Control.Info.RemovedPosition;
import zy.Control.Info.Utils.DocException;
import zy.Control.Info.Utils.ImageUtil;
import zy.Control.Info.Utils.UtilsException;
import zy.Control.Info.Utils.WordUtil;

public class CustomXWPFDocument extends XWPFDocument {

	private RemovedPosition removedPos = new RemovedPosition();

	private boolean foundMark = false;

	public boolean isFoundMark() {
		return foundMark;
	}

	public void setFoundMark(boolean foundMark) {
		this.foundMark = foundMark;
	}

	private ImageArrayList<ImageComment> imgComments = new ImageArrayList<ImageComment>();

	public ImageArrayList<ImageComment> getImgComments() {
		return imgComments;
	}

	// Whether the doc is initialized.
	private boolean initialized = false;

	public boolean isInitialized() {
		return initialized;
	}

	public CustomXWPFDocument(InputStream in) throws IOException {
		super(in);
	}

	public CustomXWPFDocument() {
		super();
	}

	public CustomXWPFDocument(OPCPackage pkg) throws IOException {
		super(pkg);
	}

	/**
	 * To initialize the doc, and the tasks include 1. get the start position
	 * and end position of the content to be removed. 2. get the pictures and
	 * comments.
	 * 
	 * @param startMark
	 * @param endMark
	 * @param images
	 */
	public void initializeds(String startMark, String endMark) {
		if (this.initialized) {
			return;
		} else {
			// set the removed positions
			getRemovedPosition(startMark, endMark);

			// If this is no end mark
			if (!this.isMarked()) {
				return;
			}

			// deal with paragraph
			try {
				getPicsFromParagraphs();
				this.processTables();
			} catch (UtilsException e) {

				e.printStackTrace();
			}

			// deal with table
			this.initialized = true;
		}
	}

	/**
	 * Split the comments, and attach them to the corresponding pics. Note that
	 * more than one comments may exist in the same line, so it is necessary to
	 * split them. TODO It is not clear what is the separator, now just use "’’∆¨"
	 * 
	 * @param images
	 * @param comments
	 */
	public void setCommets4Image(List<ImageComment> images, String comments,
			String sep) {

		List<String> listComments = WordUtil.splitComments(comments, sep);
		if (images.size() >= listComments.size()) {
			for (int i = 0; i < listComments.size(); i++) {
				images.get(images.size() - listComments.size() + i).setComment(
						listComments.get(i));
			}
		}
	}

	private void processTable(XWPFTable table) {
		List<XWPFTableRow> rows = table.getRows();

		int rowIndex = 0;

		boolean toSetSize = true;
		try {
			for (XWPFTableRow row : rows) {
				int cellIndex = 0;
				List<XWPFTableCell> cells = row.getTableCells();

				for (XWPFTableCell cell : cells) {

					List<XWPFParagraph> paragraphList = cell.getParagraphs();

					if (rowIndex == 0 && cellIndex == 0) {
						// the first row and first cell
						getPicturesFromCell(paragraphList, toSetSize);

						// if the first cell contains pictures
						if (this.imgComments.size() > 0) {
							toSetSize = false;
						}
					} else if (rowIndex % 2 == 0) {
						// row with pictures
						// deal with paragraph, assume that one cell only
						// contains one picture.
						getPicturesFromCell(paragraphList, toSetSize);
					} else {
						// row with comments
						getCommentsFromCell(paragraphList, cellIndex);
					}
					cellIndex++;
				}
				rowIndex++;
			}
		} catch (UtilsException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Get comments from cell
	 * 
	 * @param paragraphList
	 * @param imgComments
	 * @param cellIndex
	 * @throws UtilsException
	 */
	private void getCommentsFromCell(List<XWPFParagraph> paragraphList,
			int cellIndex) throws UtilsException {

		StringBuffer sb = new StringBuffer();

		if (paragraphList != null && paragraphList.size() > 0) {
			for (XWPFParagraph paragraph : paragraphList) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs) {
					sb.append(run.getText(0));
				}

			}
			this.imgComments.get(imgComments.size() - (2 - cellIndex))
					.setComment(sb.toString());
		}
	}

	/**
	 * Get pictures from cell
	 * 
	 * @param paragraphList
	 * @param toSetSize
	 * @throws UtilsException
	 */
	private void getPicturesFromCell(List<XWPFParagraph> paragraphList,
			boolean toSetSize) {

		ITraverseDocCommand docCommand = new DocTraverse(paragraphList,
				new GetPicsTraverseParagrahCommand(this, toSetSize));

		docCommand.doCommand();
	}

	/**
	 * Process tables to get the pictures
	 * 
	 * @throws UtilsException
	 */
	private void processTables() throws UtilsException {

		Iterator<XWPFTable> it = this.getTablesIterator();
		while (it.hasNext()) {

			XWPFTable table = it.next();

			// If the table is between the start mark and end mark, then get
			// pictures from it.
			// Here, it is suspected that only one table resides between them.
			int pos = this.getPosOfTable(table);

			if (pos > this.removedPos.getPosStart()
					&& pos < this.removedPos.getPosEnd()) {
				processTable(table);
			}
		}
	}

	/**
	 * Get pictures from paragraph.
	 * 
	 * @throws UtilsException
	 */
	public void getPicsFromParagraphs() throws UtilsException {

		List<XWPFParagraph> paragraphList = this.getParagraphs();

		ITraverseDocCommand docCommand = new BeforeDoTraverseDocCommand(
				paragraphList, new GetPicsFromPTraverseParagrahCommand(this));

		docCommand.doCommand();
	}

	/**
	 * Whether the doc has been marked.
	 * 
	 * @return
	 */
	public boolean isMarked() {

		if (this.removedPos.getPosEnd() == 0
				&& this.removedPos.getPosStart() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public RemovedPosition getRemovedPos() {
		return removedPos;
	}

	/**
	 * Add the pictures to word
	 * 
	 * @param pics
	 * @throws InvalidFormatException
	 */
	private void addPics2Word(ImageArrayList<ImageComment> pics,
			CustomXWPFDocument doc, XWPFParagraph paragraph, int posStart)
			throws DocException {

		if (pics == null || pics.size() == 0) {
			return;
		}

		try {
			// delete the first empty space in a paragraph
			// standardizeParagraph(paragraph);

			XmlCursor cursor = paragraph.getCTP().newCursor();

			// XWPFParagraph cP = doc.insertNewParagraph(cursor);
			XWPFTable table = doc.insertNewTbl(cursor);

			table.getCTTbl().getTblPr().getTblBorders().unsetTop();
			table.getCTTbl().getTblPr().getTblBorders().unsetBottom();
			table.getCTTbl().getTblPr().getTblBorders().unsetInsideH();
			table.getCTTbl().getTblPr().getTblBorders().unsetInsideV();
			table.getCTTbl().getTblPr().getTblBorders().unsetLeft();
			table.getCTTbl().getTblPr().getTblBorders().unsetRight();

			XWPFTableRow tableOneRowOne;

			tableOneRowOne = table.getRow(0);
			XWPFTableCell cell = tableOneRowOne.getCell(0);

			int numOfItem = 0;
			if (pics.size() % 2 == 0) {
				numOfItem = pics.size() * 2;
			} else {
				numOfItem = pics.size() * 2 + 2;
			}

			boolean end = false;
			for (int i = 0; i < numOfItem; i++) {

				String picId = "";
				if (i % 4 < 2 && (i / 2 + i % 2) < pics.size()) {
					byte[] picContent = ImageUtil.getByteContentFromIamge(pics
							.get(i / 2 + i % 2));
					ByteArrayInputStream byteInputStream = new ByteArrayInputStream(
							picContent);
					picId = doc.addPictureData(byteInputStream,
							CustomXWPFDocument.PICTURE_TYPE_JPEG);
				}

				if (i == 0) {
					// if it is the first row, then add the pic, and create new
					// cell.

					XWPFParagraph p = cell.getParagraphs().get(0);

					doc.createPicture(
							picId,
							doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
							pics.getWidth(), pics.getHeight(), p, posStart);

					cell = tableOneRowOne.addNewTableCell();
				} else if (i == 1) {
					// add pic and add a new row
					XWPFParagraph p = cell.getParagraphs().get(0);

					doc.createPicture(
							picId,
							doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
							pics.getWidth(), pics.getHeight(), p, posStart);

					tableOneRowOne = table.createRow();
				} else if (pics.size() % 2 == 1 && i == pics.size() * 2 - 1) {

					end = true;
					continue;
				} else if (i % 4 == 1) {
					// add pic and add a new row
					XWPFParagraph p = tableOneRowOne.getCell(1).getParagraphs()
							.get(0);

					doc.createPicture(
							picId,
							doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
							pics.getWidth(), pics.getHeight(), p, posStart);

					tableOneRowOne = table.createRow();

				} else if (i % 4 == 0) {
					// add pic
					XWPFParagraph p = tableOneRowOne.getCell(0).getParagraphs()
							.get(0);
//					System.out.println(tableOneRowOne.getCell(0)
//							.getParagraphs().size());
					doc.createPicture(
							picId,
							doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG),
							pics.getWidth(), pics.getHeight(), p, posStart);
				} else if (i % 4 == 2) {

					// add comments
					XWPFParagraph p = tableOneRowOne.getCell(0).getParagraphs()
							.get(0);

					p.createRun().setText(
							pics.get(i / 2 + i % 2 - 1).getComment());
					if (end) {
						break;
					}
				} else if (i % 4 == 3) {
					// add comments
					XWPFParagraph p = tableOneRowOne.getCell(1).getParagraphs()
							.get(0);
					p.createRun().setText(
							pics.get(i / 2 + i % 2 - 1).getComment());
					// add new row
					tableOneRowOne = table.createRow();
				}

			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			throw new DocException("In " + this.getClass().toString()
					+ ".addPics2Word.InvalidFormatException", e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new DocException("In " + this.getClass().toString()
					+ ".addPics2Word.IOException", e);
		}
	}

	private void writeImages2Paragraph(List<XWPFParagraph> paragraphList,
			String startMark, ImageArrayList<ImageComment> images)
			throws DocException {

		Pos4AddPics p4Pics = new Pos4AddPics();
		ITraverseDocCommand docCommand = new GetAddPosDoTraverseDocCommand(
				paragraphList, new GetPicPosTraverseParagraphCommand(startMark,
						p4Pics, this));

		docCommand.doCommand();

		if (this.foundMark) {
			try {
				addPics2Word(images, this, p4Pics.getParagraph(),
						p4Pics.getRunPos());
			} catch (DocException e) {
				throw e;
			}

			finally {
				this.setFoundMark(false);
			}
		}
	}

	/**
	 * Find the position of the start mark, remove the start mark and then write
	 * the pictures to the doc after the start mark.
	 * 
	 * @param startMark
	 * @param images
	 */
	public void writeImages2Word(String startMark,
			ImageArrayList<ImageComment> images) throws DocException {
		
		// deal with paragraph
		writeImages2Paragraph(this.getParagraphs(), startMark, images);

		// deal with the table
		Iterator<XWPFTable> it = this.getTablesIterator();
		while (it.hasNext()) {
			XWPFTable table = it.next();

			List<XWPFTableRow> rows = table.getRows();
			for (XWPFTableRow row : rows) {
				List<XWPFTableCell> cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {
					List<XWPFParagraph> paragraphListTable = cell
							.getParagraphs();
					writeImages2Paragraph(paragraphListTable, startMark, images);
				}
			}
		}
	}

	/**
	 * Remove the content between start mark and end mark.
	 * 
	 * 
	 * @param markStart
	 * @param markEnd
	 */
	public void removeContentBetweenMark(String markStart, String markEnd) {
		getRemovedPosition(markStart, markEnd);

		for (int i = this.getRemovedPos().getPosEnd(); i > this.getRemovedPos()
				.getPosStart(); i--) {
			this.removeBodyElement(i);
		}
	}

	/**
	 * Get the removed position, which is between the start mark and end mark.
	 * 
	 * @param startMark
	 * @param endMark
	 * @return
	 */
	public void getRemovedPosition(String startMark, String endMark) {

		if (this.initialized || WordUtil.isEmptyString(startMark)
				|| WordUtil.isEmptyString(endMark)) {
			return;
		}

		// deal with paragraph
		List<XWPFParagraph> paragraphList = this.getParagraphs();

		ITraverseDocCommand docCommand = new DocTraverse(paragraphList,
				new GetSEPosTraverseParagraphCommand(startMark, endMark, this));

		docCommand.doCommand();
	}

	/**
	 * @param id
	 * @param width
	 *            øÌ
	 * @param height
	 *            ∏ﬂ
	 * @param paragraph
	 *            ∂Œ¬‰
	 */
	public XWPFRun createPicture(String blipId, int id, long width,
			long height, XWPFParagraph paragraph, int pos) {

		XWPFRun run = paragraph.insertNewRun(pos);

		CTInline inline = run.getCTR().addNewDrawing().addNewInline();

		String picXml = ""
				+ "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
				+ "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
				+ "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
				+ "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
				+ id
				+ "\" name=\"Generated\"/>"
				+ "            <pic:cNvPicPr/>"
				+ "         </pic:nvPicPr>"
				+ "         <pic:blipFill>"
				+ "            <a:blip r:embed=\""
				+ blipId
				+ "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
				+ "            <a:stretch>"
				+ "               <a:fillRect/>"
				+ "            </a:stretch>"
				+ "         </pic:blipFill>"
				+ "         <pic:spPr>"
				+ "            <a:xfrm>"
				+ "               <a:off x=\"0\" y=\"0\"/>"
				+ "               <a:ext cx=\""
				+ width
				+ "\" cy=\""
				+ height
				+ "\"/>"
				+ "            </a:xfrm>"
				+ "            <a:prstGeom prst=\"rect\">"
				+ "               <a:avLst/>"
				+ "            </a:prstGeom>"
				+ "         </pic:spPr>"
				+ "      </pic:pic>"
				+ "   </a:graphicData>" + "</a:graphic>";

		inline.addNewGraphic().addNewGraphicData();
		XmlToken xmlToken = null;
		try {
			xmlToken = XmlToken.Factory.parse(picXml);
		} catch (XmlException xe) {
			xe.printStackTrace();
		}

		inline.set(xmlToken);

		inline.setDistT(0);
		inline.setDistB(0);
		inline.setDistL(0);
		inline.setDistR(0);

		CTPositiveSize2D extent = inline.addNewExtent();
		extent.setCx(width);
		extent.setCy(height);

		CTNonVisualDrawingProps docPr = inline.addNewDocPr();
		docPr.setId(id);
		docPr.setName("Õº∆¨" + id);
		docPr.setDescr("≤‚ ‘");

		return run;
	}
}
