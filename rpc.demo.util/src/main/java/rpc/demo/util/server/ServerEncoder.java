package rpc.demo.util.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import rpc.demo.util.protocol.ProtocolResponsetEntity;

public class ServerEncoder extends MessageToByteEncoder<ProtocolResponsetEntity> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolResponsetEntity entity, ByteBuf buf) throws Exception {

    }

}
