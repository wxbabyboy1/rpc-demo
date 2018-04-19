package rpc.demo.server;

import rpc.demo.server.imp.UserImp;
import rpc.demo.util.RpcServer;
import rpc.demo.util.server.ServiceContainer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

public class RpcServerBootStart {

    public static void main(String[] args) throws Exception {
        testMethod();
    }

    private static void start() throws IOException, IllegalAccessException, InstantiationException {
        RpcServer rpcServer = new RpcServer(9000);
        rpcServer.register(UserImp.class);
        rpcServer.start();
        System.out.println("server boot start");

        System.in.read();
        System.out.println("server boot end");
        rpcServer.close();
    }

    private static void testMethod() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        ServiceContainer serviceContainer = new ServiceContainer(UserImp.class);
        Object result = serviceContainer.invoke("findById", 1);
        System.out.println(result);
    }

}
