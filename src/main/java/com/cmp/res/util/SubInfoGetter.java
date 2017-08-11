package com.cmp.res.util;

import java.util.Hashtable;

/**
 * 获得摘要的实体类

 *
 */
public class SubInfoGetter {

	//定义截取摘要值的几个关键属性
	private String content;
	private int contentLength;
	private int wholeMarkLength;
	
	private Hashtable<String , String> hashHitWords=new Hashtable<String ,String>();
	private int subLength=100;
	
	
	public SubInfoGetter(String sourceContent, int hitPointsCount,int subLength) {
		
		this.content=clean(sourceContent);
		this.contentLength=content.length();
		this.subLength=subLength;
		
		//定义正文中标志关键字的几个标记
		String frontMark="<font color=red>",backMark="</font>",keyword;
		int frontIndex,backIndex,keywordLength,keywordsCount=0;
		int frontMarkLength=frontMark.length();
		int backMarkLength=backMark.length();
		int keyTimes=0;
		
		wholeMarkLength=frontMarkLength+backMarkLength;

		for(int i=0;i<content.length();i++){
			//Sytetem.out.println("keywordCount:"+keywordsCount);
			frontIndex=content.indexOf(frontMark,i);
			backIndex=content.indexOf(backMark,frontIndex);
			if(backIndex>frontIndex+frontMarkLength){
				keyTimes++;
				keywordLength=backIndex-frontIndex-frontMarkLength;
				keyword=content.substring(frontIndex+frontMarkLength,backIndex);
				
			}			
			if(keyTimes>hitPointsCount-1) break;
			
			i=backIndex+backMarkLength;
		}
	}

	//清洗内容中的特殊标记。
	private String clean(String sourceContent) {
		StringBuffer contentBuffer = new StringBuffer();
		char curChar;
		for(int i=0;i<sourceContent.length();i++){
			curChar = sourceContent.charAt(i);
			if(curChar=='\n' || curChar=='\r' || curChar=='　'){
				contentBuffer.append("");
			}else contentBuffer.append(curChar);				
		}
		return contentBuffer.toString();
	}

	public String getSub() {
		if(subLength>=contentLength)	return content;
		StringBuffer subBuffer=new StringBuffer(subLength*2);
		
		//没有取到命中点标志时,直接返回文章截取的前100个字
		//if(markList.size()==0)
		{
			subBuffer.append(getCutString(this.content,0,this.subLength)).append("...");
		}
		
		return subBuffer.toString();
	}
	
	
	/**
	 * 取摘要时,如果没有命中,则从头取固定长度作为摘要.
	 * @param sourceContent	用于取摘要的字段值
	 * @param i				摘要的理想长度
	 * @return				直接截取的摘要串
	 */
	public static  String getSub(String sourceContent, int i) {
		
		if(i<sourceContent.length()){
			StringBuffer buf = new StringBuffer();
			char [] chars = sourceContent.toCharArray();
			for(int index=0 ; index<chars.length ; index ++ ){
				char c = chars[index];
				if(index > i && isStartOrEndChar(c)){
					break;					
				}
				buf.append(chars[index]);
			}
			return buf.append("...").toString();
		}else{
			return sourceContent;
		}
	}

	private String getCutString(String content2, int subBegin, int subEnd) {
		StringBuffer buffer=new StringBuffer(subLength*2);
		if(subBegin<0) subBegin=0;
		if(subEnd>contentLength) subEnd=contentLength;
		
		if(!isStartOrEndChar(content2.charAt(subEnd-1))){
			subEnd = getMarkAfterIndex(subEnd-1)+1;
		}
		if(!isStartOrEndChar(content2.charAt(subBegin))){
			subBegin = getMarkBeforeIndex(subBegin);
		}
		if(subBegin!=0)	buffer.append("...");
		buffer.append(content2.substring(subBegin,subEnd-1));
		if(subEnd!=contentLength)	buffer.append("...");
		return buffer.toString();
	}
	
	private int getMarkBeforeIndex(int flag){
		for(int i=flag;i>0;i--){
			char c=this.content.charAt(i);
			if(isStartOrEndChar(c)){
				return i+1;
			}
		}
		return 0;
	}
	
	private int getMarkAfterIndex(int flag){
		for(int i=flag;i<this.contentLength;i++){
			char c=this.content.charAt(i);
			if(isStartOrEndChar(c)){
				return i+1;
			}
		}
		return this.contentLength;
	}
	
