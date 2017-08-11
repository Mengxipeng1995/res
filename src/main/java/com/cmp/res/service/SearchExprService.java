package com.cmp.res.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmp.res.entity.Category;
import com.google.common.collect.Lists;

public class SearchExprService {
	
	public static Logger logger = LoggerFactory.getLogger(SearchExprService.class);
	
	public static String SEARCH_AND_STR=" AND ";
	
	public static String SEARCH_OR_STR=" OR ";
	
	public static String SEARCH_EQUALES_STR="=";
	
	public static String SEARCH_HY_EQUALES_STR=":";
	
	public static String SEARCH_BETWEEN_STR=" BETWEEN ";
	
	public static String SEARCH_DATE_STR=" % ";
	
	public static String SEARCH_READ_COLUMN="lemmas_categorys;lemmas_content;lemmas_lemmaTitle;lemmas_reason4modify;lemmas_id";
	
	
	private String[] cids;
	
	private List<Category> cats=Lists.newArrayList();
	
	private String sw;
	
	
	private String title;
	
	
	private String content;
	
	private String keyword;
	

	private Long newsid;
	

	
	private String searchExpr;
	
	public String getSearchColumnExprByArray(String column,String[] value){
    	if(value==null||value.length==0){
    		return null;
    	}
		String s="";
		for(String v:value){
			if(StringUtils.isBlank(v)){
				continue;
			}
			if(s.length()==0){
				s+=v;
			}else{
				s+=(SEARCH_OR_STR+v);
			}
		}
		if(StringUtils.isBlank(s)){
			return null;
		}
		return column+SEARCH_EQUALES_STR+"("+s+")";
		
	}
	
	
	/**
	 * 日期字段的检索
	 * @param column
	 * @param startTme
	 * @param endTime
	 * @return
	 */
	public String getDateExpr(String column,String startTme,String endTime){
		if(StringUtils.isBlank(startTme)&&StringUtils.isBlank(endTime)){
			return "";
		}
		if(StringUtils.isBlank(startTme)){
			startTme=SEARCH_DATE_STR;
		}
		if(StringUtils.isBlank(endTime)){
			endTime=SEARCH_DATE_STR;
		}
		return column+SEARCH_EQUALES_STR+"["+startTme+" TO "+endTime+"]";
	}
	
	/**
	 * 获取分类检索条件
	 * @param column
	 * @return
	 */
	public String getCategorySearchExpr(String column){
		String expr="";
		
		//暂时使用cid进行查询，后期要改为实际授权的分类节点
		if(getCids()==null||getCids().length==0){
			return expr;
		}
		for(String cid:getCids()){
			
			if(expr.length()==0){
				expr+=cid;
			}else{
				expr+=(SEARCH_OR_STR+cid);
			}
		}
		
		if(StringUtils.isBlank(expr)){
			return  expr;
		}
		return column+SEARCH_EQUALES_STR+"("+expr+")";
	}
	public String getSearchColumnExpr(String column,String value){
		
		return column+SEARCH_EQUALES_STR+"("+value+")";
		
	}
	
	public String getCharColumnFuzzyExpr(String column,String value){
		
		return column+SEARCH_EQUALES_STR+"(%"+value.trim()+"%)";
		
	}
	
	public String getCaptionSearchExr(String column,String[] values){
		StringBuilder sb=new StringBuilder();
		if(values==null||values.length==0){
			return sb.toString();
		}
		
		for(String value:values){
			if(StringUtils.isBlank(value)){
				continue;
			}
			if(sb.length()==0){
				sb.append("(").append(value).append(")");
			}else{
				sb.append(SEARCH_AND_STR).append("(").append(value).append(")");
			}
		}
		if(sb.length()==0){
			return sb.toString();
		}
		
		sb.insert(0, column+SEARCH_EQUALES_STR+"(").append(")");
		return sb.toString();
	}
	
