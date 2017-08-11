package com.cmp.res.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.aspectj.util.FileUtil;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;

public class Im4javaUtil {
	private static String gmPath;
	private static boolean windowFlag=System.getProperty("os.name").indexOf("Windows")>0;
	
	static{
		InputStream is = Im4javaUtil.class.getResourceAsStream("/shiro.properties");
        
        Properties ps = new Properties();
        //加载properties资源文件
        try {
			ps.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        gmPath=ps.getProperty("gmPath");
       
	}
	
	
	//抽图
	/**
	 * 
	 * @param source   源文件
	 * @param target   目标文件
	 * @param picSize  抽图尺寸
	 * @return
	 */
	public static boolean equalScaling(String source,String target,Integer picSize){
		boolean flag=true;
		//获取原始图片信息判断是否需要抽图
		Map<String,Object> map=getInfo(source);
		if(map!=null&&(((float)(map.get("width")))<picSize)&&(((float)(map.get("height")))<picSize)){
			//不需要抽图
			try {
				FileUtil.copyFile(new File(source), new File(target));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag=false;
			}
		}else{
			//抽图
			GMOperation op = new GMOperation();
			op.resize(picSize);
			op.addImage(source);
			op.addImage(target);
			ConvertCmd cmd = new ConvertCmd(true);
			if(windowFlag){
				cmd.setSearchPath(gmPath);
			}
			try {
				cmd.run(op);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag=false;
				
			}
		}
		return flag;
		
	}
	
	//抽图
		/**
		 * 按百分比抽图
		 * 
		 * @param source   源文件
		 * @param target   目标文件
		 * @param width  宽度百分比
		 * @param height  高度百分比
		 * @return
		 */
		public static boolean equalScaling(String source,String target,Integer width,Integer height){
			boolean flag=true;
			ConvertCmd cmd = new ConvertCmd(true);
			//抽图
			if(windowFlag){
				cmd.setSearchPath(gmPath);
			}
			try {
				GMOperation op = new GMOperation();
				op.addRawArgs("-thumbnail", width+"%x"+height+"%");
				op.addImage(source);
				op.addImage(target);
				
				cmd.run(op);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag=false;
				
			}
		
			return flag;
			
		}
	
		
	public static Map<String,Object> getInfo(String filePath){
		Map<String,Object> map=new HashMap<String,Object>();
		IMOperation op = new IMOperation();
        op.format("%w,%h,%d,%f,%b");
		op.addImage(filePath);
		IdentifyCmd cmd = new IdentifyCmd(true);
		if(windowFlag){
			//windwods系统
			cmd.setSearchPath(gmPath); 
		}
		
		
		ArrayListOutputConsumer output = new ArrayListOutputConsumer();
		cmd.setOutputConsumer(output);
		try {
			cmd.run(op);
			 ArrayList<String> cmdOutput = output.getOutput();
		        if (cmdOutput.size() != 1) return null;
		        String line = cmdOutput.get(0);//样例：1301,898,d:/,test.jpg,669.3K
		        String[] infos=line.split(",");
		        map.put("width", Float.parseFloat(infos[0]));
		        map.put("height", Float.parseFloat(infos[1]));
		        map.put("dir", infos[2]);
		        map.put("fileName", infos[3]);
		        map.put("size", infos[4]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       

	        return map;

	}
	
	public static void waterMark(String waterImagePath, String srcImagePath, String destImagePath, String gravity, int dissolve) {  
        IMOperation op = new IMOperation();  
        op.gravity(gravity); //位置center：中心;northwest：左上;southeast：右下 
        op.dissolve(dissolve); //水印清晰度 ，0-100  最好设置高点要不看起来没效果 
        op.addImage(waterImagePath);  
        op.addImage(srcImagePath);  
        op.addImage(destImagePath);  
        CompositeCmd cmd = new CompositeCmd(true);  
        cmd.setSearchPath(gmPath); 
        try {  
            cmd.run(op);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        } catch (IM4JavaException e) {  
            e.printStackTrace();  
        }  
    } 
	
	  public static void addImgText(String srcPath,String outPath) throws Exception {
	        IMOperation op = new IMOperation();
	        op.font("宋体").gravity("southeast").pointsize(18).fill("#BCBFC8").draw("text 5,5 juziku.com");       
	         
	        op.addImage();
	        op.addImage();
	        ConvertCmd convert = new ConvertCmd(true);
	        convert.setSearchPath(gmPath); 
	        //linux下不要设置此值，不然会报错
	        //convert.setSearchPath(imageMagickPath);
	 
	        convert.run(op,srcPath,outPath);
	    }
	  
	  
	  private static int[] getImageSide(String imgPath) throws IOException {  
	        int[] side = new int[2];  
	        Image img = ImageIO.read(new File(imgPath));  
	        side[0] = img.getWidth(null);  
	        side[1] =img.getHeight(null);  
	        return side;  
	    } 
	  
	  private static synchronized  void watermarkImg(String srcPath,String distPath,String watermarkImg, int width, int height, int x, int y, int alpha) throws IOException, InterruptedException, IM4JavaException{  
	        CompositeCmd cmd = new CompositeCmd(true);    
	        String path = "D://GraphicsMagick-1.3.18-Q8";  
	        cmd.setSearchPath(path);      
	        IMOperation op = new IMOperation();  
	        op.dissolve(alpha);  
	        op.geometry(width, height, x, y);  
	        op.addImage(watermarkImg);    
	        op.addImage(srcPath);    
	        op.addImage(distPath);    
	        cmd.run(op);  
	    }   
	  public static  void WatermarkImg(String srcPath,String distPath,String watermarkImg, int position, int x, int y, int alpha) throws IOException, InterruptedException, IM4JavaException{  
	        int[] watermarkImgSide = getImageSide(watermarkImg);  
	        int[] srcImgSide = getImageSide(srcPath);  
	        int[] xy = getXY(srcImgSide, watermarkImgSide, position, y, x);  
	        watermarkImg(srcPath,distPath,watermarkImg,watermarkImgSide[0],watermarkImgSide[1],xy[0],xy[1],alpha);  
	    } 
	  private static int[] getXY(int[] image, int[] watermark, int position, int x, int y) {  
	        int[] xy = new int[2];  
	        if(position==1){  
	            xy[0] = x;  
	            xy[1] = y;  
	        }else if(position==2){  
	            xy[0] = (image[0]-watermark[0])/2;          //横向边距  
	            xy[1] = y;  //纵向边距  
	        }else if(position==3){  
	            xy[0] = image[0]-watermark[0]-x;  
	            xy[1] = y;  
	        }else if(position==4){  
	            xy[0] = x;  
	            xy[1] = (image[1]-watermark[1])/2;  
	        }else if(position==5){  
	            xy[0] = (image[0]-watermark[0])/2;  
	            xy[1] =  (image[1]-watermark[1])/2;  
	        }else if(position==6){  
	            xy[0] = image[0]-watermark[0]-x;  
	            xy[1] = (image[1] - watermark[1])/2;   
	        }else if(position==7){  
	            xy[0] = x;  
	            xy[1] = image[1] - watermark[1] - y;  
	        }else if(position==8){  
	            xy[0] =  (image[0]-watermark[0])/2;  
	            xy[1] = image[1] - watermark[1] - y;  
	        }else{  
	            xy[0] = image[0]-watermark[0]-x;  
	            xy[1] = image[1] - watermark[1] - y;  
	        }  
	        return xy;  
	    }  
	  
	
	public static void main(String[] args) throws Exception {
		
//		  WatermarkImg("D:\\aaaa.jpg", "D:\\aaaaa.jpg", "D:\\watermarkater.png", 1, 20,20, 20); 
		//addImgText("D:\\a.jpg","D:\\a22.jpg");
		//String waterImagePath, String srcImagePath, String destImagePath, String gravity, int dissolve
		waterMark("D:\\watermarkater.png","D:\\aaaa.jpg","D:\\aaaaa.jpg","southeast",50);
		
//		File originalImage = new File("D:\\a.jpg");
//		resize(originalImage, new File("D:\\2.png"), 1024, 0.7f);
//		resize(originalImage, new File("D:\\1.png"), 1024, 1f);
		//equalScaling("D:\\a.jpg","d://samllpic//2.png");
		
//		File file=new File("d://photo");
//		File[] files=file.listFiles();
//		
//		Date d1=new Date();
//		for(File temp:files){
//			//System.out.println(temp.getName());
//			equalScaling(temp.getAbsolutePath(),"d://samllpic//"+temp.getName());
//		}
//		System.out.println(new Date().getTime()-d1.getTime());
	}

}
