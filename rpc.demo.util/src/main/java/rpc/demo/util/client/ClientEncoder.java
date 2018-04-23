package rpc.demo.util.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import rpc.demo.util.codec.Codec;
import rpc.demo.util.codec.JsonCodec;
import rpc.demo.util.codec.XmlCodec;
import rpc.demo.util.protocol.Const;
import rpc.demo.util.protocol.ProtocolRequestEntity;

public class ClientEncoder extends MessageToByteEncoder<ProtocolRequestEntity> {

    //{[body.length]}body

    private Codec codec = new XmlCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolRequestEntity entity, ByteBuf buf) throws Exception {
        byte[] body = codec.encode(entity);
        int bodyLen = body.length;
        byte[] len = new byte[4];
        len[0] = (byte)((bodyLen >> 24) & 0xFF);
        len[1] = (byte)((bodyLen >> 16) & 0xFF);
        len[2] = (byte)((bodyLen >> 8) & 0xFF);
        len[3] = (byte)(bodyLen & 0xFF);
        //包头
        buf.writeBytes(Const.L);
        buf.writeBytes(len);
        buf.writeBytes(Const.R);
        //包体
        buf.writeBytes(body);
    }

}
