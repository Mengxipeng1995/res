package com.cmp.res.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class SystemConstant {
	
	public static String password;
	
	public static String ffmpegPath;
	static{
		InputStream is = SystemConstant.class.getResourceAsStream("/shiro.properties");
        
        Properties ps = new Properties();
        //加载properties资源文件
        try {
			ps.load(is);
			password=ps.getProperty("password");
			ffmpegPath=ps.getProperty("ffmpegPath");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
      
      
	}

}
