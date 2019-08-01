package net;

import com.mashibing.tank.GameModel;
import com.mashibing.tank.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
import nettychat.ClientFrame;
import nettycodec.TankMsg;
import nettycodec.TankMsgEncoder;

public class Client {
    public static final Client INSTANCE = new Client();

    private Channel channel = null;

    private Client(){

    }

    public void connect() {
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
                    socketChannel.pipeline()
                            //将Encoder写好，扔到channel中去即可
                            .addLast(new MsgEncoder())
                            .addLast(new MsgDecoder())
                            .addLast(new MyHandler());
                }
            });
            ChannelFuture future = b.connect("localhost", 8888).sync();
            System.out.println("Connected to Server!");
            //优雅的阻塞:通过future.channel()拿到connect的结果。
            //closeFuture()是等待有人close这个future，如果有人关就往下执行，没有人关就一直等着
            future.channel().closeFuture().sync();
            //没有这句话，程序直接就结束了，然后程序就结束了，
            //还来不及接收数据就关闭了，所以需要线程阻塞住，
            //这只是阻塞的一种方法而已
//        System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void send(TankJoinMsg msg) {
        channel.writeAndFlush(msg);
    }

    public void closeConnection() {
        channel.close();
    }

    static class MyHandler extends SimpleChannelInboundHandler<TankJoinMsg> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getGm().getMyTank()));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TankJoinMsg msg) throws Exception {
            System.out.println(msg);
            msg.handler();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

        public static void main(String[] args) {
            Client client = new Client();
            client.connect();
        }
    }
}
