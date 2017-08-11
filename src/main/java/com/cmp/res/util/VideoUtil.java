package com.cmp.res.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trs.dev4.jdk16.exec.ExecuteResult;
import com.trs.mam.bo.TranscodeParameter;
import com.trs.mam.transcode.impl.VideoProcessResult;
import com.trs.mam.transcode.impl.ffmpeg.FFMpegTool;

public class VideoUtil{
	
	
	private static FFMpegTool ffmpegTool;
	
	/**
	 * 使用mas转码逻辑
	 */
	public static void transcode(String ffmpegPath,String inputFile,String outFile,String watermarkSize,String watermarkLocation,String wartermarkeImage){
		 System.out.println(System.getProperty("user.dir"));
		
		ffmpegTool = new FFMpegTool(ffmpegPath.substring(0,ffmpegPath.lastIndexOf(File.separator)));
		System.out.println("====== ffmpeg version: " + ffmpegTool.getVersionString());
		// 注意：输出文件名中目前必须包含下划线字符(_)，即必须是xxx_yy.mp4，不能是xxx.mp4；否则在对MP4进行渐进式播放处理时会报越界错误
		//outFile 文件名中必须包含一个下划线
		transcode(inputFile, outFile, "H264", "600k",watermarkSize,watermarkLocation,wartermarkeImage);
	
	}
	
