package com.lsxy.area.agent.cti;

/**
 * Created by tandy on 16/12/23.
 */
public class AvalibleCTINodeNotFoundException extends Exception {
    public AvalibleCTINodeNotFoundException(CTIClientContext ctiClientContext, String referenceResId) {
        super("未找到有效的CTI节点异常，当前CTI连接状态："+ctiClientContext+" 参考资源标识："+referenceResId);
    }
}
