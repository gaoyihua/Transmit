package com.gary.file.core;

import com.gary.file.model.SendFileModel;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * describe:资源发送中心
 *
 * @author gary
 * @date 2018/12/25
 */
public class ResourceSenderCenter {
    private String receiveIp;
    private int receivePort;
    private Socket socket;
    private ReceiveFileSet sendFileSet;
    private List<SendFileModel> sendFileList;

    public ResourceSenderCenter(String receiveIp, int receivePort) {
        this.receiveIp = receiveIp;
        this.receivePort = receivePort;
    }

    public void startSending() throws Exception{
        if (sendFileSet == null || sendFileList == null) {
            return;
        }
        socket = new Socket(receiveIp, receivePort);
        new ResourceSender(socket, sendFileSet, sendFileList);
    }

    public void setSendFileSet(ReceiveFileSet sendFileSet) {
        this.sendFileSet = sendFileSet;
    }

    public void setSendFileList(List<SendFileModel> sendFileList) {
        this.sendFileList = sendFileList;
    }
}
