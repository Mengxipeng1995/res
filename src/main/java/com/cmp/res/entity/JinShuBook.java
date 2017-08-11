package com.cmp.res.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
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
@Table(name="jinshw_book")
public class JinShuBook extends LombokID{
	
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
		@Column(name="media_review")
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
		@Column(name="press_recommendation",columnDefinition="text")
		private String pressRecommendation;
		
		



}
