package com.cmp.res.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class Shell {
	public static boolean run(String cmd){
    	boolean flag=false;
    	try {
            Process process = Runtime.getRuntime().exec(cmd);//执行命令
  
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
  
            String line;
            while ((line = input.readLine()) != null) {//输出结果
                System.out.println(line);
            }
            
            flag=true;
        } catch (java.io.IOException e) {
            System.err.println("IOException " + e.getMessage());//捕捉异常
        }
    	
    	return flag;
    }
}
