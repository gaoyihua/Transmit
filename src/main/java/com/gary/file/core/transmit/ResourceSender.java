package com.gary.file.core.transmit;

import com.gary.file.model.SendFileModel;
import com.gary.file.util.ByteAndStringUtil;
import com.gary.util.CloseableUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.List;

/**
 * describe:资源发送者
 *
 * @author gary
 * @date 2018/12/25
 */
public class ResourceSender implements Runnable {
    public static final int BUFFER_SIZE = 1 << 15;
    public static final int HEADER_BUFFER_SIZE = 1 << 4;

    private Socket socket;
    private ReceiveFileSet sendFileSet;
    private List<SendFileModel> sendFileList;
    private DataOutputStream dos;

    public ResourceSender(Socket socket, ReceiveFileSet sendFileSet,
                          List<SendFileModel> sendFileList) throws IOException {
        this.socket = socket;
        this.sendFileList = sendFileList;
        this.sendFileSet = sendFileSet;
        this.dos = new DataOutputStream(socket.getOutputStream());
        new Thread(this, "ResourceSender").start();
    }

    @Override
    public void run() {
        if (sendFileList == null) {
            return;
        }
        byte[] header = new byte[HEADER_BUFFER_SIZE];
        byte[] buffer = new byte[BUFFER_SIZE];

        for (SendFileModel sendFileModel : sendFileList) {
            String absoluteRootPath = sendFileModel.getAbsoluteRootPath();
            String filePath = sendFileModel.getFilePath();
            int fileId = sendFileSet.getFileId(filePath);
            long offSet = sendFileModel.getOffSet();
            int length = sendFileModel.getLength();

            ByteAndStringUtil.setIntAt(header, 0, fileId);
            ByteAndStringUtil.setLongAt(header, 4, offSet);
            ByteAndStringUtil.setIntAt(header, 12, length);

            try {
                dos.write(header);
                dos.flush();

                RandomAccessFile raf = new RandomAccessFile(absoluteRootPath + filePath, "r");
                raf.seek(offSet);

                int restLen = length;
                int realLen = 0;
                int realLenth = 0;
                while (restLen > 0) {
                    realLenth = restLen >= BUFFER_SIZE ? BUFFER_SIZE : restLen;
                    realLen = raf.read(buffer, 0, realLenth);
                    dos.write(buffer, 0, realLen);
                    restLen -= realLen;
                }

                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
                close();
                break;
            }
        }

        ByteAndStringUtil.setIntAt(header, 0, -1);
        ByteAndStringUtil.setLongAt(header, 4, 0);
        ByteAndStringUtil.setIntAt(header, 12, 0);
        try {
            if (dos != null) {
                dos.write(header);
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    void close() {
        CloseableUtil.close(dos, socket);
    }
}
