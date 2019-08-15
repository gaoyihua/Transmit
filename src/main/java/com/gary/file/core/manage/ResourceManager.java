package com.gary.file.core.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe:资源管理者
 *
 * @author gary
 * @date 2019/01/28
 */
public class ResourceManager {
    /**
     * 键为根路径：如D:\\java\\
     * 值为文件 路径：如XXX\\1.jpg
     */
    private static final Map<String, List<ResourceModel>> RESOURCE_MAP;
    static {
        RESOURCE_MAP = new HashMap<>();
    }

}
