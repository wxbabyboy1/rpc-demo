package rpc.demo.client;

import rpc.demo.contract.intf.UserIntf;
import rpc.demo.util.RpcClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientDemo {

    public static void main(String[] args) throws Exception {
        client();
    }

    private static void client() throws IOException, InterruptedException {
        try (RpcClient client = RpcClient.get("localhost", 9000) ) {
            UserIntf userIntf = client.getProxy(UserIntf.class);
            userIntf.findById(999);
        }
    }

    private static void test() throws IOException {
        byte[] a = "{[".getBytes(StandardCharsets.US_ASCII);
        byte[] b = "]}".getBytes(StandardCharsets.US_ASCII);

        System.in.read();
    }

}
