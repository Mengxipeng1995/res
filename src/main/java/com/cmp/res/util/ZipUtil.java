package com.cmp.res.util;

import java.io.*;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.*;

public class ZipUtil {
	
	public static void main(String[] args) throws IOException {
		//zipMultiFile("D:\\abc","D:\\abc.zip",true);
		File zipFile = new File("H:/978-7-111-46256-9_1-1.zip");
		String path = "H:/zipfile/";
		unZipFiles(zipFile, path);
	}




	/**
	 * 压缩整个文件夹中的所有文件，生成指定名称的zip压缩包
	 * @param filepath 文件所在目录
	 * @param zippath 压缩后zip文件名称
	 * @param dirFlag zip文件中第一层是否包含一级目录，true包含；false没有
	 * 2015年6月9日
	 */
	public static void zipMultiFile(String filepath ,String zippath, boolean dirFlag) {
	    try {
	        File file = new File(filepath);// 要被压缩的文件夹
	        File zipFile = new File(zippath);
	        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
	        if(file.isDirectory()){
				File[] files = file.listFiles();
				for(File fileSec:files){
					if(dirFlag){
						recursionZip(zipOut, fileSec, file.getName() + File.separator);
					}else{
						recursionZip(zipOut, fileSec, "");
					}
				}
	        }
	        zipOut.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir) throws Exception{
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File fileSec:files){
				recursionZip(zipOut, fileSec, baseDir + file.getName() + File.separator);
			}
		}else{
			byte[] buf = new byte[1024];
			InputStream input = new FileInputStream(file);
    		zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
    		int len;
    		while((len = input.read(buf)) != -1){
    			zipOut.write(buf, 0, len);
    		}
    		input.close();
		}
	}
	/*
	* 解压文件
	* @param zipFile 文件
    * @param descDir 目录
	* */

	public static void unZipFiles(File zipFile,String descDir) throws IOException {
		File pathFile = new File(descDir);
		InputStream in = null;
		OutputStream out = null;
		if(!pathFile.exists()){
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		try {
		for(Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");

			//判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			//判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			//输出文件路径信息
			System.out.println(outPath);

			out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			//zip.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			in.close();
			out.flush();
			out.close();
		}
		System.out.println("******************解压完毕********************");
		System.gc();
		//关闭zip否则无法删除
		zip.close();
		zipFile.delete();
	}
} 