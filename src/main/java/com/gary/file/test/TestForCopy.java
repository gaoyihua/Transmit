package com.gary.file.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * describe:简单传输完整文件测试
 *
 * @author gary
 * @date 2018/12/23
 */
public class TestForCopy {
    private static final int bufferSize = 1 << 16;

    public static void main(String[] args) {
        try {
            File source = new File("D:/test/demo.mp4");
            FileInputStream fis = new FileInputStream(source);

            File target = new File("src/main/resources/demo.mp4");
            FileOutputStream fos = new FileOutputStream(target);

            long startTime = System.currentTimeMillis();

            byte[] buffer = new byte[bufferSize];
            long length = source.length();
            long restLen = length;
            int realLen = 0;
            while (restLen > 0) {
                realLen = fis.read(buffer);
                fos.write(buffer, 0, realLen);
                restLen -= realLen;
            }

            long endTime = System.currentTimeMillis();
            System.out.println("耗时：" + (endTime - startTime));

            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
