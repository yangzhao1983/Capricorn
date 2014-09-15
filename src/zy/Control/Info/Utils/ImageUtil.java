package zy.Control.Info.Utils;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import zy.Control.Info.ImageComment;
import zy.Control.Info.doc.operation.CustomXWPFDocument;

public class ImageUtil {

	/**
	 * Convert XWPFPictureData to Image, which can be displayed in UI.
	 * 
	 * @param pic
	 * @return
	 */
	public static Image convertPicData2Image(XWPFPictureData pic)
			throws IOException {

		Image image = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(pic.getData());
		Iterator<?> readers = ImageIO.getImageReadersByFormatName(ImageUtil
				.getPictureType(pic.getPictureType()));
		// ImageIO is a class containing static convenience methods
		// for locating ImageReaders
		// and ImageWriters, and performing simple encoding and
		// decoding.

		if (readers.hasNext()) {
			ImageReader reader = (ImageReader) readers.next();
			Object source = bis; // File or InputStream, it seems file
									// is OK

			try {
				ImageInputStream iis = ImageIO.createImageInputStream(source);
				// Returns an ImageInputStream that will take its input from
				// the given Object

				reader.setInput(iis, true);
				ImageReadParam param = reader.getDefaultReadParam();

				image = reader.read(0, param);
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return image;

	}

	/**
	 * 
	 * @param image
	 * @return
	 */
	public static byte[] getByteContentFromIamge(ImageComment image)
			throws IOException {

		byte[] content = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write((RenderedImage) image.getImage(),
					getPictureType(image.getType()), bos);
			content = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return content;
	}

	/**
	 * Get the corresponding type code of the picture, according to the type of
	 * the picture.
	 * 
	 * @param picType
	 * @return String
	 */
	public static String getPictureType(int picType) {
		String res = "jpg";
		if (picType == CustomXWPFDocument.PICTURE_TYPE_PNG) {
			res = "png";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_DIB) {
			res = "dib";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_EMF) {
			res = "emf";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_JPEG) {
			res = "jpeg";
		} else if (picType == CustomXWPFDocument.PICTURE_TYPE_WMF) {
			res = "wmf";
		}
		return res;
	}
}
