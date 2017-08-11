package com.cmp.res.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class FileUtil {
	
	@Autowired
	private SysPropertyService proService;
	
	@Autowired
	private ConfigService configService;
	
	/**
	 * 
	 * @param fileName     文件名
	 * @param objectType   对象类型【1、图片；2、视频】
	 * @param fileType     文件类型（如图片的小图、中图、大图）
	 * @return
	 */
	public String getDest(String fileName,int objectType,int fileType){
		String dest=null;
		switch (objectType) {
		case 1://图片
			switch (fileType) {
			case 1: //原图
				dest=configService.getS3photoStorePath()+fileName;
				break;
			case 2: //大图
				dest=configService.getS3bighotoStorePath()+fileName;
				break;
			case 3://中图
				dest=configService.getS3middleStorePath()+fileName;
				break;
			case 4://小图
				dest=configService.getS3simphotoStorePath()+fileName;
				break;
			}
			break;
		case 2://视频
			switch (fileType) {
			case 1://原始视频
				dest=configService.getS3originalVideo()+fileName;
				break;
			case 2://mp4视频
				dest=configService.getS3mp4Video()+fileName;
				break;
			case 3://视频图片
				dest=configService.getS3picVideo()+fileName;
				break;
			case 4://视频中图
				dest=configService.getS3picMiddleVideo()+fileName;
				break;
				
			case 5://视频小图
				dest=configService.getS3picSmallVideo()+fileName;
				break;
			}
			break;
		}
		
		return dest;
	}
	
	public boolean deleteFile(String fileName,int objectType,int fileType){
		boolean flag=true;
		//亚马逊S3存储
		String dest=this.getDest(fileName, objectType, fileType);
		
		try{
			delete(dest);
		}catch (Exception e) {
			// TODO: handle exception
			flag=false;
		}
		
	return flag;
	}
	/**
	 * 删除资源
	 * @param fileName
	 * @return
	 */
	public void delete(String fileName){
		
		AWSCredentials credentials = null;
//		credentials=new BasicAWSCredentials(configService.getAccessKey(),configService.getSecretKey());
		
		credentials = new ProfileCredentialsProvider().getCredentials();
		
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		Region china = Region.getRegion(Regions.CN_NORTH_1);
		   
		AmazonS3 connection = new AmazonS3Client(credentials,clientConfig);
		connection.setRegion(china);
		
		connection.deleteObject(configService.getBucket(), fileName);
	}
	
	
	/**
	 * 写文件
	 */
	public boolean save(File file,String fileName,int objectType,int fileType){
		boolean flag=true;
			//亚马逊S3存储
			String dest=this.getDest(fileName, objectType, fileType);
			
			try{
				upload(file,dest);
			}catch (Exception e) {
				// TODO: handle exception
				flag=false;
			}
			
		return flag;
		
	}
	
	/**
	 * 读文件
	 */
	public byte[] download(String fileName,int objectType,int fileType){
		String dest=this.getDest(fileName, objectType, fileType);
		
		return download(dest);
	}
	
	public  byte[] download(String dest){
		AWSCredentials credentials = null;
		byte[] s3Byte=null;
		//credentials = new ProfileCredentialsProvider().getCredentials();
		credentials=new BasicAWSCredentials(configService.getAccessKey(),configService.getSecretKey());
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		Region china = Region.getRegion(Regions.CN_NORTH_1);
		   
		AmazonS3 connection = new AmazonS3Client(credentials,clientConfig);
		connection.setRegion(china);
		
		GeneratePresignedUrlRequest httpRequest=new GeneratePresignedUrlRequest(configService.getBucket(), dest);
		String url=connection.generatePresignedUrl(httpRequest).toString();//临时链接
		System.out.print("==========================>"+url);
		
		try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
                    CloseableHttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    InputStream input = entity.getContent();
                    s3Byte=IOUtils.toByteArray(input); 
                 input.close(); 
             } catch (ClientProtocolException e) {
                    e.printStackTrace();
             } catch (IOException e) {
                    e.printStackTrace();
             }
		return s3Byte;
		
	}
	
	
	/**
	 * 本地上传文件至S3;
	 * @param //args
	 * 
	 * 
	 */
	public   void upload(File file,String dest) {
		AWSCredentials credentials = null;
		credentials=new BasicAWSCredentials(configService.getAccessKey(),configService.getSecretKey());
		//credentials = new ProfileCredentialsProvider().getCredentials();
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		Region china = Region.getRegion(Regions.CN_NORTH_1);
		   
		AmazonS3 connection = new AmazonS3Client(credentials,clientConfig);
		connection.setRegion(china);
		
		
		if (!checkBucketExists(connection, configService.getBucket())) {  
			connection.createBucket(configService.getBucket());  
        }
		
		connection.putObject(new PutObjectRequest(configService.getBucket(), dest, file));  
		
		S3Object object = connection.getObject(new GetObjectRequest(configService.getBucket(), dest));
		
		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(  
				configService.getBucket(), dest); 
		
        
        URL url = connection.generatePresignedUrl(urlRequest);  
        
        System.out.println("=========URL=================" + url + "============URL=============");  
        
		
	}
	
	public static boolean checkBucketExists (AmazonS3 s3, String bucketName) {  
	    List<Bucket> buckets = s3.listBuckets();  
	    for (Bucket bucket : buckets) {  
	        if (Objects.equals(bucket.getName(), bucketName)) {  
	            return true;  
	        }  
	    }  
	    return false;  
	}

}
