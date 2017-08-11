package image;

import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

public class Test {  
	
 
 
 private void image() throws Exception {
		//缩放操纵
		 IMOperation op = new IMOperation();
		 //添加原路径
		 op.addImage("E:\\Users\\workspace\\console\\src\\main\\webapp\\pic_tmp\\car\\20140707\\14047286651741695_s.jpg");
		 //设置宽
		 op.resize(960,100);
		 //设置高
		 //设置缩放后的路径
		 op.addImage("E:\\Users\\workspace\\console\\src\\main\\webapp\\pic_tmp\\car\\20140707\\14047286651741695_b.jpg");
		 ConvertCmd convert = new ConvertCmd(); 
		//ImageMagick-6.8.9-1-Q16-x86-dll a安装后的路径
		 convert.setSearchPath("E:/work/ImageMagick-6.8.9-Q16");
		 convert.run(op);
		 
	}
	
	//添加水印
	private void watermark() throws IOException, InterruptedException, IM4JavaException {
		IMOperation op=new IMOperation();
		//添加要添加水印的原图片
		op.addImage("D:\\a.jpg");
		//op.rotate(50D);
		//水印 
		op.addImage("D:\\watermarkater.png");
		//水印的位置(4个参数 120,44 是水印大小 15,15是 位置距边角像素 ) 
		op.gravity("southeast").geometry(120,44,15,15);
		//水印添加到图片中   //也是生成的新图片路径
		op.composite().addImage("d:\\efg.jpg");
		//创建图片处理核心类
		 ConvertCmd convert = new ConvertCmd(true);
		 //添加处理核心
		 convert.setSearchPath("D:\\GraphicsMagick-1.3.18-Q8"); 
		 convert.run(op);
	}
	
		//裁剪图片
		private void copy() throws IOException, InterruptedException, IM4JavaException {
			IMOperation op=new IMOperation();
			//添加要添加水印的原图片
			op.addImage("E:\\Users\\workspace\\console\\src\\main\\webapp\\pic_tmp\\car\\20140707\\14047286651741695_s.jpg");
			op.crop(100, 100, 100,100);
			//创建图片处理核心类
			op.addImage("E:\\Users\\workspace\\console\\src\\main\\webapp\\pic_tmp\\car\\20140707\\14047286651741695_c.jpg");
			 ConvertCmd convert = new ConvertCmd();
			 //添加处理核心
			 convert.setSearchPath("E:/work/ImageMagick-6.8.9-Q16"); 
			 convert.run(op);
		}
	public static void main(String[] args) {
		try {
			new Test().watermark();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("图片处理异常");
		}
	}
}  