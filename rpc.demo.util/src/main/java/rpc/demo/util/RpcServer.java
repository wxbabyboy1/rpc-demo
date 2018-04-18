package rpc.demo.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcServer implements Closeable {

    private int port;

    private Map<String, Object> rpcServers;

    public RpcServer(int port) {
        this.port = port;
        this.rpcServers = new ConcurrentHashMap<>();
    }

    public void register(Class clazz) throws IllegalAccessException, InstantiationException {
        //获取类继承的接口
        Class[] intfs = clazz.getInterfaces();
        if (intfs.length == 0) {
            return;
        }
        this.rpcServers.put(intfs[0].getCanonicalName(), clazz.newInstance());
    }

    public void start() {

    }

    @Override
    public void close() throws IOException {
        System.out.println("rpc server stop");
    }
}
