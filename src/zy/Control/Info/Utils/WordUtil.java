package zy.Control.Info.Utils;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;

import zy.Control.Info.ImageArrayList;
import zy.Control.Info.ImageComment;
import zy.Control.Info.doc.operation.CustomXWPFDocument;

public class WordUtil {

	/**
	 * Write the images to word doc.
	 * 
	 * @param startMark
	 * @param endMark
	 * @param path
	 * @param tmpPath
	 * @param images
	 */
	public static void writeImages2Word(String startMark, String endMark,
			String path, ImageArrayList<ImageComment> images)
			throws UtilsException {

		CustomXWPFDocument doc = null;
		FileOutputStream fopts = null;
		try {
			OPCPackage pack = POIXMLDocument.openPackage(WordUtil
					.getTmpPath(path));
			doc = new CustomXWPFDocument(pack);

			doc.writeImages2Word(startMark, images);

			fopts = new FileOutputStream(WordUtil.getTmpPath(path, "target"));
			doc.write(fopts);

		} catch (IOException e) {
			e.printStackTrace();
			throw new UtilsException("In WordUtils.writeImages2Word.IOException", e);
		} catch (DocException e1){
			e1.printStackTrace();
			throw new UtilsException("In WordUtils.writeImages2Word.DocException", e1);
		} 
		finally {
			try {
				fopts.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new UtilsException("In WordUtils.writeImages2Word.IOException", e);
			}
		}
	}

	/**
	 * Create word doc from template and parameters.
	 * 
	 * @param param
	 *            parameters for substitution.
	 * @param template
	 */
	public static CustomXWPFDocument generateWord(String markStart,
			String markEnd, String tmpPath) throws UtilsException {
		CustomXWPFDocument doc = null;

		try {
			OPCPackage pack = POIXMLDocument.openPackage(tmpPath);
			doc = new CustomXWPFDocument(pack);

			// deal with paragraph
			doc.removeContentBetweenMark(markStart, markEnd);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UtilsException("In WordUtils.generateWord", e);
		}

		return doc;
	}

	/**
	 * Deal with the paragraph, delete the paragraph between start mark and the
	 * end mark. Then, remove the tables between them.
	 * 
	 * @param paragraphList
	 * @throws UtilsException
	 */
	// public static RemovedPosition processParagraphsDel(
	// List<XWPFParagraph> paragraphList, String markStart,
	// String markEnd, CustomXWPFDocument doc) throws UtilsException {
	//
	// int posStart = 0;
	// int posEnd = 0;
	// boolean isMarked = false;
	// if (paragraphList != null && paragraphList.size() > 0) {
	// for (XWPFParagraph paragraph : paragraphList) {
	// List<XWPFRun> runs = paragraph.getRuns();
	// for (XWPFRun run : runs) {
	// String text = run.getText(0);
	//
	// if (!isMarked) {
	// if (markStart.equals(text)) {
	// isMarked = true;
	// posStart = doc.getPosOfParagraph(paragraph);
	// }
	// } else {
	// if (markEnd.equals(text)) {
	// isMarked = false;
	// run.setText("", 0);
	// posEnd = doc.getPosOfParagraph(paragraph);
	// } else if (run.getEmbeddedPictures().size() > 0) {
	// run.getCTR().removeDrawing(0);
	// } else {
	// run.setText("", 0);
	// }
	// }
	// }
	// }
	// }
	//
	// RemovedPosition position = new RemovedPosition();
	//
	// position.setPosStart(posStart);
	// position.setPosEnd(posEnd);
	//
	// return position;
	// }

	/**
	 * Traverse node tree, if anyone has the given attribute, delete it.
	 * 
	 * @param node
	 * @param attr
	 * @return
	 */
	// private static boolean removeAttribute(Node node, String attr) {
	// if (node == null || attr == null) {
	// return false;
	// }
	//
	// if (node.getAttributes().getNamedItem(attr) != null) {
	// node.getAttributes().removeNamedItem(attr);
	// return true;
	// }
	//
	// int num = node.getChildNodes().getLength();
	// Node n;
	// for (int i = 0; i < num; i++) {
	// n = node.getChildNodes().item(i);
	// if (removeAttribute(n, attr)) {
	// return true;
	// }
	// }
	// return false;
	// }

