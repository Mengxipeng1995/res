package com.cmp.res.service;

import com.cmp.res.entity.Item;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.User;
import com.cmp.res.util.ParameterfObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.JsonMapper;

import javax.servlet.http.HttpServletResponse;



@Service
public class CommonService {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PhotoService photoService;
	
	public User getCurrentLogin(){
		User user=null;
		String userName=SecurityUtils.getSubject().getPrincipal().toString();
		
		if("admin".equals(userName)){
			user=new User();
			user.setUserName("admin");
			user.setNickName("超级管理员");
			user.setUserLevel(-1);
		}else{
			user=userService.getUserByName(userName);
		}
		
		return user;
		
	}
	
	public void returnDate(HttpServletResponse response,Object obj){
	response.setContentType("text/html;charset=utf-8");
	try{
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(obj));
	}catch(Exception e){
		e.printStackTrace();
	}
	
		
	}
	
	public  String doHtmlImage(Item item,String strInput,String url)
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
			strReplacedSrc = doHTMLImgSrc(item,strUnReplaceSrc, url); // WCM路径到WAS路径的转换
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
	
	public  String doHTMLImgSrc(Item item,String strInput, String urlExt)
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
			//
			try{
				Long.parseLong(strReplacedSrc);
			}catch (Exception e) {
				// TODO: handle exception
				Photo photo=photoService.findByBookidLinkimage(item.getBookid(), strReplacedSrc);
				if(photo!=null){
					strReplacedSrc=photo.getId()+"";
				}
			}
			
			strReplacedSrc =  urlExt + "&id="+ strReplacedSrc;
			linkBuffer = new StringBuffer(strOut);
			linkBuffer.replace(begin, end, strReplacedSrc); // 替换
			strOut = linkBuffer.toString();

			src = getParameter(strOut, begin + strReplacedSrc.length(), "src");
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
	public  ParameterfObject getParameter(String link_replace, int begin,
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

}
