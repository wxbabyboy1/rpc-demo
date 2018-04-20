package rpc.demo.util.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rpc.demo.util.protocol.ProtocolResponsetEntity;

public class ClientHandler extends SimpleChannelInboundHandler<ProtocolResponsetEntity> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolResponsetEntity entity) throws Exception {

    }

}
