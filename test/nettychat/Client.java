package nettychat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class Client {

    private Channel channel = null;

    public  void connect() {
        //干活的线程
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        try {
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    channel = socketChannel;
                    socketChannel.pipeline().addLast(new Client.MyHandler());
                }
            });
            ChannelFuture future = b.connect("localhost",8888).sync();
            System.out.println("Connected to Server!");
            //优雅的阻塞:通过future.channel()拿到connect的结果。
            //closeFuture()是等待有人close这个future，如果有人关就往下执行，没有人关就一直等着
            future.channel().closeFuture().sync();
            //没有这句话，程序直接就结束了，然后程序就结束了，
            //还来不及接收数据就关闭了，所以需要线程阻塞住，
            //这只是阻塞的一种方法而已
//        System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void send(String text) {
        channel.writeAndFlush(Unpooled.copiedBuffer(text.getBytes()));
    }

    public void closeConnection() {
        send("__bye__");
        channel.close();
    }

    static class MyHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = null;
            try {
                buf = (ByteBuf)msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.getBytes(buf.readerIndex(),bytes);
                String str = new String(bytes);
                ClientFrame.INSTANCE.updateText(str);
            }finally {
                if (buf != null)
                    ReferenceCountUtil.release(buf);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
