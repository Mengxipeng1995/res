package com.cmp.res.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.Item;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.Product;
import com.cmp.res.entity.ProductItem;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.Subject;
import com.cmp.res.entity.SubjectCategory;
import com.cmp.res.entity.SubjectItem;
import com.cmp.res.entity.User;
import com.cmp.res.entity.Video;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.ItemService;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.service.PhotoService;
import com.cmp.res.service.SubjectCategoryService;
import com.cmp.res.service.SubjectItemService;
import com.cmp.res.service.SubjectService;
import com.cmp.res.service.VideoService;
import com.cmp.res.util.Log4jUtils;

@Controller
@RequestMapping("/subject/")
public class SubjectController {
	
	public static Logger logger = LoggerFactory.getLogger(SubjectController.class);
	
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private SubjectItemService subjectItemService;
	@Autowired
	private PhotoService photoService;
	@Autowired
	private SubjectCategoryService subjectCategoryService;
	@Autowired
	private VideoService videoService;
	
	@RequestMapping("outline")
	public void outline(
			@RequestParam(value = "cid",required=false) Long cid,
			@RequestParam(value = "page",required=false,defaultValue="1") int pn,
			@RequestParam(value = "limit",required=false,defaultValue="20") int ps,
			HttpServletResponse response
			){
		
		commonService.returnDate(response, subjectService.outline(cid, pn, ps));
		
		
	}
	
	@RequestMapping("findById")
	public void findById(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response
			){
		
		commonService.returnDate(response, subjectService.findById(id));
	}
	
	
	
	
	
	@RequestMapping("save")
	public void save(
			@RequestParam(value = "id",required=false) Long id,
			@RequestParam(value = "cids",required=false) String cidsStr,
			@RequestParam(value = "title") String title,
			@RequestParam(value = "desc") String desc,
			HttpServletResponse response
			){
		
		ReturnJson rj=new ReturnJson();
		StringBuffer catidsStr=new StringBuffer(),catnamesStr=new StringBuffer();
		if(StringUtils.isNoneBlank(cidsStr)&&cidsStr.split(";").length>0){
			String[] temp=cidsStr.split(";");
			Long[] cids=new Long[temp.length];
			for(int i=0;i<temp.length;i++){
				cids[i]=Long.parseLong(temp[i]);
			}
			List<SubjectCategory> cats=subjectCategoryService.findByIdIn(cids);
			for(SubjectCategory subjectCategory:cats){
				catidsStr.append(subjectCategory.getId()).append(";");
				catnamesStr.append(subjectCategory.getName()).append(";");
			}
		}
		if(StringUtils.isNotBlank(catidsStr.toString())){
			catidsStr.insert(0, ";");
			catnamesStr.insert(0,  ";");
		}
		
		User user=commonService.getCurrentLogin();
		if(id==null){
			Subject subject=new Subject();
			subject.setTitle(title);
			subject.setDesc(desc);
			subject.setStatus(0);
			subject.setCreateDate(new Date());
			subject.setCreaterName(user.getUserName());
			subject.setCreaterNickName(user.getNickName());
			subject.setCategoryids(catidsStr.toString());
			subject.setCategorynames(catnamesStr.toString());
			
			subjectService.save(subject);
			
		}else{
			Subject subject=subjectService.findById(id);
			subject.setTitle(title);
			subject.setDesc(desc);
			subject.setCategoryids(catidsStr.toString());
			subject.setCategorynames(catnamesStr.toString());
			
			subjectService.save(subject);
			
		}
		
		commonService.returnDate(response, rj);
		
	}
	
	
	@RequestMapping("delete")
	public void delete(
			@RequestParam(value = "id",required=false) Long id,
			HttpServletResponse response
			){
		
		subjectService.delete(id);
		
		subjectItemService.deleteBySubjectid(id);
		
		commonService.returnDate(response, new ReturnJson());
		
	}
	
	
	@RequestMapping("addItemToSubject")
	public void addItemToProduct(
			@RequestParam(value = "sid",required=false) Long sid,
			@RequestParam(value = "items")Long[] ids,
			@RequestParam(value = "type")Integer type,//要添加条目的类型  0：文字，1：图片；2：视频
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		StringBuffer sb=new StringBuffer();
		Subject subject=subjectService.findById(sid);
		if(subject!=null){
			switch (type) {
			case 0:
				//文字
				List<Item> items=itemService.findByIdIn(ids);
				if(items!=null&&items.size()>0){
					for(Item item:items){
						if(subjectItemService.findBySubjectidAndItemid(subject.getId(), item.getId())!=null){
							sb.append("条目("+item.getId()+":"+item.getTitle()+")在该专题中已经存在;");
							rj.setSuccess(false);
							continue;
						}
						SubjectItem si=new SubjectItem();
						si.setItemid(item.getId());
						si.setTitle(item.getTitle());
						si.setSubjectid(subject.getId());
						si.setType(type);
						si.setCreater(item.getUserName());
						si.setCreateDate(subject.getCreateDate());
						subjectItemService.save(si);
					}
				}
				break;
			case 1:
				//图片
				List<Photo> photos= photoService.findByIdIn(ids);
				if(photos!=null&&photos.size()>0){
					for(Photo photo:photos){
						if(subjectItemService.findBySubjectidAndItemid(subject.getId(), photo.getId())!=null){
							sb.append("条目("+photo.getId()+":"+photo.getTitle()+")在该专题中已经存在;");
							rj.setSuccess(false);
							continue;
						}
						SubjectItem si=new SubjectItem();
						si.setItemid(photo.getId());
						si.setTitle(photo.getTitleabbrev());
						si.setSubjectid(subject.getId());
						si.setCreater(photo.getCreaterUserName());
						si.setCreateDate(photo.getCreateTime());
						si.setType(type);
						subjectItemService.save(si);
					}
				}
				
				break;
			case 2:
				//视频
				List<Video> videos= videoService.findByIdIn(ids);
				if(videos!=null&&videos.size()>0){
					for(Video video:videos){
						if(subjectItemService.findBySubjectidAndItemid(subject.getId(), video.getId())!=null){
							sb.append("条目("+video.getId()+":"+video.getTitle()+")在该专题中已经存在;");
							rj.setSuccess(false);
							continue;
						}
						SubjectItem si=new SubjectItem();
						si.setItemid(video.getId());
						si.setTitle(video.getTitle());
						si.setSubjectid(subject.getId());
						si.setCreater(video.getCreaterUserName());
						si.setCreateDate(video.getCreateDate());
						si.setType(type);
						subjectItemService.save(si);
					}
				}
				break;
			}
			
		}else{
			rj.setSuccess(false);
			rj.setMsg("专题不存在");
		}
		
		rj.setMsg(sb.toString());
		
		commonService.returnDate(response, rj);
		
	}
	
