package com.cmp.res.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.Type;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
*2017年7月14日
*@liao
*res
NewBook.java
*
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="new_book")
public class NewBook extends LombokID{


	/**
		 * CIP号
		 */
		private String cip;
		
		/**
		*ISBN
		**/
		private String isbn;
		
		/**
		*版次
		**/
		private String revision;
		
		/**
		*版权登记号
		**/
		@Column(name="copyright_num")
		private Integer copyrightNum;
		
		/**
		*版权信息
		**/
		@Column(name="copyright_info",columnDefinition="text")
		private String copyrightInfo;
		
		/**
		*版印次
		**/
		@Column(name="print_edition")
		private String printEdition;
		
		/**
		*编辑推荐
		**/
		@Column(name="editorial_recommendation",columnDefinition="text")
		private String editorialRecommendation;
		
		/**
		*出版年月
		**/
		@Column(name="publish_ym")
		private String publishYm;
		
		/**
		*出版社 
		**/
		private String publish;
		
		/**
		*出版者国别
		**/
		@Column(name="publisher_country")
		private String publisherCountry;
		
		/**
		*丛书名
		**/
		@Column(name="series_name")
		private String seriesName;
		
		/**
		*定价
		**/
		private Float price;
		
		/**
		*读者对象
		**/
		@Column(name="reader_object")
		private String readerObject;
		
		/**
		*分类
		**/
		private String category;
		
		/**
		*分类号
		**/
		@Column(name="category_num")
		private String categoryNum;
		
		/**
		*分社
		**/
		private String branch;
		
		/**
		*广告语
		**/
		@Column(columnDefinition="text")
		private String advertising ;
		
		/**
		*后记
		**/
		@Column(columnDefinition="text")
		private String postscript;
		
		/**
		*厚
		**/
		private Integer thick;
		
		/**
		*获奖情况
		**/
		@Column(name="award_situation",columnDefinition="text")
		private String awardSituation;
		
		/**
		*建议上架类别
		**/
		@Column(name="recommended_shelf_categories")
		private String recommendedShelfCategories;
		
		/**
		*介质
		**/
		private String medium;
		
		/**
		*开本
		**/
		private String folio;
		
		/**
		*媒体评论
		**/
		@Column(name="media_review",columnDefinition="text")
		private String mediaReview;
		
		/**
		*名人推荐
		**/
		@Column(name="celebrity_recommendation",columnDefinition="text")
		private String celebrityRecommendation;
		
		/**
		*目录
		**/
		@Column(columnDefinition="text")
		private String catalog;
		
		/**
		*内容简介
		**/
		@Column(name="content_validity",columnDefinition="text")
		private String contentValidity;
		
		/**
		*前言
		**/
		@Column(columnDefinition="text")
		private String preface;
		
		/**
		*商品类型
		**/
		@Column(name="commodity_type")
		private String commodityType;
		
		/**
		*书名
		**/
		private String title;
		
		/**
		*条码书号
		**/
		private String barcode;
		
		/**
		*图书尺寸
		**/
		private String size;
		
		/**
		*图书等级 
		**/
		private String level;
		
		/**
		*图书品牌
		**/
		private String brand;
		
		/**
		*图书状态
		**/
		private String status;
		
		/**
		*序言
		**/
		@Column(columnDefinition="text")
		private String preface2;
		
		/**
		*业务分类
		**/
		@Column(name="service_classification")
		private String serviceClassification;
		
		/**
		*业务分类名称
		**/
		@Column(name="service_classification_name")
		private String serviceClassificationName;
		
		/**
		*页数
		**/
		@Column(name="page_size")
		private Integer pageSize;
		
		/**
		*译者
		**/
		private String translator;
		
		/**
		*译者简介
		**/
		@Column(name="translator_profiles",columnDefinition="text")
		private String translatorProfiles;
		
		/**
		*译者序
		**/
		@Column(name="translator_preface",columnDefinition="text")
		private String translatorPreface;
		
		/**
		*印张
		**/
		private Float sheet;
		
		/**
		*营销分类
		**/
		@Column(name="marketing_classification")
		private String marketingClassification;
		
		/**
		*用纸
		**/
		private String paper;
		
		/**
		*语种
		**/
		private String languages;
		
		/**
		*在线试读
		**/
		@Column(name="read_online",columnDefinition="text")
		private String readOnline;
		
		/**
		*责任编辑
		**/
		@Column(name="editor_charge")
		private String editorCharge;
		
