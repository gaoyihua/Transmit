package com.gary.file.core.manage;

import java.util.Date;
import java.util.Objects;

/**
 * describe:文件模型
 *
 * @author gary
 * @date 2019/01/28
 */
public class ResourceModel {
    private String filePath;
    private String absoluteRootPath;
    private long length;
    private Date time;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceModel)) return false;
        ResourceModel that = (ResourceModel) o;
        return length == that.length &&
                Objects.equals(filePath, that.filePath) &&
//                Objects.equals(absoluteRootPath, that.absoluteRootPath) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, absoluteRootPath, length, time);
    }

    @Override
    public String toString() {
        return "ResourceModel{" +
                ", filePath='" + filePath + '\'' +
                ", absoluteRootPath='" + absoluteRootPath + '\'' +
                ", length=" + length +
                '}';
    }
}