	/**
	 * 删除专题内的条目
	 * @param id
	 * @param ids
	 * @param request
	 * @param response
	 */
	@RequestMapping("deleteItems")
	public void deleteItems(
			@RequestParam(value = "subjectid")Long subjectid,
			@RequestParam(value = "subjectItemdids")Long[] subjectItemdids,
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		Subject subjcet=subjectService.findById(subjectid);
		if(subjcet!=null){
			if(subjcet.getStatus()==0){
				//编辑状态
				StringBuilder sb = new StringBuilder("");
				int length=subjectItemdids.length;
				for( int i = 0; i < length; i++ )
				{
				    sb.append(subjectItemdids[i]).append(",");
				    
				    subjectItemService.delete(subjectItemdids[i]);
				}
				if(sb.length()>0){
					sb.deleteCharAt(sb.length()-1);
				}
				
				//subjectItemService.deleteSubjectItems(subjectid, sb.toString());
				User user=commonService.getCurrentLogin();
				
				logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
						user.getUserName(), 2, subjectid, subjcet.getTitle()) ,
						Log4jDBAppender.EXTENT_MSG_DIVIDER,"删除关联条目【"+sb.toString()+"】");
				
			}else{
				//发布状态 不允许编辑
				rj.setSuccess(false);
				rj.setMsg("专题已发布不允许编辑");
			}
		}else{
			rj.setSuccess(false);
			rj.setMsg("专题不存在");
		}
		
		commonService.returnDate(response, rj);
		
		
		
	}
	@RequestMapping("pub")
	public void pubSubject(
			@RequestParam(value = "subjectid")Long subjectid,
			HttpServletRequest request,HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		
		Subject subject=subjectService.findById(subjectid);
		
		if(subject!=null){
			subject.setStatus(1);
			subjectService.save(subject);
		}else{
			rj.setMsg("专题不存在");
			rj.setSuccess(false);
		}
		
		commonService.returnDate(response, rj);
		
		
		
	}
	
	

	
	

}
