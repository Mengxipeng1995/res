package image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.chainsaw.Main;
import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.im4java.process.ArrayListOutputConsumer;

public class Im4JavaUtil {
	
	public static void main(String[] args) throws Exception {
//		rotate();
//		getInfo();
		
		equalScaling();
//		Object a=1;
//		if(((float)a)>0){
//			
//		}
	}
	/**
	 * 旋转
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void rotate() throws IOException, InterruptedException, IM4JavaException{
		IMOperation op = new IMOperation();
        op.addImage("d:\\a.jpg");
        op.rotate(45d);
        op.addImage("d:\\efg.jpg");
        ConvertCmd cmd = new ConvertCmd(true);
		cmd.setSearchPath("D:\\GraphicsMagick-1.3.18-Q8"); 
		cmd.run(op);
	}
	
	/**
	 * 裁剪图片
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void cutPic() throws IOException, InterruptedException, IM4JavaException{
		IMOperation op = new IMOperation();
        op.addImage("d:\\a.jpg");
        op.crop(200, 200, 20, 20);
        op.addImage("d:\\efg.jpg");
        ConvertCmd cmd = new ConvertCmd(true);
		cmd.setSearchPath("D:\\GraphicsMagick-1.3.18-Q8"); 
		cmd.run(op);
	}
	/**
	 * 获取图片信息
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void getInfo() throws IOException, InterruptedException, IM4JavaException{
		IMOperation op = new IMOperation();
        op.format("%w,%h,%d,%f,%b");
		op.addImage("d://test.jpg");
		IdentifyCmd cmd = new IdentifyCmd(true);
		cmd.setSearchPath("D:\\GraphicsMagick-1.3.18-Q8"); 
		
		ArrayListOutputConsumer output = new ArrayListOutputConsumer();
		cmd.setOutputConsumer(output);
		cmd.run(op);
	        ArrayList<String> cmdOutput = output.getOutput();
	        if (cmdOutput.size() != 1) return;
	        String line = cmdOutput.get(0);
	       System.out.println(line);
	        


	}
	/**
	 * 等比例缩放
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static void equalScaling() throws IOException, InterruptedException, IM4JavaException{
		GMOperation op = new GMOperation();
		//op.resize(5000);
		//op.quality(30.0);
		String raw = "";
		raw = 50+"%x"+50+"%";
		op.addRawArgs("-thumbnail", raw);
		 op.addRawArgs("-quality", "100");
		op.addImage("d://test/2.jpg");
		op.addImage("d://test/2java.jpg");
		ConvertCmd cmd = new ConvertCmd(true);
		cmd.setSearchPath("D:\\GraphicsMagick-1.3.18-Q8"); 
		cmd.run(op);
	}
}
