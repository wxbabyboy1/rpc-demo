package rpc.demo.util.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rpc.demo.util.protocol.ProtocolResponseEntity;

public class ClientHandler extends SimpleChannelInboundHandler<ProtocolResponseEntity> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolResponseEntity entity) throws Exception {
        ClientChannel cc = (ClientChannel) ctx.channel();
        cc.set(entity);
    }

}
