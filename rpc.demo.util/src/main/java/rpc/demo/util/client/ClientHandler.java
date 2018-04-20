package rpc.demo.util.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String entity) throws Exception {
        ClientChannel cc = (ClientChannel) ctx.channel();
        cc.set(entity);
    }

}
