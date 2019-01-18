package com.gary.file.model;

/**
 * describe:未接收的块记录实体
 *
 * @author gary
 * @date 2018/12/25
 */
public class BlockRecord {
    private long offset;
    private long length;

    public BlockRecord(long offset, long length) {
        this.offset = offset;
        this.length = length;
    }

    public long getOffset() {
        return offset;
    }

    public long getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "BlockRecord{" +
                "offset=" + offset +
                ", length=" + length +
                '}';
    }
}
