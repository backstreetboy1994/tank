package nettycodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TankMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        //当ByteByf达到想要的长度时，再进行处理
        if (byteBuf.readableBytes() < 8) return;

        int x = byteBuf.readInt();
        int y = byteBuf.readInt();

        out.add(new TankMsg(x, y));
    }
}
