package com.cmp.res.util;

import java.io.File;

/**
 * Created by mxp on 2017/8/9.
 */
public class DeleteAll {

    public static void deleteAll(File file){

        if(file.isFile() || file.list().length ==0)
        {
            file.delete();
        }else{
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i]);
                files[i].delete();
            }
            if(file.exists())         //如果文件本身就是目录 ，就要删除目录
                file.delete();
        }
    }
}
