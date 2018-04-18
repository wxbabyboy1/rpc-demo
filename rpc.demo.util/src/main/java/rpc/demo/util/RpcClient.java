package rpc.demo.util;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClient implements Closeable {

    private RpcClient(String address, int port) {
        System.out.println("rpc client start");
    }

    public static RpcClient get(String address, int port) {
        return new RpcClient(address, port);
    }

    public <T> T getProxy(Class<T> clazz) {
        T proxy = (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new RpcInvoker(clazz));
        return proxy;
    }

    @Override
    public void close() throws IOException {
        System.out.println("rpc client close");
    }

    /**
     * 代理执行类
     */
    static class RpcInvoker implements InvocationHandler {

        private Class clazz;

        public RpcInvoker(Class<?> clazz) {
            this.clazz = clazz;
        }

        //执行方法
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("Class:" + this.clazz.toString() + "\tmethod:" + method.getName());
            return null;
        }

    }

}
