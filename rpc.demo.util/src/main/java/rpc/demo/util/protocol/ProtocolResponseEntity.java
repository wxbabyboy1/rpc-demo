package rpc.demo.util.protocol;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProtocolResponseEntity {

    private byte[] error;

    private byte[] result;

}
