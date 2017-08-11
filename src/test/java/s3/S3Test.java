package s3;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsResult.DeletedObject;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;


public class S3Test {
	
	public static void main(String[] args) {
//		upload();
		download();
//		delete();
	}
	
	
	public static void delete(){
		AWSCredentials credentials = null;
		credentials=new BasicAWSCredentials("AKIAP5P5LZOTOWUYZY7A","GPKz+3s0X1UtQhSjPjh+8zzZNQm0vBkthBXdwneM");
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		Region china = Region.getRegion(Regions.CN_NORTH_1);
		   
		AmazonS3 connection = new AmazonS3Client(credentials,clientConfig);
		connection.setRegion(china);
		
		connection.deleteObject("trstest", "mysql-5.7.17.tar.gz");
		
		
		
	}
	
	public static void download(){
		AWSCredentials credentials = null;
		credentials=new BasicAWSCredentials("AKIAPCE2LRYAJFLZQ4QQ","0f7C0oOzUGT1XPoa5/YoTE4HX7zzxDA45zhK1YQK\n");
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		Region china = Region.getRegion(Regions.CN_NORTH_1);
		   
		AmazonS3 connection = new AmazonS3Client(credentials,clientConfig);
		connection.setRegion(china);
		
		GeneratePresignedUrlRequest httpRequest=new GeneratePresignedUrlRequest("cmanuf", "video/mp4/8128e02a-3b14-4604-95e0-de5e274beb43_.mp4");
		String url=connection.generatePresignedUrl(httpRequest).toString();//临时链接

		try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
                    CloseableHttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    InputStream input = entity.getContent();
                  //  IOUtils.toByteArray(input);
                 FileOutputStream output = new FileOutputStream(new File("d:\\8128e02a-3b14-4604-95e0-de5e274beb43_.mp4"));
                 byte[] buffer=new byte[10240]; 
                 int ch = 0; 
                 while ((ch = input.read(buffer)) != -1) { 
                  output.write(buffer,0,ch); 
                 } 
                 input.close(); 
             output.flush(); 
             output.close(); 
             } catch (ClientProtocolException e) {
                    e.printStackTrace();
             } catch (IOException e) {
                    e.printStackTrace();
             }
		
		
	}
	
	/**
	 * 本地上传文件至S3;
	 * @param args
	 *
	 *
	 * http://trstest.s3.cn-north-1.amazonaws.com.cn/aaaa.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20170112T023811Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAP5P5LZOTOWUYZY7A%2F20170112%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Signature=e3ad1145cd89968ba3215c2471f4a2fb3a14478356572346dafabddf2ff93153
	 */
	public static void upload() {
		AWSCredentials credentials = null;
		credentials=new BasicAWSCredentials("AKIAP5P5LZOTOWUYZY7A","GPKz+3s0X1UtQhSjPjh+8zzZNQm0vBkthBXdwneM");
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		Region china = Region.getRegion(Regions.CN_NORTH_1);

		AmazonS3 connection = new AmazonS3Client(credentials,clientConfig);
		connection.setRegion(china);


		if (!checkBucketExists(connection, "trstest")) {
			connection.createBucket("trstest");
        }

		connection.putObject(new PutObjectRequest("trstest", "demo/swing_d111.mp4", new File("D:\\swing_d111.mp4")));

		System.out.println(1);

//		S3Object object = connection.getObject(new GetObjectRequest("trstest", "demo/1aaaa.png"));
//
//		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
//                "trstest", "demo/1aaaa.png");

//		Date expirationDate = null;
//        try {
//            expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        urlRequest.setExpiration(expirationDate);

       // URL url = connection.generatePresignedUrl(urlRequest);

       // System.out.println("=========URL=================" + url + "============URL=============");


       // return url.toString();
//
//		//创建Bucket名称：
//		if(connection.doesBucketExist("Bucket名称")==false)
//		{
//		   connection.createBucket("Bucket名称");
//		}
//		//删除Bucket名称：
//		if(connection.doesBucketExist("Bucket名称"))
//		{
//		   connection.deleteBucket("Bucket名称");
//		}

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