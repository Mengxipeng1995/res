package com.cmp.res.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cmp.res.entity.Item;
import com.cmp.res.entity.PageEntity;
import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSConstant;
import com.eprobiti.trs.TRSException;
import com.eprobiti.trs.TRSResultSet;
import com.google.common.collect.Lists;





@Service
public class SearchService {
	
	public static Logger logger = LoggerFactory.getLogger(SearchService.class);
	
	@Value("${trs.server.host}")
	private String host;//="124.207.131.30";
	
	@Value("${trs.server.port}")
	private String port;//="8888";
	
	@Value("${trs.server.user}")
	private String user;//="system";
	
	@Value("${trs.server.password}")
	private String password;//="manager";
	
	@Value("${trs.server.dbname}")
	private String dbName;//="cmp";
	

	

	
	
	
	
	public TRSConnection getTRSConnection() throws com.eprobiti.trs.TRSException{
		TRSConnection conn= new TRSConnection();
		conn.connect(getHost(), getPort()	, getUser(), getPassword());
		return conn;
	}
	
  
	
	public PageEntity searchQuery(SearchExprService se,PageEntity page) throws TRSException{
		String sql=se.getNewsSearchExpr();
		logger.debug("trs search sql:"+sql);
		
		page=executeSearchTrsServer(page, sql,null);
		
		page.setSearchExpr(sql);
		return page;
	}
	
	
	
	
	public PageEntity relatedNewsQuery(SearchExprService se,PageEntity page) throws TRSException{
		String sql=se.getRelatedNewsExpr();
		logger.debug("trs related news sql:"+sql);
		page=executeSearchTrsServer(page, sql,"RELEVANCE");
		
		page.setSearchExpr(sql);
		return page;
	}
	
	
	public PageEntity searchUpdateNews(SearchExprService se,PageEntity page) throws TRSException{
		String sql=se.getUpdateNewsSql();
		logger.debug("trs search sql:"+sql);
		page=executeSearchTrsServer(page, sql,"-PUB_TIME");
		
		page.setSearchExpr(sql);
		return page;
	}

	
	
	
	public PageEntity executeSearchTrsServer(PageEntity page,String sql,String sort) throws com.eprobiti.trs.TRSException{
		
		TRSConnection conn =getTRSConnection();
		conn.setAutoExtend(null, null, null, TRSConstant.TCM_KAXECX);
		TRSResultSet result=conn.executeSelect(getDbName(), sql, sort, "", "",TRSConstant.TCM_LIFOSPARE, TRSConstant.TCE_OFFSET, false);
		result.setReadOptions(TRSConstant.TCE_OFFSET, SearchExprService.SEARCH_READ_COLUMN, "");
		logger.info("检索结果条数:"+result.getRecordCount());
		int total=(int)result.getRecordCount();
		page.setTotalElements(total);
		
		if(result.getRecordCount()>0){
			List<Item> list=Lists.newArrayList();
			long start=page.getStart();
			long end=(page.getStart()+page.getLimit())>total?total:(page.getStart()+page.getLimit());
			Item item=null;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			for(long i=start;i<end;i++){
				result.moveTo(0, i);
				item=new Item();
				
				item.setId(result.getLong("lemmas_id"));
				
				item.setTitle(result.getString("lemmas_lemmaTitle","red"));
				
				item.setContent(result.getString("lemmas_content","red"));
				
				item.setVersionDesc(result.getString("lemmas_reason4modify"));
				
				list.add(item);
			}
			page.setContent(list);
		}
		return page;
		
		
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}


	
	
	
	
	
	

}
