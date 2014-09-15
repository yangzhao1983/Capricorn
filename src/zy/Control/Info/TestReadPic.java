package zy.Control.Info;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.xmlbeans.XmlException;

public class TestReadPic {

	public static void main(String...strings){
		
        try {
			OPCPackage opcPackage = POIXMLDocument.openPackage("D:\\useful files\\private\\project\\word generator\\simple.docx");  
			POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);  
			String text2007 = extractor.getText();  
			System.out.println(text2007);
			
			XWPFDocument doc = new XWPFDocument(opcPackage);
			XWPFPictureData pic = doc.getAllPictures().get(0);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(pic.getData());
	        Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
	        //ImageIO is a class containing static convenience methods for locating ImageReaders
	        //and ImageWriters, and performing simple encoding and decoding. 
	 
	        ImageReader reader = (ImageReader) readers.next();
	        Object source = bis; // File or InputStream, it seems file is OK
	 
	        ImageInputStream iis = ImageIO.createImageInputStream(source);
	        //Returns an ImageInputStream that will take its input from the given Object
	 
	        reader.setInput(iis, true);
	        ImageReadParam param = reader.getDefaultReadParam();
	 
	        Image image = reader.read(0, param);
	        //got an image file
	 
	        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
	        //bufferedImage is the RenderedImage to be written
	        Graphics2D g2 = bufferedImage.createGraphics();
	        g2.drawImage(image, null, null);
	        File imageFile = new File("D:\\useful files\\private\\project\\word generator\\1.jpg");
	        ImageIO.write(bufferedImage, "jpg", imageFile);
	        //"jpg" is the format of the image
	        //imageFile is the file to be written to.
	 
	        System.out.println(imageFile.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OpenXML4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
