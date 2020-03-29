package com.springboot.chatroom.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * 文件工具类
 * 1. 通过文件头判断文件类型
 * 2. 获取格式化的文件大小
 * 3. 高效的将文件转换为字节数组
 *
 */
@Slf4j
public class FileUtils {
    private static final HashMap<String,String> fileTypes=new HashMap<>();
    static { // BOM（Byte Order Mark）文件头字节
        fileTypes.put("494433", "mp3");
        fileTypes.put("524946", "wav");
        fileTypes.put("ffd8ff", "jpg");
        fileTypes.put("FFD8FF", "jpg");
        fileTypes.put("89504E", "png");
        fileTypes.put("89504e", "png");
        fileTypes.put("474946", "gif");
    }

    private static final String B_UNIT="B";
    private static final String KB_UNIT="KB";
    private static final String MB_UNIT="MB";
    private static final String GB_UNIT="GB";
    private static final DecimalFormat decimalFormat=new DecimalFormat("#.0");

    public static String getFileType(String filePath){
        return fileTypes.get(getFileHeader3Bit(filePath));
    }

    public static String getFormattSize(long size){
        return getFormatSize((double) size);
    }

    /**
     * 获取文件头前3个字节
     * @param filePath
     * @return
     */
    private static String getFileHeader3Bit(String filePath){
        File file = new File(filePath);
        if (!file.exists()||file.length()<4){
            return "null";
        }
        FileInputStream is=null;
        String value=null;
        try {
            is=new FileInputStream(file);
            byte[] bytes = new byte[3];
            is.read(bytes,0,bytes.length);
            value = DigestUtils.md5DigestAsHex(bytes);
        }catch (Exception e){
        }finally {
            if (null!=is){
                try {
                    is.close();
                }catch (IOException e){}
            }
        }
        return value;
    }

    /**
     * 获取格式化的文件大小，格式为带单位保留一位小数
     */
    public static String getFormatSize(double size){
        String fileSizeString ="";
        if (size<1024) {
            fileSizeString = decimalFormat.format(size) + B_UNIT;
        }else if (size<1048576){
            fileSizeString = decimalFormat.format(size/1024)+KB_UNIT;
        }else if (size<1073741824){
            fileSizeString = decimalFormat.format(size/1048576)+MB_UNIT;
        }else {
            fileSizeString = decimalFormat.format(size/1073741824)+GB_UNIT;
        }
        return fileSizeString;
    }

    /**
     * 将文件转为字节数组
     */
    public static byte[] toByteArray(String filePath) throws IOException{
        FileChannel fc=null;
        try {
            fc = new RandomAccessFile(filePath,"r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            log.info(byteBuffer.isLoaded()+"");
            byte[] bytes = new byte[(int) fc.size()];
            if (byteBuffer.remaining()>0){
                byteBuffer.get(bytes,0,byteBuffer.remaining());
            }
            return bytes;
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            try {
                fc.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

