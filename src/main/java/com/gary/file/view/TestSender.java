package com.gary.file.view;

import com.gary.file.core.transmit.ReceiveFileSet;
import com.gary.file.core.transmit.ResourceSender;
import com.gary.file.core.transmit.ResourceSenderCenter;
import com.gary.file.model.ReceiveFileModel;
import com.gary.file.model.SendFileModel;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author gary
 * @date 2018/12/28
 */
public class TestSender {
    public static void main(String[] args) {
        String absolutePath = "D:\\test\\";
        try {
            String filePath;
            int length;
            ReceiveFileSet fileSet = new ReceiveFileSet();
            ReceiveFileModel file;
            List<SendFileModel> fileList = new ArrayList<>();

            filePath = "demo1.mp4";
            length = 294393759;
            file = new ReceiveFileModel(filePath, absolutePath, length);
            fileSet.addReceiveFile(1, file);

            long offset = 0;
            while (length > 0) {
                int len = length > ResourceSender.BUFFER_SIZE
                        ? ResourceSender.BUFFER_SIZE : length;
                SendFileModel sendFile = new SendFileModel()
                        .setAbsoluteRootPath(absolutePath)
                        .setFilePath(filePath)
                        .setOffSet(offset)
                        .setLength(len);
                fileList.add(sendFile);
                length -= len;
                offset += len;
            }

            filePath = "demo2.mp4";
            length = 294393759;
            file = new ReceiveFileModel(filePath, absolutePath, length);
            fileSet.addReceiveFile(2, file);

            offset = 0;
            while (length > 0) {
                int len = length > ResourceSender.BUFFER_SIZE
                        ? ResourceSender.BUFFER_SIZE : length;
                SendFileModel sendFile = new SendFileModel()
                        .setAbsoluteRootPath(absolutePath)
                        .setFilePath(filePath)
                        .setOffSet(offset)
                        .setLength(len);
                fileList.add(sendFile);
                length -= len;
                offset += len;
            }

            filePath = "demo3.mp4";
            length = 294393759;
            file = new ReceiveFileModel(filePath, absolutePath, length);
            fileSet.addReceiveFile(3, file);

            offset = 0;
            while (length > 0) {
                int len = length > ResourceSender.BUFFER_SIZE
                        ? ResourceSender.BUFFER_SIZE : length;
                SendFileModel sendFile = new SendFileModel()
                        .setAbsoluteRootPath(absolutePath)
                        .setFilePath(filePath)
                        .setOffSet(offset)
                        .setLength(len);
                fileList.add(sendFile);
                length -= len;
                offset += len;
            }

            filePath = "demo4.mp4";
            length = 294393759;
            file = new ReceiveFileModel(filePath, absolutePath, length);
            fileSet.addReceiveFile(4, file);

            offset = 0;
            while (length > 0) {
                int len = length > ResourceSender.BUFFER_SIZE
                        ? ResourceSender.BUFFER_SIZE : length;
                SendFileModel sendFile = new SendFileModel()
                        .setAbsoluteRootPath(absolutePath)
                        .setFilePath(filePath)
                        .setOffSet(offset)
                        .setLength(len);
                fileList.add(sendFile);
                length -= len;
                offset += len;
            }

            filePath = "demo5.mp4";
            file = new ReceiveFileModel(filePath, absolutePath, 294393759);
            fileSet.addReceiveFile(5, file);
            fileList.add(new SendFileModel()
                    .setAbsoluteRootPath(absolutePath)
                    .setFilePath(filePath)
                    .setOffSet(0)
                    .setLength(294393759));

            filePath = "demo6.mp4";
            length = 294393759;
            file = new ReceiveFileModel(filePath, absolutePath, length);
            fileSet.addReceiveFile(6, file);

            offset = 0;
            while (length > 0) {
                int len = length > ResourceSender.BUFFER_SIZE
                        ? ResourceSender.BUFFER_SIZE : length;
                SendFileModel sendFile = new SendFileModel()
                        .setAbsoluteRootPath(absolutePath)
                        .setFilePath(filePath)
                        .setOffSet(offset)
                        .setLength(len);
                fileList.add(sendFile);
                length -= len;
                offset += len;
            }

            ResourceSenderCenter senderCenter = new ResourceSenderCenter(
                    "127.0.0.1", 54190);
            senderCenter.setSendFileSet(fileSet);
            senderCenter.setSendFileList(fileList);
            senderCenter.startSending();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
