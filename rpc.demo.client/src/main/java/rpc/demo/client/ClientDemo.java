package rpc.demo.client;

import rpc.demo.contract.intf.UserIntf;
import rpc.demo.util.RpcClient;

import java.io.IOException;

public class ClientDemo {

    public static void main(String[] args) throws IOException {
        try (RpcClient client = RpcClient.get("", 9000) ) {
            UserIntf userIntf = client.getProxy(UserIntf.class);
            userIntf.findById(999);
        }
    }

}
