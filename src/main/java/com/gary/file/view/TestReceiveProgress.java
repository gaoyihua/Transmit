package com.gary.file.view;

import com.gary.file.core.transmit.ReceiveFileSet;
import com.gary.file.core.transmit.ResourceReceiverServer;
import com.gary.file.model.ReceiveFileModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * describe:
 *
 * @author gary
 * @date 2018/12/28
 */
public class TestReceiveProgress {
    private JFrame jfrmMainView;
    private Container container;

    private JButton jbtnOk;
    private ReceiveFileSet fileSet;

    public TestReceiveProgress() {
        init();
        dealAction();
    }

    void init() {
        jfrmMainView = new JFrame("关于进度条");
        jfrmMainView.setSize(500, 400);
        jfrmMainView.setLocationRelativeTo(null);
        jfrmMainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container = jfrmMainView.getContentPane();
        container.setLayout(null);

        jbtnOk = new JButton("开始");
        jbtnOk.setFont(new Font("宋体", Font.PLAIN, 14));
        jbtnOk.setBounds(300, 300, 65, 40);
        container.add(jbtnOk);
        String absolutePath = "D:\\test\\test\\";
        try {
            String filePath = "demo1.mp4";
            int length = 294393759;
            fileSet = new ReceiveFileSet();
            ReceiveFileModel file = new ReceiveFileModel(
                    filePath, absolutePath, length);
            fileSet.addReceiveFile(1, file);

            filePath = "demo2.mp4";
            file = new ReceiveFileModel(filePath, absolutePath, 294393759);
            fileSet.addReceiveFile(2, file);

            filePath = "demo3.mp4";
            file = new ReceiveFileModel(filePath, absolutePath, 294393759);
            fileSet.addReceiveFile(3, file);

            filePath = "demo4.mp4";
            file = new ReceiveFileModel(filePath, absolutePath, 294393759);
            fileSet.addReceiveFile(4, file);

            filePath = "demo5.mp4";
            file = new ReceiveFileModel(filePath, absolutePath, 294393759);
            fileSet.addReceiveFile(5, file);

            filePath = "demo6.mp4";
            file = new ReceiveFileModel(filePath, absolutePath, 294393759);
            fileSet.addReceiveFile(6, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void dealAction() {
        jbtnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IReceiveProgress rrp = new ResourceReceiveProgressJDialog(
                        jfrmMainView, " 接收文件 ");
                ResourceReceiverServer rrs
                        = new ResourceReceiverServer(54190, rrp);
                rrs.setReceiveFileSet(fileSet);
                rrs.setSenderCount(1);
                try {
                    rrs.startUpReceiverServer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void showView() {
        jfrmMainView.setVisible(true);
    }

    void closeView() {
        jfrmMainView.dispose();
    }

    public static void main(String[] args) {
        new TestReceiveProgress().showView();
    }
}
