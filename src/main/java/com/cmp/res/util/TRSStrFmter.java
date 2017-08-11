package com.cmp.res.util;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;


import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.perl.Perl5Util;



/**
 * 工具方法,将构造检索词的类,将用户输入格式化为标准的TRS查询串
 * 
 * @author trs
 * 
 */
public class TRSStrFmter {
	/**
	 * 文本显示格式
	 */
	/**
	 * search word mark split character
	 */
	public static final String MARK_SPLIT_CHARACTER = "$^~";
	/**
	 * PRE格式显示文本
	 */
	public static final int DISPLAYFORMAT_PRE = 1;
	/**
	 * HTML格式显示文本
	 */
	public static final int DISPLAYFORMAT_HTML = 2;
	/**
	 * 自动折行显示文本
	 */
	public static final int DISPLAYFORMAT_AUTO = 3;

	/**
	 * 附加字段文本显示格式
	 */
	/**
	 * 不做附加格式显示文本
	 */
	public static final int COLUMN_DISPLAYFORMAT_NONE = 0;
	/**
	 * 转换空格
	 */
	public static final int COLUMN_DISPLAYFORMAT_NBSP = 1;
	/**
	 * 以段为单位进行自动折行
	 */
	public static final int COLUMN_DISPLAYFORMAT_P = 2;
	/**
	 * 转换回车
	 */
	public static final int COLUMN_DISPLAYFORMAT_RETRUN = 4;
	/**
	 * 转换时将原来的换行删除
	 */
	public static final int COLUMN_DISPLAYFORMAT_DELETE_RETURN = 8;
	/**
	 * 是否替换特殊字符
	 */
	public static final int COLUMN_DISPLAYFORMAT_ONLY_SPECIAL_RETURN = 0x10;
	public static final String TXTENTER = "\n";
	
	public static final String HTMLENTER = "<br/>&nbsp;&nbsp;";
	
	public static final int MAX_WORDS_SIZE=100;
	
	public static final String FOUNDER_FONT_MARK_RED=" color=red ";

	
	
	/**
	 * HTML字段做图片的替换
	 * 
	 * @param config
	 *            Config 配置信息
	 * @param strIn
	 *            String 格式化前的信息
	 * 
	 * @return <code>String</code> 格式化后的信息
	 * 
	 * @exception com.trs.was.Exception
	 *                出现异常
	 */
	public static String doHtmlImage(String strInput, String[] urls)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strUnReplaceSrc, strReplacedSrc;
		int begin = 0;
		int end;
		int i=0;

