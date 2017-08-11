package com.cmp.res.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cmp.res.entity.Video;
import com.cmp.res.service.ConfigService;
import com.cmp.res.service.VideoService;
import com.cmp.res.util.HttpTookit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//@Component
//@Lazy(value=false)
//public class MasTast {
//	@Autowired
//	private VideoService videoService;
//	@Autowired
//	private ConfigService configService;
//	
//	 @Scheduled(cron = "0 0/1 * * * ?") 
//	public void updateMasPic(){
//		 System.out.println("视频任务执行");
//		List<Video> videos=videoService.getVideo();
//		if(videos!=null&videos.size()>0){
//			for(Video video:videos){
//				String videoInfo=HttpTookit.doPost(configService.getMasUrl()+"/openapi/pages.do", "method=exQuery&appKey="+configService.getAppKey()+"&id="+video.getMasId(), "utf8");
//				
//				JSONArray jArray=JSONArray.fromObject(videoInfo);  
//				JSONObject object = (JSONObject)jArray.get(0);
//				
//				video.setPic(object.getString("pic"));
//				video.setPicS(object.getString("picS"));
//
//				videoService.save(video);
//				
//				}
//		}
//	}
//
//}