		/**
		*中图法分类
		**/
		@Column(name="graph_classification")
		private String graphClassification;
		
		/**
		*重量
		**/
		private Integer weight;
		
		/**
		*主题词
		**/
		private String keywords;
		
		/**
		*装帧
		**/
		private String binding;
		
		/**
		*字数
		**/
		private Long wordcount;
		
		/**
		*作者
		**/
		private String author;
		
		/**
		*作者简介
		**/
		@Column(name="author_brief",columnDefinition="text")
		private String authorBrief;
		
		/**
		*作者序
		**/
		@Column(name="author_preface",columnDefinition="text")
		private String authorPreface;
		
		/**
		 * 关键词
		 */
		@Column(name="key_sentences")
		private String keySentences;
		/**
		 * 上架日期
		 */
		@Column(name="shelves_date")
		private Date shelvesDate;
		
		/**
		 * 分类code
		 */
		@Column(name="category_code")
		private String categoryCode;
		
		/**
		 * 上架建议
		 */
		@Column(name="shelf_suggestions",columnDefinition="text")
		private String shelfSuggestions;

		/**
		 * 章节数
		 */
		@Column(name="chapter_count")
		private Integer chapterCount;
		
		/**
		 * 原书名
		 */
		@Column(name="original_title")
		private String originalTitle;
		
		/**
		 * 原出版社
		 */
		@Column(name="originalPublisher")
		private String original_publisher;
		
		/**
		 * 有无CD
		 */
		@Column(name="cd_count")
		private Integer cdCount;
		
		/**
		 * 有无磁盘
		 */
		@Column(name="disk_count")
		private Integer diskCount;
		
		/**
		 * 有无磁带
		 */
		@Column(name="magnetic_count")
		private Integer magneticCount;
		
		/**
		 * mark数据
		 */
		@Column(name="mark_data",columnDefinition="text")
		private String markData;
		
		/**
		 * 作者国籍
		 */
		@Column(name="authorNationality")
		private String author_nationality;
		
		/**
		 * 出版社推荐
		 */
		@Column(name="press_recommendation")
		private String pressRecommendation;

	public String getCip() {
		return cip;
	}

