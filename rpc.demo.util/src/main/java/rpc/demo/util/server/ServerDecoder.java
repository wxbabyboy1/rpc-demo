package rpc.demo.util.server;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import rpc.demo.util.protocol.ProtocolRequestEntity;

import java.util.List;

public class ServerDecoder extends ByteToMessageDecoder {

    //{[body.length]}body

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        //先解析包头
        int bodyLen = 0;
        if (buf.readableBytes() >= 8) {
            //{[  ]}  标记位处理，这里不做演示
            byte[] head = new byte[8];
            buf.readBytes(head);
            //获取包体长度
            bodyLen = (head[2] << 24) + (head[3] << 16) + (head[4] << 8) + head[5];
        }
        //然后解析包体
        if (bodyLen > 0 && buf.readableBytes() >= bodyLen) {
            byte[] body = new byte[bodyLen];
            Object entity = JSON.parseObject(body, ProtocolRequestEntity.class);
            list.add(entity);
        }
    }

}
