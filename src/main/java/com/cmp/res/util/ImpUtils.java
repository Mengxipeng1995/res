package com.cmp.res.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class ImpUtils {
	
	
	public static String getFileFormat(String name){
		if(StringUtils.isBlank(name)){
			return null;
		}
		int i=name.lastIndexOf(".");
		if(i==-1){
			return null;
		}
		return name.substring(i+1);
	}
	
	public static String getFileName(String name){
		if(StringUtils.isBlank(name)){
			return null;
		}
		int i=name.lastIndexOf(".");
		if(i==-1){
			return null;
		}
		return name.substring(0,i);
	}
	
	
	public static String getCodeStr(int code){
		String v=code+"";
		while (v.length()<3) {
			v="0"+v;
		}
		return v;
	}
	
	/**
	 * linux 环境下的文件移动，目录和文件均可
	 * @param commands
	 * @param param
	 * @param origin
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFileLinux(String commands,String param,String origin,String target) throws IOException{
		List<String> command = new ArrayList<String>();
		ProcessBuilder pb = null;
        Process p = null;
        command.add(commands);
        command.add(param);
        command.add(origin);
        command.add(target);
        pb = new ProcessBuilder();
        pb.command(command);
        p = pb.start();
        return print(p);
		
	}
	public static boolean print(Process p) throws IOException{
		InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
        	System.out.println("line:"+line);
            return false;
        }
	    return true;
	
	}
	
	
	public static String getResourceCode(){
		
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static List<File> traversalFile(File dir,List<File> files){
		File[] fs=dir.listFiles();
		for(File f:fs){
			if(f.isFile()){
				files.add(f);
			}
			if(f.isDirectory()){
				traversalFile(f, files);
			}
		}
		return files;
	}
	public static void main(String[] args) {
		System.out.print(getResourceCode());
	}
	
	

}