	public static void transcode(String inputVideoFile, String outputVideoFile, String format, String bitrate,String watermarkSize,String watermarkLocation,String wartermarkeImage) {
		try {
			// 1)设定格式、视频比特率
			TranscodeParameter param = new TranscodeParameter(format, bitrate);
			// 以下三项目前不能为null，否则空指针
			param.setWatermarkOpened(true);
			param.setWatermarkSize(watermarkSize);
			param.setWatermarkLocation(watermarkLocation);
			param.setWatermarkImage(wartermarkeImage);
			
			System.out.println("###path:"+System.getProperty("user.dir"));

			// 2)调用ffmpeg进行转码；(ffmpeg需使用MAS 5.0.85107介质中的版本)
			VideoProcessResult vpr = ffmpegTool.transcode(inputVideoFile, outputVideoFile, param);

			// 3)获取转码结果；
			ExecuteResult cmdResult = vpr.getCmdResult();
			int exitValue = cmdResult.getExitValue();
			System.out.println("exitValue=" + exitValue + "; cmd=[" + cmdResult.getCommand() + "]; estimated "
					+ (cmdResult.getEndTime() - cmdResult.getStartTime()) + " ms.");

			// 4)检查转码是否成功（转码进程的退出值为0就表示成功）
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	/**
     * 视频转码+水印
     * @param ffmpegPath    转码工具的存放路径
     * @param upFilePath    用于指定要转换格式的文件,要截图的视频源文件
     * @param codcFilePath    格式转换后的的文件保存路径
     * @param logo    视频logo
     * @return
     * @throws Exception
     */
    public static boolean executeCodecs(String ffmpegPath, String upFilePath, String codcFilePath,
            String logo) throws Exception {
        // 创建一个List集合来保存转换视频文件为flv格式的命令
        List<String> convert = new ArrayList<String>();
        convert.add(ffmpegPath); // 添加转换工具路径
        convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
        convert.add(upFilePath); // 添加要转换格式的视频文件的路径
        convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        convert.add("-strict");   
        convert.add("-2");
        convert.add("-q:v");     //指定转换的质量
        convert.add("4");
        convert.add("-ab");        //设置音频码率
        convert.add("64");
        convert.add("-ac");        //设置声道数
        convert.add("2");
        convert.add("-ar");        //设置声音的采样频率
        convert.add("22050");
        convert.add("-r");        //设置帧频
        convert.add("24");

      
        List<String> cutpic = new ArrayList<String>();
        cutpic.add(ffmpegPath);
        cutpic.add("-i");
        cutpic.add(codcFilePath); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
        cutpic.add("-y");
        cutpic.add("-f");
        cutpic.add("image2");
        cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
        cutpic.add("0"); // 添加起始时间为第17秒
        //添加水印
        cutpic.add("-vf");
        cutpic.add("movie="+logo+"[logo],[in][logo]overlay=x=0:y=0[out]");
       
        
        
        convert.add(codcFilePath);

        boolean mark = true;
        ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command(convert);
            final Process p=builder.start();
            
            /**
             * jdk8下不加下边几行代码，执行视频转换为异步执行的（jdk7下本身就是同步执行）；
             * 具体原因待查
             */
            
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
            StringBuffer sb = new StringBuffer();  
            String line = "";  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            System.out.println(sb.toString());
           // br.close();  
            
            
            builder.command(cutpic);
            builder.redirectErrorStream(true);
            // 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
            //因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
            final Process p2= builder.start();
            br=new BufferedReader(new InputStreamReader(p.getErrorStream()));;
            sb=new StringBuffer();
            line = ""; 
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            System.out.println(sb.toString());
            br.close();  
            
        } catch (Exception e) {
            mark = false;
            System.out.println(e);
            e.printStackTrace();
        }
        return mark;
    }
    
    /** 
     * 获取视频总时间 
     * @param viedo_path    视频路径 
     * @param ffmpeg_path   ffmpeg路径 
     * @return 
     */  
    public static Long getVideoTime(String ffmpeg_path,String video_path) {  
    	System.out.println(ffmpeg_path+":"+video_path);
        List<String> commands = new java.util.ArrayList<String>();  
        commands.add(ffmpeg_path);  
        commands.add("-i");  
        commands.add(video_path);  
        try {  
            ProcessBuilder builder = new ProcessBuilder();  
            builder.command(commands);  
            final Process p = builder.start();  
              
            //从输入流中读取视频信息  
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
            StringBuffer sb = new StringBuffer();  
            String line = "";  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            br.close();  
              
            //从视频信息中解析时长  
            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";  
            Pattern pattern = Pattern.compile(regexDuration);  
            Matcher m = pattern.matcher(sb.toString());  
            if (m.find()) {  
                Long time = getTimelen(m.group(1));  
                System.out.println(video_path+",视频时长："+time+", 开始时间："+m.group(2)+",比特率："+m.group(3)+"kb/s");
                return time;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
        return 0l;  
    }  
      
    //格式:"00:00:10.68"  
    private static Long getTimelen(String timelen){  
        Long min=0l;  
        String strs[] = timelen.split(":");  
        if (strs[0].compareTo("0") > 0) {  
            min+=Long.valueOf(strs[0])*60*60;//秒  
        }  
        if(strs[1].compareTo("0")>0){  
            min+=Long.valueOf(strs[1])*60;  
        }  
        if(strs[2].compareTo("0")>0){  
            min+=Math.round(Float.valueOf(strs[2]));  
        }  
        return min;  
    } 
    
    /**
     * 视频转码
     * @param ffmpegPath    转码工具的存放路径
     * @param upFilePath    用于指定要转换格式的文件,要截图的视频源文件
     * @param codcFilePath    格式转换后的的文件保存路径
     * @param mediaPicPath    截图保存路径
     * @return
     * @throws Exception
     */
    public static boolean cutPic(String ffmpegPath, String upFilePath,
            String mediaPicPath,Long picSecond) throws Exception {
        
        // 创建一个List集合来保存从视频中截取图片的命令
        List<String> cutpic = new ArrayList<String>();
        cutpic.add(ffmpegPath);
        cutpic.add("-i");
        cutpic.add(upFilePath); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
        cutpic.add("-y");
        cutpic.add("-f");
        cutpic.add("image2");
        cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
        cutpic.add(picSecond+""); // 添加起始时间为第17秒
        cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
        cutpic.add("0.001"); // 添加持续时间为1毫秒
        cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
        cutpic.add("500*377"); // 添加截取的图片大小为350*240
        cutpic.add(mediaPicPath); // 添加截取的图片的保存路径
        
        

        boolean mark = true;
        ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command(cutpic);
            builder.redirectErrorStream(true);
            builder.start();
        } catch (Exception e) {
            mark = false;
            System.out.println(e);
            e.printStackTrace();
        }
        return mark;
    }
    /**
     * 判断返回内容通过正则表达式判断
     * @param str
     * @return
     */
      public static String dealString( String str ){  
                  Matcher m=java.util.regex.Pattern.compile("^frame=.*" ).matcher(str);
                 String msg="";
                  while (m.find()){
                         msg = m.group();  
                  }
                  return msg;
      } 
      
	 public static void main(String[] args) throws Exception {
		// System.out.println(getVideoTime("D:\\TRS\\TRSMAS\\win32\\ffmpeg\\ffmpeg.exe","d:\\bbbbb.flv"));
//		 VideoUtil test= new VideoUtil();
//		 test.get
		 System.out.println(System.getProperty("user.dir"));
		// transcode("D:\\TRS\\TRSMAS\\win32\\ffmpeg\\ffmpeg.exe","d:\\bbbbb.flv","d:\\swing_d111.mp4");
	
	 
	 }
	
	
}
