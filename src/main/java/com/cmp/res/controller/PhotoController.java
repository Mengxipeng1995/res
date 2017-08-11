package com.cmp.res.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.AssertFalse.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cmp.res.entity.Category;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.PhotoCategoryRel;
import com.cmp.res.entity.PhotoParameter;
import com.cmp.res.entity.ProductItem;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.SubjectItem;
import com.cmp.res.entity.User;
import com.cmp.res.service.CategoryService;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ConfigService;
import com.cmp.res.service.FileUtil;
import com.cmp.res.service.ItemService;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.service.PhotoCategoryRelService;
import com.cmp.res.service.PhotoService;
import com.cmp.res.service.ProductItemService;
import com.cmp.res.service.SubjectItemService;
import com.cmp.res.util.Im4javaUtil;
import com.cmp.res.util.Log4jUtils;
import com.cmp.res.util.image.Cut;
import com.cmp.res.util.image.ImageUtils;
import com.cmp.res.util.image.Positions;
import com.cmp.res.util.image.Watermark;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/photo/")
public class PhotoController {
	
	private static Logger logger=LoggerFactory.getLogger(PhotoController.class);
	
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private ConfigService configService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private PhotoCategoryRelService photoCategoryRelService;
	
	@Autowired
	private ProductItemService productItemService;
	
	@Autowired
	private SubjectItemService  subjectItemService;
	
