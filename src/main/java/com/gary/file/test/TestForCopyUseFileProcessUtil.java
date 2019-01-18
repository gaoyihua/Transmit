package com.gary.file.test;

import com.gary.file.util.FileProcessUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * describe:使用FileChannel传输文件测试
 *
 * @author gary
 * @date 2018/12/23
 */
public class TestForCopyUseFileProcessUtil {
    private static final int bufferSize = 1 << 16;

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();

            FileProcessUtil.fileCopy("D:/test/demo.mp4", "src/main/resources/demo.mp4");

            long endTime = System.currentTimeMillis();
            System.out.println("耗时：" + (endTime - startTime));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
