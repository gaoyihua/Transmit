package com.gary.file.core;

import com.gary.file.view.IReceiveProgress;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * describe:资源收发服务器
 *
 * @author gary
 * @date 2018/12/25
 */
public class ResourceReceiverServer implements Runnable {
    private ServerSocket serverSocket;
    private int port;
    private volatile boolean continueWaittingSender;
    private volatile int senderCount;
    private ReceiveFileSet receiveFileSet;
    private ThreadPoolExecutor threadPool;
    private IReceiveProgress receiveProgress;

    public ResourceReceiverServer(int port) {
        this.port = port;
        threadPool = new ThreadPoolExecutor(50, 100, 500, TimeUnit.MICROSECONDS,
                new LinkedBlockingDeque<>());
    }

    public ResourceReceiverServer(int port, IReceiveProgress receiveProgress) {
        threadPool = new ThreadPoolExecutor(50, 100, 500,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>());
        this.receiveProgress = receiveProgress;
        this.port = port;
    }

    public ReceiveFileSet getReceiveFileSet() {
        return receiveFileSet;
    }

    public ResourceReceiverServer setReceiveFileSet(ReceiveFileSet receiveFileSet) {
        this.receiveFileSet = receiveFileSet;
        if (receiveProgress != null) {
            receiveProgress.setSenderPlan(
                    receiveFileSet.getTotalReceiveFileSize(),
                    receiveFileSet.getTotalReveiveBytes());
        }
        return this;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSenderCount(int senderCount) {
        this.senderCount = senderCount;
        if (receiveProgress != null) {
            receiveProgress.setSenderInfo(senderCount);
        }
    }

    public void startUpReceiverServer() throws IOException {
        serverSocket = new ServerSocket(port);
        continueWaittingSender = true;
        Thread thread = new Thread(this, "ResourceReveiverServer");
        synchronized (ResourceReceiverServer.class) {
            thread.start();
            try {
                ResourceReceiverServer.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (receiveProgress != null) {
            receiveProgress.startShowProgress();
        }

    }

    @Override
    public void run() {
        int currentSenderCount = 0;
        synchronized (ResourceReceiverServer.class) {
            ResourceReceiverServer.class.notify();
        }
        if (receiveProgress != null) {
            boolean receiveProgressIsShow = false;
            while (!receiveProgressIsShow) {
                receiveProgressIsShow = ((JDialog) receiveProgress).isActive();
            }
        }
        while (continueWaittingSender && currentSenderCount < senderCount) {
            try {
                Socket sender = serverSocket.accept();
                if (receiveProgress != null) {
                    String senderInfo = sender
                            .getInetAddress()
                            .getHostName();
                    receiveProgress.acceptOneSender(senderInfo);
                }
                new ResourceReceiver(this, sender, threadPool, receiveProgress);
                currentSenderCount ++;
            } catch (IOException e) {
                continueWaittingSender = false;
                //TODO 可能存在网络故障造成无法再继续连接发送端
                //TODO 应该进行断点续传前的资源信息查询和整合, 即对之前已经接收到的资源和还缺失的资源进行整合。
            }
        }
        boolean threadIsAllFinished = threadPool.getActiveCount() <= 0;
        while (!threadIsAllFinished) {
            threadIsAllFinished = threadPool.getActiveCount() <= 0;
        }
        threadPool.shutdown();
        if (receiveProgress != null) {
            receiveProgress.finishedReceive();
        }
        //TODO 完成接收，执行善后工作
        try {
            RandomAccessFilePool.clearPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String unReceivedBlockRecordList = receiveFileSet.getUnReceivedBlockRecordList();
        System.out.println("未接收\n" + unReceivedBlockRecordList);
    }
}
