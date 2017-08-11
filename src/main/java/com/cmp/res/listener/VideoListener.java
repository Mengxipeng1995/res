package com.cmp.res.listener;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.cmp.res.entity.User;
import com.cmp.res.entity.Video;
import com.cmp.res.entity.VideoCategoryMapping;
import com.cmp.res.event.VideoEvent;
import com.cmp.res.service.BookService;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ConfigService;
import com.cmp.res.service.FileUtil;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.service.VideoCategoryMappingService;
import com.cmp.res.service.VideoService;
import com.cmp.res.util.Im4javaUtil;
import com.cmp.res.util.Log4jUtils;
import com.cmp.res.util.SystemConstant;
import com.cmp.res.util.VideoUtil;

public class VideoListener   implements ApplicationListener{
	
	@Autowired
	private ConfigService configService;
	
	private CommonService commonService;
	@Autowired
	private VideoService videoService;
	@Autowired
	private VideoCategoryMappingService videoCategoryMappingService;
	@Autowired
	private FileUtil fileUtil;
	
	public static Logger logger = LoggerFactory.getLogger(BookService.class);

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		if(event instanceof VideoEvent){
			
			Video video=(Video) event.getSource();
			User user=video.getUser();
			
			try{
				//转码+水印
			//	VideoUtil.executeCodecs(SystemConstant.ffmpegPath,configService.getOriginalVideo()+File.separator+video.getFileName()+"."+video.getFileFormat() , configService.getMp4Video()+File.separator+video.getFileName()+".mp4", configService.getVideoWatermarkater());
				VideoUtil.transcode(SystemConstant.ffmpegPath,configService.getOriginalVideo()+File.separator+video.getFileName()+"."+video.getFileFormat(),configService.getMp4Video()+File.separator+video.getFileName()+"_.mp4",configService.getWatermarkSize(),configService.getWatermarkLocation(),configService.getWartermarkeImage());
				//获取视频长度
				Long videoLength=VideoUtil.getVideoTime(SystemConstant.ffmpegPath, configService.getMp4Video()+File.separator+video.getFileName()+"_.mp4");
				//随机截图
				Long picTime=(long)(1+Math.random()*(videoLength-1+1));
				
				VideoUtil.cutPic(SystemConstant.ffmpegPath, configService.getMp4Video()+File.separator+video.getFileName()+"_.mp4", configService.getPicVideo()+File.separator+video.getFileName()+".jpg", picTime);
				File pic=new File(configService.getPicVideo()+File.separator+video.getFileName()+".jpg");
				while(!pic.exists()){
					Thread.sleep(10000);
					pic=new File(configService.getPicVideo()+File.separator+video.getFileName()+".jpg");
				}
				
				//抽图
				Im4javaUtil.equalScaling(configService.getPicVideo()+File.separator+video.getFileName()+".jpg", configService.getPicMiddleVideo()+File.separator+video.getFileName()+".jpg",configService.getMiddlePicSize());
				
				Im4javaUtil.equalScaling(configService.getPicVideo()+File.separator+video.getFileName()+".jpg", configService.getPicSmallVideo()+File.separator+video.getFileName()+".jpg",configService.getSimPicSize());
				
				if("S3".equals(configService.getDiskType())){
					
					//上传源文件
					File originalFile=new File(configService.getOriginalVideo()+File.separator+video.getFileName()+"."+video.getFileFormat());
					if(originalFile.exists()){
						if(fileUtil.save(originalFile, video.getFileName()+"."+video.getFileFormat(), 2, 1)){
							originalFile.delete();
						}else{
							 logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
										user.getUserName(), 3, video.getId(), video.getTitle()) ,
										Log4jDBAppender.EXTENT_MSG_DIVIDER,"源文件上传S3失败");
						}
						
					}
					
					//上传mp4视频
					File mp4File=new File(configService.getMp4Video()+File.separator+video.getFileName()+"_.mp4");
					if(mp4File.exists()){
						if(fileUtil.save(mp4File, video.getFileName()+"_.mp4", 2, 2)){
							mp4File.delete();
						}else{
							logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
									user.getUserName(), 3, video.getId(), video.getTitle()) ,
									Log4jDBAppender.EXTENT_MSG_DIVIDER,"MP4上传S3失败");
						}
						
					}
					//上传截图
					File picFile=new File(configService.getPicVideo()+File.separator+video.getFileName()+".jpg");
					if(picFile.exists()){
						if(fileUtil.save(picFile, video.getFileName()+".jpg", 2, 3)){
							picFile.delete();
						}else{
							logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
									user.getUserName(), 3, video.getId(), video.getTitle()) ,
									Log4jDBAppender.EXTENT_MSG_DIVIDER,"视频截图大图上传S3失败");
						}
						
					}
					
					//上传中图
					File middlePicFile=new File(configService.getPicMiddleVideo()+File.separator+video.getFileName()+".jpg");
					if(middlePicFile.exists()){
						if(fileUtil.save(middlePicFile, video.getFileName()+".jpg", 2, 4)){
							middlePicFile.delete();
						}else{
							logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
									user.getUserName(), 3, video.getId(), video.getTitle()) ,
									Log4jDBAppender.EXTENT_MSG_DIVIDER,"视频截图中图上传S3失败");
						}
						
						
						
					}
					
					//上传小图
					File smallPicFile=new File(configService.getPicSmallVideo()+File.separator+video.getFileName()+".jpg");
					if(smallPicFile.exists()){
						if(fileUtil.save(smallPicFile, video.getFileName()+".jpg", 2, 5)){
							smallPicFile.delete();
						}else{
							logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
									user.getUserName(), 3, video.getId(), video.getTitle()) ,
									Log4jDBAppender.EXTENT_MSG_DIVIDER,"视频截图小图上传S3失败");
						}
						
					}
					
				}
				
				
				video.setLength(videoLength);
				
				video.setStatus(1);
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				video.setStatus(2);
			}
			
			
			videoService.save(video);
			
			List<VideoCategoryMapping> videos=videoCategoryMappingService.findByVideoId(video.getId());
			if(videos!=null&&videos.size()>0){
				for(VideoCategoryMapping ccm:videos){
					ccm.setStatus(video.getStatus());
					videoCategoryMappingService.save(ccm);
				}
			}
			
			
	       }
		
	}

}
