package mas;

/*
 * Created: liushen@Jan 26, 2010 3:25:52 PM
 */


import org.apache.log4j.Logger;


import com.trs.dev4.jdk16.exec.ExecuteResult;
import com.trs.mam.bo.TranscodeParameter;
import com.trs.mam.transcode.impl.VideoProcessResult;
import com.trs.mam.transcode.impl.ffmpeg.FFMpegTool;

/**
 * 
 * @author TRS信息技术股份有限公司
 */
public class Test {
	private static String ffmpegDirPath;

	private static FFMpegTool ffmpegTool;

	private static Logger LOG = Logger.getLogger(Test.class);

	

	/**
	 * 测试转码为H264+AAC编码的MP4视频(支持苹果Safari播放).
	 */
	public static void main(String[] args) {
		ffmpegDirPath = "D:\\TRS\\TRSMAS\\win32\\ffmpeg";
		ffmpegTool = new FFMpegTool(ffmpegDirPath);
		System.out.println("====== ffmpeg version: " + ffmpegTool.getVersionString());
		
		String inputVideoFile = "d:/bbbbb.flv";
		// 注意：输出文件名中目前必须包含下划线字符(_)，即必须是xxx_yy.mp4，不能是xxx.mp4；否则在对MP4进行渐进式播放处理时会报越界错误
		String outputVideoFile = "d:/mastest_.mp4";
		transcode(inputVideoFile, outputVideoFile, "H264", "600k");
	}

	public static void transcode(String inputVideoFile, String outputVideoFile, String format, String bitrate) {
		try {
			// 1)设定格式、视频比特率
			TranscodeParameter param = new TranscodeParameter(format, bitrate);
			// 以下三项目前不能为null，否则空指针
			param.setWatermarkSize("20");
			param.setWatermarkLocation("");
			param.setWatermarkImage("d:\\watermarkater.png");

			// 2)调用ffmpeg进行转码；(ffmpeg需使用MAS 5.0.85107介质中的版本)
			VideoProcessResult vpr = ffmpegTool.transcode(inputVideoFile, outputVideoFile, param);

			// 3)获取转码结果；
			ExecuteResult cmdResult = vpr.getCmdResult();
			int exitValue = cmdResult.getExitValue();
			System.out.println("exitValue=" + exitValue + "; cmd=[" + cmdResult.getCommand() + "]; estimated "
					+ (cmdResult.getEndTime() - cmdResult.getStartTime()) + " ms.");

			// 4)检查转码是否成功（转码进程的退出值为0就表示成功）
		} catch (Throwable e) {
			LOG.error("fail to transcode " + inputVideoFile, e);
		}
	}
}