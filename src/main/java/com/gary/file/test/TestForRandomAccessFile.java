package com.gary.file.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * describe:指定位置传输测试
 *
 * @author gary
 * @date 2018/12/23
 */
public class TestForRandomAccessFile {
    private static final int bufferSize = 1 << 16;

    public static void main(String[] args) {
        File source = new File("D:/test/demo.mp4");
        File target = new File("src/main/resources/demo.mp4");

        long length = source.length();
        byte[] buffer = new byte[bufferSize];

        try {
            RandomAccessFile rafSource = new RandomAccessFile(source, "r");
            RandomAccessFile rafTarget = new RandomAccessFile(target, "rw");
            rafSource.seek(0);
            rafSource.read(buffer);
            rafTarget.seek(0);
            rafTarget.write(buffer);
            rafSource.close();
            rafTarget.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
