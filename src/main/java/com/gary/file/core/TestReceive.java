package com.gary.file.core;

import com.gary.file.model.ReceiveFileModel;

/**
 * describe:
 *
 * @author gary
 * @date 2018/12/26
 */
public class TestReceive {
    public static void main(String[] args) {
        String absolutePath = "D:\\test\\test\\";
        String filePath = "demo.mp4";
        int length = 294393759;
        try {
            ReceiveFileSet fileSet = new ReceiveFileSet();
            ReceiveFileModel file = new ReceiveFileModel(filePath, absolutePath, length);
            fileSet.addReceiveFile(1, file);

            ResourceReceiverServer rrs = new ResourceReceiverServer(54190);
            rrs.setReceiveFileSet(fileSet);
            rrs.setSenderCount(1);
            rrs.startUpReceiverServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
