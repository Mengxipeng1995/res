package test;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cmp.res.util.Dom4jUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		//System.out.println("式1·2-1，式1·2-2，式1·2-3，式1·2-4，式1·2-5，式1·2-6".split("，")[0]);
	
		String str="[22]Edward Wilson Kimbark.Direct Current Transmission.New York：1971，(1)";
		
		System.out.println(str.substring(str.indexOf("]")+1));
	}
	
	public static List getImgStr(String htmlStr) {
		String img = "";
		Pattern p_image;
		Matcher m_image;
		List pics = new ArrayList();
		String regEx_img = "]*?>";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
		img = img + "," + m_image.group();
		Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
		while (m.find()) {
		pics.add(m.group(1));
		}
		}
		return pics;
		}
	

}

class Mas{
	private Integer masId;
	
	private String type;
	
	public Integer getMasId() {
		return masId;
	}
	public void setMasId(Integer masId) {
		this.masId = masId;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}

