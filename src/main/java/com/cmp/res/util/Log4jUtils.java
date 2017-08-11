package com.cmp.res.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 记录日志到数据库时
 *objid、objectName、logType三个字段需要割接，此方法为objectName、logType拼装
 * @author trs
 *
 */
public class Log4jUtils {
	
	
	public static String getLogString(String separator,String operator,Integer logType,Long objectid,String objectName){
		if(StringUtils.isBlank(separator)){
			//分割符不可为空 
			return null;
		}
		if(StringUtils.isBlank(operator)){
			//操作人不可为空 
			return null;
		}
		if(logType==null){
			logType=1;
		}
		if(objectid==null){
			objectid=new Long(0);
		}
		if(StringUtils.isBlank(objectName)){
			objectName="";
		}
		System.err.println("**************************");
		System.err.println(operator+separator+logType+separator+objectid+separator+objectName);
		System.err.println("**************************");
		return operator+separator+logType+separator+objectid+separator+objectName;
	}
	
	
	
	
	/**
	 * 组装objectName 和logType，用于日志信息的记录
	 * @param objectName
	 * @param logType
	 * @return
	 */
	public static String packagObjNameAndLogType(String objectName,String logType){
		//logType默认值为1
		if(logType.equals("")) logType = "1";
		String objNameAndLogType = ","+objectName +","+logType;
		return objNameAndLogType;
	}
}
