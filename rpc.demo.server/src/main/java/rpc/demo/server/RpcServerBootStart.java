package rpc.demo.server;

import rpc.demo.server.imp.UserImp;
import rpc.demo.util.RpcServer;

import java.io.IOException;

public class RpcServerBootStart {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
        RpcServer rpcServer = new RpcServer(9000);
        rpcServer.register(UserImp.class);
        rpcServer.start();
        System.out.println("server boot start");

        System.in.read();
        System.out.println("server boot end");
        rpcServer.close();
    }

}