	@RequestMapping("delete")
	public void delete(@RequestParam(value = "id") long id,
			HttpServletResponse response){
		ReturnJson rj=new ReturnJson();
		User user=commonService.getCurrentLogin();
		Photo photo=photoService.findById(id);
		if(photo!=null){
			if(photo.getType()==null){
				//原始入库图片不允许删除
				rj.setSuccess(false);
				rj.setMsg("原始图片不允许删除");
			}else{
				
				//
				
				
				//判断图片是否有引用
				boolean flag=true;
				java.util.List<ProductItem> pis= productItemService.findByItemIdAndType(id,1);
				
				if(pis!=null&&pis.size()>0){
					flag=false;
				}else{
					java.util.List<SubjectItem> sis=subjectItemService.findByItemidAndType(id, 1);
					if(sis!=null&&sis.size()>0){
						flag=false;
					}
				}
				
				if(flag){
					//物理删除
					//删除图片文件
					if("S3".equals(configService.getDiskType())){
						fileUtil.deleteFile(photo.getPath(), 1, 1);
						fileUtil.deleteFile(photo.getPath(), 1, 2);
						fileUtil.deleteFile(photo.getPath(), 1, 3);
						fileUtil.deleteFile(photo.getPath(), 1, 4);
					}else{
						//原始图片
						File file=new File(configService.getPhotoStorePath()+File.separator+photo.getPath());
						if(file.exists()){
							file.delete();
						} 
						//大图
						file=new File(configService.getBighotoStorePath()+File.separator+photo.getPath());
						if(file.exists()){
							file.delete();
						} 
						//中图
						file=new File(configService.getMiddleStorePath()+File.separator+photo.getPath());
						if(file.exists()){
							file.delete();
						}
						
						//小图
						file=new File(configService.getSimphotoStorePath()+File.separator+photo.getPath());
						if(file.exists()){
							file.delete();
						}
						photoService.delete(id);
						logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
								user.getUserName(), 4, photo.getId(), photo.getTitle()) ,
								Log4jDBAppender.EXTENT_MSG_DIVIDER,"物理删除图片成功");
					}
					
				}else{
					
					photo.setDeleteFlag(1);
					photoService.savePhoto(photo);
					
					logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
							user.getUserName(), 4, photo.getId(), photo.getTitle()) ,
							Log4jDBAppender.EXTENT_MSG_DIVIDER,"逻辑删除图片成功");
				}
				
				
			}
		}else{
			rj.setSuccess(false);
			rj.setMsg("图片不存在");
		}
		
		commonService.returnDate(response, rj);
		
	}
	
	
	@RequestMapping(value="add" ,method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	public void add(
			@RequestParam(value = "title") String title,
			@RequestParam(value = "cid") Long cid,
			@RequestParam(value = "avatar_data") String avatar_data,
			@RequestParam(value = "image", required = false) MultipartFile pic,
			@RequestParam(value = "originalFlag", required = false,defaultValue="0") int originalFlag,
			HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		User user=commonService.getCurrentLogin();
		Photo photo=new Photo();
		String picNmae=null;
		if(pic!=null){
			String[] names=pic.getOriginalFilename().split("\\.");
			if(names.length>1){
				picNmae=UUID.randomUUID().toString();
				picNmae+="."+names[names.length-1];
				
				if(originalFlag==1){
				try{
					 JSONObject jsonobject = JSONObject.fromObject(avatar_data);
					 PhotoParameter pp=(PhotoParameter) JSONObject.toBean(jsonobject, PhotoParameter.class);
					 //1、上传的原图
					 File attachment=new File(configService.getPhotoTempPath(),picNmae);
					 pic.transferTo(attachment); 
					 String rotateSrc=picNmae;
					 
					 //2、旋转图片
					 if(pp.getRotate()!=0){
						 rotateSrc="a_"+picNmae;
						 ImageUtils.fromFile(new File(configService.getPhotoTempPath()+picNmae)).scale(1)
							.quality(1).rotate(pp.getRotate())
							.bgcolor(Color.BLUE)
							.toFile(new File(configService.getPhotoTempPath()+rotateSrc));
					 }
					 //裁剪图片   将裁剪后的图片保存为原图
					 Cut.cutImage(configService.getPhotoTempPath()+rotateSrc, configService.getPhotoStorePath()+File.separator+picNmae, pp.getX().intValue(), pp.getY().intValue(), pp.getWidth().intValue(), pp.getHeight().intValue());
					
					 
					 
					 
					 //添加水印  并保存到大图
					 BufferedImage watermarkImage = ImageIO.read(new File(configService.getWatermarkater()));
					Watermark watermark = new Watermark(Positions.TOP_LEFT,
							watermarkImage, 0.3f);
					
					ImageUtils.fromFile(new File(configService.getPhotoStorePath()+File.separator+picNmae))
					.scale(1)
					.watermark(watermark)
					.toFile(new File(configService.getBighotoStorePath()+File.separator+picNmae));
					 
					
						
					//抽图-》中图
					Im4javaUtil.equalScaling(configService.getPhotoStorePath()+File.separator+picNmae, configService.getMiddleStorePath()+File.separator+picNmae,configService.getMiddlePicSize());
					
					//抽图-》小图
					Im4javaUtil.equalScaling(configService.getPhotoStorePath()+File.separator+picNmae, configService.getSimphotoStorePath()+File.separator+picNmae,configService.getSimPicSize());
					
					 
					
					
				}catch(Exception e){
					e.printStackTrace();
					picNmae=null;
				}finally{
					//删除临时文件
					File tempFile=new File(configService.getPhotoTempPath()+picNmae);
					if(tempFile.exists()){
						tempFile.delete();
					}
					tempFile=new File(configService.getPhotoTempPath()+"a_"+picNmae);
					if(tempFile.exists()){
						tempFile.delete();
					}
					
					tempFile=new File(configService.getPhotoTempPath()+"a_a_"+picNmae);
					if(tempFile.exists()){
						tempFile.delete();
					}
				}
			}else{
				
				
				 try {
					//存储原 图
					File attachment=new File(configService.getPhotoStorePath(),picNmae);
					pic.transferTo(attachment);
					
					//添加水印
					 BufferedImage watermarkImage = ImageIO.read(new File(configService.getWatermarkater()));
						
						Watermark watermark = new Watermark(Positions.TOP_LEFT,
								watermarkImage, 0.3f);
						
						ImageUtils.fromFile(new File(configService.getPhotoStorePath()+File.separator+picNmae))
						.scale(1)
						.watermark(watermark)
						.toFile(new File(configService.getBighotoStorePath()+File.separator+picNmae));
						//抽图-》中图
						Im4javaUtil.equalScaling(configService.getPhotoStorePath()+File.separator+picNmae, configService.getMiddleStorePath()+File.separator+picNmae,configService.getMiddlePicSize());
						//抽图-》小图
						Im4javaUtil.equalScaling(configService.getPhotoStorePath()+File.separator+picNmae, configService.getSimphotoStorePath()+File.separator+picNmae,configService.getSimPicSize());
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				
			}
			}
			if(picNmae!=null){
				/**
				 * 判断S3环境
				 */
				if("S3".equals(configService.getDiskType())){
					//原图到s3
					File orgPic=new File(configService.getPhotoStorePath()+File.separator+picNmae);
					if(orgPic.exists()){
						if(fileUtil.save(orgPic, picNmae, 1, 1)){
							orgPic.delete();
						}else{
							
							logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
									user.getUserName(), 4, 0L, title+"["+picNmae+"]") ,
									Log4jDBAppender.EXTENT_MSG_DIVIDER,"图片原图上传S3失败");
						}
					}
					
					
					//大图
					File bigPic=new File(configService.getBighotoStorePath()+File.separator+picNmae);
					if(bigPic.exists()){
						if(fileUtil.save(bigPic, picNmae, 1, 2)){
							bigPic.delete();
						}else{
							
							logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
									user.getUserName(), 4, 0L, title+"["+picNmae+"]") ,
									Log4jDBAppender.EXTENT_MSG_DIVIDER,"图片大图上传S3失败");
						}
					}
					
					//中图
					File middlePic=new File(configService.getMiddleStorePath()+File.separator+picNmae);
					if(middlePic.exists()){
						if(fileUtil.save(middlePic, picNmae, 1, 3)){
							middlePic.delete();
						}else{
							
							logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
									user.getUserName(), 4, 0L, title+"["+picNmae+"]") ,
									Log4jDBAppender.EXTENT_MSG_DIVIDER,"图片中图图上传S3失败");
						}
					}
					
					//小图
					File smallPic=new File(configService.getSimphotoStorePath()+File.separator+picNmae);
					if(smallPic.exists()){
						if(fileUtil.save(smallPic, picNmae, 1, 4)){
							smallPic.delete();
						}else{
							
							logger.error(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
									user.getUserName(), 4, 0L, title+"["+picNmae+"]") ,
									Log4jDBAppender.EXTENT_MSG_DIVIDER,"图片小图上传S3失败");
						}
					}
					
					
				}
				
				
				photo.setPath(picNmae);
				photo.setType(1);
				photo.setTitle(title);
				photo.setRole("图表");
				
				photoService.savePhoto(photo);
				
				Category cat=categoryService.findById(cid);
				while(cat.getParentid()!=null){
					PhotoCategoryRel pcr=new PhotoCategoryRel();
				    pcr.setTitle(title);
					pcr.setCatid(cat.getId());
					pcr.setPhotoid(photo.getId());
					pcr.setRole("图表");
					photoCategoryRelService.savePhotoCategoryRel(pcr);
					cat=categoryService.findById(cat.getParentid());
				}
				
				
			}else{
				rj.setSuccess(false);
				rj.setMsg("图片保存异常");
			}
			
		}else{
			rj.setSuccess(false);
			rj.setMsg("请上传图片");
		}
		
		commonService.returnDate(response, rj);
		
	}
	
	/**
	 * 获取大图
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getImage")
	public void getImage(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response) throws IOException{
		
		Photo photo=photoService.findById(id);
		if(photo==null){
			return;
		}
		byte[] fileByte=null;
		if("S3".equals(configService.getDiskType())){
			fileByte=fileUtil.download(photo.getPath(), 1, 2);
		}else{
			File image=new File(configService.getBighotoStorePath()+File.separator+photo.getPath());
			if(image.exists()){
				fileByte=FileUtils.readFileToByteArray(image);
			}
		}
		
		response.setHeader("Content-Disposition", "filename="+photo.getPath());
		response.setContentType("image/jpeg");
		System.out.println(fileByte);
		response.getOutputStream().write(fileByte);
	}
	/**
	 * 获取小图
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getSmallImage")
	public void getSmallImage(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response) throws IOException{
		
		Photo photo=photoService.findById(id);
		if(photo==null){
			return;
		}
		String fileName="";
		
		//判断图片类型  
		if("线条图".equals(photo.getRole())||"表格".equals(photo.getRole())){
			//小图
			fileName=configService.getSimphotoStorePath()+File.separator+photo.getPath();
		}else{
			//原始图片
			fileName=configService.getPhotoStorePath()+File.separator+photo.getPath();
		}
		byte[] fileByte=null;
		if("S3".equals(configService.getDiskType())){
			fileByte=fileUtil.download(photo.getPath(), 1, 4);
		}else{
			File image=new File(fileName);
			if(image.exists()){
				fileByte=FileUtils.readFileToByteArray(image);
			}
		}
		
		response.setHeader("Content-Disposition", "filename="+photo.getPath());
		response.setContentType("image/jpeg");
		response.getOutputStream().write(fileByte);
		
	
	}
	
	
	
	@RequestMapping("list")
	public void list(
			@RequestParam(value = "page",required=false,defaultValue="1") Integer page,
			@RequestParam(value = "limit",required=false,defaultValue="20") Integer limit,
			HttpServletResponse response
			){
		
		commonService.returnDate(response, photoService.list(page, limit));
		
	}

}
