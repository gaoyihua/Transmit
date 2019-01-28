package com.gary.file.core;

import com.gary.file.model.ReceiveFileModel;
import com.gary.file.model.ResourceBlock;
import com.gary.file.util.ByteAndStringUtil;
import com.gary.file.view.IReceiveProgress;
import com.gary.util.CloseableUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * describe:资源收发者
 *
 * @author gary
 * @date 2018/12/25
 */
public class ResourceReceiver implements Runnable {
    private Socket sender;
    private ResourceReceiverServer receiverServer;
    private ThreadPoolExecutor threadPool;
    private DataInputStream dis;
    private volatile Object lock;
    private IReceiveProgress receiveProgress;
    private static final ThreadLocal<Integer> THREAD_FILE_ID
            = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return -1;
        }
    };

    public ResourceReceiver(ResourceReceiverServer receiverServer,
                            Socket sender, ThreadPoolExecutor threadPool,
                            IReceiveProgress receiveProgress) throws IOException {
        this.sender = sender;
        this.receiverServer = receiverServer;
        this.threadPool = threadPool;
        this.receiveProgress = receiveProgress;
        this.dis = new DataInputStream(sender.getInputStream());

        lock = new Object();
        synchronized (lock) {
            try {
                this.threadPool.execute(this);
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 先接收头再接收真正的资源块
     * @return
     * @throws IOException
     */
    private boolean ReceiveOneBlock() throws IOException {
        byte[] header = receiveBytes(16);
        int fileId = ByteAndStringUtil.getIntAt(header, 0);
        long offset = ByteAndStringUtil.getLongAt(header, 4);
        int length = ByteAndStringUtil.getIntAt(header, 12);

        if (fileId == -1) {
            return true;
        }
        if (receiveProgress != null) {
            int oldFileId = THREAD_FILE_ID.get();
            if (oldFileId != fileId) {
                THREAD_FILE_ID.set(fileId);
                ReceiveFileModel rfm = receiverServer
                        .getReceiveFileSet()
                        .getReceiveFileModel(fileId);
                String fileName = rfm.getFilePath();
                int fileLength = (int) rfm.getLength();
                receiveProgress.receiveNewFile(fileId, fileName, fileLength);
            }
        }

        byte[] buffer = receiveBytes(length);
        ResourceBlock resourceBlock = new ResourceBlock(
                receiverServer.getReceiveFileSet(), threadPool, receiveProgress);
        resourceBlock.setFileId(fileId);
        resourceBlock.setOffset(offset);
        resourceBlock.setLength(length);
        resourceBlock.setContent(buffer);
        resourceBlock.startWriteOut();

        return false;
    }

    private byte[] receiveBytes(int length) throws IOException {
        byte[] buffer = new byte[length];
        int realReceiveLength = 0;
        int offset = 0;

        while (length > 0) {
            realReceiveLength = dis.read(buffer, offset, length);
            offset += realReceiveLength;
            length -= realReceiveLength;
        }

        return buffer;
    }

    @Override
    public void run() {
        boolean finished = false;
        synchronized (lock) {
            lock.notify();
        }
        while (!finished) {
            try {
                finished = ReceiveOneBlock();
            } catch (IOException e) {
                //TODO 发送方异常掉线
                finished = true;
                e.printStackTrace();
            }
        }
        CloseableUtil.close(dis, sender);
    }
}
