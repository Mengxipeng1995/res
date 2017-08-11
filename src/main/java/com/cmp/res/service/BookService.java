package com.cmp.res.service;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cmp.res.dao.NewBookDAO;
import com.cmp.res.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.chainsaw.Main;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springside.modules.mapper.JsonMapper;

import com.cmp.res.dao.BookDAO;
import com.cmp.res.dao.UserDAO;
import com.cmp.res.entity.Attachment;
import com.cmp.res.entity.Bibliography;
import com.cmp.res.entity.BibliographyContent;
import com.cmp.res.entity.Book;
import com.cmp.res.entity.Indexentry;
import com.cmp.res.entity.InlinePic;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.NewBook;
import com.cmp.res.entity.Note;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.PicAnnotation;
import com.cmp.res.entity.Standard;
import com.cmp.res.entity.StandardItemMapping;
import com.cmp.res.entity.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.json.util.JSONUtils;
import org.springside.modules.mapper.JsonMapper;


@Service
@Transactional(readOnly = false)
public class BookService {

    public static Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private NewBookDAO newbookDAO;

    @Autowired
    private ConfigService configService;
    @Autowired
    private NoteService noteService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private NewBookService newBookService;

    public Book findById(Long id) {
        return bookDAO.findOne(id);
    }

    public Long getCount() {
        return bookDAO.findAll(CommonUtil.buildPageRequest(1, 1, null, null, null)).getTotalElements();
    }

    public Page<Book> outline(int pn, int ps) {
        return bookDAO.findAll(CommonUtil.buildPageRequest(pn, ps, null, null, null));
    }

    @Transactional(readOnly = false)
    public void saveBook(Book book) {

        bookDAO.save(book);

    }

    public void delete(Long id) {
        bookDAO.delete(id);
    }
/*新增newbook*/


    public void saveNewBook(NewBook book) {

        newbookDAO.save(book);

    }

    public void deletenew(Long id) {
        newbookDAO.delete(id);
    }


    /**
     * 解析xml获取图书的章节信息  针对有中图分类图书  classification标签
     *
     * @param e
     * @return
     */
    public Item getItemByXml2(Element e, File xml, Book book, boolean flag, Item parentItem, String source) {
        Item item = new Item();
        item.setLinkend(e.attributeValue("id"));
        item.setBookTitle(e.elementText("title"));
        //item.setEtitle(e.elementText("etitle"));
        item.setEtitle(e.elementText("etitleabbrev"));//保存不带序号的英文标题
        item.setOriginalVersionFlag(0);
        item.setDeleteFlag(0);
        item.setResCode(ImpUtils.getResourceCode());
        item.setCatName(e.elementText("titleabbrev"));


        /**老版本正文解析	 begin*/
        //item.setContent(parseContent(e.elements("para"), xml, item,book));
        /**老版本正文解析	 end*/


        /**
         * 解析标题中的参考文献
         */
        if (!e.getName().equalsIgnoreCase("classification")) {
            Map<String, Object> titleInfos = parseInfo(e.elements("titleabbrev"), xml, book);
            List<Bibliography> titleBiblios = (List<Bibliography>) titleInfos.get("biblios");
            String title = titleInfos.get("content").toString();
            if (!title.contains("titleabbrev")) {
                item.setTitle(title.substring(21, title.length() - 4));
            } else {
                item.setTitle(title.substring(72, title.length() - 18));
            }

            item.getPhotos().addAll((Collection<? extends Photo>) titleInfos.get("photos"));

            if (titleBiblios != null && titleBiblios.size() > 0) {
                item.getBiblios().addAll(titleBiblios);
            }
        } else {
            item.setTitle(e.elementText("title"));
        }


        if (StringUtils.isNoneBlank(source)) {
            item.setSource(source + ">" + item.getTitle());
        } else {
            item.setSource(item.getTitle());
        }


//		List<Photo> tittlePhotos=(List<Photo>) titleInfos.get("photos");
//		item.setTitlePhotos(tittlePhotos);


        /**新版本正文解析  begin*/
        Map<String, Object> infos = parseInfo(e.elements("para"), xml, book);
        item.getPhotos().addAll((Collection<? extends Photo>) infos.get("photos"));
        item.getBiblios().addAll((Collection<? extends Bibliography>) infos.get("biblios"));
        item.getStands().putAll((Map<? extends String, ? extends Object>) infos.get("standards"));
        item.setContent((String) infos.get("content"));

        /**新版本正文解析  end*/

        item.setCreateDate(new Date());
        item.setModifyDate(item.getCreateDate());
        item.setVersionDesc("加工资源");
        item.setIndexs(getIndex(e.attributeValue("id"), book));
        if (flag) {
            item.setPubShowFlag(1);
        }


        List<Element> classNodes = e.elements("classification");
        for (Element classNode : classNodes) {
            item.getItems().add(getItemByXml2(classNode, xml, book, flag, null, item.getSource()));
        }

        List<Element> list = e.elements("chapter");


        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item chapter = getItemByXml2(temp, xml, book, flag, null, item.getSource());
                if (StringUtils.isNotBlank(chapter.getContent())) {
                    chapter.setPubShowFlag(1);
                    ItemContent firstLevelItem = new ItemContent(chapter.getTitle(), chapter.getContent());
                    firstLevelItem.setChapter(null);
                    chapter.setContent(JsonMapper.nonEmptyMapper().toJson(firstLevelItem));

                    List<BibliographyContent> bcs = Lists.newArrayList();
                    TreeMap<String, BibliographyContent> map = Maps.newTreeMap();
                    //参考文献排重
                    for (Bibliography bi : chapter.getBiblios()) {
                        BibliographyContent bc = new BibliographyContent();
                        bc.setId(bi.getXmlId());
                        bc.setType(1);
                        bc.setTitle(bi.getTitle());
                        bc.setContent(bi.getDescp().substring(bi.getDescp().indexOf("]") + 1));
                        bc.setAuthor(bi.getAuthor());
                        //bcs.add(bc);
                        map.put(bc.getId(), bc);
                    }
                    Iterator it = map.keySet().iterator();
                    while (it.hasNext()) {
                        bcs.add(map.get(it.next()));
                    }

                    chapter.setBiblioIds(JsonMapper.nonEmptyMapper().toJson(bcs));

                }

                item.getItems().add(chapter);
            }
        }
        list = e.elements("sect1");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                //	Map<String,>
                Item sect1 = getItemByXml2(temp, xml, book, true, null, item.getSource());

                sect1.setPubShowFlag(1);
                //拼接正文
                ItemContent ic = new ItemContent("正文", sect1.getContent());
                //一级目录
                for (Item firstLevel : sect1.getItems()) {

                    ItemContent firstLevelItem = new ItemContent(firstLevel.getTitle(), firstLevel.getContent());
                    ic.getSonChapters().add(firstLevelItem);
                    mergeItem(sect1, firstLevel);
                    //二级目录
                    for (Item secondLevel : firstLevel.getItems()) {
                        mergeItem(sect1, secondLevel);
                        ItemContent secondLevleItem = new ItemContent(secondLevel.getTitle(), secondLevel.getContent());
                        firstLevelItem.getSonChapters().add(secondLevleItem);
                        //三级合并到二级
                        for (Item thirdLevel : secondLevel.getItems()) {
                            mergeItem(sect1, thirdLevel);

                            secondLevleItem.setContent(secondLevleItem.getContent() + "<p class=thirdLevel>" + thirdLevel.getTitle() + "</p><p>" + thirdLevel.getContent() + "</p>");
                            //四级合并到二级
                            for (Item fourthLevel : thirdLevel.getItems()) {
                                mergeItem(sect1, fourthLevel);
                                secondLevleItem.setContent(secondLevleItem.getContent() + "<p class=fourthLevel>" + fourthLevel.getTitle() + "</p><p>" + fourthLevel.getContent() + "</p>");
                                //五级合并到二级
                                for (Item fifthLevel : fourthLevel.getItems()) {
                                    mergeItem(sect1, fifthLevel);
                                    secondLevleItem.setContent(secondLevleItem.getContent() + "<p class=fourthLevel>" + fifthLevel.getTitle() + "</p><p>" + fifthLevel.getContent() + "</p>");
                                }
                            }

                        }

                    }


                }
                List<BibliographyContent> bcs = Lists.newArrayList();
                TreeMap<String, BibliographyContent> map = Maps.newTreeMap();
                //参考文献排重
                for (Bibliography bi : sect1.getBiblios()) {
                    BibliographyContent bc = new BibliographyContent();
                    bc.setId(bi.getXmlId());
                    bc.setType(1);
                    bc.setTitle(bi.getTitle());
                    bc.setContent(bi.getDescp().substring(bi.getDescp().indexOf("]") + 1));
                    bc.setAuthor(bi.getAuthor());
                    //bcs.add(bc);
                    map.put(bc.getId(), bc);
                }
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    bcs.add(map.get(it.next()));
                }

                sect1.setBiblioIds(JsonMapper.nonEmptyMapper().toJson(bcs));

                sect1.setContent(JsonMapper.nonEmptyMapper().toJson(ic));
                sect1.setItems(Lists.newArrayList());
                item.getItems().add(sect1);
            }
        }
        list = e.elements("sect2");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item tempItem = getItemByXml2(temp, xml, book, flag, item, item.getSource());
                //item=mergeItem(item,tempItem,"sect2" );
                item.getItems().add(tempItem);
            }
        }
        list = e.elements("sect3");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item tempItem = getItemByXml2(temp, xml, book, flag, item, item.getSource());
