package rpc.demo.util.codec;

import com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;

public class JsonCodec implements Codec {

    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T decode(byte[] obj, Class<T> clazz) {
        return JSON.parseObject(new String(obj, StandardCharsets.UTF_8), clazz);
    }

    @Override
    public Object decode(String obj, Class clazz) {
        return JSON.parseObject(obj, clazz);
    }

    @Override
    public Object decodeArray(String obj, Class clazz) {
        return JSON.parseArray(obj, clazz);
    }

}
