package com.gary.file.view;

import com.gary.file.util.ByteAndStringUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe:资源接受进度条模态框
 *
 * @author gary
 * @date 2018/12/28
 */
public class ResourceReceiveProgressJDialog extends JDialog implements IReceiveProgress, Runnable {

    private Map<Integer, ProgressPanel> fileReceiveMap;

    private Container container;
    private JLabel jlblTopic;
    private JLabel jlblReceivePlanFile;
    private JLabel jlblReceivePlanSender;
    private JLabel jlblReceiveAction;
    private JLabel jlblCurrSpeed;
    private JLabel jlblTotalSpeed;

    private ProgressPanel frppSender;
    private ProgressPanel frppFiles;

    private volatile int receiveFileCount;
    private volatile int currentReceiveFileCount;
    private volatile long startTime;
    private volatile long lastTime;
    private volatile long lastReceiveBytes;
    private volatile long currentReceiveBytes;
    private volatile boolean goon;
    private volatile Object lock;

    public ResourceReceiveProgressJDialog(Frame owner, String title) {
        super(owner, title, true);

        fileReceiveMap = new ConcurrentHashMap<>();
        lock = new Object();

        container = getContentPane();
        container.setLayout(new GridLayout(0, 1));
        setSize(RECEIVE_PROGRESS_WIDTH, PROGRESS_MIN_HEIGHT);
        setLocationRelativeTo(owner);

        jlblTopic = new JLabel(title, JLabel.CENTER);
        jlblTopic.setFont(TOPIC_FONT);
        jlblTopic.setForeground(TOPIC_COLOR);
        container.add(jlblTopic);

        // 接收计划
        JPanel jpnlReceivePlan = new JPanel(new GridLayout(3, 1));
        container.add(jpnlReceivePlan);

        JLabel jlblReceivePlanTitle = new JLabel("本次接收计划", JLabel.CENTER);
        jlblReceivePlanTitle.setFont(NORMAR_FONT);
        jlblReceivePlanTitle.setForeground(TITLE_COLOR);
        jpnlReceivePlan.add(jlblReceivePlanTitle);

        jlblReceivePlanFile = new JLabel("本次共接收F个文件，共B字节。", JLabel.LEFT);
        jlblReceivePlanFile.setFont(NORMAR_FONT);
        jpnlReceivePlan.add(jlblReceivePlanFile);

        jlblReceivePlanSender = new JLabel("共S发送端。", JLabel.LEFT);
        jlblReceivePlanSender.setFont(NORMAR_FONT);
        jpnlReceivePlan.add(jlblReceivePlanSender);

        // 发送端进度
        frppSender = new ProgressPanel("发送端：", "", 1);
        container.add(frppSender);
        // 当前进行的接收动作
        jlblReceiveAction = new JLabel("尚未确定接收任务", 0);
        jlblReceiveAction.setFont(IMPORTANT_FONT);
        jlblReceiveAction.setForeground(IMPORTTANT_COLOR);
        container.add(jlblReceiveAction);
        // 文件接收进度
        frppFiles = new ProgressPanel("接收文件：", "0/1", 1);
        container.add(frppFiles);

        JPanel jpnlSpeed = new JPanel(new GridLayout(1, 2));
        container.add(jpnlSpeed);

        jlblCurrSpeed = new JLabel("字节/秒");
        jlblCurrSpeed.setFont(NORMAR_FONT);
        jpnlSpeed.add(jlblCurrSpeed);

        jlblTotalSpeed = new JLabel("字节/秒");
        jlblTotalSpeed.setFont(NORMAR_FONT);
        jpnlSpeed.add(jlblTotalSpeed);
    }

    @Override
    public void setSenderPlan(int receiveFileCount, long byteCount) {
        String planContext = jlblReceivePlanFile.getText();
        planContext = planContext.replace("F", String.valueOf(receiveFileCount));
        planContext = planContext.replace("B", String.valueOf(byteCount));
        jlblReceivePlanFile.setText(planContext);

        this.receiveFileCount = receiveFileCount;
        this.currentReceiveFileCount = 0;
        frppFiles.setContext(currentReceiveFileCount + "/" + receiveFileCount);

        jlblReceiveAction.setText("已确定发送任务计划！");
    }

    @Override
    public void setSenderInfo(int senderCount) {
        String planContext = jlblReceivePlanSender.getText();
        planContext = planContext.replace("S", String.valueOf(senderCount));
        jlblReceivePlanSender.setText(planContext);
        frppFiles.setMaxValue(receiveFileCount);
    }

    @Override
    public synchronized void receiveNewFile(int fileId, String fileName, int fileLength) {
        ProgressPanel frpp = fileReceiveMap.get(fileId);
        if (frpp == null) {
            frpp = new ProgressPanel("接收", fileName, fileLength);
            fileReceiveMap.put(fileId, frpp);
            setSize(RECEIVE_PROGRESS_WIDTH, getHeight() + RECEIVE_PROGRESS_HEIGHT);
            container.add(frpp);
            currentReceiveFileCount++;
            frppFiles.setContext(currentReceiveFileCount + "/" + receiveFileCount);
            frppFiles.receiveOneDelta(1);
        }
    }

    @Override
    public synchronized void receiveOneBlock(int fileId, int length) {
        ProgressPanel frpp = fileReceiveMap.get(fileId);
        if (frpp.receiveOneDelta(length)) {
            container.remove(frpp);
            fileReceiveMap.remove(fileId, frpp);
            setSize(RECEIVE_PROGRESS_WIDTH, getHeight() - RECEIVE_PROGRESS_HEIGHT);
        }
        getReceiveSpeed(length);
    }

    private void getReceiveSpeed(int length) {
        currentReceiveBytes += length;
        long currentTime = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = currentTime;
            jlblReceiveAction.setText("开始接收文件……");
            return;
        }
        // 计算接收速度
        long deltaTime = currentTime - lastTime;
        if (deltaTime > MIN_TIME_FOR_CUR_SPEED) {
            // 计算间隔时间
            long deltaByte = currentReceiveBytes - lastReceiveBytes;
            long curSpeed = deltaByte * 1000 / deltaTime;

            jlblCurrSpeed.setText("瞬时速度： "
                    + ByteAndStringUtil.bytesToKMG(curSpeed)
                    + "B/秒");
            lastTime = currentTime;
            lastReceiveBytes = currentReceiveBytes;
        }
    }

    @Override
    public void finishedReceive() {
        this.dispose();
        goon = false;
    }

    @Override
    public void acceptOneSender(String sender) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
            synchronized (lock) {
                lock.notify();
            }
        }
        frppSender.receiveOneDelta(1);
        frppSender.setContext(frppSender.getContext() + "" + sender);
        jlblReceiveAction.setText("接入一个发送者：" + sender);
    }

    @Override
    public void startShowProgress() {
        synchronized (lock) {
            try {
                new Thread(this).start();
                goon = true;
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setVisible(true);
    }

    @Override
    public void run() {
        synchronized (lock) {
            try {
                lock.notify();
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (goon) {
            long currentTimeMillis = System.currentTimeMillis();
            long zero = currentTimeMillis - startTime;
            if (zero == 0L) {
                continue;
            }
            long totalSpeed = currentReceiveBytes * 1000 / zero;
            jlblTotalSpeed.setText("平均速度："
                    + ByteAndStringUtil.bytesToKMG(totalSpeed)
                    + "B/秒");
            synchronized (Class.class) {
                try {
                    Class.class.wait(MIN_TIME_FOR_TOT_SPEED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
