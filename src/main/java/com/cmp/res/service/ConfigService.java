package com.cmp.res.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class ConfigService {
	
	
	/**
	 * 原始图片存放地址
	 */
	@Value("${photo.store.path}")
    private String photoStorePath;
	
	@Value("${s3.photo.store.path}")
    private String s3photoStorePath;
	/**
	 * 小图存放地址
	 */
	@Value("${simphoto.store.path}")
    private String simphotoStorePath;
	
	@Value("${s3.simphoto.store.path}")
    private String s3simphotoStorePath;
	/**
	 * 大图存放地址
	 */
	@Value("${bigPic.store.path}")
    private String bighotoStorePath;
	
	@Value("${s3.bigPic.store.path}")
    private String s3bighotoStorePath;
	
	/**
	 * 中图存放地址
	 */
	@Value("${middlePic.store.path}")
    private String middleStorePath;
	
	@Value("${s3.middlePic.store.path}")
    private String s3middleStorePath;
	
	@Value("${simPicSize}")
	private Integer simPicSize; 
	
	@Value("${video.watermarkater}")
	private String videoWatermarkater;
	
	
	
	
	@Value("${middlePicSize}")
	private Integer middlePicSize; 
	
	@Value("${video.original}")
	private String originalVideo;
	@Value("${s3.video.original}")
	private String s3originalVideo;
	@Value("${video.mp4}")
	private String mp4Video;
	@Value("${s3.video.mp4}")
	private String s3mp4Video;
	@Value("${video.pic}")
	private String picVideo;
	@Value("${s3.video.pic}")
	private String s3picVideo;
	
	@Value("${video.Middlepic}")
	private String picMiddleVideo;
	
	@Value("${s3.video.Middlepic}")
	private String s3picMiddleVideo;
	
	@Value("${video.smallpic}")
	private String picSmallVideo;
	
	@Value("${s3.video.smallpic}")
	private String s3picSmallVideo;
	

	@Value("${photo.store.tempPath}")
    private String photoTempPath;
	@Value("${photo.store.watermarkater}")
	private String watermarkater;
	@Value("${mas.url}")
	private String masUrl;
	
	@Value("${mas.appKey}")
	private String appKey;
	
	
	@Value("${downlTempPath}")
	private String downlTempPath;
	
	@Value("${diskType}")
	private String diskType;
	
	@Value("${s3.accessKey}")
	private String accessKey;
	
	@Value("${s3.secretKey}")
	private String secretKey;
	
	@Value("${s3.bucket}")
	private String bucket;
	@Value("${video.watermarkSize}")
	private String watermarkSize;
	@Value("${video.watermarkLocation}")
	private String watermarkLocation;
	@Value("${video.wartermarkeImage}")
	private String wartermarkeImage;
	
	@Value("${attach.image}")
	private String attachImage;
	
	@Value("${attach.pdf}")
	private String attachPdf;
	
	@Value("${attach.epub}")
	private String attchEpub;
	
	@Value("${attach.xml}")
	private String attachXml;
	
	@Value("${attach.cover}")
	private String attachCover;
	
	@Value("${attach.bak}")
	private String attchBak;
	
	
	

	public String getWatermarkSize() {
		return watermarkSize;
	}

	public void setWatermarkSize(String watermarkSize) {
		this.watermarkSize = watermarkSize;
	}

	public String getWatermarkLocation() {
		return watermarkLocation;
	}

	public void setWatermarkLocation(String watermarkLocation) {
		this.watermarkLocation = watermarkLocation;
	}

	public String getWartermarkeImage() {
		return wartermarkeImage;
	}

	public void setWartermarkeImage(String wartermarkeImage) {
		this.wartermarkeImage = wartermarkeImage;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMasUrl() {
		return masUrl;
	}

	public void setMasUrl(String masUrl) {
		this.masUrl = masUrl;
	}

	public String getPhotoStorePath() {
		return photoStorePath;
	}

	public void setPhotoStorePath(String photoStorePath) {
		this.photoStorePath = photoStorePath;
	}

	public String getPhotoTempPath() {
		return photoTempPath;
	}

	public void setPhotoTempPath(String photoTempPath) {
		this.photoTempPath = photoTempPath;
	}

	public String getWatermarkater() {
		return watermarkater;
	}

	public void setWatermarkater(String watermarkater) {
		this.watermarkater = watermarkater;
	}

	public String getDownlTempPath() {
		return downlTempPath;
	}

	public void setDownlTempPath(String downlTempPath) {
		this.downlTempPath = downlTempPath;
	}

	public String getBighotoStorePath() {
		return bighotoStorePath;
	}

	public void setBighotoStorePath(String bighotoStorePath) {
		this.bighotoStorePath = bighotoStorePath;
	}

	public String getMiddleStorePath() {
		return middleStorePath;
	}

	public void setMiddleStorePath(String middleStorePath) {
		this.middleStorePath = middleStorePath;
	}

	public Integer getSimPicSize() {
		return simPicSize;
	}

	public void setSimPicSize(Integer simPicSize) {
		this.simPicSize = simPicSize;
	}

	public Integer getMiddlePicSize() {
		return middlePicSize;
	}

	public void setMiddlePicSize(Integer middlePicSize) {
		this.middlePicSize = middlePicSize;
	}

	public String getSimphotoStorePath() {
		return simphotoStorePath;
	}

	public void setSimphotoStorePath(String simphotoStorePath) {
		this.simphotoStorePath = simphotoStorePath;
	}

	public String getOriginalVideo() {
		return originalVideo;
	}

	public void setOriginalVideo(String originalVideo) {
		this.originalVideo = originalVideo;
	}

	public String getMp4Video() {
		return mp4Video;
	}

	public void setMp4Video(String mp4Video) {
		this.mp4Video = mp4Video;
	}

	public String getPicVideo() {
		return picVideo;
	}

	public void setPicVideo(String picVideo) {
		this.picVideo = picVideo;
	}

	public String getVideoWatermarkater() {
		return videoWatermarkater;
	}

	public void setVideoWatermarkater(String videoWatermarkater) {
		this.videoWatermarkater = videoWatermarkater;
	}

	public String getPicMiddleVideo() {
		return picMiddleVideo;
	}

	public void setPicMiddleVideo(String picMiddleVideo) {
		this.picMiddleVideo = picMiddleVideo;
	}

	public String getPicSmallVideo() {
		return picSmallVideo;
	}

	public void setPicSmallVideo(String picSmallVideo) {
		this.picSmallVideo = picSmallVideo;
	}

	public String getDiskType() {
		return diskType;
	}

	public void setDiskType(String diskType) {
		this.diskType = diskType;
	}

	public String getS3photoStorePath() {
		return s3photoStorePath;
	}

	public void setS3photoStorePath(String s3photoStorePath) {
		this.s3photoStorePath = s3photoStorePath;
	}

	public String getS3simphotoStorePath() {
		return s3simphotoStorePath;
	}

	public void setS3simphotoStorePath(String s3simphotoStorePath) {
		this.s3simphotoStorePath = s3simphotoStorePath;
	}

	public String getS3bighotoStorePath() {
		return s3bighotoStorePath;
	}

	public void setS3bighotoStorePath(String s3bighotoStorePath) {
		this.s3bighotoStorePath = s3bighotoStorePath;
	}

	public String getS3middleStorePath() {
		return s3middleStorePath;
	}

	public void setS3middleStorePath(String s3middleStorePath) {
		this.s3middleStorePath = s3middleStorePath;
	}

	public String getS3originalVideo() {
		return s3originalVideo;
	}

	public void setS3originalVideo(String s3originalVideo) {
		this.s3originalVideo = s3originalVideo;
	}

	public String getS3mp4Video() {
		return s3mp4Video;
	}

	public void setS3mp4Video(String s3mp4Video) {
		this.s3mp4Video = s3mp4Video;
	}

	public String getS3picVideo() {
		return s3picVideo;
	}

	public void setS3picVideo(String s3picVideo) {
		this.s3picVideo = s3picVideo;
	}

	public String getS3picMiddleVideo() {
		return s3picMiddleVideo;
	}

	public void setS3picMiddleVideo(String s3picMiddleVideo) {
		this.s3picMiddleVideo = s3picMiddleVideo;
	}

	public String getS3picSmallVideo() {
		return s3picSmallVideo;
	}

	public void setS3picSmallVideo(String s3picSmallVideo) {
		this.s3picSmallVideo = s3picSmallVideo;
	}

	public String getAttachImage() {
		return attachImage;
	}

	public void setAttachImage(String attachImage) {
		this.attachImage = attachImage;
	}

	public String getAttachPdf() {
		return attachPdf;
	}

	public void setAttachPdf(String attachPdf) {
		this.attachPdf = attachPdf;
	}

	public String getAttchEpub() {
		return attchEpub;
	}

	public void setAttchEpub(String attchEpub) {
		this.attchEpub = attchEpub;
	}

	public String getAttachXml() {
		return attachXml;
	}

	public void setAttachXml(String attachXml) {
		this.attachXml = attachXml;
	}

	public String getAttchBak() {
		return attchBak;
	}

	public void setAttchBak(String attchBak) {
		this.attchBak = attchBak;
	}

	public String getAttachCover() {
		return attachCover;
	}

	public void setAttachCover(String attachCover) {
		this.attachCover = attachCover;
	}

	
	
	
	


	
	
	
	

}