//				item=mergeItem(item,tempItem,"sect3");
                item.getItems().add(tempItem);
            }
        }
        list = e.elements("sect4");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item tempItem = getItemByXml2(temp, xml, book, flag, item, item.getSource());
                item.getItems().add(tempItem);
//				item=mergeItem(item,tempItem,"sect4");
            }
        }
        list = e.elements("sect5");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item tempItem = getItemByXml2(temp, xml, book, flag, item, item.getSource());
                item.getItems().add(tempItem);
//				item=mergeItem(item,tempItem,"sect5");
            }
        }
        return item;
    }


    /**
     * 解析xml获取图书的章节信息
     *
     * @param e
     * @return
     */
    public Item getItemByXml(Element e, File xml, Book book, boolean flag, Item parentItem, String source) {
        Item item = new Item();
        item.setLinkend(e.attributeValue("id"));
        item.setBookTitle(e.elementText("title"));
        //item.setEtitle(e.elementText("etitle"));
        item.setEtitle(e.elementText("etitleabbrev"));//保存不带序号的英文标题
        item.setOriginalVersionFlag(0);
        item.setDeleteFlag(0);
        item.setResCode(ImpUtils.getResourceCode());
        item.setCatName(e.elementText("titleabbrev"));


        /**老版本正文解析	 begin*/
        //item.setContent(parseContent(e.elements("para"), xml, item,book));
        /**老版本正文解析	 end*/


        /**
         * 解析标题中的参考文献
         */
        Map<String, Object> titleInfos = parseInfo(e.elements("titleabbrev"), xml, book);
        List<Bibliography> titleBiblios = (List<Bibliography>) titleInfos.get("biblios");
        String title = titleInfos.get("content").toString();
        if (!title.contains("titleabbrev")) {
            item.setTitle(title.substring(21, title.length() - 4));
        } else {
            item.setTitle(title.substring(72, title.length() - 18));
        }

        item.getPhotos().addAll((Collection<? extends Photo>) titleInfos.get("photos"));

        if (titleBiblios != null && titleBiblios.size() > 0) {
//			StringBuffer sb=new StringBuffer();
//			for(Bibliography bi:titleBiblios){
//				sb.append("<sup name="+bi.getXmlId()+">[引用]</sup>");
//				
//			}
//			item.setTitleBibliography(sb.toString());


            item.getBiblios().addAll(titleBiblios);
        }
        if (StringUtils.isNoneBlank(source)) {
            item.setSource(source + ">" + item.getTitle());
        } else {
            item.setSource(item.getTitle());
        }


//		List<Photo> tittlePhotos=(List<Photo>) titleInfos.get("photos");
//		item.setTitlePhotos(tittlePhotos);


        /**新版本正文解析  begin*/
        Map<String, Object> infos = parseInfo(e.elements("para"), xml, book);
        item.getPhotos().addAll((Collection<? extends Photo>) infos.get("photos"));
        item.getBiblios().addAll((Collection<? extends Bibliography>) infos.get("biblios"));
        item.getStands().putAll((Map<? extends String, ? extends Object>) infos.get("standards"));
        item.setContent((String) infos.get("content"));

        /**新版本正文解析  end*/

        item.setCreateDate(new Date());
        item.setModifyDate(item.getCreateDate());
        item.setVersionDesc("加工资源");
        item.setIndexs(getIndex(e.attributeValue("id"), book));
        if (flag) {
            item.setPubShowFlag(1);
        }
        List<Element> list = e.elements("chapter");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item chapter = getItemByXml(temp, xml, book, flag, null, item.getSource());
                if (StringUtils.isNotBlank(chapter.getContent())) {
                    chapter.setPubShowFlag(1);
                    ItemContent firstLevelItem = new ItemContent(chapter.getTitle(), chapter.getContent());
                    firstLevelItem.setChapter(null);
                    chapter.setContent(JsonMapper.nonEmptyMapper().toJson(firstLevelItem));

                    List<BibliographyContent> bcs = Lists.newArrayList();
                    TreeMap<String, BibliographyContent> map = Maps.newTreeMap();
                    //参考文献排重
                    for (Bibliography bi : chapter.getBiblios()) {
                        BibliographyContent bc = new BibliographyContent();
                        bc.setId(bi.getXmlId());
                        bc.setType(1);
                        bc.setTitle(bi.getTitle());
                        bc.setContent(bi.getDescp().substring(bi.getDescp().indexOf("]") + 1));
                        bc.setAuthor(bi.getAuthor());
                        //bcs.add(bc);
                        map.put(bc.getId(), bc);
                    }
                    Iterator it = map.keySet().iterator();
                    while (it.hasNext()) {
                        bcs.add(map.get(it.next()));
                    }

                    chapter.setBiblioIds(JsonMapper.nonEmptyMapper().toJson(bcs));

                }

                item.getItems().add(chapter);
            }
        }
        list = e.elements("sect1");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                //	Map<String,>
                Item sect1 = getItemByXml(temp, xml, book, true, null, item.getSource());

                sect1.setPubShowFlag(1);
                //拼接正文
                ItemContent ic = new ItemContent("正文", sect1.getContent());
                //一级目录
                for (Item firstLevel : sect1.getItems()) {

                    ItemContent firstLevelItem = new ItemContent(firstLevel.getTitle(), firstLevel.getContent());
                    ic.getSonChapters().add(firstLevelItem);
                    mergeItem(sect1, firstLevel);
                    //二级目录
                    for (Item secondLevel : firstLevel.getItems()) {
                        mergeItem(sect1, secondLevel);
                        ItemContent secondLevleItem = new ItemContent(secondLevel.getTitle(), secondLevel.getContent());
                        firstLevelItem.getSonChapters().add(secondLevleItem);
                        //三级合并到二级
                        for (Item thirdLevel : secondLevel.getItems()) {
                            mergeItem(sect1, thirdLevel);

                            secondLevleItem.setContent(secondLevleItem.getContent() + "<p class=thirdLevel>" + thirdLevel.getTitle() + "</p><p>" + thirdLevel.getContent() + "</p>");
                            //四级合并到二级
                            for (Item fourthLevel : thirdLevel.getItems()) {
                                mergeItem(sect1, fourthLevel);
                                secondLevleItem.setContent(secondLevleItem.getContent() + "<p class=fourthLevel>" + fourthLevel.getTitle() + "</p><p>" + fourthLevel.getContent() + "</p>");
                                //五级合并到二级
                                for (Item fifthLevel : fourthLevel.getItems()) {
                                    mergeItem(sect1, fifthLevel);
                                    secondLevleItem.setContent(secondLevleItem.getContent() + "<p class=fourthLevel>" + fifthLevel.getTitle() + "</p><p>" + fifthLevel.getContent() + "</p>");
                                }
                            }

                        }

                    }


                }
                List<BibliographyContent> bcs = Lists.newArrayList();
                TreeMap<String, BibliographyContent> map = Maps.newTreeMap();
                //参考文献排重
                for (Bibliography bi : sect1.getBiblios()) {
                    BibliographyContent bc = new BibliographyContent();
                    bc.setId(bi.getXmlId());
                    bc.setType(1);
                    bc.setTitle(bi.getTitle());
                    bc.setContent(bi.getDescp().substring(bi.getDescp().indexOf("]") + 1));
                    bc.setAuthor(bi.getAuthor());
                    //bcs.add(bc);
                    map.put(bc.getId(), bc);
                }
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    bcs.add(map.get(it.next()));
                }

                sect1.setBiblioIds(JsonMapper.nonEmptyMapper().toJson(bcs));

                sect1.setContent(JsonMapper.nonEmptyMapper().toJson(ic));
                sect1.setItems(Lists.newArrayList());
                item.getItems().add(sect1);
            }
        }
        list = e.elements("sect2");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item tempItem = getItemByXml(temp, xml, book, flag, item, item.getSource());
                //item=mergeItem(item,tempItem,"sect2" );
                item.getItems().add(tempItem);
            }
        }
        list = e.elements("sect3");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item tempItem = getItemByXml(temp, xml, book, flag, item, item.getSource());
