package com.gary.file.core.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe:资源、发送者管理
 *
 * @author gary
 * @date 2019/01/28
 */
public class ResourceSenderManager {
    /**
     * 键:资源文件
     * 值:多个发送者
     */
    private Map<ResourceModel, List<SenderModel>> map = new HashMap<>();


}
