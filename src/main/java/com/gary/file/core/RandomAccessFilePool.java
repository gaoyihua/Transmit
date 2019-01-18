package com.gary.file.core;

import com.gary.util.CloseableUtil;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe:
 *
 * @author gary
 * @date 2019/01/18
 */
public class RandomAccessFilePool {
    private static Map<String, ThreadLocal<RandomAccessFile>> map = new ConcurrentHashMap<>();
    @SuppressWarnings("unchecked")
    public static RandomAccessFile getRandomAccessFile(String filePath){
        if (!map.containsKey(filePath)) {
            ThreadLocal<RandomAccessFile> threadLocal = new ThreadLocal<RandomAccessFile>(){
                @Override
                protected RandomAccessFile initialValue() {
                    RandomAccessFile raf = null;
                    try {
                        raf = new RandomAccessFile(filePath, "rwd");
                        raf.getChannel();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return raf;
                }
            };
            map.put(filePath, threadLocal);
            return  threadLocal.get();
        }
        return map.get(filePath).get();
    }

    public static void clearPool() throws Exception {
        Iterator<Map.Entry<String, ThreadLocal<RandomAccessFile>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ThreadLocal<RandomAccessFile>> entry = iterator.next();
            String key = entry.getKey();
            ThreadLocal<RandomAccessFile> value = entry.getValue();
            RandomAccessFile raf = value.get();
            CloseableUtil.close(raf);
        }
        map.clear();
    }
}
