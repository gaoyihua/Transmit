package com.gary.file.core;

import com.gary.file.exception.ReceiveFileIdDefinedException;
import com.gary.file.model.ReceiveFileModel;
import com.gary.file.model.SendFileModel;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author gary
 * @date 2018/12/26
 */
public class TestSender {
    public static void main(String[] args) {
        String absolutePath = "D:\\test\\";
        String filePath = "demo.mp4";
        int length = 294393759;
        try {
            ReceiveFileSet fileSet = new ReceiveFileSet();
            ReceiveFileModel file = new ReceiveFileModel(filePath, absolutePath, length);
            fileSet.addReceiveFile(1, file);

            List<SendFileModel> fileList = new ArrayList<>();
            long offset = 0;
            while (length > 0) {
                int len = length > ResourceSender.BUFFER_SIZE ? ResourceSender.BUFFER_SIZE : length;
                SendFileModel sendFile = new SendFileModel()
                            .setAbsoluteRootPath(absolutePath)
                            .setFilePath(filePath)
                            .setOffSet(offset)
                        .setLength(len);
                fileList.add(sendFile);
                length -= len;
                offset += len;
            }

            ResourceSenderCenter senderCenter =
                    new ResourceSenderCenter("127.0.0.1", 54190);
            senderCenter.setSendFileSet(fileSet);
            senderCenter.setSendFileList(fileList);
            senderCenter.startSending();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
