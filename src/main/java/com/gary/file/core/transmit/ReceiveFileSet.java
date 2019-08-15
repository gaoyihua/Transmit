package com.gary.file.core.transmit;

import com.gary.file.exception.ReceiveFileIdDefinedException;
import com.gary.file.model.BlockRecord;
import com.gary.file.model.ReceiveFileModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe:接收文件集合
 *
 * @author gary
 * @date 2018/12/25
 */
public class ReceiveFileSet {
    private Map<Integer, ReceiveFileModel> receiveFileMap;
    private long totalReveiveBytes;

    public ReceiveFileSet() {
        receiveFileMap = new ConcurrentHashMap<>();
    }

    int getFileId(String fileName) {
        for (int fileId : receiveFileMap.keySet()) {
            if (receiveFileMap.get(fileId).equals(fileName)) {
                return fileId;
            }
        }
        return -1;
    }

    public void addReceiveFile(Integer fileId, ReceiveFileModel file) throws ReceiveFileIdDefinedException {
        ReceiveFileModel orgFile = receiveFileMap.get(fileId);
        if (orgFile != null) {
            throw new ReceiveFileIdDefinedException("文件编号[" +
                    + fileId +"]");
        }
        totalReveiveBytes += file.getLength();
        receiveFileMap.put(fileId, file);
    }

    String getUnReceivedBlockRecordList() {
        StringBuffer sb = new StringBuffer();

        for (int fieId : receiveFileMap.keySet()) {
            ReceiveFileModel fileModel = receiveFileMap.get(fieId);
            sb.append(fieId).append(":")
                    .append(fileModel.getAbsoluteRootPath())
                    .append(fileModel.getFilePath());
            List<BlockRecord> blockList = fileModel.getUnReceivedBlockRecord().getBlockList();
            for (BlockRecord blockRecord : blockList) {
                sb.append("\n\t").append(blockRecord);
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public ReceiveFileModel getReceiveFileModel(Integer fileId) {
        return receiveFileMap.get(fileId);
    }

    public int getTotalReceiveFileSize() {
        return receiveFileMap.size();
    }

    public long getTotalReveiveBytes() {
        return totalReveiveBytes;
    }
}
