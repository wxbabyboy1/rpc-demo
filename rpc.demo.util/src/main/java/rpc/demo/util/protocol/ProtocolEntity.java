package rpc.demo.util.protocol;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProtocolEntity implements Serializable {

    /**
     * 执行接口全名
     */
    private String clazzName;

    /**
     * 执行方法名
     */
    private String methodName;

    /**
     * 执行参数
     */
    private Object[] params;

    @Override
    public String toString() {
        return "";
    }

}
