package test;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class TestImage{
	
	private TestImage(){
		
	}
	
	private void imageOp(InputStream inFile, String outFilePath, int width, int height){
		Image image = null;
		try {
			image = ImageIO.read(inFile);
		} catch (IOException e) {
			System.out.println("file path error...");
		}
		
		int originalImageWidth = image.getWidth(null);
		int originalImageHeight = image.getHeight(null);
		
		BufferedImage originalImage = new BufferedImage(
				originalImageWidth,
				originalImageHeight,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = originalImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		
		BufferedImage changedImage =
			new BufferedImage(
					width,
					height,
					BufferedImage.TYPE_3BYTE_BGR);
		
		double widthBo = (double)width/originalImageWidth;
		double heightBo = (double)width/originalImageHeight;
		
		AffineTransform transform = new AffineTransform();
		transform.setToScale(widthBo, heightBo);
		
		AffineTransformOp ato = new AffineTransformOp(transform, null);
		ato.filter(originalImage, changedImage);
		
		File fo = new File(outFilePath); //将要转换出的小图文件 

		try {
			ImageIO.write(changedImage, "jpeg", fo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void imageOp(String inFilePath, String outFilePath, int width, int height){
		File tempFile = new File(inFilePath);
		Image image = null;
		try {
			image = ImageIO.read(tempFile);
		} catch (IOException e) {
			System.out.println("file path error...");
		}
		
		int originalImageWidth = image.getWidth(null);
		int originalImageHeight = image.getHeight(null);
		
		BufferedImage originalImage = new BufferedImage(
				originalImageWidth,
				originalImageHeight,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = originalImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		
		BufferedImage changedImage =
			new BufferedImage(
					width,
					height,
					BufferedImage.TYPE_3BYTE_BGR);
		
		double widthBo = (double)width/originalImageWidth;
		double heightBo = (double)width/originalImageHeight;
		
		AffineTransform transform = new AffineTransform();
		transform.setToScale(widthBo, heightBo);
		
		AffineTransformOp ato = new AffineTransformOp(transform, null);
		ato.filter(originalImage, changedImage);
		
		File fo = new File(outFilePath); //将要转换出的小图文件 

		try {
			ImageIO.write(changedImage, "jpeg", fo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 public static void zoomImage(String src,String dest,int w,int h) throws Exception {
	        
	        double wr=0,hr=0;
	        File srcFile = new File(src);
	        File destFile = new File(dest);

	        BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
	        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板
	        
	        wr=w*1.0/bufImg.getWidth();     //获取缩放比例
	        hr=h*1.0 / bufImg.getHeight();

	        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
	        Itemp = ato.filter(bufImg, null);
	        try {
	            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile); //写入缩减后的图片
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	public static void main(String[] args) throws Exception {
		
		zoomImage("d://watermarkater.png","d://watermarkater3.png",20,20);
//		TestImage t1 = new TestImage();
//		t1.imageOp("d://a.jpg", "d:/b.jpg", 200, 200);

//		InputStream in = new FileInputStream(new File("C:/p02.jpg"));
//		t1.imageOp(in, "C:/p04.jpg", 400, 300);

	}
	
}

