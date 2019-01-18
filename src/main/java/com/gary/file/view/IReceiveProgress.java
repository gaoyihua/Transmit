package com.gary.file.view;

import java.awt.*;
/**
 * describe:接收进度条接口
 *
 * @author gary
 * @date 2018/12/29
 */
public interface IReceiveProgress {
    Font TOPIC_FONT = new Font("微软雅黑", Font.BOLD, 30);
    Font NORMAR_FONT = new Font("宋体", Font.PLAIN, 16);
    Font IMPORTANT_FONT = new Font("黑体", Font.BOLD, 16);
    int NORMAR_FoONT_SIZE = NORMAR_FONT.getSize();
    int TOPIC_FONT_SIZE = TOPIC_FONT.getSize() + 4;

    Color TOPIC_COLOR = new Color(5, 5, 209);
    Color TITLE_COLOR = new Color(167, 82, 3);
    Color IMPORTTANT_COLOR = new Color(255, 0, 0);

    int RECEIVE_PROGRESS_WIDTH = 400;
    int RECEIVE_PROGRESS_HEIGHT = 50;
    int PROGRESS_MIN_HEIGHT = 320;
    int PADDING = 5;

    long MIN_TIME_FOR_CUR_SPEED = 250;
    long MIN_TIME_FOR_TOT_SPEED = 500;

    /**
     * 设置发送计划
     * @param receiveFileCount
     * @param byteCount
     */
    void setSenderPlan(int receiveFileCount, long byteCount);

    /**
     * 设置发送者数量
     * @param senderCount
     */
    void setSenderInfo(int senderCount);

    /**
     * 显示进度条
     */
    void startShowProgress();

    /**
     * 接收发送者
     * @param sender
     */
    void acceptOneSender(String sender);

    /**
     * 接收新文件
     * @param fileId
     * @param fileName
     * @param fileLength
     */
    void receiveNewFile(int fileId, String fileName, int fileLength);

    /**
     * 接收一资源块
     * @param fileId
     * @param length
     */
    void receiveOneBlock(int fileId, int length);

    /**
     * 完成接收
     */
    void finishedReceive();
}
