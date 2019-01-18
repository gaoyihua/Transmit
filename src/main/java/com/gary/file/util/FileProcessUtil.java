package com.gary.file.util;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * describe:FileChannel传输工具类
 *
 * @author gary
 * @date 2018/12/23
 */
public class FileProcessUtil {
    public static final int DEFAULT_BUFFER_SIZE = 1 << 22;

    public static boolean fileCopy(String source, String target) {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            return false;
        }
        File targetFile = new File(target);
        return  fileCopy(sourceFile, targetFile);
    }

    public static boolean fileCopy(File sourceFile, File targetFile) {
        int restSize = (int) sourceFile.length();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fisChannel = null;
        FileChannel fosChannel = null;
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);
            fisChannel = fis.getChannel();
            fosChannel = fos.getChannel();

            fisChannel.transferTo(0, restSize, fosChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }
}
