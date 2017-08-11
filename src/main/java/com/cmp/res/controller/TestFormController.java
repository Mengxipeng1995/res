package com.cmp.res.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.shiro.SecurityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;

import com.cmp.res.entity.Bibliography;
import com.cmp.res.entity.BibliographyContent;
import com.cmp.res.entity.Book;
import com.cmp.res.entity.Category;
import com.cmp.res.entity.Indexentry;
import com.cmp.res.entity.InlinePic;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.ItemCategoryMapping;
import com.cmp.res.entity.ItemIndexRel;
import com.cmp.res.entity.ItemPhotoMapping;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.PhotoCategoryRel;
import com.cmp.res.entity.PicAnnotation;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.Standard;
import com.cmp.res.entity.StandardBookMapping;
import com.cmp.res.entity.StandardItemMapping;
import com.cmp.res.entity.User;
import com.cmp.res.service.BibliographyService;
import com.cmp.res.service.BookService;
import com.cmp.res.service.CategoryService;
import com.cmp.res.service.ConfigService;
import com.cmp.res.service.IndexentryService;
import com.cmp.res.service.InlinePicService;
import com.cmp.res.service.ItemCategoryMappingService;
import com.cmp.res.service.ItemIndexRelService;
import com.cmp.res.service.ItemPhotoMappingService;
import com.cmp.res.service.ItemService;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.service.PhotoCategoryRelService;
import com.cmp.res.service.PhotoService;
import com.cmp.res.service.PicAnnotationService;
import com.cmp.res.service.StandardBookMappingService;
import com.cmp.res.service.StandardItemMappingService;
import com.cmp.res.service.StandardService;
import com.cmp.res.service.UserService;
import com.cmp.res.util.Dom4jUtils;
import com.cmp.res.util.Im4javaUtil;
import com.cmp.res.util.ImpUtils;
import com.cmp.res.util.Log4jUtils;
import com.cmp.res.util.image.ImageUtils;
import com.cmp.res.util.image.Positions;
import com.cmp.res.util.image.Watermark;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/test")
public class TestFormController {

	public static Logger logger = LoggerFactory.getLogger(TestFormController.class);


	@Autowired
	private BookService bookService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PhotoService photoService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private IndexentryService indexentryService;
	@Autowired
	private BibliographyService bibliographyService;
	@Autowired
	private ItemIndexRelService itemIndexRelService;
	@Autowired
	private PhotoCategoryRelService photoCategoryRelService;
	@Autowired
	private StandardService standardService;
	@Autowired
	private StandardBookMappingService standardBookMappingService;
	@Autowired
	private StandardItemMappingService standardItemMappingService;
	@Autowired
	private ItemPhotoMappingService itemPhotoMappingService;
	@Autowired
	private PicAnnotationService picAnnotationService;
	@Autowired
	private InlinePicService inlinePicService;
	@Autowired
	private ItemCategoryMappingService itemCategoryMappingService;



	private POIFSFileSystem fs;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFRow row;

	@RequestMapping("excel")
	public void importExcel(@RequestParam(value = "catid") Long catid,
			@RequestParam(value = "path") String path) throws Exception{
		 File file=new File(path);
		 if(file.exists()){
			/* InputStream is = new FileInputStream(file);
			 String[] title = this.readExcelTitle(is);*/
			 Long first=0l,second=0l,third=0l;


			 InputStream is2 = new FileInputStream(file);
             Map<Integer, List<String>> map = this.readExcelContent(is2);
             System.out.println("获得Excel表格的内容:");
             for (int i = 1; i <= map.size(); i++) {
                List<String> list=map.get(i);
                String firstStr=list.get(0);
                if(StringUtils.isNoneBlank(firstStr)){
                	Category cat=new Category();
                	cat.setCreateTime(new Date());
                	cat.setName(firstStr);
                	cat.setParentid(catid);
                	cat.setResourcesType(0);
                	cat.setType(0);
                	categoryService.saveCategory(cat);

                	first=cat.getId();
                }
                String secondStr=list.get(1);
                if(StringUtils.isNoneBlank(secondStr)){
                	Category cat=new Category();
                	cat.setCreateTime(new Date());
                	cat.setName(secondStr);
                	cat.setParentid(first);
                	cat.setResourcesType(0);
                	cat.setType(0);
                	categoryService.saveCategory(cat);

                	second=cat.getId();
                }
                String thirdStr=list.get(2);
                if(StringUtils.isNoneBlank(thirdStr)){
                	Category cat=new Category();
                	cat.setCreateTime(new Date());
                	cat.setName(thirdStr);
                	cat.setParentid(second);
                	cat.setResourcesType(0);
                	cat.setType(0);
                	categoryService.saveCategory(cat);
                }


             }

		 }




	}

