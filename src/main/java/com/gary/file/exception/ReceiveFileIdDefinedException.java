package com.gary.file.exception;

/**
 * describe:接受文件编号重复定义异常
 *
 * @author gary
 * @date 2018/12/25
 */
public class ReceiveFileIdDefinedException extends Exception {
    public ReceiveFileIdDefinedException() {
        super();
    }

    public ReceiveFileIdDefinedException(String message) {
        super(message);
    }
}
