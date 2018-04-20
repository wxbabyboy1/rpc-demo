package rpc.demo.util.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rpc.demo.util.protocol.ProtocolRequestEntity;

import java.lang.reflect.Method;
import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<ProtocolRequestEntity> {

    private Map<String, ServiceContainer> services;

    public ServerHandler(Map<String, ServiceContainer> services) {
        this.services = services;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolRequestEntity entity) throws Exception {
        String intfName = entity.getClazzName().replaceAll("interface ", "");
        String methodName = entity.getMethodName();
        Object[] params = entity.getParams();

        ServiceContainer service = this.services.get(intfName);
        Object result = service.invoke(methodName, params);

        ctx.channel().writeAndFlush(result);
    }

}
