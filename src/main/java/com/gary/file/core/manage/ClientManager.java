package com.gary.file.core.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe:客户端管理者
 *
 * @author gary
 * @date 2019/01/28
 */
public class ClientManager {
    /**
     * 键：客户端信息
     * 值：本地的根路径 和 文件 的映射
     */
    private static final Map<ClientModel, Map<String, List<ResourceModel>>> map;

    static {
        map = new HashMap<>();
    }

}
