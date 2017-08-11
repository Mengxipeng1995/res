package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.VideoCategoryMappingDAO;
import com.cmp.res.entity.VideoCategoryMapping;
import com.cmp.res.util.CommonUtil;

@Service
public class VideoCategoryMappingService implements BaseService<VideoCategoryMapping>{
	
	@Autowired
	private VideoCategoryMappingDAO videoCategoryMappingDAO;
	
	public Page<VideoCategoryMapping> outline(int pn,int ps,long catid){
		return videoCategoryMappingDAO.findByDeleteFlagIsNullAndCatid(catid, CommonUtil.buildPageRequest(pn, ps, null, null, null));
	}
	
	/**
	 * 
	 * @param videoId
	 * @param type  0逻辑删除，1物理删除
	 */
	public void deleteByVideoId(Long videoId,int type){
		if(type==0){
			videoCategoryMappingDAO.deleteLogic(videoId);
		}else{
			videoCategoryMappingDAO.deleteNas(videoId);
			
		}
	}

	@Override
	public void save(VideoCategoryMapping t) {
		// TODO Auto-generated method stub
		videoCategoryMappingDAO.save(t);
		
	}
	
	public List<VideoCategoryMapping> findByVideoId(long videoId){
		return videoCategoryMappingDAO.findByVideoId(videoId);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		videoCategoryMappingDAO.delete(id);
		
	}

	@Override
	public VideoCategoryMapping findById(Long id) {
		// TODO Auto-generated method stub
		return videoCategoryMappingDAO.findOne(id);
	}

	@Override
	public void delete(VideoCategoryMapping t) {
		// TODO Auto-generated method stub
		videoCategoryMappingDAO.delete(t);
		
	}

}
