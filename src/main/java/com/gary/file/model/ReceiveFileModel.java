package com.gary.file.model;

import java.util.Objects;

/**
 * describe:接收文件实体
 *
 * @author gary
 * @date 2018/12/25
 */
public class ReceiveFileModel {
    private String filePath;
    private String absoluteRootPath;
    private long length;
    private UnReceivedBlockRecord unReceivedBlockRecord;

    public ReceiveFileModel(String filePath, String absoluteRootPath, long length) {
        this.filePath = filePath;
        this.absoluteRootPath = absoluteRootPath;
        this.length = length;
        this.unReceivedBlockRecord = new UnReceivedBlockRecord(this.length);
    }

    public UnReceivedBlockRecord getUnReceivedBlockRecord() {
        return unReceivedBlockRecord;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAbsoluteRootPath() {
        return absoluteRootPath;
    }

    public void setAbsoluteRootPath(String absoluteRootPath) {
        this.absoluteRootPath = absoluteRootPath;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean equals(String fileName) {
        return this.filePath.equals(fileName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiveFileModel)) return false;
        ReceiveFileModel that = (ReceiveFileModel) o;
        return length == that.length &&
                Objects.equals(filePath, that.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, length);
    }

    @Override
    public String toString() {
        return "ReceiveFileModel{" +
                "filePath='" + filePath + '\'' +
                ", absoluteRootPath='" + absoluteRootPath + '\'' +
                ", length=" + length +
                ", unReceivedBlockRecord=" + unReceivedBlockRecord +
                '}';
    }
}
