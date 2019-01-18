package com.gary.file.view;

import javax.swing.*;
import java.awt.*;

/**
 * describe:进度条面板
 *
 * @author gary
 * @date 2018/12/27
 */
public class ProgressPanel extends JPanel {
    private static final long serialVersionUID = -5204525073717791610L;

    private JLabel jlblContext;
    private JProgressBar jpgbBar;
    private JLabel jlblFileNameCaption;
    private int count;
    private int currentCount;

    public ProgressPanel(String caption, String context, int count) {
        this.count = count;
        this.currentCount = 0;
        this.setLayout(new GridLayout(2, 1));

        JPanel jpnlCaption = new JPanel();
        add(jpnlCaption);

        jlblFileNameCaption = new JLabel(caption);
        jlblFileNameCaption.setFont(IReceiveProgress.NORMAR_FONT);
        jpnlCaption.add(jlblFileNameCaption);

        jlblContext = new JLabel(context);
        jlblContext.setFont(IReceiveProgress.NORMAR_FONT);
        jpnlCaption.add(jlblContext);

        jpgbBar = new JProgressBar();
        jpgbBar.setFont(IReceiveProgress.NORMAR_FONT);
        jpgbBar.setMaximum(this.count);
        jpgbBar.setValue(currentCount);
        jpgbBar.setStringPainted(true);
        add(jpgbBar);
    }

    void setMaxValue(int count) {
        jpgbBar.setMaximum(count);
    }

    void setContext(String context) {
        jlblContext.setText(context);
    }

    String getContext() {
        return jlblContext.getText();
    }

    void setCaption(String caption) {
        jlblFileNameCaption.setText(caption);
    }

    boolean receiveOneDelta(int delta) {
        currentCount += delta;
        jpgbBar.setValue(currentCount);

        return currentCount >= count;
    }
}
