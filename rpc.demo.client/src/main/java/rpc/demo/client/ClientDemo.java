package rpc.demo.client;

import rpc.demo.contract.intf.UserIntf;
import rpc.demo.contract.model.UserInfo;
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
            for (int i = 0; i < 50000; i++) {
                UserInfo userInfo = userIntf.find(i);
                System.out.println("查询的结果：" + userInfo);
            }
        }

        System.exit(0);
    }

    private static void test() throws IOException {
        byte[] a = "{[".getBytes(StandardCharsets.UTF_8);
        byte[] b = "]}".getBytes(StandardCharsets.UTF_8);

        System.in.read();
    }

}
