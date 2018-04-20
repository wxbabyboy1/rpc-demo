package rpc.demo.util.protocol;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProtocolResponsetEntity implements Serializable {

    /**
     * 执行参数
     */
    private Object result;

    @Override
    public String toString() {
        return "";
    }

}
