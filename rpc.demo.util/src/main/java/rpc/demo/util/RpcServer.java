package rpc.demo.util;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcServer implements Closeable {

    private int port;

    private Map<String, Class<?>> rpcServers;

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
        this.rpcServers.put(intfName, clazz);
    }

    public void start() {

    }

    @Override
    public void close() throws IOException {
        System.out.println("rpc server stop");
    }

}