//				item=mergeItem(item,tempItem,"sect3");
                item.getItems().add(tempItem);
            }
        }
        list = e.elements("sect4");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item tempItem = getItemByXml(temp, xml, book, flag, item, item.getSource());
                item.getItems().add(tempItem);
//				item=mergeItem(item,tempItem,"sect4");
            }
        }
        list = e.elements("sect5");
        if (list != null && list.size() > 0) {
            for (Element temp : list) {
                Item tempItem = getItemByXml(temp, xml, book, flag, item, item.getSource());
                item.getItems().add(tempItem);
//				item=mergeItem(item,tempItem,"sect5");
            }
        }
        return item;
    }

/*	public String parseContent(List<Element> paras,File xml,Item item,Book book){
        StringBuilder sb = new StringBuilder();
		String temp=null;
		for (Element e : paras) {
			if(e.elements()==null||e.elements().size()==0){
				temp=e.getText();
				if(StringUtils.isNoneBlank(temp)){
					sb.append("<p class='book_para'>").append(temp).append("</p>");
				}
				continue;
			}
			List<Element> list=e.elements();
			temp=e.asXML();
			for(Element e1:list){
				//解析图片
				if(e1.getName().equalsIgnoreCase("inlinemediaobject")){
					Photo photo=parseImageObject(e, xml,book);
					item.getPhotos().add(photo);
					System.out.println(temp);
					if(StringUtils.isNotBlank(temp)){
						//sb.append(temp); 
						System.out.println(temp);
						if(StringUtils.isNoneBlank(getImageContent(photo))){
							temp=temp.replaceAll(e1.asXML().replaceAll("<inlinemediaobject xmlns=\"http://docbook.org/ns/docbook\"" , "<inlinemediaobject"), getImageContent(photo));
						}
						item.getPhotos().add(photo);
					}
				}
				if(e1.getName().equalsIgnoreCase("mediaobject")){
					Photo photo=parseImageObject(e, xml,book);
					item.getPhotos().add(photo);
					temp=null;
					//temp=getImageContent(photo);
					//System.out.println(e1.asXML());
					//System.out.println(e1.asXML().replaceAll("<mediaobject xmlns=\"http://docbook.org/ns/docbook\"" , "<mediaobject"));
					//temp=temp.replaceAll(e1.asXML().replaceAll("<mediaobject xmlns=\"http://docbook.org/ns/docbook\"" , "<mediaobject"),"");
					
//					if(StringUtils.isNotBlank(temp)){
//						item.getPhotos().add(photo);
//					}
				}
//				//处理见图问题
				if(e1.getName().equalsIgnoreCase("link")&&"linkimage".equalsIgnoreCase(e1.attributeValue("type"))){
					
					temp=temp.replaceAll(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"" , "<link"), getImageLint(e1.attributeValue("linkend")));
					System.out.println(temp);
				}

				if(e1.getName().equalsIgnoreCase("link")&&"linktable".equalsIgnoreCase(e1.attributeValue("type"))){
					
					//sb.append(getImageLint(e1.attributeValue("linkend", "")));
					temp=temp.replaceAll(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"" , "<link"), getImageLint(e1.attributeValue("linkend")));
					
				}
				
				if(e1.getName().equalsIgnoreCase("link")&&"linkbiblio".equalsIgnoreCase(e1.attributeValue("type"))){
					Bibliography b=book.getBiblios().get(e1.attributeValue("linkend"));
					if(b!=null){
						item.getBiblios().add(b);
					}
				}
				
				//解析标准
				if(e1.getName().equalsIgnoreCase("standard")){
					book.getStandards().put(e1.getTextTrim(), null);
					
					item.getStands().put(e1.getTextTrim(), null);
				}
				
				if(e1.getName().equalsIgnoreCase("link")&&"linkitem".equalsIgnoreCase(e1.attributeValue("type"))){
//					sb.append("<span class='linkItem'>"+e1.attribute("linkend")+"</span>");
					temp=temp.replaceAll(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"" , "<link"), "<span class='linkitem' value='"+e1.attributeValue("linkend")+"'></span>");
					
				
			}
			
		}
			//temp=e.getText();
			if(StringUtils.isNoneBlank(temp)){
				sb.append("<p class='book_para'>").append(temp).append("</p>");
			}
		}
		if (sb.length() <= 0) {
			return null;
		}
		return sb.toString();
	}*/
	
	