	/**
	 * <w:p w:rsidR="00005EFA" w:rsidRDefault="00005EFA" w:rsidP="006A5C9D">
	 * <w:pPr> <w:spacing w:line="360" w:lineRule="auto" /> <w:ind w:left="2" />
	 * <w:rPr> <w:rFonts w:ascii="·ÂËÎ_GB2312" w:eastAsia="·ÂËÎ_GB2312" w:hAnsi="ËÎÌå"
	 * /> <w:sz w:val="24" /> </w:rPr> </w:pPr> <w:r> <w:t xml:space="preserve">
	 * </w:t> </w:r> <w:r> <w:drawing> ...
	 * 
	 * 
	 * 
	 * @param paragraph
	 */
	// private static void standardizeParagraph(XWPFParagraph paragraph) {
	//
	// removeAttribute(paragraph.getCTP().getDomNode(), "w:firstLineChars");
	// removeAttribute(paragraph.getCTP().getDomNode(), "w:leftChars");
	// paragraph.setIndentationFirstLine(2);
	// paragraph.setIndentationLeft(2);
	//
	// paragraph.setSpacingLineRule(LineSpacingRule.AUTO);
	// paragraph.getCTP().getPPr().getSpacing().setLine(new BigInteger("360"));
	// paragraph.getCTP().getPPr().getInd().setLeft(new BigInteger("2"));
	//
	// }

	/**
	 * Get the corresponding type code of the picture, according to the type of
	 * the picture.
	 * 
	 * @param picType
	 * @return int
	 */
	// private static int getPictureType(String picType) {
	// int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
	// if (picType != null) {
	// if (picType.equalsIgnoreCase("png")) {
	// res = CustomXWPFDocument.PICTURE_TYPE_PNG;
	// } else if (picType.equalsIgnoreCase("dib")) {
	// res = CustomXWPFDocument.PICTURE_TYPE_DIB;
	// } else if (picType.equalsIgnoreCase("emf")) {
	// res = CustomXWPFDocument.PICTURE_TYPE_EMF;
	// } else if (picType.equalsIgnoreCase("jpg")
	// || picType.equalsIgnoreCase("jpeg")) {
	// res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
	// } else if (picType.equalsIgnoreCase("wmf")) {
	// res = CustomXWPFDocument.PICTURE_TYPE_WMF;
	// }
	// }
	// return res;
	// }

	/**
	 * Write the data of the input stream to byte[].
	 * 
	 * @param in
	 * @return
	 */
	public static byte[] inputStream2ByteArray(InputStream in, boolean isClose)
			throws UtilsException {
		byte[] byteArray = null;
		try {
			int total = in.available();
			byteArray = new byte[total];
			in.read(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UtilsException("In WordUtils.inputStream2ByteArray", e);
		} finally {
			if (isClose) {
				try {
					in.close();
				} catch (Exception e2) {
					e2.printStackTrace();
					throw new UtilsException(
							"In WordUtils.inputStream2ByteArray:Failed to close the stream!!",
							e2);
				}
			}
		}
		return byteArray;
	}

	/**
	 * Get pictures from file.
	 * 
	 * @param path
	 * @return
	 * @throws UtilsException
	 */
	public static ImageComment getPicture(String path) throws UtilsException {
		File imageFile = new File(path);
		ImageComment imgComment = new ImageComment();
		Image image = null;
		try {
			image = ImageIO.read(imageFile);
			imgComment.setImage(image);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UtilsException("In WordUtils.getPicture", e);
		}
		return imgComment;
	}

	public static void main(String... strings) {
		// generateWord("D:\\useful files\\private\\project\\word generator\\simple.docx");

		splitComments("ÕÕÆ¬1 ÕÕÆ¬2", "ÕÕÆ¬");
	}

	public static String getTmpPath(String path, String suffix) {

		if (suffix == null || suffix.trim().equals("")) {
			suffix = "tmp";
		}

		String tmpPath = "";
		if (path != null) {
			int index = path.lastIndexOf(".");
			if (index >= 0) {
				tmpPath = path.substring(0, index) + "." + suffix + "."
						+ "docx";
			} else {
				tmpPath = path + "." + suffix + ".docx";
			}
		}

		return tmpPath;
	}

	/**
	 * Get temporary path
	 * 
	 * @param path
	 * @return
	 */
	public static String getTmpPath(String path) {
		return getTmpPath(path, "");
	}

	/**
	 * Copy file from old path to new path.
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyFile(String oldPath, String newPath)
			throws UtilsException {
		// int bytesum = 0;
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];

				while ((byteread = inStream.read(buffer)) != -1) {
					// bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new UtilsException(
					"In WordUtils.copyFile.FileNotFoundException", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UtilsException("In WordUtils.copyFile.IOException", e);
		}
	}

	/**
	 * Multi-comments may exist in the same
	 * 
	 * @param comments
	 * @return
	 */
	public static List<String> splitComments(String comments, String mark) {
		List<String> list = new ArrayList<String>();
		List<Integer> indexList = new ArrayList<Integer>();
		if (comments != null) {
			Pattern p = Pattern.compile(mark);
			Matcher matcher = p.matcher(comments);
			while (matcher.find()) {
				indexList.add(matcher.start());
			}
			for (int i = 0; i < indexList.size(); i++) {
				if (i == indexList.size() - 1) {
					list.add(comments.substring(indexList.get(i)));
				} else {
					list.add(comments.substring(indexList.get(i),
							indexList.get(i + 1) - 1));
				}
			}
		}
		return list;
	}

	/**
	 * If string is null or contains nothing than "", then it is empty string.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}
}
