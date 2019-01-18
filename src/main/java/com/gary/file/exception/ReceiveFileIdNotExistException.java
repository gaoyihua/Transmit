package com.gary.file.exception;

/**
 * describe:接受文件编号不存在异常
 *
 * @author gary
 * @date 2018/12/25
 */
public class ReceiveFileIdNotExistException extends Exception {
    public ReceiveFileIdNotExistException() {
        super();
    }

    public ReceiveFileIdNotExistException(String message) {
        super(message);
    }
}