	public Map<Integer, List<String>> readExcelContent(InputStream is) {
        Map<Integer, List<String>> content = new HashMap<Integer, List<String>>();
        List<String> str = Lists.newArrayList();
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                // str += getStringCellValue(row.getCell((short) j)).trim() +
                // "-";
                //str.add(getCellFormatValue(row.getCell((short) j)).trim());
                str.add(getStringCellValue(row.getCell((short) j)).trim());
                j++;
            }
            content.put(i, str);
            str =Lists.newArrayList(); ;
        }
        return content;
    }


	public String[] readExcelTitle(InputStream is) {
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getStringCellValue(row.getCell((short) i));


          //  title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }

	private String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {/*
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式

                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();

                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);

                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        */
        	try{
        	cellvalue = cell.getStringCellValue();
        	}catch (Exception e) {
				// TODO: handle exception
        		cellvalue="";
			}
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }


	/**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
        	DecimalFormat df = new DecimalFormat("0");

            strCell =df.format(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }

        return strCell;
    }

	/**
	 * 处理章标题上带参考文献的的记录
	 */
	@RequestMapping("resoveChapter")
	public void resoveChapter(@RequestParam(value = "path") Long bookId
			){
		List<Item> chapters= itemService.findChaper(bookId);
		for(Item chapter:chapters){
			if(chapter.getId()==27l){
				System.out.println(1);
			}
			if(chapter.getTitle().contains("<sup name=")&&StringUtils.isNoneBlank(chapter.getBiblioIds())){
				List<BibliographyContent> bis=parse(chapter.getBiblioIds());
				TreeMap<String,BibliographyContent> maps=Maps.newTreeMap();
				for(BibliographyContent bi:bis){
					maps.put(bi.getId(), bi);
				}
				//处理章节标题
				String title=chapter.getTitle();
				String reg = "<sup name=(.*?)>\\[引用\\]</sup>";
			    Matcher m = Pattern.compile(reg).matcher(title);
			    List<BibliographyContent> childbis=Lists.newArrayList();
			    StringBuilder chapterLinkeds=new StringBuilder("");
			    TreeMap<String,String> childrenLinks=Maps.newTreeMap();
			    while (m.find()) {
		            String r = m.group(1);
		            childrenLinks.put(r, "");
		            chapterLinkeds.append("<sup name="+r+">[引用]</sup>");
		            childbis.add(maps.get(r));
		            //判断章节正文中是否存在该参考文献的引用
		            String content=chapter.getContent();
		            if(!(StringUtils.isNotBlank(content)&&content.indexOf("<sup name="+r+">[引用]</sup>")>-1)){
		            	//如果章节正文中没有引用直接删除
		            	maps.remove(r);
		            }

		        }
			    chapter.setTitle(title.substring(0, title.indexOf("<sup name=")));
			    if(maps.size()>0){
			    	bis=Lists.newArrayList();
			    	Iterator it=maps.keySet().iterator();
			    	while (it.hasNext()) {
			    		bis.add(maps.get(it.next()));
					}

			    }else{
			    	bis=Lists.newArrayList();
			    }
			    chapter.setBiblioIds(JsonMapper.nonEmptyMapper().toJson(bis));
			    itemService.saveItem(chapter);

			    if(childbis.size()>0){
			    	//查找该章下的条目
			    	String linekd=chapter.getLinkend();

			    	List<Item> sects=itemService.findSect(bookId, "sect"+linekd.substring(7,linekd.length())+".%");

			    	for(Item sect:sects){
			    		List<BibliographyContent> sectBis=parse(sect.getBiblioIds());
			    		sectBis.addAll(childbis);
			    		TreeMap<String,BibliographyContent> sectMaps=Maps.newTreeMap();
			    		for(BibliographyContent bi:sectBis){
			    			sectMaps.put(bi.getId(), bi);
			    		}
			    		String sectTitle=sect.getTitle();
			    		if(sectTitle.indexOf("<sup name=")>-1){

			    			 m = Pattern.compile(reg).matcher(sectTitle);
			    			 while (m.find()) {
			 		            String r = m.group(1);
			 		           childrenLinks.put(r, "");
			 		        }
			    			 chapterLinkeds.delete(0, chapterLinkeds.length());
			    			 Iterator<String> it=childrenLinks.keySet().iterator();
			    			 while(it.hasNext()){
			    				 chapterLinkeds.append("<sup name="+it.next()+">[引用]</sup>");
			    			 }



			    		sectTitle=sectTitle.substring(0,sectTitle.indexOf("<sup name="))+chapterLinkeds.toString();
			    		}else{
			    			sectTitle+=chapterLinkeds.toString();
			    		}
			    		sect.setTitle(sectTitle);
			    		Iterator it=sectMaps.keySet().iterator();
			    		sectBis=Lists.newArrayList();
				    	while (it.hasNext()) {
				    		sectBis.add(sectMaps.get(it.next()));
						}

				    	sect.setBiblioIds(JsonMapper.nonEmptyMapper().toJson(sectBis));

				    	itemService.saveItem(sect);

			    	}

			    }





			}
		}
	}
	public static void main(String[] args) {
		String sectTitle="锻造加热技术<sup name=a13.[5]>[引用]</sup><sup name=a13.[6]>[引用]</sup><sup name=a13.[7]>[引用]</sup>";
		sectTitle=sectTitle.substring(0,sectTitle.indexOf("<sup name="))+"<sup name=a13.[4]>[引用]</sup>"+sectTitle.substring(sectTitle.indexOf("<sup name="),sectTitle.length());


		System.out.println(sectTitle);
	}



	public static List<BibliographyContent> parse(String json){
		JSONObject ja=JSONObject.fromObject("{roleList:"+json+"}");
		JSONArray jsonDeptArray=ja.getJSONArray("roleList");
		return (List<BibliographyContent>) JSONArray.toCollection(jsonDeptArray,BibliographyContent.class);
	}


	@RequestMapping("bookImp")
	public void bookImp(@RequestParam(value = "path") String path,
			@RequestParam(value = "cid") Long cid,
			@RequestParam(value = "type",required=false) String type,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Date d1=new Date();
		File xml=new File(path);
		if(!xml.exists()){
			logger.info("文件["+path+"]不存在！");
			return;
		}
		Document doc=Dom4jUtils.getDocument(xml);
		Element root=doc.getRootElement();
		Book book=new Book();
		book.setTitle(root.elementText("title"));
		Element info=root.element("info");
		book.setKeywords(Dom4jUtils.getValuesByElements(info.element("keywordset").elements(), ";"));
		book.setPageNums(Integer.parseInt(info.elementText("pagenums")));
		book.setPubDateStr(info.elementText("pubdate"));
		book.setPublisherName(info.element("publisher").elementText("publishername"));
		book.setPublisherAddress(info.element("publisher").elementText("address"));
		//Element part=root.element("part");
		//解析索引词
		bookService.parseIndexEntry(root.element("Index"), book);
		List<Element> parts=root.elements("part");
		for(Element part:parts){
			//优先解析参考文献

			//解析参考文献
			List<Element> list=part.elements("bibliography");
			if(list!=null&&list.size()>0){
				for(Element t:list){
					bookService.parseBibliography(t, book,xml);
				}
			}

			try {
//				List<Element> list=part.elements("chapter");
//				if(list!=null&&list.size()>0){
//					for(Element temp:list){
//						//解析参考文献
//						list=temp.elements("bibliography");
//						if(list!=null&&list.size()>0){
//							for(Element t:list){
//								bookService.parseBibliography(t, book);
//							}
//						}
//					}
//				}
			} catch (Exception e) {
				logger.error("解析参考文献时发生异常",e);
			}
			if(StringUtils.isBlank(type)){
				book.getItems().add(bookService.getItemByXml(part,xml,book,false,null,null));
			}else{
				book.getItems().add(bookService.getItemByXml2(part,xml,book,false,null,null));
			}

		}

		bookService.saveBook(book);
		logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
				"admin", 5, book.getId(), "admin") ,
				Log4jDBAppender.EXTENT_MSG_DIVIDER,"入库图书：【"+book.getId()+","+book.getTitle()+"】");
		//保存索引词
		for(Indexentry index:book.getIndexs()){
			index.setBookid(book.getId());
			indexentryService.saveIndexentry(index);
			logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
					"admin", 6, index.getId(), "admin") ,
					Log4jDBAppender.EXTENT_MSG_DIVIDER,"入库索引词：【"+index.getName()+"】");
		}
		//保存参考文献
		for(Bibliography biblio:book.getBiblios().values()){
			//判断参考文献中是否有图片
			if(biblio.getPhotos().size()>0){
				for(Photo photo:biblio.getPhotos()){
						try{
							File newImage=new File(configService.getPhotoStorePath()+File.separator+photo.getTempCode()+".jpg");
							FileUtils.copyFile(photo.getImageFile(), newImage);
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							continue;
						}
						photo.setType(1);
						photo.setRole("标题图");
						photo.setPath(photo.getTempCode()+".jpg");
						photo.setBookid(book.getId());
						photo.setCreaterNickName("超级管理员");
						photo.setCreaterUserName("admin");
						photo.setCreateTime(new Date());
						photoService.savePhoto(photo);


						biblio.setTitle(biblio.getTitle().replace(photo.getTempCode(), photo.getId().toString()));

				}
			}

			biblio.setBookid(book.getId());
			bibliographyService.saveBibliography(biblio);
			logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
					"admin", 7, biblio.getId(), "admin") ,
					Log4jDBAppender.EXTENT_MSG_DIVIDER,"入库参考文献：【"+biblio.getTitle().replaceAll("'", "\"")+"】");
		}

		//保存标准
		Map<String,Object> stands=book.getStandards();
		Map<String,Standard> temp=Maps.newHashMap();
		Iterator<String> it=stands.keySet().iterator();
		while(it.hasNext()){
			String title=it.next();
			Standard stand=standardService.findByTitle(title);
			if(stand==null){
				stand=new Standard(title,0,"admin","超级管理员");
				standardService.save(stand);
				logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
						"admin", 8, stand.getId(), "admin") ,
						Log4jDBAppender.EXTENT_MSG_DIVIDER,"入库标准：【"+stand.getTitle()+"】");
			}
			temp.put(title, stand);
			//保存图书标准关联
			StandardBookMapping sbm=new StandardBookMapping();
			sbm.setTitle(title);
			sbm.setStandardId(stand.getId());
			sbm.setBookId(book.getId());
			standardBookMappingService.save(sbm);
		}

