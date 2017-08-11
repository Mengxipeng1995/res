package com.cmp.res.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cmp.res.entity.Category;
import com.cmp.res.entity.ProductItem;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.SubjectItem;
import com.cmp.res.entity.User;
import com.cmp.res.entity.Video;
import com.cmp.res.entity.VideoCategoryMapping;
import com.cmp.res.event.VideoEvent;
import com.cmp.res.service.CategoryService;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ConfigService;
import com.cmp.res.service.FileUtil;
import com.cmp.res.service.ProductItemService;
import com.cmp.res.service.SubjectItemService;
import com.cmp.res.service.VideoCategoryMappingService;
import com.cmp.res.service.VideoService;


@Controller
@RequestMapping("/video/")
public class VideoController {
	@Autowired
	private VideoService videoService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ProductItemService productItemService;
	
	@Autowired
	private SubjectItemService  subjectItemService;
	
	@Autowired
	private VideoCategoryMappingService videoCategoryMappingService;
	
	@Autowired
	private FileUtil fileUtil;
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "page",required=false,defaultValue="1") Integer pn,
			@RequestParam(value = "limit",required=false,defaultValue="20") Integer ps,
			@RequestParam(value = "catid",required=false)Long catid,
			HttpServletResponse response
			){
		
		commonService.returnDate(response, videoService.outline(pn, ps, catid));
		
	}
	@RequestMapping("record")
	public void getRecord(@RequestParam(value = "id") long id,HttpServletResponse response){
		commonService.returnDate(response, videoService.findById(id));
	}
	
	@RequestMapping("edit")
	public void edit(@RequestParam(value = "id") Long id,
			@RequestParam(value = "title")String title,
			@RequestParam(value = "desc",required=false)String desc,
			HttpServletResponse response){
		ReturnJson rj=new ReturnJson();
		Video video=videoService.findById(id);
		video.setTitle(title);
		video.setDesc(desc);
		try{
			videoService.save(video);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			rj.setSuccess(false);
			rj.setMsg("保存失败");
		}
		
		commonService.returnDate(response,rj);
		
	}
	
	@RequestMapping("delete")
	public void delete(@RequestParam(value = "id") Long id,
			HttpServletResponse response){
		boolean flag=true;
		ReturnJson rj=new ReturnJson();
		Video video=videoService.findById(id);
		if(video!=null){
			/**
			 * 判断该视频是否存在引用
			 */
			List<ProductItem> pis= productItemService.findByItemIdAndType(id,2);
			if(pis!=null&&pis.size()>0){
				flag=false;
			}else{
				List<SubjectItem> sis=subjectItemService.findByItemidAndType(id, 2);
				if(sis!=null&&sis.size()>0){
					flag=false;
				}
			}
			
			if(flag){
				//物理删除
				//删除附件
				if("S3".equals(configService.getDiskType())){
					fileUtil.deleteFile(video.getFileName()+File.separator+video.getFileFormat(), 2, 1);
					fileUtil.deleteFile(video.getFileName()+File.separator+"_.mp4", 2, 2);
					fileUtil.deleteFile(video.getFileName()+File.separator+".jpg", 2, 3);
					fileUtil.deleteFile(video.getFileName()+File.separator+".jpg", 2, 4);
					fileUtil.deleteFile(video.getFileName()+File.separator+".jpg", 2, 5);
				}else{
					//原视频
					File file=new File(configService.getOriginalVideo()+File.separator+video.getFileName()+"."+video.getFileFormat());
					if(file.exists()){
						file.delete();
					}
					//mp4
					file=new File(configService.getMp4Video()+File.pathSeparator+video.getFileName()+"._mp4");
					if(file.exists()){
						file.delete();
					}
					//大图
					file=new File(configService.getPicVideo()+File.pathSeparator+video.getFileName()+".jpg");
					if(file.exists()){
						file.delete();
					}
					//中图
					file=new File(configService.getPicMiddleVideo()+File.pathSeparator+video.getFileName()+".jpg");
					if(file.exists()){
						file.delete();
					}
					
					//小图
					file=new File(configService.getPicSmallVideo()+File.pathSeparator+video.getFileName()+".jpg");
					if(file.exists()){
						file.delete();
					}
					
				}
				videoService.delete(id);
				
				videoCategoryMappingService.deleteByVideoId(id, 1);
				
			}else{
				
				video.setDeleteFlag(1);
				videoService.save(video);
				videoCategoryMappingService.deleteByVideoId(id, 0);
			}
			
			
			
			
		}else{
			rj.setMsg("视频不存在");
			rj.setSuccess(false);
		}
		commonService.returnDate(response, rj);
	}
			
	
	@RequestMapping(value="save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public void save(
			@RequestParam(value = "catid") Long catid,
			@RequestParam(value = "title")String title,
			@RequestParam(value = "desc",required=false)String desc,
			@RequestParam(value = "video", required = false) MultipartFile videoFile,
			HttpServletRequest request,
			HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		if(!videoFile.isEmpty()){
			String fileName=UUID.randomUUID().toString();
			String[] videoName=videoFile.getOriginalFilename().split("\\.");
			try {
				videoFile.transferTo(new File(configService.getOriginalVideo()+File.separator+fileName+"."+videoName[videoName.length-1]));
			
				//保存视频
				User user=commonService.getCurrentLogin(); 
				Date createDate=new Date();
				Video video=new Video();
				video.setCatid(catid);
				video.setCreateDate(createDate);
				video.setCreaterUserName(user.getUserName());
				video.setCreaterNickName(user.getNickName());
				video.setTitle(title);
				video.setDesc(desc);
				video.setFileName(fileName);
				video.setFileFormat(videoName[videoName.length-1]);
				video.setOriginalFileName(videoName[videoName.length-2]);
				video.setStatus(0);
				video.setUser(user);
				videoService.save(video);
				
				
				
				Category cat=categoryService.findById(catid);
				while(cat.getParentid()!=null){
					VideoCategoryMapping vcm=new VideoCategoryMapping();
					vcm.setCatid(cat.getId());
					vcm.setCreateDate(createDate);
					vcm.setCreaterUserName(user.getUserName());
					vcm.setCreaterNickName(user.getNickName());
					vcm.setTitle(title);
					vcm.setDesc(desc);
					vcm.setStatus(0);
					vcm.setVideoId(video.getId());
					videoCategoryMappingService.save(vcm);
					cat=categoryService.findById(cat.getParentid());
					
					
				}
				
				WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
				VideoEvent videoEvent = new VideoEvent(video);
				webApplicationContext.publishEvent(videoEvent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rj.setSuccess(false);
				rj.setMsg("视频保存失败");
			} 
			
		}else{
			rj.setSuccess(false);
			rj.setMsg("上传文件为空");
		}
		commonService.returnDate(response,rj );
		
		
	}
	/**
	 * 获取视频缩略图
	 * @throws IOException 
	 */
	@RequestMapping(value="getVideoPic/{id}/{type}",method=RequestMethod.GET)
	public void getVideoPic(
			@PathVariable(value = "id") Long id,
			@PathVariable(value = "type") int type,
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException{
		Video video=videoService.findById(id);
		
		if(video==null){
			return;
		}
		
		byte[] fileByte=null;
		if("S3".equals(configService.getDiskType())){
			fileByte=fileUtil.download(video.getFileName()+".jpg", 2, type==0?4:5);
		}else{
			String fileName="";
			if(type==0){
				//中图
				fileName=configService.getPicMiddleVideo()+File.separator+video.getFileName()+".jpg";
			}else{
				//小图
				fileName=configService.getPicSmallVideo()+File.separator+video.getFileName()+".jpg";
			}
			File image=new File(fileName);
			if(image.exists()){
				fileByte=FileUtils.readFileToByteArray(image);
			}
		}
		
		if(fileByte!=null){
			response.setHeader("Content-Disposition", "filename="+video.getTitle()+".jpg");
			response.setContentType("image/jpeg");
			response.getOutputStream().write(fileByte);
		}
		
		
	}
	
	
	
	@RequestMapping(value="getVideo/{id}",method=RequestMethod.GET)
	public void getVideo(
			@PathVariable(value = "id") Long id,
			HttpServletRequest request,
			HttpServletResponse response
			){
		response.setContentType("text/html;charset=UTF-8");
		Video video=videoService.findById(id);
		
		if(video==null){
			return;
		}
		
		response.addHeader("Content-Disposition", "attachment;filename=" + video.getOriginalFileName()+"."+video.getFileFormat());
		OutputStream os = null;
		InputStream is = null;
		try{
			os = response.getOutputStream();
			if("S3".equals(configService.getDiskType())){
				byte[] videoByte=fileUtil.download(video.getFileName()+"_.mp4", 2,2);
				os.write(videoByte);
			}else{
				File file=new File(configService.getMp4Video()+File.separator+video.getFileName()+"_.mp4");
				 is = new FileInputStream(file);
				 byte[] bs = new byte[4096];
				 int length = -1;
				while ((length = is.read(bs)) != -1) {
					os.write(bs, 0, length);
				}
			
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(os!=null)
				os.close();
				if(is!=null)
				is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
		
		
	}
	

}
