package com.gary.file.core.manage;

import com.gary.file.exception.PortNotDefinedException;
import com.gary.util.CloseableUtil;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * describe:总控服务器
 *
 * @author gary
 * @date 2019/01/28
 */
public class ManagerCenter implements Runnable {
    private ServerSocket server;
    private int port;
    private volatile boolean goon;
    private ThreadPoolExecutor threadPool;
    private ResourceManager resourceManager;
    private ClientManager clientManager;

    public ManagerCenter(int port) {
        threadPool = new ThreadPoolExecutor(50,
                100, 500, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>());
        this.goon = false;
        this.port = port;
    }

    @Override
    public void run() {
        while (goon) {
            try {
                Socket socket = server.accept();
                //TODO 处理客户端请求
                //new ManagerCenterExecutor(this, socket, threadPool);
            } catch (Exception e) {
                goon = false;
                e.printStackTrace();
            }
        }
        stopServer();

    }

    public void startServer() throws Exception {
        if (this.port == 0) {
            throw new PortNotDefinedException();
        }
        this.server = new ServerSocket(port);
        this.goon = true;
        new Thread(this, "MANAGE_CENTER").start();
    }

    private void stopServer() {
        CloseableUtil.close(this.server);
    }
}
