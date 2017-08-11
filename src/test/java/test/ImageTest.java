package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTest {
	public static void main(String[] args) throws IOException {
		//图片旋转
//		ImageUtils.fromFile(new File("d://d.png")).scale(1)
//		.rotate(90)		//旋转角度
//		.quality(1)
//		.bgcolor(Color.BLUE)
//		.toFile(new File("d:\\test.jpg"));
		
		
		BufferedImage watermarkImage = ImageIO.read(new File("d://watermarkater.png"));
		
		Watermark watermark = new Watermark(Positions.CENTER,
				watermarkImage, 0.3f);
		
//		ImageUtils.fromFile(new File("d://d.png"))
//		.scale(1).
		
//		watermarkImage.
//		ImageUtils.
//		ImageIO.write(watermarkImage, "jpg", new File("d://d.png"));
		
		ImageUtils.fromFile(new File("d://a.jpg")).scale(1).keepRatio(true).size(200, 200).toFile(new File("d://b.jpg"));
		
	}
	
	

}
