package rpc.demo.util.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rpc.demo.util.protocol.ProtocolRequestEntity;

import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<ProtocolRequestEntity> {

    private Map<String, ServiceContainer> servers;

    public ServerHandler(Map<String, ServiceContainer> servers) {
        this.servers = servers;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolRequestEntity entity) throws Exception {
        String intfName = entity.getClazzName();
        String methodName = entity.getMethodName();
        Object[] params = entity.getParams();

    }

}
