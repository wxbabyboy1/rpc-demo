package rpc.demo.util.client;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import rpc.demo.util.protocol.Const;
import rpc.demo.util.protocol.ProtocolRequestEntity;

public class ClientEncoder extends MessageToByteEncoder<ProtocolRequestEntity> {

    //{[body.length]}body

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolRequestEntity entity, ByteBuf buf) throws Exception {
        byte[] body = JSON.toJSONBytes(entity);
        int bodyLen = body.length;
        byte[] len = new byte[4];
        len[0] = (byte)((bodyLen & 0xFF000000) >> 24);
        len[1] = (byte)((bodyLen & 0x00FF0000) >> 16);
        len[2] = (byte)((bodyLen & 0x0000FF00) >> 8);
        len[3] = (byte)(bodyLen & 0x000000FF);
        //包头
        buf.writeBytes(Const.L);
        buf.writeBytes(len);
        buf.writeBytes(Const.R);
        buf.writeBytes(body);
    }

}