//		for(Standard stand:book.getStandards()){
//			//
//			stand.setBookId(book.getId());
//			stand.setCreateDate(new Date());
//		}

		for(Item item:book.getItems()){
			item.setBookid(book.getId());
			//item.setParentid(item.getId());
			saveItem(item, book.getId(),cid,temp);
		}

		this.resoveChapter(book.getId());

		System.out.println(new Date().getTime()-d1.getTime());

	}

	public void saveItem(Item item,Long bookid,Long cid,Map<String,Standard> stands){
		Category cat=getCategoryByItem(item, cid);
		item.setCatid(cat.getId());
		item.setCat(cat);
		/**废弃
		try {
			//获取item的参考文献ids
			StringBuilder sb=new StringBuilder();
			for(Bibliography biblio:item.getBiblios()){
				sb.append(biblio.getId()).append(";");
			}
			if(sb.toString().length()>0){
				item.setBiblioIds(sb.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
		Long catid=cat.getId();
		//保持item之前保存图片
		savePhotos(item, bookid,cat);

		itemService.saveItem(item);

		logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
				"admin", 8, item.getId(), "admin") ,
				Log4jDBAppender.EXTENT_MSG_DIVIDER,"入库条目：【"+item.getTitle()+"】");

		/**
		 * 保存条目分类关联关系
		 */
		while(cat!=null){
			itemCategoryMappingService.save(new ItemCategoryMapping(item,cat.getId()));
			cat=cat.getParentid()!=null?categoryService.findById(cat.getParentid()):null;

		}
		//itemPhotoMappingService
		for(Photo photo:item.getPhotos()){
			ItemPhotoMapping ipm=new ItemPhotoMapping();
			ipm.setItemid(item.getId());
			ipm.setPhtotoid(photo.getId());
			itemPhotoMappingService.save(ipm);
		}
		//保存索引词关系
		saveItemIndex(item);
		for(Item i:item.getItems()){
			i.setBookid(bookid);
			i.setParentid(item.getId());
			saveItem(i, bookid,catid,stands);
		}
		//保存标准条目管理
		Iterator<String> it=item.getStands().keySet().iterator();
		while(it.hasNext()){
			String str=it.next();
			StandardItemMapping sim=new StandardItemMapping();
			sim.setItemId(item.getId());
			sim.setStandardId(stands.get(str).getId());
			sim.setTittle(str);

			standardItemMappingService.save(sim);
		}


	}
	/**
	 * 保存词条和索引词关系
	 * @param item
	 */
	public void saveItemIndex(Item item){
		for(Indexentry index:item.getIndexs()){
			ItemIndexRel rel=new ItemIndexRel();
			rel.setEtitle(item.getEtitle());
			rel.setIndexid(index.getId());
			rel.setIndexName(index.getName());
			rel.setItemid(item.getId());
			rel.setTitle(item.getTitle());
			itemIndexRelService.saveItemIndexRel(rel);
		}
	}





	public Category getCategoryByItem(Item item,Long pid){

		Category cat=categoryService.findByParentidAndNameResourcesType(pid, item.getCatName(),0);
		if(cat!=null){
			return cat;
		}
		cat=new Category();
		cat.setCreateTime(new Date());
		cat.setModifyTime(cat.getCreateTime());
		cat.setName(item.getCatName());
		cat.setParentid(pid);
		cat.setType(0);
		cat.setResourcesType(0);

		categoryService.saveCategory(cat);
		return cat;

	}

	public void savePhotos(Item item,Long bookid,Category cat){

		for(Photo photo:item.getPhotos()){

			//保存图片标题中的图片
			for(Photo p:photo.getTitlePhoto()){
				try{
					File newImage=new File(configService.getPhotoStorePath()+File.separator+p.getTempCode()+".jpg");
					FileUtils.copyFile(p.getImageFile(), newImage);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;
				}
				p.setType(1);
				p.setRole("标题图");
				p.setPath(p.getTempCode()+".jpg");
				p.setBookid(bookid);
				p.setCreaterNickName("超级管理员");
				p.setCreaterUserName("admin");
				p.setCreateTime(new Date());
				photoService.savePhoto(p);

				//photo.setTitle(photo.getTitle().replace(p.getTempCode(), p.getId().toString()));

				photo.setTitleabbrev(photo.getTitleabbrev().replace(p.getTempCode(), p.getId().toString()));

			}

			File newImage=new File(configService.getPhotoStorePath()+File.separator+photo.getTempCode()+".jpg");
			try {
				//原图
				FileUtils.copyFile(photo.getImageFile(), newImage);
				if("表格".equals(photo.getRole())||"线条图".equals(photo.getRole())){
					//大图+水印
				//	Im4javaUtil.waterMark(configService.getWatermarkater(), newImage.getAbsolutePath(), configService.getBighotoStorePath()+File.separator+photo.getTempCode()+".jpg", "northwest", 50);

					BufferedImage watermarkImage = ImageIO.read(new File(configService.getWatermarkater()));
					Watermark watermark = new Watermark(Positions.TOP_LEFT,
							watermarkImage, 0.3f);

					ImageUtils.fromFile(newImage)
					.scale(1)
					.watermark(watermark)
					.toFile(new File(configService.getBighotoStorePath()+File.separator+photo.getTempCode()+".jpg"));
					/**
					 * 按比例抽图
					 *
					 */
					Im4javaUtil.equalScaling(newImage.getAbsolutePath(), configService.getMiddleStorePath()+File.separator+photo.getTempCode()+".jpg",50,50);
					Im4javaUtil.equalScaling(newImage.getAbsolutePath(), configService.getSimphotoStorePath()+File.separator+photo.getTempCode()+".jpg",35,35);

					//抽图-》中图
					//Im4javaUtil.equalScaling(newImage.getAbsolutePath(), configService.getMiddleStorePath()+File.separator+photo.getTempCode()+".jpg",configService.getMiddlePicSize());
					//抽图-》小图
					//Im4javaUtil.equalScaling(newImage.getAbsolutePath(), configService.getSimphotoStorePath()+File.separator+photo.getTempCode()+".jpg",configService.getSimPicSize());

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			photo.setPath(photo.getTempCode()+".jpg");
			photo.setBookid(bookid);
			photo.setCreaterNickName("超级管理员");
			photo.setCreaterUserName("admin");
			photo.setCreateTime(new Date());
			photoService.savePhoto(photo);

			logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
					"admin", 4, photo.getId(), "admin") ,
					Log4jDBAppender.EXTENT_MSG_DIVIDER,"入库图片：【"+photo.getTitle()+"】");
			//保存图注
			this.savePicAnnotation(photo);

			if(item.getContent()!=null){
				item.setContent(item.getContent().replace(photo.getTempCode(), photo.getId().toString()));
			}
			if(StringUtils.isNotBlank(item.getTitle())){
				item.setTitle(item.getTitle().replace(photo.getTempCode(), photo.getId().toString()));
			}
			if("表格".equals(photo.getRole())||"线条图".equals(photo.getRole()))
			savePhotoCategory(photo, cat);
		}
	}
	/**
	 * 保存图注
	 * @param photo
	 */
	public void savePicAnnotation(Photo photo){
		for(PicAnnotation pa:photo.getPicAnnotation()){
			pa.setPhotoId(photo.getId());

			picAnnotationService.save(pa);

			this.svaeInlinePic(pa);

		}
	}
	/**
	 * 保存图片
	 * @param pa
	 */
	public void svaeInlinePic(PicAnnotation pa){
		//String fileName=ImpUtils.getResourceCode();
		for(InlinePic ip:pa.getIps()){
			ip.setObjectId(pa.getId());
			try {
				ip.setPath(ip.getTempCode()+".jpg");
				FileUtils.copyFile(ip.getFile(), new File(configService.getPhotoStorePath()+File.separator+ip.getPath()));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			inlinePicService.save(ip);
			pa.setContent(pa.getContent().replace(ip.getTempCode(), ip.getId()+""));
			picAnnotationService.save(pa);


		}

	}





	public void savePhotoCategory(Photo photo,Category cat){
		PhotoCategoryRel rel=new PhotoCategoryRel();
		rel.setBookTitle(photo.getBookTitle());
		rel.setCatid(cat.getId());
		rel.setPhotoid(photo.getId());
		rel.setRole(photo.getRole());
		rel.setTitle(photo.getTitleabbrev());
		rel.setCreateTime(photo.getCreateTime());
		rel.setCeaterUser(photo.getCreaterUserName());
		rel.setCreaterNickName(photo.getCreaterUserName());
		photoCategoryRelService.savePhotoCategoryRel(rel);
		if(cat.getParentid()!=null){
			cat=categoryService.findById(cat.getParentid());
			if(cat!=null){
				savePhotoCategory(photo, cat);
			}

		}
	}





}
