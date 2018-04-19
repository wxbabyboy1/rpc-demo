package rpc.demo.util;

import rpc.demo.util.server.ServiceContainer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcServer implements Closeable {

    private int port;

    private Map<String, ServiceContainer> rpcServers;

    public RpcServer(int port) {
        this.port = port;
        this.rpcServers = new ConcurrentHashMap<>();
    }

    public void register(Class clazz) {
        //获取类继承的接口
        Class[] intfs = clazz.getInterfaces();
        if (intfs.length == 0) {
            return;
        }
        String intfName = intfs[0].getCanonicalName();
        ServiceContainer serviceContainer = new ServiceContainer(clazz);
        this.rpcServers.put(intfName, serviceContainer);
    }

    public void start() {

    }

    @Override
    public void close() throws IOException {
        System.out.println("rpc server stop");
    }

}