/*	public String parseContent2(List<Element> paras,File xml,Item item,Book book){
		StringBuilder sb = new StringBuilder();
		String temp=null;
		for (Element e : paras) {
			if(e.elements()==null||e.elements().size()==0){
				temp=e.getText();
				if(StringUtils.isNoneBlank(temp)){
					sb.append("<p class='book_para'>").append(temp).append("</p>");
				}
				continue;
			}
			List<Element> list=e.elements();
			for(Element e1:list){
				//解析图片
				if(e1.getName().equalsIgnoreCase("inlinemediaobject")){
					Photo photo=parseImageObject(e, xml,book);
					item.getPhotos().add(photo);
					temp=getImageContent(photo);
					if(StringUtils.isNotBlank(temp)){
						sb.append(temp);
						item.getPhotos().add(photo);
					}
				}
				if(e1.getName().equalsIgnoreCase("mediaobject")){
					Photo photo=parseImageObject(e, xml,book);
					item.getPhotos().add(photo);
					temp=getImageContent(photo);
					if(StringUtils.isNotBlank(temp)){
						item.getPhotos().add(photo);
					}
				}
//				//处理见图问题
				if(e1.getName().equalsIgnoreCase("link")&&"linkimage".equalsIgnoreCase(e1.attributeValue("type"))){
					sb.append(getImageLint(e1.attributeValue("linkend", "")));
				}
				
				if(e1.getName().equalsIgnoreCase("link")&&"linkbiblio".equalsIgnoreCase(e1.attributeValue("type"))){
					Bibliography b=book.getBiblios().get(e1.attributeValue("linkend"));
					if(b!=null){
						item.getBiblios().add(b);
					}
				}
				if(e1.getName().equalsIgnoreCase("link")&&"linkitem".equalsIgnoreCase(e1.attributeValue("type"))){
					sb.append("<span class='linkItem'>"+e1.attribute("linkend")+"</span>");
				}
				
			}
			temp=e.getText();
			if(StringUtils.isNoneBlank(temp)){
				sb.append("<p class='book_para'>").append(temp).append("</p>");
			}
		}
		if (sb.length() <= 0) {
			return null;
		}
		return sb.toString();
	}*/

    /**
     * 解析正文中的图片
     *
     * @param //para
     * @param xml
     * @return
     */
    public Photo parseImageObject(Element imageXml, File xml, Book book, List<Bibliography> biblios) {
        Photo photo = new Photo();
        try {
            Element imageDate = null;
            if ("mediaobject".equals(imageXml.getName())) {
                imageDate = imageXml.element("imageobject").element("imagedata");
                photo.setLinkimage(imageXml.attributeValue("id", null));//保存图片的链接id，引用时需要
            } else if ("inlinemediaobject".equals(imageXml.getName())) {
                imageDate = imageXml.element("imageobject").element("imagedata");
            } else {
                return photo;
            }
//			if(para.element("mediaobject")!=null){
//				 imageDate=para.element("mediaobject").element("imageobject").element("imagedata");
//				 photo.setLinkimage(para.element("mediaobject").attributeValue("id",null));//保存图片的链接id，引用时需要
//			}else if(para.element("inlinemediaobject")!=null){
//				 imageDate=para.element("inlinemediaobject").element("imageobject").element("imagedata");
//			}else{
//				return photo;
//			}


            //Element imageDate=para.element("mediaobject").element("imageobject").element("imagedata");
            if (imageDate == null) {
                return photo;
            }


            File image = new File(xml.getParent() + File.separator + imageDate.attributeValue("fileref"));
            System.out.println(image.exists() + ":" + xml.getParent() + File.separator + imageDate.attributeValue("fileref"));
            if (!image.exists()) {
                return photo;
            }
            photo.setTempCode(ImpUtils.getResourceCode());
            photo.setImageFile(image);
            photo.setRole(imageDate.attributeValue("role"));
            Element imageInfo = imageDate.element("info");


            if (imageInfo != null) {
                //photo.setTitle(this.parseTitle(imageInfo.element("title"), xml, book, photo).get("content").toString());
                photo.setBookTitle(imageInfo.elementText("title"));
                photo.setTitleabbrev(this.parseTitle(imageInfo.element("titleabbrev"), xml, book, photo, biblios).get("content").toString());
                //photo.setPdfPage(Integer.parseInt(imageInfo.elementText("")));
                //photo.setBookPage(Integer.parseInt(imageInfo.elementText("")));

                //图注
                Element annotation = imageInfo.element("annotation");
                if (annotation != null) {
                    List<Element> paras = annotation.elements("para");
                    int order = 0;
                    for (Element temp : paras) {
                        List<Element> tt = Lists.newArrayList();
                        tt.add(temp);
                        Map<String, Object> infos = parseInfo(tt, xml, book);
                        PicAnnotation pa = new PicAnnotation();
                        pa.setContent((String) infos.get("content"));
                        pa.setOrder(order++);
                        List<Photo> photos = (List<Photo>) infos.get("photos");
                        biblios.addAll((Collection<? extends Bibliography>) infos.get("biblios"));
                        for (Photo p : photos) {
                            InlinePic ip = new InlinePic();
                            ip.setFile(p.getImageFile());
                            ip.setType(1);
                            ip.setTempCode(p.getTempCode());
                            pa.getIps().add(ip);

                        }
                        photo.getPicAnnotation().add(pa);

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photo;
    }


    public Map<String, Object> parseTitle(Element element, File xml, Book book, Photo p, List<Bibliography> biblios) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("content", "");//默认值
        if (element != null) {
            List<Element> children = element.elements();
            if (children != null && children.size() > 0) {
                String temp = element.asXML();
                for (Element child : children) {
                    /**
                     * 解析斜体    italic
                     */
                    if ("italic".equalsIgnoreCase(child.getName())) {
                        temp = temp.replace(child.asXML().replaceAll("<italic xmlns=\"http://docbook.org/ns/docbook\"", "<italic"), "<i>" + child.getTextTrim() + "</i>");
                    }

                    /**
                     * 解析图片
                     */
                    if (child.getName().equalsIgnoreCase("inlinemediaobject")) {
                        Photo photo = parseImageObject(child, xml, book, biblios);
                        if (StringUtils.isNotBlank(getImageContent(photo))) {
                            p.getTitlePhoto().add(photo);
                            temp = temp.replaceAll(child.asXML().replaceAll("<inlinemediaobject xmlns=\"http://docbook.org/ns/docbook\"", "<inlinemediaobject"), getImageContent(photo));
                        }
                    }
                    /**
                     * 解析参考文献
                     */
                    if (child.getName().equalsIgnoreCase("link") && "linkbiblio".equalsIgnoreCase(child.attributeValue("type"))) {
                        Bibliography b = book.getBiblios().get(child.attributeValue("linkend"));
                        if (b != null) {
                            biblios.add(b);
                            temp = temp.replace(child.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), "<sup name=" + b.getXmlId() + ">[引用]</sup>");
                        }
                    }

                }
                if ("title".equalsIgnoreCase(element.getName())) {
                    map.put("content", temp.substring(45, temp.length() - 8));
                } else if ("titleabbrev".equalsIgnoreCase(element.getName())) {
                    map.put("content", temp.substring(51, temp.length() - 14));
                }

            } else {
                map.put("content", element.getTextTrim());
            }
        }

        return map;
    }

    /**
     * 解析图注解
     *
     * @return
     */
    public void parseAnnotation(Photo photo, Element annotation, File xml) {
        if (annotation != null) {
            List<Element> paras = annotation.elements("para");

            if (paras != null && paras.size() > 0) {
                for (int i = 0; i < paras.size(); i++) {
                    Element para = paras.get(i);
                    String temp = para.asXML();
                    para.elements();
                    List<Element> child = para.elements();
                    for (Element e : child) {
                        PicAnnotation pa = new PicAnnotation();
                        //处理行间图
                        if (e.getName().equalsIgnoreCase("inlinemediaobject")) {
                            //	Photo tempPhoto=parseImageObject(e,xml);
                            InlinePic ip = new InlinePic();
                            //	ip.setFile(tempPhoto.getImageFile());
                            pa.getIps().add(ip);
                            temp = temp.replaceAll(e.asXML().replaceAll("<inlinemediaobject xmlns=\"http://docbook.org/ns/docbook\"", "<inlinemediaobject"), getImageContent(photo));
                        }
                        //处理参见条目

                        if (e.getName().equalsIgnoreCase("link") && "linkitem".equalsIgnoreCase(e.attributeValue("type"))) {
                            //sb.append("<span class='linkItem'>"+e1.attribute("linkend")+"</span>");
                            //temp=temp.replaceAll(e.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"" , "<link"), getImageContent(photo));
                        }
                        //标准    <para>注：本表摘自<standard>GB3095—82《大气环境质量标准》</standard>。</para>

                        //见表 para>③  主厂房基础挖方工期不包括地基处理的工期。对需进行地基处理的工程，且该地基处理工艺是在土方开挖后进行的，不同地基处理方法的参考工期见<link linkend="表3·13-4" type="linktable">表3·13-4</link>。</para>

                        //见图 <link linkend="图3·13-3" type="linkimage">图3·13-3</link>
                    }

                }
            }

        }
    }


    public String getImageContent(Photo photo) {
        StringBuilder sb = new StringBuilder();
        if (photo.getImageFile() == null || photo.getTempCode() == null) {
            return null;
        }
        sb.append("<img class='book_image' src='").append(photo.getTempCode()).append("' title='")
                .append(photo.getTitle()).append("' role='").append(photo.getRole()).append("' />");
        // .append("<div class='book_image_title'>").append(photo.getTitleabbrev()).append("</div></p>");
        if ("表格".equals(photo.getRole()) || "线条图".equals(photo.getRole())) {
            sb.append("<div class='book_image_title'>").append(photo.getTitleabbrev()).append("</div>");
        }
        //sb.append("</p>");
        return sb.toString();

    }

    public String getImageLint(String imageLink) {
        StringBuilder sb = new StringBuilder();
        for (String link : imageLink.split("，")) {
            sb.append("<img class='book_image' src='").append(link).append("'/> ");
        }
        return sb.toString().trim();
    }

    /**
     * 解析索引词
     *
     * @param index
     */
    public void parseIndexEntry(Element index, Book book) {
        if (index == null) {
            return;
        }
        List<Element> list = index.elements("indexdiv");
        for (Element e : list) {
            List<Element> indexs = e.elements("indexentry");
            for (Element i : indexs) {
                Indexentry entry = new Indexentry();
                entry.setName(i.getText());
                entry.setPage(i.attributeValue("page"));
                entry.setXmlId(i.attributeValue("href"));
                book.getIndexs().add(entry);
            }
        }
    }

    public List<Indexentry> getIndex(String xmlid, Book book) {
        List<Indexentry> list = Lists.newArrayList();
        if (StringUtils.isBlank(xmlid)) {
            return list;
        }
        try {
            for (Indexentry index : book.getIndexs()) {
                if (StringUtils.isBlank(index.getXmlId())) {
                    continue;
                }
                String[] ids = index.getXmlId().split("，");
                for (String id : ids) {
                    if (xmlid.equals(id)) {
                        list.add(index);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }


    /**
     * 解析参考文献内容
     *
     * @param biblio
     */
    public void parseBibliography(Element biblio, Book book, File xml) {
        List<Element> list = biblio.elements("biblioentry");
        for (Element e : list) {
            Bibliography b = new Bibliography();
            b.setXmlId(e.attributeValue("id"));
            System.out.println(e.attributeValue("id"));

            if (e.element("title").elements().size() > 0) {
                Map<String, Object> infos = parseInfo(e.elements("title"), xml, book);


                b.getPhotos().addAll((Collection<? extends Photo>) infos.get("photos"));
                b.setTitle(infos.get("content").toString().replace("<p class='book_para'><title xmlns=\"http://docbook.org/ns/docbook\">", "").replace("</title></p>", ""));
            } else {
                b.setTitle(e.elementText("title"));
            }


            //e.elementText("title");
//			List children=e.elements();
//			if(children.size()>0){
//				String temp=e.asXML();
//				
//			}else{
//				b.setTitle(e.elementText("title"));
//			}
            //
            try {

                b.setDescp(e.element("annotation").elementText("para"));
                if (e.element("author") != null) {
                    b.setAuthor(e.element("author").elementText("personname"));
                }
            } catch (Exception e2) {
                logger.error("解析参考文献时发生异常！", e);
            }
            book.getBiblios().put(b.getXmlId(), b);
        }
    }

    public Map<String, Object> parseInfo(List<Element> paras, File xml, Book book) {
        Map<String, Object> map = Maps.newHashMap();
        List<Photo> photos = Lists.newArrayList();
        Map<String, Object> standards = Maps.newHashMap();
        List<Bibliography> biblios = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        String temp = null;
        for (Element e : paras) {
            if (e.elements() == null || e.elements().size() == 0) {
                temp = e.getText();
                if (StringUtils.isNoneBlank(temp)) {
                    sb.append("<p class='book_para'>").append(temp).append("</p>");
                }
                continue;
            }
            List<Element> list = e.elements();
            temp = e.asXML();

            /**
             *  <para>（<link linkend="表2·3-13" type="linktable">表2·3-13</link>）</para>
             */
            if ("（）".equalsIgnoreCase(e.getTextTrim()) && list.size() == 1 && list.get(0).getName().equalsIgnoreCase("link") && (list.get(0).attributeValue("type").equalsIgnoreCase("linkimage") || list.get(0).attributeValue("type").equalsIgnoreCase("linktable"))) {
                temp = "<para xmlns=\"http://docbook.org/ns/docbook\">" + getImageLint(list.get(0).attributeValue("linkend")) + "如图:</para>";
            } else {
                for (Element e1 : list) {
                    //解析图片
                    if (e1.getName().equalsIgnoreCase("inlinemediaobject")) {
                        Photo photo = parseImageObject(e1, xml, book, biblios);
                        //	item.getPhotos().add(photo);
                        //System.out.println(temp);
                        if (StringUtils.isNotBlank(getImageContent(photo))) {
                            photos.add(photo);
                            //sb.append(temp);
                            temp = temp.replaceAll(e1.asXML().replaceAll("<inlinemediaobject xmlns=\"http://docbook.org/ns/docbook\"", "<inlinemediaobject"), getImageContent(photo));
                            //item.getPhotos().add(photo);
                        }
                    }
                    if (e1.getName().equalsIgnoreCase("mediaobject")) {
                        Photo photo = parseImageObject(e1, xml, book, biblios);
                        photos.add(photo);//item.getPhotos().add(photo);
                        if (!("线条图".equals(photo.getRole()) || "表格".equals(photo.getRole())) || StringUtils.isBlank(photo.getLinkimage())) {
                            //temp="<img class='book_image' src='表1·1-1'/>";

                            temp = temp.replace(e1.asXML().replace("<mediaobject xmlns=\"http://docbook.org/ns/docbook\"", "<mediaobject"), getImageContent(photo));

                        } else {
                            temp = null;
                        }

                        //temp=getImageContent(photo);
                        //System.out.println(e1.asXML());
                        //System.out.println(e1.asXML().replaceAll("<mediaobject xmlns=\"http://docbook.org/ns/docbook\"" , "<mediaobject"));
                        //temp=temp.replaceAll(e1.asXML().replaceAll("<mediaobject xmlns=\"http://docbook.org/ns/docbook\"" , "<mediaobject"),"");

//					if(StringUtils.isNotBlank(temp)){
//						item.getPhotos().add(photo);
//					}
                    }
//				//处理见图问题
                    if (e1.getName().equalsIgnoreCase("link") && "linkimage".equalsIgnoreCase(e1.attributeValue("type"))) {

                        temp = temp.replaceAll(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), getImageLint(e1.attributeValue("linkend")));
                        //System.out.println(temp);
                    }

                    if (e1.getName().equalsIgnoreCase("link") && "linktable".equalsIgnoreCase(e1.attributeValue("type"))) {

                        //sb.append(getImageLint(e1.attributeValue("linkend", "")));
                        temp = temp.replaceAll(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), getImageLint(e1.attributeValue("linkend")));

                    }

                    if (e1.getName().equalsIgnoreCase("link") && "linkbiblio".equalsIgnoreCase(e1.attributeValue("type"))) {
                        Bibliography b = book.getBiblios().get(e1.attributeValue("linkend"));
                        if (b != null) {
//						item.getBiblios().add(b);
                            biblios.add(b);
                            temp = temp.replace(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), "<sup name=" + b.getXmlId() + ">[引用]</sup>");
                        }
                    }

                    if (e1.getName().equalsIgnoreCase("formulaId")) {
                        temp = temp.replace(e1.asXML().replaceAll("<formulaId xmlns=\"http://docbook.org/ns/docbook\"", "<formulaId"), "");
                    }

                    //解析标准
                    if (e1.getName().equalsIgnoreCase("standard")) {
                        book.getStandards().put(e1.getTextTrim(), null);

                        standards.put(e1.getTextTrim(), null);//item.getStands().put(e1.getTextTrim(), null);
                    }

                    if (e1.getName().equalsIgnoreCase("link") && "linkitem".equalsIgnoreCase(e1.attributeValue("type"))) {
                        if (StringUtils.isNotBlank(e1.attributeValue("linkend"))) {
                            StringBuffer linkitemStr = new StringBuffer("");
                            for (String linkitem : e1.attributeValue("linkend").split("，")) {
                                linkitemStr.append("<span class='linkitem' value='" + linkitem + "'></span> ");
                            }
                            temp = temp.replace(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), linkitemStr.toString().trim());

                        } else {
                            temp = temp.replace(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), "<trs_outside>" + e1.getTextTrim() + "</trs_outside>");

                        }

                    }
                    //解析引用公式
                    if (e1.getName().equalsIgnoreCase("link") && "linkformula".equalsIgnoreCase(e1.attributeValue("type"))) {
                        StringBuffer linkformulaStr = new StringBuffer("");
                        for (String linkformula : e1.attributeValue("linkend").split("，")) {
                            linkformulaStr.append("<img name='linkformula' value='" + linkformula + "'></img> ");
                        }
                        temp = temp.replace(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), linkformulaStr.toString().trim());


                    }


                    //解析斜体
                    if (e1.getName().equalsIgnoreCase("italic")) {
                        temp = temp.replace(e1.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<italic"), "<span class='font-style:italic;'>" + e.getTextTrim() + "</span>");

                    }


                    //解析注释  <note><para>小时热化系数与年热化系数的关系是由不同热负荷特性来确定的，<link linkend="图3·6-5" type="linkimage">图3·6-5</link>是作为一个具体示例，而不是一组通用关系曲线。</para></note>
                    if (e1.getName().equalsIgnoreCase("note")) {
                        StringBuffer notesStr = new StringBuffer("");
                        List<Element> nodes = e1.elements("para");
                        for (Element node : nodes) {
                            Note note = parsetNode(node, biblios, 1, book, xml, photos);
                            noteService.save(note);
                            notesStr.append("<note class=note value=" + note.getId() + "></note>");
                        }
                        temp = temp.replace(e1.asXML().replace("<note xmlns=\"http://docbook.org/ns/docbook\"", "<note"), "<notes>" + notesStr.toString() + "</notes>");

                    }


                }
            }
            //temp=e.getText();
            if (StringUtils.isNoneBlank(temp)) {
                sb.append("<p class='book_para'>").append(temp).append("</p>");
            }
        }
        map.put("photos", photos);
        map.put("standards", standards);
        map.put("biblios", biblios);


        if (sb.length() <= 0) {
            sb.append("");
        }
        map.put("content", sb.toString());
        return map;
    }

    public Note parsetNode(Element e, List<Bibliography> biblios, int type, Book book, File xml, List<Photo> photos) {
        Note note = new Note();
        note.setType(type);
        List<Element> children = e.elements();
        if (children.size() == 0) {
            note.setContent(e.getTextTrim());
            return note;
        } else {
            String temp = e.asXML();
            for (Element child : children) {
                if (child.getName().equalsIgnoreCase("link") && "linkimage".equalsIgnoreCase(child.attributeValue("type"))) {
                    //处理见图
                    temp = temp.replaceAll(child.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), getImageLint(child.attributeValue("linkend")));
                }

                if (child.getName().equalsIgnoreCase("link") && "linkitem".equalsIgnoreCase(child.attributeValue("type"))) {
                    //处理条目引用
                    temp = temp.replaceAll(child.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), "<span class='linkitem' value='" + child.attributeValue("linkend") + "'></span>");
                }

                if (child.getName().equalsIgnoreCase("link") && "linkbiblio".equalsIgnoreCase(child.attributeValue("type"))) {
                    //处理参考文献引用
                    Bibliography b = book.getBiblios().get(child.attributeValue("linkend"));
                    if (b != null) {
                        biblios.add(b);
                    }

                    temp = temp.replace(child.asXML().replace("<link xmlns=\"http://docbook.org/ns/docbook\"", "<link"), "<sup name=" + child.attributeValue("linkend") + ">[引用]</sup>");

                }

                //解析斜体
                if (child.getName().equalsIgnoreCase("italic")) {
                    temp = temp.replace(child.asXML().replaceAll("<link xmlns=\"http://docbook.org/ns/docbook\"", "<italic"), "<span class='font-style:italic;'>" + child.getTextTrim() + "</span>");
                }

                if (child.getName().equalsIgnoreCase("inlinemediaobject")) {
                    Photo photo = parseImageObject(child, xml, book, biblios);
                    //	item.getPhotos().add(photo);
                    //System.out.println(temp);
                    if (StringUtils.isNotBlank(getImageContent(photo))) {
                        photos.add(photo);
                        temp = temp.replaceAll(child.asXML().replaceAll("<inlinemediaobject xmlns=\"http://docbook.org/ns/docbook\"", "<inlinemediaobject"), getImageContent(photo));
                        //item.getPhotos().add(photo);
                    }
                }


            }

            temp = temp.substring("<para xmlns=\"http://docbook.org/ns/docbook\">".length(), temp.lastIndexOf("</para>"));

            note.setContent(temp);

            return note;

        }
    }


    public Item mergeItem(Item parent, Item child) {
//		String parentContent=StringUtils.isNoneBlank(parent.getContent())?parent.getContent():"";
//		if(StringUtils.isNoneBlank(child.getTitle())){
//			parentContent+="<p class="+type+">"+child.getTitle()+"</p>";
//		}
//		if(StringUtils.isNoneBlank(child.getContent())){
//			parentContent+=child.getContent();
//		}
//		
//		
//		parent.setContent(parentContent);
        parent.getPhotos().addAll(child.getPhotos());
        parent.getBiblios().addAll(child.getBiblios());
        parent.getIndexs().addAll(child.getIndexs());
        parent.getStands().putAll(child.getStands());
        return parent;
    }


    class ItemContent {
        private String chapter;

        private String content;


        private List<ItemContent> sonChapters = Lists.newArrayList();

        public String getChapter() {
            return chapter;
        }

        public void setChapter(String chapter) {
            this.chapter = chapter;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


        public List<ItemContent> getSonChapters() {
            return sonChapters;
        }

        public void setSonChapters(List<ItemContent> sonChapters) {
            this.sonChapters = sonChapters;
        }

        public ItemContent() {
        }

        public ItemContent(String chapter, String content) {
            this.chapter = chapter;
            this.content = content;
        }


    }

    /**
     * 图书扫描目录入库
     */
    public void importBook(String path) {

        File file = new File(path);
        File[] bookFiles = file.listFiles();
        Attachment attchmnet;
        for (File bookFile : bookFiles) {
            System.out.println("===========================>文件" + bookFile);
            String fileName = bookFile.getName();
            System.out.println("===========================>文件名" + fileName);
            //System.out.println("===============>切割前"+fileName.split("_")[0]+"-----"+fileName.split("_")[0]);

            //NewBook newBook=newBookService.findByIsbnAndPrintEdition(fileName.split("_")[0], fileName.split("_")[1]);
            String Isbn = fileName.split("_")[0];
            String printEdition = fileName.split("_")[1];
            NewBook newBook = newBookService.findByIsbn(Isbn);
            Long booid = newBook.getId();
            Iterable<Attachment> bookresouce = attachmentService.findAllByResouceId(booid);
            //Attachment bookresouce = attachmentService.findByRecouseId(booid);
            System.out.println(bookresouce.iterator().hasNext());

            //System.out.println("===============>切割后"+fileName.split("_")[0]+"-----"+fileName.split("_")[2]);
            System.out.println(newBook);
            if (!(bookresouce.iterator().hasNext())) {
                if (newBook != null) {

                    Long resourceId = newBook.getId();
                    //
                    String xmlPath = bookFile.getAbsolutePath() + File.separator + "XML" + File.separator + fileName + ".xml";


                    System.out.println("==============================>xml路径" + xmlPath);

                    String coverPic = bookFile.getAbsolutePath() + File.separator + "Cover" + File.separator + fileName + "_Cover1.jpg";

                    //图片目录
                    String imagePath = bookFile.getAbsolutePath() + File.separator + "XML" + File.separator + "Images";

                    System.out.println("==============================>imagePath路径" + imagePath);

                    String pfdPath1 = bookFile.getAbsolutePath() + File.separator + "PDF" + File.separator + fileName + "_1.pdf";

                    System.out.println("===========================>pdf1路径" + pfdPath1);

                    String pfdPath2 = bookFile.getAbsolutePath() + File.separator + "PDF" + File.separator + fileName + "_2.pdf";

                    System.out.println("===========================>pdf2路径" + pfdPath2);

                    String epubPath = bookFile.getAbsolutePath() + File.separator + "Epub" + File.separator + fileName + ".epub";

                    System.out.println("===========================>epubPath路径" + epubPath);

                    System.out.println("============>aaaaaaaaa|||" + "cp " + xmlPath + " " + configService.getAttachXml() + File.separator + fileName + ".xml");

                    //复制xml
                    if (Shell.run("cp " + xmlPath + " " + configService.getAttachXml() + File.separator + fileName + ".xml")) {
                        attchmnet = new Attachment(resourceId, fileName + ".xml", configService.getAttachXml() + File.separator + fileName + ".xml", 2, "XML");
                        attachmentService.save(attchmnet);
                    }

                    //复制pdf
                    if (Shell.run("cp " + pfdPath1 + " " + configService.getAttachPdf() + File.separator + fileName + "_1.pdf")) {
                        attchmnet = new Attachment(resourceId, fileName + "_1.pdf", configService.getAttachPdf() + File.separator + fileName + "_1.pdf", 1, "PDF");
                        attachmentService.save(attchmnet);
                    }
                    if (Shell.run("cp " + pfdPath2 + " " + configService.getAttachPdf() + File.separator + fileName + "_2.pdf")) {
                        attchmnet = new Attachment(resourceId, fileName + "_2.pdf", configService.getAttachPdf() + File.separator + fileName + "_2.pdf", 1, "PDF");
                        attachmentService.save(attchmnet);
                    }

                    //复制epub
                    if (Shell.run("cp " + epubPath + " " + configService.getAttchEpub() + File.separator + fileName + ".epub")) {
                        attchmnet = new Attachment(resourceId, fileName + ".epub", configService.getAttchEpub() + File.separator + fileName + ".epub", 4, "EPUB");
                        attachmentService.save(attchmnet);
                    }

                    //复制图片
                    File imageFilePath = new File(imagePath);

                    for (File image : imageFilePath.listFiles()) {
                        if (Shell.run("cp " + image.getAbsolutePath() + " " + configService.getAttachImage() + File.separator + image.getName())) {
                            attchmnet = new Attachment(resourceId, image.getName(), configService.getAttachImage() + File.separator + image.getName(), 3, "图片");
                            attachmentService.save(attchmnet);
                        }
                    }
                    //复制封面
                    if (Shell.run("cp " + coverPic + " " + configService.getAttachCover() + File.separator + fileName + "._Cover1.jpg")) {
                        attchmnet = new Attachment(resourceId, fileName + "._Cover1.jpg", configService.getAttachCover() + File.separator + fileName + "._Cover1.jpg", 5, "COVER");
                        attachmentService.save(attchmnet);
                    }
                    //修改版次
                    newBookService.edition(printEdition, resourceId);
                }
            }
            Shell.run("mv " + bookFile.getAbsolutePath() + " " + configService.getAttchBak() + File.separator + bookFile.getName());
        }

    }

    //图书上传
    public void uploadbook(String path) {
        File file = new File(path);
        File[] bookFiles = file.listFiles();
        Attachment attchmnet;
        for (File bookFile : bookFiles) {
            String fileName = bookFile.getName();
            System.out.println("===========================>文件名" + fileName);
            //System.out.println("===============>切割前"+fileName.split("_")[0]+"-----"+fileName.split("_")[0]);

            //NewBook newBook=newBookService.findByIsbnAndPrintEdition(fileName.split("_")[0], fileName.split("_")[1]);
            String Isbn = fileName.split("_")[0];
            String printEdition = fileName.split("_")[1];
            NewBook newBook = newBookService.findByIsbn(Isbn);
            Long booid = newBook.getId();
            Iterable<Attachment> bookresouce = attachmentService.findAllByResouceId(booid);
            //Attachment bookresouce = attachmentService.findByRecouseId(booid);
            System.out.println(bookresouce.iterator().hasNext());

            //System.out.println("===============>切割后"+fileName.split("_")[0]+"-----"+fileName.split("_")[2]);
            System.out.println(newBook);
            if (!(bookresouce.iterator().hasNext())) {
                if (newBook != null) {

                    Long resourceId = newBook.getId();
                    //
                    String xmlPath = bookFile.getAbsolutePath() + File.separator + "XML" + File.separator + fileName + ".xml";


                    String coverPic = bookFile.getAbsolutePath() + File.separator + "Cover" + File.separator + fileName + "_Cover1.jpg";

                    //图片目录
                    String imagePath = bookFile.getAbsolutePath() + File.separator + "XML" + File.separator + "Images";

                    System.out.println("==============================>imagePath路径" + imagePath);

                    String pfdPath1 = bookFile.getAbsolutePath() + File.separator + "PDF" + File.separator + fileName + "_1.pdf";

                    System.out.println("===========================>pdf1路径" + pfdPath1);

                    String pfdPath2 = bookFile.getAbsolutePath() + File.separator + "PDF" + File.separator + fileName + "_2.pdf";

                    System.out.println("===========================>pdf2路径" + pfdPath2);

                    String epubPath = bookFile.getAbsolutePath() + File.separator + "Epub" + File.separator + fileName + ".epub";

                    System.out.println("===========================>epubPath路径" + epubPath);

                    System.out.println("============>aaaaaaaaa|||" + "cp " + xmlPath + " " + configService.getAttachXml() + File.separator + fileName + ".xml");

                    //复制xml
                    if (Shell.run("cp " + xmlPath + " " + configService.getAttachXml() + File.separator + fileName + ".xml")) {
                        attchmnet = new Attachment(resourceId, fileName + ".xml", configService.getAttachXml() + File.separator + fileName + ".xml", 2, "XML");
                        attachmentService.save(attchmnet);
                    }

                    //复制pdf
                    if (Shell.run("cp " + pfdPath1 + " " + configService.getAttachPdf() + File.separator + fileName + "_1.pdf")) {
                        attchmnet = new Attachment(resourceId, fileName + "_1.pdf", configService.getAttachPdf() + File.separator + fileName + "_1.pdf", 1, "PDF");
                        attachmentService.save(attchmnet);
                    }
                    if (Shell.run("cp " + pfdPath2 + " " + configService.getAttachPdf() + File.separator + fileName + "_2.pdf")) {
                        attchmnet = new Attachment(resourceId, fileName + "_2.pdf", configService.getAttachPdf() + File.separator + fileName + "_2.pdf", 1, "PDF");
                        attachmentService.save(attchmnet);
                    }

                    //复制epub
                    if (Shell.run("cp " + epubPath + " " + configService.getAttchEpub() + File.separator + fileName + ".epub")) {
                        attchmnet = new Attachment(resourceId, fileName + ".epub", configService.getAttchEpub() + File.separator + fileName + ".epub", 4, "EPUB");
                        attachmentService.save(attchmnet);
                    }

                    //复制图片
                    File imageFilePath = new File(imagePath);

                    for (File image : imageFilePath.listFiles()) {
                        if (Shell.run("cp " + image.getAbsolutePath() + " " + configService.getAttachImage() + File.separator + image.getName())) {
                            attchmnet = new Attachment(resourceId, image.getName(), configService.getAttachImage() + File.separator + image.getName(), 3, "图片");
                            attachmentService.save(attchmnet);
                        }
                    }
                    //复制封面
                    if (Shell.run("cp " + coverPic + " " + configService.getAttachCover() + File.separator + fileName + "._Cover1.jpg")) {
                        attchmnet = new Attachment(resourceId, fileName + "._Cover1.jpg", configService.getAttachCover() + File.separator + fileName + "._Cover1.jpg", 5, "COVER");
                        attachmentService.save(attchmnet);
                    }
                    //修改版次
                    newBookService.edition(printEdition, resourceId);
                    //上传后删除上传文件
                    DeleteAll.deleteAll(file);
                }

                Shell.run("mv " + bookFile.getAbsolutePath() + " " + configService.getAttchBak() + File.separator + bookFile.getName());
            }
        }
    }


}