		begin = strOut.indexOf("<img"); // 图片开始
		while (begin != -1) // 图片存在
		{
			if(i==urls.length) break;
			end = strOut.indexOf(">", begin); // 图片结束
			strUnReplaceSrc = strOut.substring(begin, end + 1); // 图片信息
			 //System.out.println("IMG is:"+strUnReplaceSrc);
			strReplacedSrc = doHTMLImgSrc(strUnReplaceSrc, urls[i]); // WCM路径到WAS路径的转换
			//System.out.println(strReplacedSrc);
			// System.out.println("Replaced IMG is:"+strReplacedSrc);
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<img", begin + strReplacedSrc.length()); // 下一个
			i++;
		}
		return strOut;

	}
	
	/*public static String doHtmlImage(String strInput, String[] urls,Article article)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strUnReplaceSrc, strReplacedSrc;
		int begin = 0;
		int end;
		int i=0;
		List<String> list=new ArrayList<String>();

		begin = strOut.indexOf("<img"); // 图片开始
		while (begin != -1) // 图片存在
		{
			if(i==urls.length) break;
			end = strOut.indexOf(">", begin); // 图片结束
			strUnReplaceSrc = strOut.substring(begin, end + 1); // 图片信息
			 //System.out.println("IMG is:"+strUnReplaceSrc);
			strReplacedSrc = doHTMLImgSrc(strUnReplaceSrc, urls[i]); // WCM路径到WAS路径的转换
			//System.out.println(strReplacedSrc);
			// System.out.println("Replaced IMG is:"+strReplacedSrc);
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<img", begin + strReplacedSrc.length()); // 下一个
			i++;
		}
		
		return strOut;

	}
	
	
	public static String doHtmlImage(String strInput, List<PublishAttachment> list)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strUnReplaceSrc, strReplacedSrc;
		int begin = 0;
		int end;
		int i=0;

		begin = strOut.indexOf("<img"); // 图片开始
		while (begin != -1) // 图片存在
		{
			if(i==list.size()) break;
			end = strOut.indexOf(">", begin); // 图片结束
			strUnReplaceSrc = strOut.substring(begin, end + 1); // 图片信息
			String url=null;
			for(PublishAttachment attachment:list){
				if(strUnReplaceSrc.indexOf(attachment.getFileName())!=-1){
					url=attachment.getLargeFileUrl();
					break;
				}
			}
			if(url==null){
				continue;
			}
			 //System.out.println("IMG is:"+strUnReplaceSrc);
			strReplacedSrc = doHTMLImgSrc(strUnReplaceSrc, url); // WCM路径到WAS路径的转换
			//System.out.println(strReplacedSrc);
			// System.out.println("Replaced IMG is:"+strReplacedSrc);
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<img", begin + strReplacedSrc.length()); // 下一个
			i++;
		}
		return strOut;

	}
	
	public static String doHtmlImageDMT(String strInput, List<PublishNewsAttachment> list)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strUnReplaceSrc, strReplacedSrc;
		int begin = 0;
		int end;
		int i=0;

		begin = strOut.indexOf("<img"); // 图片开始
		while (begin != -1) // 图片存在
		{
			if(i==list.size()) break;
			end = strOut.indexOf(">", begin); // 图片结束
			strUnReplaceSrc = strOut.substring(begin, end + 1); // 图片信息
			String url=null;
			for(PublishNewsAttachment attachment:list){
				if(strUnReplaceSrc.indexOf(attachment.getEntId())!=-1){
					url=attachment.getLargeFileUrl();
					break;
				}
			}
			if(url==null){
				continue;
			}
			 //System.out.println("IMG is:"+strUnReplaceSrc);
			strReplacedSrc = doHTMLImgSrc(strUnReplaceSrc, url); // WCM路径到WAS路径的转换
			//System.out.println(strReplacedSrc);
			// System.out.println("Replaced IMG is:"+strReplacedSrc);
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<img", begin + strReplacedSrc.length()); // 下一个
			i++;
		}
		return strOut;

	}*/
	
	

	
	public static String doHtmlImage(String strInput,String url)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strUnReplaceSrc, strReplacedSrc;
		int begin = 0;
		int end;
		int i=0;

		begin = strOut.indexOf("<img"); // 图片开始
		while (begin != -1) // 图片存在
		{
			
			end = strOut.indexOf(">", begin); // 图片结束
			strUnReplaceSrc = strOut.substring(begin, end + 1); // 图片信息
			 //System.out.println("IMG is:"+strUnReplaceSrc);
			strReplacedSrc = doHTMLImgSrc(strUnReplaceSrc, url); // WCM路径到WAS路径的转换
			//System.out.println(strReplacedSrc);
			// System.out.println("Replaced IMG is:"+strReplacedSrc);
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<img", begin + strReplacedSrc.length()); // 下一个
			i++;
		}
		return strOut;

	}
	
	public static String doHtmlImageForItem(String strInput,String url)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strUnReplaceSrc, strReplacedSrc;
		int begin = 0;
		int end;
		int i=0;

		begin = strOut.indexOf("<img"); // 图片开始
		while (begin != -1) // 图片存在
		{
			
			end = strOut.indexOf(">", begin); // 图片结束
			strUnReplaceSrc = strOut.substring(begin, end + 1); // 图片信息
			 //System.out.println("IMG is:"+strUnReplaceSrc);
			strReplacedSrc = doHTMLImgSrc(strUnReplaceSrc, url); // WCM路径到WAS路径的转换
			//System.out.println(strReplacedSrc);
			// System.out.println("Replaced IMG is:"+strReplacedSrc);
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<img", begin + strReplacedSrc.length()); // 下一个
			i++;
		}
		return strOut;

	}
	
	public static String doHTMLImgSrcForItem(String strInput, String urlExt)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strReplacedSrc;
		int begin = 0;
		int end;
		int fileIdIndex;
		ParameterfObject src = null; // src参数对象
		ParameterfObject oldsrc = null; // oldsrc参数对象
		src = getParameter(strOut, begin, "src");
		while (src != null) // 图片存在
		{
			strReplacedSrc = src.getParameterValue(); // 保存src部分
			begin = src.getBegin();
			end = src.getEnd();
			/*fileIdIndex = strReplacedSrc.lastIndexOf("/"); // 寻找最后一个"/"
			if ((fileIdIndex != -1)
					&& ((fileIdIndex + 1) < strReplacedSrc.length())) {
				strReplacedSrc = strReplacedSrc.substring(fileIdIndex + 1);
			}*/
			// Need Update
			//strReplacedSrc =  urlExt;
			strReplacedSrc =  urlExt + "&id="+ strReplacedSrc;
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			src = getParameter(strOut, begin + strReplacedSrc.length(), "src");
		}
		return strOut;

	}

	
	
	/**
	 * HTML字段做图片的替换
	 * 
	 * @param config
	 *            Config 配置信息
	 * @param strIn
	 *            String 格式化前的信息
	 * 
	 * @return <code>String</code> 格式化后的信息
	 * 
	 * @exception com.trs.was.Exception
	 *                出现异常
	 */
	public static String doWCMHtmlImage(String strInput, String urlExt)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strUnReplaceSrc, strReplacedSrc;
		int begin = 0;
		int end;

		begin = strOut.indexOf("<img"); // 图片开始
		while (begin != -1) // 图片存在
		{
			end = strOut.indexOf(">", begin); // 图片结束
			strUnReplaceSrc = strOut.substring(begin, end + 1); // 图片信息
			strReplacedSrc = doWCMImgSrc(strUnReplaceSrc, urlExt); // WCM路径到WAS路径的转换
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<img", begin + strReplacedSrc.length()); // 下一个
		}
		return strOut;

	}
	
	
	public static String doHtmlImageEnter(String strInput, String enter)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		int begin = 0;
		int end;

		begin = strOut.indexOf("<img"); // 图片开始
		while (begin != -1) // 图片存在
		{
			end = strOut.indexOf(">", begin); // 图片结束
			
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.insert(end+1, enter);
			//linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<img", end + enter.length()+1); // 下一个
		}
		return strOut;

	}
	
	

	/**
	 * WCM中HTML字段做图片的替换
	 * 
	 * @param config
	 *            Config 配置信息
	 * @param strIn
	 *            String 格式化前的信息
	 * 
	 * @return <code>String</code> 格式化后的信息
	 * 
	 * @exception com.trs.was.Exception
	 *                出现异常
	 */
	public static String doWCMImgSrc(String strInput, String urlExt)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strReplacedSrc;
		int begin = 0;
		int end;
		int fileIdIndex;
		ParameterfObject src = null; // src参数对象
		ParameterfObject oldsrc = null; // oldsrc参数对象
		src = getParameter(strOut, begin, "src");
		while (src != null) // 图片存在
		{
			strReplacedSrc = src.getParameterValue(); // 保存src部分
			begin = src.getBegin();
			end = src.getEnd();
			fileIdIndex = strReplacedSrc.lastIndexOf("/"); // 寻找最后一个"/"
			if ((fileIdIndex != -1)
					&& ((fileIdIndex + 1) < strReplacedSrc.length())) {
				strReplacedSrc = strReplacedSrc.substring(fileIdIndex + 1);
			}
			// Need Update
			strReplacedSrc =urlExt+ strReplacedSrc;
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			src = getParameter(strOut, begin + strReplacedSrc.length(), "src");
		}
		return strOut;

	}
	/**
	 * WCM中HTML字段做图片的替换
	 * 
	 * @param config
	 *            Config 配置信息
	 * @param strIn
	 *            String 格式化前的信息
	 * 
	 * @return <code>String</code> 格式化后的信息
	 * 
	 * @exception com.trs.was.Exception
	 *                出现异常
	 */
	public static String doHTMLImgSrc(String strInput, String urlExt)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strReplacedSrc;
		int begin = 0;
		int end;
		int fileIdIndex;
		ParameterfObject src = null; // src参数对象
		ParameterfObject oldsrc = null; // oldsrc参数对象
		src = getParameter(strOut, begin, "src");
		while (src != null) // 图片存在
		{
			strReplacedSrc = src.getParameterValue(); // 保存src部分
			begin = src.getBegin();
			end = src.getEnd();
			/*fileIdIndex = strReplacedSrc.lastIndexOf("/"); // 寻找最后一个"/"
			if ((fileIdIndex != -1)
					&& ((fileIdIndex + 1) < strReplacedSrc.length())) {
				strReplacedSrc = strReplacedSrc.substring(fileIdIndex + 1);
			}*/
			// Need Update
			//strReplacedSrc =  urlExt;
			strReplacedSrc =  urlExt + "&id="+ strReplacedSrc;
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			src = getParameter(strOut, begin + strReplacedSrc.length(), "src");
		}
		return strOut;

	}
	

	/**
	 * HTML字段做视频的替换
	 * 
	 * @param config
	 *            Config 配置信息
	 * @param strIn
	 *            String 格式化前的信息
	 * 
	 * @return <code>String</code> 格式化后的信息
	 * 
	 * @exception com.trs.was.Exception
	 *                出现异常
	 */
	public static String doWCMHtmlParam(String strInput, String urlExt)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strUnReplaceSrc, strReplacedSrc;
		int begin = 0;
		int end;

		begin = strOut.indexOf("<param"); // 图片开始
		while (begin != -1) // 图片存在
		{
			end = strOut.indexOf(">", begin); // 图片结束
			strUnReplaceSrc = strOut.substring(begin, end + 1); // 图片信息
			strReplacedSrc = doWCMParamValue(strUnReplaceSrc, urlExt); // WCM路径到WAS路径的转换
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end + 1, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			begin = strOut.indexOf("<param", begin + strReplacedSrc.length()); // 下一个
		}
		return strOut;

	}

	/**
	 * WCM中HTML字段做视频的替换
	 * 
	 * @param config
	 *            Config 配置信息
	 * @param strIn
	 *            String 格式化前的信息
	 * 
	 * @return <code>String</code> 格式化后的信息
	 * 
	 * @exception com.trs.was.Exception
	 *                出现异常
	 */
	public static String doWCMParamValue(String strInput, String urlExt)
			throws Exception {

		String strOut = strInput;

		StringBuffer linkBuffer;
		String strReplacedSrc;
		int begin = 0;
		int end;
		int fileIdIndex;
		ParameterfObject src = null; // src参数对象
		ParameterfObject oldsrc = null; // oldsrc参数对象
		src = getParameter(strOut, begin, "value");
		while (src != null) // 图片存在
		{
			strReplacedSrc = src.getParameterValue(); // 保存src部分
			begin = src.getBegin();
			end = src.getEnd();
			fileIdIndex = strReplacedSrc.lastIndexOf("/"); // 寻找最后一个"/"
			if ((fileIdIndex != -1)
					&& ((fileIdIndex + 1) < strReplacedSrc.length())) {
				strReplacedSrc = strReplacedSrc.substring(fileIdIndex + 1);
			}
			// Need Update
			strReplacedSrc = urlExt+ strReplacedSrc;
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			src = getParameter(strOut, begin + strReplacedSrc.length(), "value");
		}
		return strOut;

	}

	/**
	 * 获得HTML参数对象
	 * 
	 * @param link_replace
	 *            String 包含href的字符串
	 * @param begin
	 *            int 开始位置
	 * @param paramName
	 *            String 参数名
	 * 
	 * @return <code>ParameterfObject</code> 参数对象
	 * 
	 * @exception com.trs.was.Exception
	 *                出现异常
	 */
	public static ParameterfObject getParameter(String link_replace, int begin,
			String paramName) throws Exception {
		if ((paramName == null) || (paramName.equals(""))) {
			throw new Exception("XXX#参数名为空#PTools.getParameter#");
		}
		if (link_replace == null) {
			throw new Exception("XXX#包含参数'" + paramName
					+ "'的字符串为空#PTools.getParameter#");
		}
		if ((begin < 0) || (begin >= link_replace.length())) {
			throw new Exception("XXX#非法的开始位置'" + begin
					+ "'#PTools.getParameter#");
		}

		ParameterfObject parm = null; // 参数对象
		int hrefBegin = link_replace.indexOf(paramName, begin);
		int iBegin, iEnd;
		char c_mark; // 分隔符
		while (hrefBegin != -1) {
			if (hrefBegin == link_replace.length() - paramName.length()) {
				// 参数之后没有其他内容了，结束本次寻找
				break;
			}
			if ((hrefBegin != 0)
					&& (link_replace.charAt(hrefBegin - 1) != ' ')
					|| (((link_replace.charAt(hrefBegin + paramName.length()
							+ 1) >= 'a') && (link_replace.charAt(hrefBegin
							+ paramName.length() + 1) <= 'z'))
							|| ((link_replace.charAt(hrefBegin
									+ paramName.length() + 1) >= 'A') && (link_replace
									.charAt(hrefBegin + paramName.length() + 1) <= 'Z')) || ((link_replace
							.charAt(hrefBegin + paramName.length() + 1) >= '0') && (link_replace
							.charAt(hrefBegin + paramName.length() + 1) <= '9')))) {
				// 参数的前后前面必须是空格，后面不能是字母或数字
				hrefBegin = link_replace.indexOf(paramName, hrefBegin
						+ paramName.length());
				continue;
			}
			parm = new ParameterfObject(); // 构造新的参数对象
			hrefBegin = hrefBegin + paramName.length(); // 默认为参数后开始
			while ((hrefBegin < link_replace.length())
					&& (link_replace.charAt(hrefBegin) != '=')) // 获得'='位置
			{
				hrefBegin++;
			}
			if (hrefBegin == link_replace.length()) // 没有参数值，不完整的参数
			{
				throw new Exception("XXX#参数未设置值#PTools.getParameter#");
			}
			hrefBegin++; // 跳过'='号
			while ((hrefBegin < link_replace.length())
					&& (link_replace.charAt(hrefBegin) == ' ')) // 忽略参数值前空格
			{
				hrefBegin++;
			}
			if (hrefBegin == link_replace.length()) // 没有参数值，不完整的参数
			{
				throw new Exception("XXX#错误的Href格式#PTools.getParameter#");
			}
			if ((link_replace.charAt(hrefBegin) == '\'')
					|| (link_replace.charAt(hrefBegin) == '"')) // 有引号
			{
				c_mark = link_replace.charAt(hrefBegin); // 记录引号类型，找到完全匹配的后一个引号
				hrefBegin++; // 跳过引号
			} else // 无引号，直接写参数值
			{
				c_mark = ' '; // 分隔符号为空格
			}
			parm.setMark(c_mark); // 设置分隔符

			iBegin = hrefBegin; // 记录参数值开始位置
			while ((hrefBegin < link_replace.length())
					&& (link_replace.charAt(hrefBegin) != c_mark)) // 获得参数值
			{
				hrefBegin++;
			}
			iEnd = hrefBegin; // 参数值结束位置
			if (hrefBegin == link_replace.length()) // 到最后了
			{
				if (c_mark != ' ') // 未找到匹配的引号
				{
					throw new Exception("XXX#参数使用的引号" + c_mark
							+ "没有匹配#PTools.getParameter#");
				}
			}
			parm.setBegin(iBegin);
			parm.setEnd(iEnd);
			parm.setParameterValue(link_replace.substring(iBegin, iEnd));
			break;
		}
		return parm;
	}

	public static String displayFormat(String strIn, String strColumnFormat)
			throws Exception {
		// System.out.println("PTOOLS : strColumnFormat = "+strColumnFormat);
		String strOut;
		
		int iColumnFormat;
		try {
			iColumnFormat = Integer.parseInt(strColumnFormat);
		} catch (NumberFormatException ne) {
			return strIn; // 非正确的格式化参数，忽略其，不做格式化处理
		}
		if ((strIn == null) || (strIn.length() == 0)) // 不需要做格式化
		{
			return strIn;
		}
		int count = strIn.length();
		int i;
		char ch, ch_tmp;
		StringBuffer strBufferIn = new StringBuffer(strIn);
		StringBuffer strBufferOut = new StringBuffer(strIn.length());
		
		for (i = 0; i < count; i++) {
			ch = strBufferIn.charAt(i);
			// if(((int)ch == 65440)||((int)ch == 160)||((int)ch == 63))
			// //是空格，并且需要转换空格
			if (((int) ch == 65440) || ((int) ch == 160)) // 是unicode空格，必须转换，否则页面上显示为问号
			{
				if (i < count) {
					strBufferOut.append("&nbsp;");
				}
				continue;
			}
			if (((int) ch == 65441) || ((int) ch == 161)) // 是unicode圆点，必须转换，否则页面上显示为问号
			{
				if (i < count) {
					strBufferOut.append(".");
				}
				continue;
			}
			if ((ch == ' ')
					&& (isSetFlag(iColumnFormat, COLUMN_DISPLAYFORMAT_NBSP))) // 是空格，并且需要转换空格
			{
				if (i < count - 1) {
					ch_tmp = strBufferIn.charAt(i + 1);
					if (((ch_tmp >= 'a') && (ch_tmp <= 'z'))
							|| ((ch_tmp >= 'A') && (ch_tmp <= 'Z'))
							|| ((ch_tmp >= '0') && (ch_tmp <= '9'))
							|| (ch_tmp == '_'))
					// if(((ch_tmp<=127))&&(ch_tmp!=' '))
					{
						// 英文字符中的空格不转换
						strBufferOut.append(ch);
					} else {
						if ((ch_tmp == ' ')) {
							strBufferOut.append("&nbsp;");
						} else {
							strBufferOut.append(ch);
						}
					}
				}
				continue;
			}
			if ((ch == '\t')
					&& (isSetFlag(iColumnFormat, COLUMN_DISPLAYFORMAT_NBSP))) // 是tab，并且需要转换
			{
				strBufferOut.append("&#09;");
				continue;
			}
			if ((ch == '\r')
					&& (isSetFlag(iColumnFormat, COLUMN_DISPLAYFORMAT_RETRUN))) // 是换行，并且需要转换
			{
				if (!isSetFlag(iColumnFormat,
						COLUMN_DISPLAYFORMAT_DELETE_RETURN)) // 保留回行
				{
					strBufferOut.append("\r");
				}
				if (i < count - 1) {
					ch_tmp = strBufferIn.charAt(i + 1);
					if (ch_tmp == '\n') {
						if (!isSetFlag(iColumnFormat,
								COLUMN_DISPLAYFORMAT_DELETE_RETURN)) // 保留回行
						{
							strBufferOut.append("\n");
						}
						i++;
						
					}
				} 
				
				strBufferOut.append("<br>"); // 直接回行
				
				continue;
			}
			if ((ch == '\n')
					&& (isSetFlag(iColumnFormat, COLUMN_DISPLAYFORMAT_RETRUN))) // 是换行，并且需要转换
			{
				if (!isSetFlag(iColumnFormat,
						COLUMN_DISPLAYFORMAT_DELETE_RETURN)) // 保留回行
				{
					strBufferOut.append("\n");
				}
				if (i < count - 1) {
					ch_tmp = strBufferIn.charAt(i + 1);
					if (ch_tmp == '\r') {
						if (!isSetFlag(iColumnFormat,
								COLUMN_DISPLAYFORMAT_DELETE_RETURN)) // 保留回行
						{
							strBufferOut.append("\r");
						}
						i++;
						
					}
				}
				
				strBufferOut.append("<br>"); // 直接回行
				
				continue;
			}
			strBufferOut.append(ch);
		}
		return strBufferOut.toString();

	}

	/**
	 * 判断参数是否被设置
	 * 
	 * @param currentValue
	 *            int 当前设置的参数值
	 * @param checkValue
	 *            int 需要判断的参数
	 * 
	 * @return <code>boolean</code> 是否被设置
	 * 
	 */
	public static boolean isSetFlag(int currentValue, int checkValue) {
		if ((currentValue & checkValue) != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能:获取与TRS数据库中与时间有关的查询条件
	 * 
	 * @param intPast
	 * @return
	 * @throws Exception
	 */
	public static String getTRSDateTimeExp(String strSpan, String field,
			int maxExp) throws Exception {

		if (StringUtils.isBlank(strSpan))
			return "";

		// -99999为全部时间段的代号，故将不用生成查询条件
		if (strSpan.trim().equals("-99999"))
			return "";
		Calendar cal = Calendar.getInstance();
		String strCurDay = getFormatTime(cal);

		cal.add(Calendar.DATE, Integer.parseInt(strSpan));
		return field + "=between['" + getFormatTime(cal) + "','" + strCurDay
				+ "']";
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
		sb.append(cal.get(Calendar.MONTH) + 1);
		sb.append("-");
		sb.append(cal.get(Calendar.DAY_OF_MONTH));
		return sb.toString();
	}

	/**
	 * 工具方法,将用户输入格式化为标准TRS查询字符串格式.
	 * 
	 * @param swd
	 *            用户输入的查询串
	 * @param bSimilarSearch
	 *            是否使用模糊检索
	 * @param maxLength
	 *            为防止恶意输入,将用户输入的检索串加入长度限制.如果不需要加入限制请用Integer.MIN_VALUE
	 * @return 经过格式化后的检索词
	 */
	public static String formatSW(String swd, String space2rel,
			boolean bSimilarSearch, int maxLength) {

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
				sw = sw.replaceAll("'", "\\\\'");
				return fmtAftTrm(sw, space2rel, bSimilarSearch);
			}
		}
	}

	public static String formatSW_x(String swd, String space2rel,
			boolean bSimilarSearch, int maxLength) {

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
				sw = sw.replaceAll("'", "\\\\'");
				if (bSimilarSearch)
					return fmtAftTrm_like(sw, space2rel, bSimilarSearch);
				else
					return fmtAftTrm_x(sw, space2rel, bSimilarSearch);

			}
		}
	}

	/**
	 * 格式化TRS检索词的主体方法
	 * 
	 * @param sw
	 * @param bSimilarSearch
	 * @return
	 */
	private static String fmtAftTrm(String sw, String space2rel,
			boolean bSimilarSearch) {

		// 确保第一个字符是
		StringBuffer buffer = new StringBuffer(1024).append('(');
		StringTokenizer st = new StringTokenizer(sw, " ");
		StringBuffer firstTokenBuffer = giveEachToken(st.nextToken().trim(),
				bSimilarSearch);
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
						buffer.append(space2rel).append(
								giveEachToken(thisToken, bSimilarSearch));
					} else {
						buffer.append(giveEachToken(thisToken, bSimilarSearch));
					}
				} else if (bApdOpt) {// 如果下一个append的应该是连接符
					if (bOpt(thisToken) == null) {
						buffer.append(space2rel).append(
								giveEachToken(thisToken, bSimilarSearch));
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

	private static String fmtAftTrm_x(String sw, String space2rel,
			boolean bSimilarSearch) {

		// 确保第一个字符是
		StringBuffer buffer = new StringBuffer(1024).append('(');
		StringTokenizer st = new StringTokenizer(sw, " ");
		StringBuffer firstTokenBuffer = giveEachToken(st.nextToken().trim(),
				bSimilarSearch);
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
						buffer.append(space2rel).append(
								giveEachToken(thisToken, bSimilarSearch));
					} else {
						buffer.append(giveEachToken(thisToken, bSimilarSearch));
					}
				} else if (bApdOpt) {
					if (thisToken != null) {
						buffer.append(space2rel).append(
								giveEachToken(thisToken, bSimilarSearch));
					} else {

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
	 * 适用于like模糊检索
	 * 
	 * @param sw
	 * @param space2rel
	 * @param bSimilarSearch
	 * @return
	 */
	private static String fmtAftTrm_like(String sw, String space2rel,
			boolean bSimilarSearch) {

		// 确保第一个字符是
		StringBuffer buffer = new StringBuffer(1024).append("like(");
		StringTokenizer st = new StringTokenizer(sw, " ");
		StringBuffer firstTokenBuffer = giveEachToken(st.nextToken().trim(),
				bSimilarSearch); // 如果不需要%形式，则将bbSimilarSearch设置为false即可
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
				b2End = !st.hasMoreTokens();
				if (b2End) {
					if (bApdOpt) {
						buffer.append(space2rel).append(" like(").append(
								giveEachToken(thisToken, false)).append(") ");
					} else {
						buffer.append(giveEachToken(thisToken, bSimilarSearch));

					}
				} else if (bApdOpt) {
					if (thisToken != null) {
						buffer.append(space2rel).append(" like(").append(
								giveEachToken(thisToken, false)).append(") ");
					} else {

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
		/*
		 * if (similarSearch) {
		 * partBuffer.append('%').append(thisToken).append('%'); } else {
		 */
		partBuffer.append(thisToken);
		// }
		partBuffer.append('\'');
		return partBuffer.append(' ');
	}

	/**
	 * 将字符串数组以指定的分隔符添加到原字符串之后。
	 * 
	 * @param strSource
	 *            String 现有的字符串
	 * @param appendArray
	 *            String[] 需要添加的字符串数组
	 * @param separator
	 *            String 分隔符
	 * @param isSplit
	 *            boolean 是否判断重复的字符串
	 * 
	 * @return <code>String</code> 添加完成后的字符串
	 * @exceprtion com.trs.was.Exception 父字符串或分隔符为空
	 */
	public static String appendString(String strSource, String[] appendArray,
			String separator, boolean isSplit) throws Exception {
		if (strSource == null) {
			throw new Exception("21001#原字符串为空#PTools.appendString#");
		}
		if (appendArray == null) {
			throw new Exception("21001#需要添加的字符串数组为空#PTools.appendString#");
		}
		if (separator == null) {
			throw new Exception("21001#分隔符为空#PTools.appendString#");
		}

		String strTarget = strSource;
		boolean b_isFirstItem = true; // 第一个字符串，前面不加分隔符
		Vector v_words = null; // 保存字符串组，以便进行排重判断
		StringBuffer sb_target = new StringBuffer(strSource); // 构造StringBuffer,而不用String的直接向加，保证效率
		try {
			if (isSplit) {
				v_words = new Vector(); // 构造空的
				String[] sourceStrs = splitString(strSource, separator); // 切割添加到的字符串，进行去重的判断
				if ((sourceStrs != null) && (sourceStrs.length > 0)) //
				{
					for (int i = 0; i < sourceStrs.length; i++) // 添加到的字符串存在
					{
						if (!sourceStrs[i].equals("")) // 不是空串
						{
							v_words.add(sourceStrs[i]); // 进行保存
						}
					}
				}
			}
			if (!strSource.equals("")) {
				b_isFirstItem = false;
			}

			for (int i = 0; i < appendArray.length; i++) {
				if ((isSplit) && (v_words.contains(appendArray[i]))) // 重复字符串
				{
					continue; // 下一个字符串
				}

				if (b_isFirstItem) {
					b_isFirstItem = false;
				} else {
					sb_target = sb_target.append(separator);
				}
				sb_target = sb_target.append(appendArray[i]);

				if (isSplit) {
					v_words.add(appendArray[i]); // 保存这个字符串，以便排重
				}
			}
			strTarget = sb_target.toString(); // 获得最终组合完成的字符串

			return strTarget;
		} catch (Exception e) {

			throw new Exception("20001#将字符串数组以指定的分隔'" + separator
					+ "'符添加到原字符串'" + strSource
					+ "'之后的操作出现异常#PTools.appendString#" + e.getMessage());
		}
	}

	/**
	 * 将字符串按照指定的分隔符分割成字符串数组
	 * 
	 * @param strItem
	 *            String 需要分割的字符串
	 * @param separator
	 *            String 分隔符
	 * 
	 * @return <code>String[]</code> 分割后的数组，如果需要分割的字符串为""或分隔符不存在，返回包含需要分割的字符串的数组
	 * @exceprtion com.trs.was.Exception 父字符串或分隔符为空
	 */
	public static String[] splitString(String strItem, String separator)
			throws Exception {
		if (strItem == null) {
			throw new Exception("XXX#父字符串为空#PTools.splitString#");
		}
		if (separator == null) {
			throw new Exception("XXX#分隔符为空#PTools.splitString#");
		}

		int i = 1, index1 = 0, index2;
		String subStr;
		Vector v = new Vector();

		try {
			index2 = strItem.indexOf(separator);
			while (index2 != -1) {
				v.add(strItem.substring(index1, index2));
				index1 = index2 + separator.length();
				index2 = strItem.indexOf(separator, index1);
			}
			if (index1 != 0) {
				v.add(strItem.substring(index1));
			}
			/*
			 * subStr=getSubItem(strItem,i++,separator); while(subStr!=null) {
			 * v.add(subStr); subStr=getSubItem(strItem,i++,separator); }
			 */
			if (v.size() > 0) {
				String[] words = new String[v.size()];
				v.copyInto(words); // 保存到数组
				return words;
			} else {
				String[] splitStr = new String[1];
				splitStr[0] = strItem;
				return splitStr;
			}
		}
		// catch(Exception wase)
		// {
		// throw wase;
		// }
		catch (Exception e) {
			throw new Exception("XXX#以分隔符'" + separator + "'分割字符串'" + strItem
					+ "'时出现异常#PTools.splitString#" + e.getMessage());
		}
	}
	@Deprecated
	/**
	 * 格式化输出信息，替换指定的字符串
	 * 
	 * @param strIn
	 *            String 格式化前的信息
	 * 
	 * @return <code>String</code> 格式化后的信息
	 * 
	 * @exception com.trs.was.Exception
	 *                出现异常
	 */
	public static String replaceStringIgnoreCase(String strInput,
			String strReplaceFrom, String strReplaceTo,
			String strReplaceSeparator, String fontContent) throws Exception {
		if ((strInput == null) || (strReplaceFrom.equals(""))) {
			return strInput;
		}

		String strIn = strInput;
		String strInLower, strReplaceFromLower;
		String strAddPoint;
		String[] strReplaceFromItems = null, strReplaceToItems = null;
		String strReplaceFromItem, strReplaceToItem;
		// 循环次数限定值
		int rewhiletimes = 80;

		StringBuffer bodyBuffer;

		Vector v_from, v_to;
		v_from = new Vector();
		v_to = new Vector();

		int i = 1, j, index;
		int strFromLength, strToLength;

		if (strReplaceSeparator.equals("")) {
			strReplaceFromItems = new String[1];
			strReplaceFromItems[0] = strReplaceFrom;
			strReplaceToItems = new String[1];
			strReplaceToItems[0] = strReplaceTo;
		} else {
			strReplaceFromItem = getSubItem(strReplaceFrom, i,
					strReplaceSeparator);
			while (strReplaceFromItem != null) {
				strReplaceToItem = getSubItem(strReplaceTo, i,
						strReplaceSeparator);
				if (strReplaceToItem != null) {
					v_from.add(strReplaceFromItem);
					v_to.add(strReplaceToItem);
				}
				i++;
				strReplaceFromItem = getSubItem(strReplaceFrom, i,
						strReplaceSeparator);
			}
		}

		if (v_from.size() != 0) {
			strReplaceFromItems = new String[v_from.size()];
			strReplaceToItems = new String[v_from.size()];

			v_from.copyInto(strReplaceFromItems);
			v_to.copyInto(strReplaceToItems);
		}

		if (strReplaceFromItems == null) {
			return strIn;
		}
		// System.out.println("replace from:"+strIn);
		for (j = 0; j < strReplaceFromItems.length; j++) {
			i = 0;
			strInLower = strIn.toLowerCase(); // 转换成小写的
			index = strInLower.indexOf(strReplaceFromItems[j].toLowerCase()); // 查找小写的字段

			// index = strIn.indexOf(strReplaceFromItems[j]);

			strFromLength = strReplaceFromItems[j].length(); // 需要替换的串
			strToLength = strReplaceToItems[j].length(); // 替换成的串
			int retimes = 0;
			bodyBuffer = new StringBuffer(strIn);
			while (index != -1) // 找到所有的需要替换的串
			{
				retimes++;
				// System.out.println("index '"+i+"' is "+index);
				// try {
				// bodyBuffer.replace(index+i*(strToLength-strFromLength),index+i*(strToLength-strFromLength)+strFromLength,strReplaceToItems[j]);
				strAddPoint = bodyBuffer.substring(index + i
						* (strToLength - strFromLength), index + i
						* (strToLength - strFromLength) + strFromLength); // 取出命中的词
				// add 09-04-17

				int beginPoint = index + i * (strToLength - strFromLength);
				int endPoint = index + i * (strToLength - strFromLength)
						+ strFromLength;

				String strBegin = "";
				String strEnd = "";

				if (beginPoint > 0)
					beginPoint--;
				else
					strBegin = " ";
				if (endPoint < strIn.length() - 1)
					endPoint++;
				else
					strEnd = " ";

				String strExtAddPoint = strBegin
						+ bodyBuffer.substring(beginPoint, endPoint) + strEnd;

				if (StringUtils.isAsciiPrintable(strAddPoint)
						&& isEngWord(strExtAddPoint)
						|| !StringUtils.isAsciiPrintable(strAddPoint))// 如果是英文　并且是词时才进行过滤{
				{
					bodyBuffer.replace(index + i
							* (strToLength - strFromLength), index + i
							* (strToLength - strFromLength) + strFromLength,
							"<font " + fontContent + ">" + strAddPoint
									+ "</font>"); // 将命中的词加红以后再替换回去
					i++;
				}

				index = strInLower.indexOf(
						strReplaceFromItems[j].toLowerCase(), index
								+ strFromLength); // 查找小写的字段
				if (retimes == rewhiletimes)
					break;
			}
			strIn = bodyBuffer.toString();
			// System.out.println("replace "+j+":"+strIn);
		}
		// System.out.println("replace final:"+strIn);
		return strIn;
	}

	public static boolean isEngWord(String source) {
		if (StringUtils.isBlank(source))
			return false;
		// System.out.println(source);
		if (SubInfoGetter.isStartOrEndChar(source.charAt(0))
				&& SubInfoGetter.isStartOrEndChar(source
						.charAt(source.length() - 1)))
			return true;
		return false;
	}

	/**
	 * 获得字符串中指定分隔符分隔的指定子串
	 * 
	 * @param strItem
	 *            String 父字符串
	 * @param Index
	 *            int 第几个子串
	 * @param separator
	 *            String 分隔符
	 * 
	 * @return <code>String</code> 指定的子串，如果不存在，则返回""
	 * @exceprtion com.trs.was.Exception 父字符串或分隔符为空
	 */
	public static String getSubItem(String strItem, int Index, String separator)
			throws Exception {

		if (strItem == null) {
			throw new Exception("XXX#父字符串为空#PTools.getSubItem#");
		}
		if (separator == null) {
			throw new Exception("XXX#分隔符为空#PTools.getSubItem#");
		}

		if (Index <= 0) {
			return "";
		}

		int pos1, pos2, i;
		String strSub;
		pos1 = pos2 = 0;
		i = 1;
		try {
			do {
				pos2 = strItem.indexOf(separator, pos1); // 找到一个分隔符
				if (i == Index) // 指定的子串
				{
					if (pos2 != -1) // 不是最后一个，pos2不为-1，从pos1到pos2中间的字符
						strSub = strItem.substring(pos1, pos2);
					else
						// 最后一个，从pos1开始以后所有的字符
						strSub = strItem.substring(pos1);

					return strSub; // 返回获得的子串
				}
				pos1 = pos2 + separator.length(); // 跳过分隔符，以便寻找下一个分隔符
				i++;
			} while (pos2 != -1);

			return null;
		} catch (Exception e) {
			throw new Exception("XXX#在获得父字符串'" + strItem + "'以'" + separator
					+ "'分隔的第'" + Index + "'个子串时出现异常#PTools.getSubItem#"
					+ e.getMessage());
		}
	}

	/**
	 * light current text content
	 * 
	 * @param original
	 * @param searchWord
	 * @param split
	 * @param fontContent
	 *            class='detail_center ' color='#FF0000'
	 * @return
	 * @throws Exception
	 */
	public static String light(String original, String searchWord,
			String split, String fontContent) throws Exception {
		searchWord=StringUtils.remove(searchWord, "(");
		searchWord=StringUtils.remove(searchWord, ")");
		String[] redFromWords = StringUtils.split(searchWord, split);
		/*if (redFromWords != null) // 存在需要替换的词
		{
			String[] redToWords = new String[redFromWords.length];
			String strRedFrom = "", strRedTo = "", strRedSp = "######";
			for (int iRed = 0; iRed < redFromWords.length; iRed++) {

				redToWords[iRed] = "<font " + fontContent + ">"
						+ redFromWords[iRed] + "</font>"; // 替换的词加红
			}
			strRedFrom = TRSStrFmter.appendString(strRedFrom, redFromWords,
					strRedSp, true); // 组合需要替换的词
			strRedTo = TRSStrFmter.appendString(strRedTo, redToWords, strRedSp,
					true); // 组合替换成的词，带红色

			original = TRSStrFmter.replaceStringIgnoreCase(original,
					strRedFrom, strRedTo, strRedSp, fontContent); // 进行替换
		}*/
		
		

		return highlightWords(original,redFromWords,"<font "+fontContent+">","</font>");
		//return null;
	}
	
	public static final String highlightWords(String string, String[] words,
			String startHighlight, String endHighlight) {

		if (string == null || words == null || startHighlight == null
				|| endHighlight == null) {
			return string;
		}
		
		//It seems to be a dangerous operation if words size exceeds its limit.
		if(words.length>MAX_WORDS_SIZE) return string;
		
		StringBuffer regexp = new StringBuffer();
		StringBuffer regexp_cn = new StringBuffer();
		// for Chinese character
		// Iterate through each word and generate a word
		// list for the regexp or regexp_cn
		Perl5Util perl5Util = new Perl5Util();
		
		for (int x = 0; x < words.length; x++) {
			// Excape "|" and "/" to keep us out of trouble in our regexp.
			words[x] = perl5Util.substitute("s#([\\|\\/\\.])#\\\\$1$2#g",
					words[x]);
			if (words[x].charAt(0) > 127) { // 中文不做边界匹配
				if (regexp_cn.length() > 0) {
					regexp_cn.append("|");
				}
				regexp_cn.append(words[x]);
			} else {
				// 英文单词做放宽条件的边界匹配检查 －－－ 边界为：[^A-Za-z0-9_]|\b
				if (regexp.length() > 0) {
					regexp.append("|");
				}
				regexp.append(words[x]);
			}
		}
		// Escape the regular expression delimiter ("/").
		startHighlight = perl5Util.substitute("s#\\/#\\\\/#g", startHighlight);
		endHighlight = perl5Util.substitute("s#\\/#\\\\/#g", endHighlight);
		String rs = string;
		// if keywords include English (or other western language words):
		if (regexp.length() > 0) {
			// Build the regular expression. insert() the first part.
			regexp.insert(0, "s/([^A-Za-z0-9_]|\\b)(");
			// The word list is here already, so just append the REST.
			regexp.append(")([^A-Za-z0-9_]|\\b)/$1");
			regexp.append(startHighlight);
			regexp.append("$2");
			regexp.append(endHighlight);
			regexp.append("$3/igm");
			// Do the actual substitution via a simple regular expression.
			rs = perl5Util.substitute(regexp.toString(), string);
		}
		// if keywords include Chinese (or Japanese, Korean words):
		if (regexp_cn.length() > 0) {
			// Build the regular expression. insert() the first part.
			regexp_cn.insert(0, "s/(");
			// The word list is here already, so just append the REST.
			regexp_cn.append(")/");
			regexp_cn.append(startHighlight);
			regexp_cn.append("$1");
			regexp_cn.append(endHighlight);
			regexp_cn.append("/igm");
			// Do the actual substitution via a simple regular expression.
			rs = perl5Util.substitute(regexp_cn.toString(), rs);
		}
		return rs;
	}
	
	public static String txtEnterToHtml(String content){
		if(StringUtils.isBlank(content)){
			return "";
		}
		return content.replaceAll(TXTENTER, HTMLENTER);
		
		
	}


	public static void main(String[] args) throws Exception {
		System.out.println(doHtmlImageEnter("<img src=\"http://192.168.1.162/upload/pu_68.jpg\" alt=\"\" />象个洋娃娃吗33333<img src=\"http://192.168.1.152/upload/33288.jpg\" alt=\"\" />888rrrrrr", HTMLENTER));
	}

}
