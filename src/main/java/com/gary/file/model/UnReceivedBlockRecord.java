package com.gary.file.model;

import com.gary.file.model.BlockRecord;

import java.util.LinkedList;
import java.util.List;

/**
 * describe:未接收的块记录
 *
 * @author gary
 * @date 2018/12/25
 */
public class UnReceivedBlockRecord {
    private List<BlockRecord> blockList;

    public UnReceivedBlockRecord(long fileLength) {
        blockList = new LinkedList<>();
        blockList.add(new BlockRecord(0, fileLength));
    }

    private int getTheBlock(long curOffset) throws Exception {
        int index = 0;
        for (index = 0; index < blockList.size(); index++) {
            BlockRecord org = blockList.get(index);
            if (org.getOffset() + org.getLength() >= curOffset) {
                return index;
            }
        }
        throw new Exception("块编号未找到:" + curOffset);
    }

    synchronized void receiveBlock(ResourceBlock block) throws Exception {
        long curOffset = block.getOffset();
        long curLength = block.getLength();

        int orgBlockIndex = getTheBlock(curOffset);
        BlockRecord orgBlock = blockList.get(orgBlockIndex);
        long orgOffset = orgBlock.getOffset();
        long orgLength = orgBlock.getLength();

        long leftOffset = orgOffset;
        long leftLength = curOffset - orgOffset;
        long rightOffset = curOffset + curLength;
        long rightLength = orgOffset + orgLength - curOffset - curLength;

        blockList.remove(orgBlockIndex);
        if (rightLength > 0) {
            blockList.add(orgBlockIndex, new BlockRecord(rightOffset, rightLength));
        }
        if (leftLength > 0) {
            blockList.add(orgBlockIndex, new BlockRecord(leftOffset, leftLength));
        }
    }

    boolean isReveivedOver() {
        return blockList.isEmpty();
    }

    public List<BlockRecord> getBlockList() {
        return blockList;
    }
}
