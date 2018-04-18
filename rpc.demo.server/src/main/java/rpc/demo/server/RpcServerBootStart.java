package rpc.demo.server;

import rpc.demo.server.imp.UserImp;
import rpc.demo.util.RpcServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

public class RpcServerBootStart {

    public static void main(String[] args) throws Exception {
        testMethod();
    }

    private static void start() throws IOException {
        RpcServer rpcServer = new RpcServer(9000);
        rpcServer.register(UserImp.class);
        rpcServer.start();
        System.out.println("server boot start");

        System.in.read();
        System.out.println("server boot end");
        rpcServer.close();
    }

    private static void testMethod() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Class clazz = UserImp.class;
        Method[] methods = clazz.getMethods();
        Class[] classes = null;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("findById")) {
                classes = methods[i].getParameterTypes();
            }
        }

        if (classes == null) return;
        Method method = clazz.getMethod("findById", classes);
        Object result = method.invoke(clazz.newInstance(), 1);

        System.out.println(result);
    }

}