	public String getCaptionSearchExr(String[] columns,String[] values){
		StringBuilder sb=new StringBuilder();
		if(values==null||values.length==0||columns==null||columns.length==0){
			return null;
		}
		
		for(String value:values){
			if(StringUtils.isBlank(value)){
				continue;
			}
			if(sb.length()==0){
				sb.append("(").append(value).append(")");
			}else{
				sb.append(SEARCH_OR_STR).append("(").append(value).append(")");
			}
		}
		if(sb.length()==0){
			return sb.toString();
		}
		String str=sb.toString();
		sb.setLength(0);
		for(String column:columns){
			if(StringUtils.isBlank(column)){
				continue;
			}
			if(sb.length()==0){
				sb.append(column).append(SEARCH_EQUALES_STR).append("(").append(str).append(")");
			}else{
				sb.append(SEARCH_OR_STR).append(column).append(SEARCH_EQUALES_STR).append("(").append(str).append(")");
			}
		}
		sb.insert(0, "(").append(")");
		return sb.toString();
	}
	
	public String getSWSearchExpr(String[] columns,String[] values){
		StringBuilder sb=new StringBuilder();
		if(values==null||values.length==0||columns==null||columns.length==0){
			return null;
		}
		String columnStr="";
		for(String column:columns){
			if(StringUtils.isNoneBlank(column)){
				columnStr+=column+",";
			}
		}
		if(!"".equals(columnStr)){
			columnStr=columnStr.substring(0, columnStr.length()-1);
			
			for(String value:values){
				if(StringUtils.isNotBlank(value)){
					sb.append("("+columnStr+"+=(%"+value+"%)) and ");
				}
			}
			if(StringUtils.isNoneBlank(sb.toString())){
				return "("+sb.toString().substring(0, sb.toString().length()-5)+")";
			}
		}
		return sb.toString();
		
	}
	
	
	public String getNewsSearchExpr(){
		StringBuilder sb=new StringBuilder();
		String str="";
	
		//标题、正文检索
		if(StringUtils.isNotBlank(getSw())){
			String[] words=getSw().split(" ");
			String[] fileds={"lemmas_lemmaTitle","lemmas_content"};
			str=getSWSearchExpr(fileds,words);
		}
		
		return str;
	}
	
	
	
	public String getRelatedNewsExpr(){
		StringBuilder sb=new StringBuilder();
		String str="";
		
		
		if(getNewsid()!=null){
			sb.append(SEARCH_AND_STR).append(" NEWSID!=").append(getNewsid());
		}
		
		
		
		str=sb.toString().trim();
		if(str.startsWith("AND")){
			str=str.substring(3);
		}
		
		//是否发布的检索条件
		if(StringUtils.isBlank(str)){
			str=getSearchColumnExpr("PUB_STATUS", "1");
		}else{
			str="("+str+")"+SEARCH_AND_STR+"("+getSearchColumnExpr("PUB_STATUS", "1")+")";
		}
		return str;
	}
	
	public String getUpdateNewsSql(){
		StringBuilder sb=new StringBuilder();
		String str="";
		
		str=sb.toString().trim();
		if(str.startsWith("AND")){
			str=str.substring(3);
		}
		
		//是否发布的检索条件
		if(StringUtils.isBlank(str)){
			str=getSearchColumnExpr("PUB_STATUS", "1");
		}else{
			str="("+str+")"+SEARCH_AND_STR+"("+getSearchColumnExpr("PUB_STATUS", "1")+")";
		}
		return str;
		
		
		
		
	}
	
	
	
	
	

	
	public String getSearchExpr() {
		return searchExpr;
	}

	public void setSearchExpr(String searchExpr) {
		this.searchExpr = searchExpr;
	}

	public static SearchExprService getInstance(){
		return new SearchExprService();
	}

	public String[] getCids() {
		return cids;
	}

	public void setCids(String[] cids) {
		this.cids = cids;
	}

	

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public Long getNewsid() {
		return newsid;
	}


	public void setNewsid(Long newsid) {
		this.newsid = newsid;
	}


	


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getSw() {
		return sw;
	}


	public void setSw(String sw) {
		this.sw = sw;
	}


	
	
	
	

}
