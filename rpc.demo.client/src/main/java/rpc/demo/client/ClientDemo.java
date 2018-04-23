package rpc.demo.client;

import rpc.demo.contract.intf.UserIntf;
import rpc.demo.contract.model.UserInfo;
import rpc.demo.util.RpcClient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientDemo {

    public static void main(String[] args) throws Exception {
        client();
    }

    private static void client() throws IOException, InterruptedException {
        for (int i = 0; i < 1; i++) {
            final int b = i + 1;
            new Thread(() -> {
                try (RpcClient client = RpcClient.get("localhost", 9000) ) {
                    UserIntf userIntf = client.getProxy(UserIntf.class);
                    int start = (b - 1) * 5;
                    int end = b * 5;
                    for (int j = start; j < end; j++) {
                        UserInfo userInfo = userIntf.find(j);
                        System.out.println("查询的结果1：" + userInfo);
                        List<UserInfo> userInfos = userIntf.findById(j);
                        System.out.println("查询的结果1：" + userInfos);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        //System.exit(0);
    }

    private static void test() throws IOException {
        byte[] a = "{[".getBytes(StandardCharsets.UTF_8);
        byte[] b = "]}".getBytes(StandardCharsets.UTF_8);

        System.in.read();
    }

}
