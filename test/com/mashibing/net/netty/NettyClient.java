package com.mashibing.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws Exception {
        //干活的线程
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new MyHandler());
            }
        });
        ChannelFuture future = b.connect("localhost",8888).sync();

        //优雅的阻塞:通过future.channel()拿到connect的结果。
        //closeFuture()是等待有人close这个future，如果有人关就往下执行，没有人关就一直等着
        future.channel().closeFuture().sync();

        //没有这句话，程序直接就结束了，然后程序就结束了，
        //还来不及接收数据就关闭了，所以需要线程阻塞住，
        //这只是阻塞的一种方法而已
//        System.in.read();

        workerGroup.shutdownGracefully();
    }

    static class MyHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(msg.toString());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ByteBuf buf = Unpooled.copiedBuffer("mashibing".getBytes());
            ctx.writeAndFlush(buf);
        }
    }
}