	public void setCip(String cip) {
		this.cip = cip;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Integer getCopyrightNum() {
		return copyrightNum;
	}

	public void setCopyrightNum(Integer copyrightNum) {
		this.copyrightNum = copyrightNum;
	}

	public String getCopyrightInfo() {
		return copyrightInfo;
	}

	public void setCopyrightInfo(String copyrightInfo) {
		this.copyrightInfo = copyrightInfo;
	}

	public String getPrintEdition() {
		return printEdition;
	}

	public void setPrintEdition(String printEdition) {
		this.printEdition = printEdition;
	}

	public String getEditorialRecommendation() {
		return editorialRecommendation;
	}

	public void setEditorialRecommendation(String editorialRecommendation) {
		this.editorialRecommendation = editorialRecommendation;
	}

	public String getPublishYm() {
		return publishYm;
	}

	public void setPublishYm(String publishYm) {
		this.publishYm = publishYm;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getPublisherCountry() {
		return publisherCountry;
	}

	public void setPublisherCountry(String publisherCountry) {
		this.publisherCountry = publisherCountry;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getReaderObject() {
		return readerObject;
	}

	public void setReaderObject(String readerObject) {
		this.readerObject = readerObject;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryNum() {
		return categoryNum;
	}

	public void setCategoryNum(String categoryNum) {
		this.categoryNum = categoryNum;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAdvertising() {
		return advertising;
	}

	public void setAdvertising(String advertising) {
		this.advertising = advertising;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public Integer getThick() {
		return thick;
	}

	public void setThick(Integer thick) {
		this.thick = thick;
	}

	public String getAwardSituation() {
		return awardSituation;
	}

	public void setAwardSituation(String awardSituation) {
		this.awardSituation = awardSituation;
	}

	public String getRecommendedShelfCategories() {
		return recommendedShelfCategories;
	}

	public void setRecommendedShelfCategories(String recommendedShelfCategories) {
		this.recommendedShelfCategories = recommendedShelfCategories;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getMediaReview() {
		return mediaReview;
	}

	public void setMediaReview(String mediaReview) {
		this.mediaReview = mediaReview;
	}

	public String getCelebrityRecommendation() {
		return celebrityRecommendation;
	}

	public void setCelebrityRecommendation(String celebrityRecommendation) {
		this.celebrityRecommendation = celebrityRecommendation;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getContentValidity() {
		return contentValidity;
	}

	public void setContentValidity(String contentValidity) {
		this.contentValidity = contentValidity;
	}

	public String getPreface() {
		return preface;
	}

	public void setPreface(String preface) {
		this.preface = preface;
	}

	public String getCommodityType() {
		return commodityType;
	}

	public void setCommodityType(String commodityType) {
		this.commodityType = commodityType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPreface2() {
		return preface2;
	}

	public void setPreface2(String preface2) {
		this.preface2 = preface2;
	}

	public String getServiceClassification() {
		return serviceClassification;
	}

	public void setServiceClassification(String serviceClassification) {
		this.serviceClassification = serviceClassification;
	}

	public String getServiceClassificationName() {
		return serviceClassificationName;
	}

	public void setServiceClassificationName(String serviceClassificationName) {
		this.serviceClassificationName = serviceClassificationName;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public String getTranslatorProfiles() {
		return translatorProfiles;
	}

	public void setTranslatorProfiles(String translatorProfiles) {
		this.translatorProfiles = translatorProfiles;
	}

	public String getTranslatorPreface() {
		return translatorPreface;
	}

	public void setTranslatorPreface(String translatorPreface) {
		this.translatorPreface = translatorPreface;
	}

	public Float getSheet() {
		return sheet;
	}

	public void setSheet(Float sheet) {
		this.sheet = sheet;
	}

	public String getMarketingClassification() {
		return marketingClassification;
	}

	public void setMarketingClassification(String marketingClassification) {
		this.marketingClassification = marketingClassification;
	}

	public String getPaper() {
		return paper;
	}

	public void setPaper(String paper) {
		this.paper = paper;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getReadOnline() {
		return readOnline;
	}

	public void setReadOnline(String readOnline) {
		this.readOnline = readOnline;
	}

	public String getEditorCharge() {
		return editorCharge;
	}

	public void setEditorCharge(String editorCharge) {
		this.editorCharge = editorCharge;
	}

	public String getGraphClassification() {
		return graphClassification;
	}

	public void setGraphClassification(String graphClassification) {
		this.graphClassification = graphClassification;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public Long getWordcount() {
		return wordcount;
	}

	public void setWordcount(Long wordcount) {
		this.wordcount = wordcount;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorBrief() {
		return authorBrief;
	}

	public void setAuthorBrief(String authorBrief) {
		this.authorBrief = authorBrief;
	}

	public String getAuthorPreface() {
		return authorPreface;
	}

	public void setAuthorPreface(String authorPreface) {
		this.authorPreface = authorPreface;
	}

	public String getKeySentences() {
		return keySentences;
	}

	public void setKeySentences(String keySentences) {
		this.keySentences = keySentences;
	}

	public Date getShelvesDate() {
		return shelvesDate;
	}

	public void setShelvesDate(Date shelvesDate) {
		this.shelvesDate = shelvesDate;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getShelfSuggestions() {
		return shelfSuggestions;
	}

	public void setShelfSuggestions(String shelfSuggestions) {
		this.shelfSuggestions = shelfSuggestions;
	}

	public Integer getChapterCount() {
		return chapterCount;
	}

	public void setChapterCount(Integer chapterCount) {
		this.chapterCount = chapterCount;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public String getOriginal_publisher() {
		return original_publisher;
	}

	public void setOriginal_publisher(String original_publisher) {
		this.original_publisher = original_publisher;
	}

	public Integer getCdCount() {
		return cdCount;
	}

	public void setCdCount(Integer cdCount) {
		this.cdCount = cdCount;
	}

	public Integer getDiskCount() {
		return diskCount;
	}

	public void setDiskCount(Integer diskCount) {
		this.diskCount = diskCount;
	}

	public Integer getMagneticCount() {
		return magneticCount;
	}

	public void setMagneticCount(Integer magneticCount) {
		this.magneticCount = magneticCount;
	}

	public String getMarkData() {
		return markData;
	}

	public void setMarkData(String markData) {
		this.markData = markData;
	}

	public String getAuthor_nationality() {
		return author_nationality;
	}

	public void setAuthor_nationality(String author_nationality) {
		this.author_nationality = author_nationality;
	}

	public String getPressRecommendation() {
		return pressRecommendation;
	}

	public void setPressRecommendation(String pressRecommendation) {
		this.pressRecommendation = pressRecommendation;
	}
}