	/**
	 * 判断一个字符是否是句子结束符号或分隔符号.
	 * @param c	要判断的字符
	 * @return	是否是分隔符
	 */
	public static boolean isStartOrEndChar(char c){		
		char [] chars = { 
				',' , '.' , '?' , '!' , ';' , ':' , '：' ,		//半角结束号
				'，' , '．' , '？' , '！' , '；' ,'。',			//全角结束号
				'(' , ')' , '[' , ']' , '{' , '}',				//半角括号
				'（' , '）' , '［' , '］' , '｛' , '｝',			//全角括号
				'\"' , '\'' , '＂' , '＇' , '_' , '＿' , '－','-','/','@','#','$' 	//一般分割符号 
				,' ','　'										//半角 全角空格
				};

		for(int i = 0 ; i < chars.length ; i ++ ){
			if(c == chars[i])
				return true;
		}
		return false;
	}
	
	/**
	 * 对正文遍历一次,过滤掉所有html代码,保留server检索时候自动标红的部分.
	 * 同时过滤掉不符合XML规范的字符,但是GB18030中的字符可能识别错误,请见isInvalidXMLChar(char c)的实现
	 * @param 	strToClean	需要进行过滤的带html的正文.
	 * @return	过滤掉HTML后的正文串.
	 */
	public static String cleanString(String strToClean){
		StringBuffer buffer = new StringBuffer();
		StringBuffer bufferTemp = new StringBuffer(32);
		char [] ch= "<font color=red>".toCharArray();
		char [] ch2="</font>".toCharArray();
		char[] chArray = strToClean.toCharArray();
		for(int i=0;i<chArray.length;i++){
			char c=chArray[i];
			if(isInvalidXMLChar(c)){
				continue;
			}
			if(c=='<'){
				if(chArray[i+1]=='f'){
					bufferTemp.append('f');
					boolean flag = true;
					i=i+2;
					for(int index=2;index<ch.length;index++){
						if(chArray[i] != ch[index]){
							flag = false;
							break;
						}else{
							bufferTemp.append(chArray[i]);
							i++;
						}
					}
					i--;
					if(flag){
						buffer.append('<').append(bufferTemp);
						bufferTemp = new StringBuffer(32);
					}else{
						buffer.append("&lt;").append(bufferTemp);
						bufferTemp = new StringBuffer(32);
					}
				}else if(chArray[i+1]=='/'){
					bufferTemp.append('/');
					boolean flag = true;
					i=i+2;
					for(int index=2;index<ch2.length;index++){
						if(chArray[i] != ch2[index]){
							flag = false;
							break;
						}else{
							bufferTemp.append(chArray[i]);
							i++;
						}
					}
					i--;
					if(flag){
						buffer.append('<').append(bufferTemp);
						bufferTemp = new StringBuffer(32);
					}else{
						buffer.append("&lt;").append(bufferTemp);
						bufferTemp = new StringBuffer(32);
					}
				}else{
					buffer.append("&lt;");
				}
			}else{
				if(c=='>'){
					buffer.append("&gt;");
				}else{
					buffer.append(c);
				}
			}
		}		
		return buffer.toString();
	}
	
	/**
	 * 过滤String使得其中不符合XML规范的字符被过滤掉,GB18030定义的字符可能会有一些也被错当做不符合规范,见isInvalidXMLChar(char c)的实现
	 * @param str	要过滤的字符串
	 * @return		过滤后的字符串
	 */
	protected String filterInvalidXMLChar(String str) {
		if(str == null){
			return "";
		}
		StringBuffer buf = new StringBuffer();
		int length = str.length();
		for(int i = 0; i < length; i ++ ){
			char c = str.charAt(i);
			if(isInvalidXMLChar(c)){
				continue;
			}
			buf.append(c);
		}
		return buf.toString();
	}

	/**
	 * 对于GB18030中的一些字符可能不会支持,目前没有详细的资料来确定.
	 * @param c	要判断的char,看该字符是否XML中不合法的字符
	 * @return
	 */
	public static boolean isInvalidXMLChar(char c){
		if(c == 0x9 || c == 0xA || c == 0xD || (c >= 0x20 && c <= 0xD7FF) ||
				(c >= 0xE000 && c <= 0xFFFD) || (c >= 0x10000 && c <= 0x10FFFF)){
			return false;
		}
		return true;
	}


}

