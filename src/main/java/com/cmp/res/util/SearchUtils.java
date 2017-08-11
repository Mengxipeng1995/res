package com.cmp.res.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSConstant;

/**
 * TRS全文检索工具类。
 */
public class SearchUtils {
	
	/**
	 * 检索词分隔符
	 */
	public static final String MARK_SPLIT_CHARACTER="$^~";
	
	public static String EXP_AND = "and";
	
	public static String EXP_OR = "or";
	
	/**
	 * TRS 关键字列表
	 */
	public static final String[] TRS_KEYWORDS = { "\\", "'", "%", "?", "\"", "/", "\"", "(", ")", "*",",","，" };
	//public static final String[] TRS_KEYWORDS = { "\\", "'", "%", "?", "\"", "/", "", "", "", "", };
	/**
	 * 过滤后列表
	 */
	public static final String[] REPLACED_TRS_KEYWORDS={ " ", " ", " ", " "," ", " ", " ", " ", " ", " "," "," " };
	//public static final String[] REPLACED_TRS_KEYWORDS={ "\\\\", "\\'", "\\%", "\\?","", "\\/", "", "", "", "" };
	
	
	/**
	 * 过滤TRS关键字
	 * @param text 要过滤的文字
	 * @return
	 */
	public static String escapeTRS(String text){
		return StringUtils.replaceEach(text, TRS_KEYWORDS,REPLACED_TRS_KEYWORDS);
	}
	/**
	 * 去除标点符号 包括数学符号等
	 * @param text
	 * @return
	 */
	public static String escapeDot(String text){
		return text.replaceAll("[\\pP|\\pS]", " ");
	}
	/**
	 * 将检索词  解析为检索表达式
	 * @param caption
	 * @param operator
	 * @param isMatch
	 * @return
	 */
	public static String parseSearchWord(String caption,String operator,boolean isMatch){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isBlank(caption)){
			return sb.toString();
		}
		String[] cs=caption.split(" ");
		for(String str:cs){
			if(StringUtils.isBlank(str)){
				continue;
			}
			if(isMatch){
				str="\""+str+"\"";
			}
			if(sb.length()==0){
				sb.append(str);
			}else{
				sb.append(operator).append(str);
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 解析用户输入的检索字符串，并重新组合成一个合法的TRS检索表达式。<BR>
	 * 使用列子：<BR>
	 * "中国 北京"的解析结果为"中国 AND 北京"<BR>
	 * "中国 | 北京"的解析结果为"中国 OR 北京"<BR>
	 * "中国 and 北京 and "的解析结果为"中国 AND 北京"<BR>
	 * 此方法不适宜用在 用户检索 and or nor 等 关键词。
	 * @param searchWord 用户输入的检索字符串。
	 * @return 合法的TRS检索表达式
	 */
	public static String parseSearchWord(String searchWord) {
		if (searchWord == null || (searchWord = searchWord.trim()).length() == 0) {
			return "";
		}

		StringBuffer buff = new StringBuffer(256);
		String operator = null; // 各检索词之间的逻辑运算符，在 {null|'AND'|'OR'|'NOT'}取值。

		while ((searchWord = searchWord.trim()).length() != 0) {
			int index = searchWord.indexOf(" ");
			if (index == -1) // 解析最后一个检索词。
			{
				if (parseOperator(searchWord) == null) // 将不是运算符的检索词加入检索表达式中。
				{
					if (operator != null) {
						buff.append(" ");
						buff.append(operator);
						buff.append(" ");
					}

					buff.append(searchWord);
				}

				break;
			}

			String word = searchWord.substring(0, index);
			String _operator = parseOperator(word); // 运算符处理。
			if (_operator != null) {
				operator = _operator;
			} else {
				if (operator != null) {
					buff.append(" ");
					buff.append(operator);
					buff.append(" ");
				}
				buff.append(word);
				operator = "AND";
			}
			searchWord = searchWord.substring(index);
		}

		return buff.toString();
	}

	/**
	 * 解析输入的词是不是TRS逻辑运算符。规则如下：<BR>
	 * 如果输入的词为"AND"(不区分大小写)、"&"、"+"则为"与"操作。<BR>
	 * 如果输入的词为"NOT"(不区分大小写)、"!"、"-"则为"非"操作。<BR>
	 * 如果输入的词为"OR"(不区分大小写)、"|"则为"或"操作。<BR>
	 * 如果输入的词为"XOR"(不区分大小写)、"^"则为"异或"操作。<BR>
	 * 
	 * @param word
	 *            待解析的词。
	 * @return 如果是逻辑运算符则返回一致的逻辑运算符，否则返回<code>null</code>
	 */
	public static String parseOperator(String word) {
		if (word.equalsIgnoreCase("and") || word.equalsIgnoreCase("&") || word.equalsIgnoreCase("+")) {
			return "AND";
		}

		if (word.equalsIgnoreCase("not") || word.equalsIgnoreCase("!") || word.equalsIgnoreCase("-")) {
			return "NOT";
		}

		if (word.equalsIgnoreCase("or") || word.equalsIgnoreCase("|")) {
			return "OR";
		}

		if (word.equalsIgnoreCase("xor") || word.equalsIgnoreCase("^")) {
			return "XOR";
		}

		return null;
	}

	public static String getBrief(String content, String color) {
		try {
			int minLength = 200;
			int maxLength = 400;
			int startIndex = 0;
			int closeIndex = 0;
			int length = 0;
			int iFirst = 0;
			int iLast = 0;
			int iIndex = 0;

			if ((content = filterBlank(content)) == null) // 去除多余的空格,并判断是否是空字符串
			{
				return "";
			}

			length = content.length();
			if (length <= maxLength) {
				return content;
			}

			if (color == null || (color = color.trim().toLowerCase()).length() == 0) {
				color = "red";
			}

			iFirst = content.indexOf("<font color=" + color + ">");
			if (iFirst >= 0) {
				startIndex = iFirst - 70;
				if (startIndex < 0)
					startIndex = 0;
				iLast = iFirst + "<font color=red>".length();
				for (iIndex = content.indexOf("</font>", iLast); iIndex > 0 && iIndex - startIndex < maxLength; iIndex = content
						.indexOf("</font>", iLast)) {
					iLast = iIndex + 7;
				}
				if (iIndex > 0) {
					closeIndex = content.lastIndexOf("<font color=red>", iIndex);
					if (closeIndex - startIndex > maxLength) {
						closeIndex = iLast + 70;
						if (closeIndex - startIndex < minLength) {
							closeIndex = startIndex + minLength;
						}
						if (closeIndex >= length) {
							closeIndex = length - 1;
						}
					}
				} else {
					closeIndex = iLast + 70;
					if (closeIndex - startIndex < minLength) {
						closeIndex = startIndex + minLength;
					}
					if (closeIndex >= length) {
						closeIndex = length - 1;
					}
				}
			} else {
				startIndex = 0;
				closeIndex = minLength;
			}
			return content.substring(startIndex, closeIndex);
		} catch (Exception _ex) {
			return "";
		}
	}
	/**
	 * 去除空格-如果字符串间双空格，会保留一个空格
	 * @param src
	 * @return
	 */
	public static String filterBlank(String src) {
		boolean flag = false;// 标记前一个字符是否是空格。
		if (src == null || src.length() == 0) {
			return null;
		}

		int length = src.length();
		StringBuffer buff = new StringBuffer(length);
		for (int j = 0; j < length; j++) {
			char ch = src.charAt(j);
			if (flag) {
				if (ch != ' ') {
					buff.append(ch);
					flag = false;
				}
			} else {
				buff.append(ch);
				if (ch == ' ')
					flag = true;
			}
		}
		return buff.toString();
	}
	/**
	 * 去除字符串中间全空格
	 * @param str
	 * @return
	 */
	public static String filterBlank_new(String str){
		if(str == null || str.length() < 1){
			return null;
		}
		StringBuffer buff = new StringBuffer(str.length());
		for(int i = 0; i < str.length(); i++){
			char ch = str.charAt(i);
			if(ch != ' ')
				buff.append(ch);				
		}
		return buff.toString();
	}

	public static String getCharset(boolean defaultCharset) {
		String charset = null;
		switch (TRSConnection.getCharset(defaultCharset)) {
		case TRSConstant.TCE_CHARSET_GB:
			charset = "GBK";
			break;
		default:
			charset = "utf-8";
			break;
		}

		return charset;
	}

	public static int getCharset(String charset) {
		int iCharset = 0;
		charset = charset.toUpperCase();

		if (charset.equalsIgnoreCase("GBK")) {
			iCharset = TRSConstant.TCE_CHARSET_GB;
		} else {
			iCharset = TRSConstant.TCE_CHARSET_UTF8;
		}

		return iCharset;
	}

	public static void addItem(StringBuffer buff, String itemName, String itemValue) {
		if (itemValue == null || itemValue.length() == 0) {
			buff.append("<");
			buff.append(itemName);
			buff.append("/>");
		} else {
			buff.append("<");
			buff.append(itemName);
			buff.append(">");

			buff.append(itemValue);

			buff.append("</");
			buff.append(itemName);
			buff.append(">");
		}
	}

	public static void addCDataItem(StringBuffer buff, String itemName, String itemValue) {
		if (itemValue == null || itemValue.length() == 0) {
			buff.append("<");
			buff.append(itemName);
			buff.append("/>");
		} else {
			buff.append("<");
			buff.append(itemName);
			buff.append(">");

			buff.append("<![CDATA[");
			buff.append(itemValue);
			buff.append("]]>");

			buff.append("</");
			buff.append(itemName);
			buff.append(">");
		}
	}

	/**
	 * 语句连接，前后语句任意为空则返回前一语句
	 * 
	 * @param sw 语句A
	 * @param append 语句B
	 * @param rel 关系
	 * return 语句A 关系 语句B 
	 */
	public static String buildSearchExp(String sw, String append, String rel) {
		if (StringUtils.isBlank(sw) || StringUtils.isBlank(append))
			return sw;
		StringBuilder sb = new StringBuilder((sw.length() + append.length()) * 2);
		sb.append(sw);
		if (StringUtils.isNotBlank(rel)) {
			sb.append(" ");
			sb.append(rel);
			sb.append(" ");
		}
		sb.append(append);
		return sb.toString();
	}
	
	
	//***********TRS检索条件格式化代码**********************************************************************
	
	/**
	 * 工具方法,将用户输入格式化为标准TRS查询字符串格式.
	 * 
	 * @param swd 用户输入的查询串
	 * @param bSimilarSearch 是否使用模糊检索
	 * @param maxLength 为防止恶意输入,将用户输入的检索串加入长度限制.如果不需要加入限制请用Integer.MIN_VALUE
	 * @return 经过格式化后的检索词
	 */
	public static String formatSW(String swd, String space2rel, boolean bSimilarSearch, int maxLength) {

		if (swd == null) {
			return "";
		} else {
			String sw = swd.trim();
			if (sw.length() < 1 || "'".equalsIgnoreCase(sw)) {
				return "";
			} else {
				if (maxLength > 0 && sw.length() > maxLength) {
					sw = swd.substring(0, maxLength).trim();
				}
				//sw = sw.replaceAll("'", "\\\\'");
				return fmtAftTrm(sw, space2rel, bSimilarSearch);
			}
		}
	}
	/**
	 * 适用与TRS检索的检索词拼写，同时支持多词模糊检索的content = like（A）*content = like（B）格式的转换生成
	 * @param trs_condition
	 * @param swd
	 * @param space2rel
	 * @param bSimilarSearch
	 * @param maxLength
	 * @return
	 */
	public static String formatSW_x(String trs_condition, String swd, String space2rel, boolean bSimilarSearch, int maxLength) {

		if (swd == null) {
			return "";
		} else {
			String sw = swd.trim();
			if (sw.length() < 1 || "'".equalsIgnoreCase(sw)) {
				return "";
			} else {
				if (maxLength > 0 && sw.length() > maxLength) {
					sw = swd.substring(0, maxLength).trim();
				}
				//sw=sw.replaceAll("'", "\\\\'");
				if(bSimilarSearch){					
					int i = 0;
					StringTokenizer sTokenizer = new StringTokenizer(sw, " ");
					String[] like_fomatString = new String[sTokenizer.countTokens()];
					while(sTokenizer.hasMoreTokens()){
						String sword = fmtAftTrm_like(sTokenizer.nextToken(), space2rel, bSimilarSearch);
						like_fomatString[i] = buildSearchExp(trs_condition, sword, null);
						i++;
					}
					String likeSW = "(";
					if(like_fomatString.length > 0){
						for(int j = 0; j < like_fomatString.length; j++){
							likeSW += like_fomatString[j];
							if(j != (like_fomatString.length-1)){
								if("*".equalsIgnoreCase(space2rel.trim()))
									likeSW += " * ";
								else
									likeSW += " or ";
							}
						}
					}
					likeSW += ")";
					return likeSW;
				}else
					return fmtAftTrm_x(sw ,space2rel, bSimilarSearch);

			}
		}
	}

	/**
	 * 格式化TRS检索词的主体方法
	 * 关键词为连词 则会 进行 连词转义 如 搜索 china and usa ==> content=('china' and 'usa')
	 * @param sw
	 * @param bSimilarSearch
	 * @return
	 */
	private static String fmtAftTrm(String sw, String space2rel, boolean bSimilarSearch) {

		// 确保第一个字符是
		StringBuffer buffer = new StringBuffer(1024).append('(');
		StringTokenizer st = new StringTokenizer(sw, " ");
		StringBuffer firstTokenBuffer = giveEachToken(st.nextToken().trim(), bSimilarSearch);
		buffer.append(firstTokenBuffer);

		// 是否到达结尾
		boolean b2End = !st.hasMoreTokens();
		// 下面是否应该append连接符
		boolean bApdOpt = true;

		String thisToken;
		try {
			while (!b2End) {
				// 先取得本节字符串,然后检验,看本串是否已经到达了串尾
				thisToken = st.nextToken().trim();
				b2End = !st.hasMoreTokens();
				if (b2End) {
					if (bApdOpt) {
						buffer.append(space2rel).append(giveEachToken(thisToken, bSimilarSearch));
					} else {
						buffer.append(giveEachToken(thisToken, bSimilarSearch));
					}
				} else if (bApdOpt) {// 如果下一个append的应该是连接符
					if (bOpt(thisToken) == null) {
						buffer.append(space2rel).append(giveEachToken(thisToken, bSimilarSearch));
					} else {
						buffer.append(bOpt(thisToken));
						bApdOpt = false;
					}
				} else {
					buffer.append(giveEachToken(thisToken, bSimilarSearch));
					bApdOpt = true;
				}
			}
		} catch (Throwable e) {
			return firstTokenBuffer.toString();
		}

		return buffer.append(')').toString();
	}

	/**
	 * 格式化TRS检索词的主体方法 
	 * 关键词如为连词不做连词转义 如搜索 china and usa ==> content=('china' and 'and' and 'usa')
	 * @param sw
	 * @param bSimilarSearch
	 * @return
	 */
	private static String fmtAftTrm_x(String sw, String space2rel, boolean bSimilarSearch) {

		// 确保第一个字符是
		StringBuffer buffer = new StringBuffer(1024).append('(');
		StringTokenizer st = new StringTokenizer(sw, " ");
		StringBuffer firstTokenBuffer = giveEachToken(st.nextToken().trim(), bSimilarSearch);
		buffer.append(firstTokenBuffer);

		// 是否到达结尾
		boolean b2End = !st.hasMoreTokens();
		// 下面是否应该append连接符
		boolean bApdOpt = true;

		String thisToken;
		try {
			while (!b2End) {
				// 先取得本节字符串,然后检验,看本串是否已经到达了串尾
				thisToken = st.nextToken().trim();
				b2End = !st.hasMoreTokens();
				if (b2End) {
					if (bApdOpt) {
						buffer.append(space2rel).append(giveEachToken(thisToken, bSimilarSearch));
					} else {
						buffer.append(giveEachToken(thisToken, bSimilarSearch));
					}
				}else if(bApdOpt){
					if( thisToken != null){
						buffer.append(space2rel).append(giveEachToken(thisToken , bSimilarSearch));
					}else{

						bApdOpt = false;
					}
				}
			}
		} catch (Throwable e) {
			return firstTokenBuffer.toString();
		}

		return buffer.append(')').toString();
	}

	/**
	 * 格式化TRS检索词的主体方法 
	 * 关键词如为连词不做连词转义 如搜索 china and usa ==> content=('china' and 'and' and 'usa')
	 * 适用于like模糊检索
	 * @param sw
	 * @param space2rel
	 * @param bSimilarSearch
	 * @return
	 */
	private static String fmtAftTrm_like(String sw,String space2rel,boolean bSimilarSearch) {
		
		//确保第一个字符是
		StringBuffer buffer = new StringBuffer(1024).append("like(");
		StringTokenizer st = new StringTokenizer(sw, " ");
		StringBuffer firstTokenBuffer = giveEachToken(st.nextToken().trim(), bSimilarSearch); //如果不需要%形式，则将bbSimilarSearch设置为false即可
		buffer.append(firstTokenBuffer);
		buffer.append(") ");
		// 是否到达结尾
		boolean b2End = !st.hasMoreTokens();
		// 下面是否应该append连接符
		boolean bApdOpt = true;

		String thisToken;
		try {
			while (!b2End) {
				// 先取得本节字符串,然后检验,看本串是否已经到达了串尾
				thisToken = st.nextToken().trim();
				b2End = ! st.hasMoreTokens();
				if( b2End ){
					if(bApdOpt){
						buffer.append(space2rel).append(" like(").append(giveEachToken(thisToken , false)).append(") ");
					}else{
						buffer.append(giveEachToken(thisToken , bSimilarSearch));

					}
				}else if(bApdOpt){
					if( thisToken != null){
						buffer.append(space2rel).append(" like(").append(giveEachToken(thisToken , false)).append(") ");
					}else{

						bApdOpt = false;
					}
				}
			}
		} catch (Throwable e) {
			return firstTokenBuffer.toString();
		}

		return buffer.toString();
	}
	
	/**
	 * 列举所有是操作符的情况
	 * 
	 * @param thisToken
	 * @return
	 */
	private static String bOpt(String opt) {
		opt = opt.toLowerCase();
		if (opt.equals("and") || opt.equals("&") || opt.equals("+"))
			return "AND";
		if (opt.equals("or") || opt.equals("|"))
			return "OR";
		if (opt.equals("not") || opt.equals("!") || opt.equals("-"))
			return "NOT";
		return null;
	}

	/**
	 * 两头增加单引号
	 * 
	 * @param str
	 * @param similarSearch
	 * @return
	 */
	private static StringBuffer giveEachToken(String str, boolean similarSearch) {
		String thisToken;
		StringBuffer partBuffer = new StringBuffer(64);
		thisToken = str;

		partBuffer.append(' ').append('\'');
		/*if (similarSearch) {
			partBuffer.append('%').append(thisToken).append('%');
		} else {*/
		partBuffer.append(thisToken);
		//}
		partBuffer.append('\'');
		return partBuffer.append(' ');
	}

	/**
	 * 功能:获取与TRS数据库中与时间有关的查询条件
	 * 
	 * @param intPast
	 * @return
	 * @throws Exception
	 */
	public static String getTRSDateTimeExp(String strSpan, String field, int maxExp) throws Exception {

		if (StringUtils.isBlank(strSpan))
			return "";

		// -99999为全部时间段的代号，故将不用生成查询条件
		if (strSpan.trim().equals("-99999"))
			return "";
		Calendar cal = Calendar.getInstance();
		String strCurDay = getFormatTime(cal);

		cal.add(Calendar.DATE, Integer.parseInt(strSpan));
		return field + "=between['" + getFormatTime(cal) + "','" + strCurDay + "']";
	}
	
	/**
	 * 功能:获取与TRS数据库中与时间有关的查询条件 生成字符串数组 string[0]时间首  string[1]时间尾
	 * 
	 * @param intPast
	 * @return
	 * @throws Exception
	 */
	public static String[] getTRSDateTimeExp_List(String strSpan, int maxExp) throws Exception {

		String[] dateList = new String[2];
		if (StringUtils.isBlank(strSpan))
			return null;

		// -99999为全部时间段的代号，故将不用生成查询条件
		if (strSpan.trim().equals("-99999"))
			return null;
		Calendar cal = Calendar.getInstance();
		String strCurDay = getFormatTime(cal);

		cal.add(Calendar.DATE, Integer.parseInt(strSpan));
		
		dateList[0] = getFormatTime(cal);
		dateList[1] = strCurDay;
		return dateList;
	}
	/**
	 * 功能：格式化日期格式，形如：2005-06-18
	 * 
	 * @param cal
	 * @return
	 */
	public static String getFormatTime(Calendar cal) {
		StringBuffer sb = new StringBuffer();
		sb.append(cal.get(Calendar.YEAR));
		sb.append("-");
		if((cal.get(Calendar.MONTH) + 1)<10)
			sb.append("0"+(cal.get(Calendar.MONTH) + 1));
		else			
			sb.append(cal.get(Calendar.MONTH) + 1);
		sb.append("-");
		if((cal.get(Calendar.DAY_OF_MONTH))<10)
			sb.append("0"+(cal.get(Calendar.DAY_OF_MONTH)));
		else
			sb.append(cal.get(Calendar.DAY_OF_MONTH));
		return sb.toString();
	}
	
	/**
	 * 时光隧道，flex展示xml
	 * 
	 * @param name
	 * 
	 * @param size
	 * 
	 * @param columnValues
	 * 
	 * @return
	 */
	public static String buildTree( String name, int size, String columnValues) {
		StringBuilder sb = new StringBuilder(128);
		if (columnValues.equals("DOC_WRITEDATE_YEAR")) {
			sb.append("<yearData>");
			sb.append("<year>");
			sb.append(name);
			sb.append("</year>");
			sb.append("<value>");
			sb.append(size);
			sb.append("</value>");
			sb.append("</yearData>");
		}
		if (columnValues.equals("DOC_WRITEDATE_MONTH")) {
			  sb.append("<monthData>");
				sb.append("<month>");
				sb.append(name);
				sb.append("</month>");
				sb.append("<value>");
				sb.append(size);
				sb.append("</value>");
				sb.append("</monthData>");
		}
		return sb.toString();
	}
	
	/**
	 * 分类统计XML
	 * @param name
	 * @param size
	 * @return
	 */
	public static String amChart_FL(String name, int size) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("<slice title='"+name+"'>");
		sb.append(size);
		sb.append("</slice>");
		return sb.toString();
	}
	
	public static String getDateStr(Date date,String pattern){
		if(pattern==null){
			pattern="yyyyMMdd";
		}
		DateFormat dateDire=new SimpleDateFormat(pattern);
		return dateDire.format(date);
	}
	
	public static String getDateStr(String str,int days){
		Calendar calendar=Calendar.getInstance();
		String pattern="yyyyMMdd";
		DateFormat dateDire=new SimpleDateFormat(pattern);
		try {
			Date date=dateDire.parse(str);
			calendar.setTimeInMillis(date.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, days);
		} catch (Exception e) {
			return str;
		}
		
		return dateDire.format(calendar.getTime());
	}
	
	public static String getDateStrByMonth(String str,int month){
		Calendar calendar=Calendar.getInstance();
		String pattern="yyyyMMdd";
		DateFormat dateDire=new SimpleDateFormat(pattern);
		try {
			Date date=dateDire.parse(str);
			calendar.setTimeInMillis(date.getTime());
			calendar.add(Calendar.MONTH, month);
		} catch (Exception e) {
			return str;
		}
		
		return dateDire.format(calendar.getTime());
	}
	
	public static String getSortColumn(String sortColumn){
		if("pubTime".equalsIgnoreCase(sortColumn)){
			return "pub_time";
		}else if("newsid".equalsIgnoreCase(sortColumn)){
			return "newsid";
		}else if("newsType".equalsIgnoreCase(sortColumn)){
			return "news_Type";
		}else if("originFileName".equalsIgnoreCase(sortColumn)){
			return "origin_File_Name";
		}else if("title".equalsIgnoreCase(sortColumn)){
			return "title";
		}else if("author".equalsIgnoreCase(sortColumn)){
			return "author";
		}else if("importTime".equalsIgnoreCase(sortColumn)){
			return "import_Time";
		}else if("landTime".equalsIgnoreCase(sortColumn)){
			return "land_Time";
		}else if("pubStatus".equalsIgnoreCase(sortColumn)){
			return "pub_Status";
		}
		return sortColumn;
	}
	
	
	
	public static void main(String[] args) throws ParseException {
		String str="add,f.as啊发;生（*阿斯+=蒂芬";
		System.out.println(getDateStrByMonth("20151130", -1));
		
	}
	
	
}
