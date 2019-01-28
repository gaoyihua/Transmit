package com.gary.file.model;

import com.gary.file.core.RandomAccessFilePool;
import com.gary.file.core.ReceiveFileSet;
import com.gary.file.exception.ReceiveFileIdNotExistException;
import com.gary.file.view.IReceiveProgress;

import java.io.RandomAccessFile;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * describe:传输资源块
 *
 * @author gary
 * @date 2018/12/25
 */
public class ResourceBlock implements Runnable {
    private int fileId;
    private long offset;
    private int length;
    private byte[] content;
    private ReceiveFileSet reveiveFileSet;
    private ThreadPoolExecutor threadPool;
    private IReceiveProgress receiveProgress;

    public ResourceBlock(ReceiveFileSet reveiveFileSet, ThreadPoolExecutor threadPool
                    ,IReceiveProgress receiveProgress ) {
        this.reveiveFileSet = reveiveFileSet;
        this.receiveProgress = receiveProgress;
        this.threadPool = threadPool;
    }

    void writeBlock(ReceiveFileModel receiveFileModel) throws Exception {
        if (receiveFileModel == null) {
            throw new ReceiveFileIdNotExistException("文件号[" + fileId +
                    "]不存在");
        }
        String absoluteRootPath = receiveFileModel.getAbsoluteRootPath();
        String filePath = receiveFileModel.getFilePath();

       // RandomAccessFile raf = new RandomAccessFile(absoluteRootPath + filePath, "rwd");
        RandomAccessFile raf = RandomAccessFilePool.getRandomAccessFile(absoluteRootPath + filePath);
        raf.seek(offset);
        raf.write(content);
        //raf.close();

        if (receiveProgress != null) {
            receiveProgress.receiveOneBlock(fileId, content.length);
        }
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void startWriteOut() {
        threadPool.execute(this);
    }

    @Override
    public void run() {
        try {
            ReceiveFileModel receiveFileModel = reveiveFileSet.getReceiveFileModel(fileId);
            writeBlock(receiveFileModel);
            receiveFileModel.getUnReceivedBlockRecord().receiveBlock(this);
        } catch (Exception e) {
            //TODO 资源块写入文件失败的处理
            e.printStackTrace();
        }
    }
}
