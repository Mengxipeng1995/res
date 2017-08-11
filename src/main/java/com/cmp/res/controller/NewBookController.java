package com.cmp.res.controller;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;

import com.cmp.res.entity.Attachment;
import com.cmp.res.entity.BookCategoryMapping;
import com.cmp.res.service.*;
import com.cmp.res.util.ZipUtil;
import com.cmp.res.util.image.ImageZip;
import com.google.common.collect.Lists;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.mapper.JsonMapper;

import com.cmp.res.entity.JinShuBook;
import com.cmp.res.entity.NewBook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2017年7月14日
 *
 * @liao res
 * NewBookController.java
 **/
@Controller
@RequestMapping("newbook")
public class NewBookController {

    @Autowired
    private NewBookService newBookService;

    @Autowired
    private TestFormController testFormController;

    @Autowired
    private JinshuwBookService jinshuwBookService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private BookCategoryMappingService bookCategoryMappingService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private BookService bookService;


    @RequestMapping("bookdetails")
    public ModelAndView book(@RequestParam(value = "id") Long id) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("id", id);
        mv.setViewName("book");
        return mv;
    }


    @ResponseBody
    @RequestMapping("details")
    public ModelAndView details(@RequestParam(value = "id") Long id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        System.out.println(id);
        NewBook book = newBookService.findById(id);
        Iterable<Attachment> attachmen = attachmentService.findAllByResouceId(id);
        List<Attachment> attachmenlist = Lists.newArrayList(attachmen);
        System.out.println(attachmenlist);
        System.out.println(attachmen);
        String mulu = book.getCatalog();
        String cata= null;
        if(mulu != null) {
           cata = mulu.replace(" ", "<br/>");
        }
        mv.addObject("cata", cata);
        mv.addObject("book", book);
        mv.addObject("attachmen", attachmenlist);
        mv.setViewName("bookdeta");

        //request.setAttribute("book",book);
        return mv;
    }

    //下载pdf
    @RequestMapping("/uploadPDF")
    public void displayPDF(HttpServletResponse response, @RequestParam(value = "path") String filepath) {
        try {
            //修改路径的/在linx环境此处注释掉
            //String newpdf = filepath.replace("\\", "/");
            //获取系统时间
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String date = format.format(new Date());
            //生成6位随机数
            String temp = "";
            for (int i = 0; i < 6; i++) {
                int rom = (int) (Math.random() * 10);
                temp += rom;
            }
            //生成下载文件名
            String uploadpdf = temp + date;
            //获取后缀名
            String suffix = filepath.substring(filepath.lastIndexOf(".") + 1);

            System.out.println(filepath);
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment;fileName=" + uploadpdf + "." + suffix);
            response.setContentType("multipart/form-data");
            OutputStream outputStream = response.getOutputStream();
            IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载IMAGE
    @RequestMapping(value = "/downloadZip")
    public String downloadFiles(@RequestParam(value = "imagefile") String imagefile, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //获取下载路径
        String imagepath = imagefile.substring(0, imagefile.lastIndexOf("/"));
        //获取下载文件名
        String imagename = imagefile.substring( imagefile.lastIndexOf("/")+1);
        //处理文件名获取isbn进行匹配
        String Isbn = imagename.split("_")[0];
        System.out.println(Isbn);

        List<File> files = new ArrayList<>();
        File Allfile = new File(imagepath);
        if (Allfile.exists()) {
            File[] fileArr = Allfile.listFiles();
            for (File file2 : fileArr) {
                files.add(file2);
            }
        }

        String fileName = UUID.randomUUID().toString() + ".zip";
        // 在服务器端创建打包下载的临时文件
        String outFilePath = request.getSession().getServletContext().getRealPath("/") + "upload/";

        File fileZip = new File(outFilePath + fileName);
        // 文件输出流
        FileOutputStream outStream = new FileOutputStream(fileZip);
        // 压缩流
        ZipOutputStream toClient = new ZipOutputStream(outStream);
        //  toClient.setEncoding("gbk");
        ImageZip.zipFile(files, toClient,Isbn);
        toClient.close();
        outStream.close();
        this.downloadFile(fileZip, response, true);
        return null;
    }

    //文件下载
    public void downloadFile(File file, HttpServletResponse response, boolean isDelete) {
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            if (isDelete) {
                file.delete();        //是否将生成的服务器端文件删除
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping("getbooks")
    public void getbooks(@RequestParam(value = "bookid")Long id,HttpServletResponse response){
        NewBook books = newBookService.findById(id);
        commonService.returnDate(response,books);
    }


    //上传附件
    @RequestMapping("uploadfiles")
    public String uploadfiles(HttpServletResponse response,
                            HttpServletRequest request,
                            MultipartFile filepath,
                              @RequestParam(value = "bookid") String id){
        String serviceRoot=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        //File file  = new File(filepath);
        String filename = filepath.getOriginalFilename();
        System.out.println(filepath);
        //"/home/trs/soft/books/"+filename
        //"/home/trs/soft/"+filename+"/"
        //"/home/trs/soft/"+filename+"/"

        //上传的位置
        File file = new File("/home/trs/soft/upload/"+filename);
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            //上传文件
            filepath.transferTo(file);
            //根据上传到文件的位置解压到什么位置
            ZipUtil.unZipFiles(file,"/home/trs/soft/upload/");
            //根据解压位置进行移动复制文件
            bookService.uploadbook("/home/trs/soft/upload/");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(id);
        return "redirect:"+serviceRoot+"newbook/details?id="+id;



    }





    @RequestMapping("outline")
    public void outline(
            @RequestParam(value = "pn", required = false, defaultValue = "1") int pn,
            @RequestParam(value = "ps", required = false, defaultValue = "20") int ps,
            HttpServletResponse response) throws IOException {

        commonService.returnDate(response, newBookService.outline(pn, ps));

    }


    @RequestMapping("compare")
    public void compare() throws Exception {
        Iterable<NewBook> iterableNewBooks = newBookService.findAll();
        Iterator<NewBook> books = iterableNewBooks.iterator();
        Field[] fields = NewBook.class.getDeclaredFields();

        while (books.hasNext()) {
            NewBook book = books.next();

            JinShuBook jinshuBook = jinshuwBookService.findByBarcode(book.getBarcode());

            if (jinshuBook != null) {

                for (Field field : fields) {
                    String setMethodName = "set" + captureName(field.getName());
                    String getMethodName = "get" + captureName(field.getName());
                    Class type = field.getType();
                    Method setMethod = NewBook.class.getDeclaredMethod(setMethodName, type);
                    Method getMethod = JinShuBook.class.getDeclaredMethod(getMethodName);
                    Object obj = getMethod.invoke(jinshuBook);
                    if (obj != null) {


                        setMethod.invoke(book, obj);
                    }


                }

                newBookService.save(book);
            }

        }

    }


    @RequestMapping("import123")
    public void import123(@RequestParam(value = "pathbook") String pathbook) throws Exception {
        //String path = pathbook.replace("/","");
        File file = new File(pathbook);
        InputStream is = new FileInputStream(file);
        Map<Integer, List<String>> map = testFormController.readExcelContent(is);
        Field[] fields = NewBook.class.getDeclaredFields();
        // int fieldsSize=fields.length;
        for (int i = 1; i <= map.size(); i++) {
            List<String> list = map.get(i);
            NewBook book = new NewBook();
            for (int j = 0; j < 56; j++) {
                Field field = fields[j];
                Class type = field.getType();
                String value = list.get(j);
                if (StringUtils.isNotBlank(value)) {
                    String methodName = "set" + captureName(field.getName());
                    Method method = NewBook.class.getDeclaredMethod(methodName, type);
                    if (String.class == type) {
                        method.invoke(book, value.trim());
                    } else if (Float.class == type) {
                        try {
                            method.invoke(book, Float.parseFloat(value.trim()));
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    } else if (Integer.class == type) {
                        try {
                            method.invoke(book, Integer.parseInt(value.trim()));
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }


            }
            Long catid = 1L;
            BookCategoryMapping bcm = new BookCategoryMapping();


            //根据isbn号去重
            if (book.getIsbn() != null) {
                if (newBookService.findByIsbn(book.getIsbn()) == null) {
                    System.out.println(JsonMapper.nonDefaultMapper().toJson(book));
                    System.out.print(book);
                    newBookService.save(book);
                    if (book.getId() != null) {
                        bcm.setBookid(book.getId());
                    }
                    if (book.getTitle() != null) {
                        bcm.setTitle(book.getTitle());
                    }
                    if (book.getPageSize() != null) {
                        bcm.setPageNums(book.getPageSize());
                    }
                    if (book.getIsbn() != null) {
                        bcm.setPublisherAddress(book.getIsbn());
                    }
                    if (book.getPublish() != null) {
                        bcm.setPublisherName(book.getPublish());
                    }
                    if (book.getKeywords() != null) {
                        bcm.setKeywords(book.getKeywords());
                    }
                    if (book.getRevision() != null) {
                        bcm.setPubDateStr(book.getRevision());
                    }
                    bcm.setCatid(catid);
                    // \bcm.setPubDateStr(book.getShelvesDate().toString());

                    bookCategoryMappingService.save(bcm);
                } else {
                    System.out.println("数据重复不进行插入");
                }
            }




            System.out.println("==========================>" + book.getShelvesDate());
            System.out.println("==========================>" + book.getId());


        }

    }

    @RequestMapping("importjinshu")
    public void importJinshu(@RequestParam(value = "jinshupath") String jinshupath) throws Exception {
        //	String path =jinshupath.replace("/","\\");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        Date d = null;
        File file = new File(jinshupath);
        InputStream is = new FileInputStream(file);
        Map<Integer, List<String>> map = testFormController.readExcelContent(is);
        Field[] fields = JinShuBook.class.getDeclaredFields();
        // int fieldsSize=fields.length;
        for (int i = 1; i <= map.size(); i++) {
            List<String> list = map.get(i);
            JinShuBook book = new JinShuBook();
            for (int j = 0; j < 69; j++) {
                Field field = fields[j];
                Class type = field.getType();
                String value = list.get(j);
                if (StringUtils.isNotBlank(value)) {
                    String methodName = "set" + captureName(field.getName());
                    Method method = JinShuBook.class.getDeclaredMethod(methodName, type);
                    if (String.class == type) {
                        method.invoke(book, value.trim());
                    } else if (Float.class == type) {
                        try {
                            method.invoke(book, Float.parseFloat(value.trim()));
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    } else if (Integer.class == type) {
                        try {
                            method.invoke(book, Integer.parseInt(value.trim()));
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    } else if (Date.class == type) {
                        d = sdf.parse(value.trim());
                        method.invoke(book, d);
                    }
                }

            }


            System.out.println(JsonMapper.nonDefaultMapper().toJson(book));
            jinshuwBookService.save(book);


        }


    }

    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);

    }

    public static void main(String[] args) {
        Field[] fields = JinShuBook.class.getDeclaredFields();

        System.out.println(fields.length);

    }

}
