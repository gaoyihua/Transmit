package com.gary.file.model;

/**
 * describe:发送文件实体
 *
 * @author gary
 * @date 2018/12/26
 */
public class SendFileModel {
    private String filePath;
    private String absoluteRootPath;
    private long offSet;
    private int length;

    public SendFileModel() {
    }

    public String getFilePath() {
        return filePath;
    }

    public SendFileModel setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getAbsoluteRootPath() {
        return absoluteRootPath;
    }

    public SendFileModel setAbsoluteRootPath(String absoluteRootPath) {
        this.absoluteRootPath = absoluteRootPath;
        return this;
    }

    public long getOffSet() {
        return offSet;
    }

    public SendFileModel setOffSet(long offSet) {
        this.offSet = offSet;
        return this;
    }

    public int getLength() {
        return length;
    }

    public SendFileModel setLength(int length) {
        this.length = length;
        return this;
    }
}
