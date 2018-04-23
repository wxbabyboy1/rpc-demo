package rpc.demo.util.codec;


public class CodecFactory {

    private static final Codec codec = new XmlCodec();

    public static Codec codec() {
        return codec;
    }

}
