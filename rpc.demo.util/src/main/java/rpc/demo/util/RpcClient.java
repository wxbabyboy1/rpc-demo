package rpc.demo.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.demo.util.client.ClientChannel;
import rpc.demo.util.client.ClientDecoder;
import rpc.demo.util.client.ClientEncoder;
import rpc.demo.util.client.ClientHandler;
import rpc.demo.util.protocol.ProtocolRequestEntity;
import rpc.demo.util.protocol.ProtocolResponsetEntity;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

public class RpcClient implements Closeable {

    private String address;

    private int port;

    private ChannelFuture future;

    private RpcClient(String address, int port) throws InterruptedException {
        this.address = address;
        this.port = port;

        //打开远程连接
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline cp = socketChannel.pipeline();
                        //write
                        cp.addFirst("write", new ClientEncoder());
                        //read
                        cp.addLast("read", new ClientDecoder());
                        cp.addLast("read-handler",  new ClientHandler());
                    }
                });
        this.future = bootstrap.connect(address, port).sync();
        System.out.println("rpc client start");
    }

    public static RpcClient get(String address, int port) throws InterruptedException {
        return new RpcClient(address, port);
    }

    public <T> T getProxy(Class<T> clazz) {
        T proxy = (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new RpcInvoker(this.future));
        return proxy;
    }

    @Override
    public void close() {
        //关闭远程连接
        try {
            this.future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("rpc client close");
    }

    static class RpcInvoker implements InvocationHandler {

        private ChannelFuture future;

        public RpcInvoker(ChannelFuture future) {
            this.future = future;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("Class:" + method.getDeclaringClass().toString() + "\tmethod:" + method.getName());

            Object result = null;

            //拼装包体
            ProtocolRequestEntity entity = new ProtocolRequestEntity();
            entity.setClazzName(method.getDeclaringClass().toString());
            entity.setMethodName(method.getName());
            entity.setParams(args);

            ChannelFuture responseFuture = this.future.channel().writeAndFlush(entity);

            //执行远程函数
            /*ClientChannel channel = (ClientChannel)this.future.channel();
            ChannelFuture responseFuture = channel.writeAndFlush(entity);
            ProtocolResponsetEntity responsetEntity = channel.get(20000);
            Object remoteResult = responsetEntity.getResult();*/

            //反序列化结果
            Class returnClazz = method.getReturnType();


            return result;
        }
    }

}
