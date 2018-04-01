package com.xj.qqdemo_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 丢弃任何进入的数据 
 * 启动服务端的DiscardServerHandler
 * @author xxj
 *
 */
public class DiscardServer {
	private int port;
	public DiscardServer(int port){
		super();
		this.port=port;
	}
	
	/**
	 * NioEventLoopGroup用来处理I/O操作的多线程事件循环
	 * @throws Exception
	 */
	public void run() throws Exception{
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		System.out.println("准备运行端口："+port);
		try {
			//ServerBootstrap是一个启动NIO服务的辅助启动类
			ServerBootstrap b=new ServerBootstrap();
			//这一步必须，如果没有设置group将会报错
			b=b.group(bossGroup, workerGroup);
			//ServerSocketChannel以NIO的selector为基础进行实现的，用来接收新的连接
			//这里告诉Channel如何获取新的连接
			b=b.channel(NioServerSocketChannel.class);
			
			b=b.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new DiscardServerHandler());
				}
			});
			
			b=b.option(ChannelOption.SO_BACKLOG, 128);
			
			b=b.childOption(ChannelOption.SO_KEEPALIVE, true);
			//绑定端口并启动去接收进来的连接
			ChannelFuture f=b.bind(port).sync();
			
			//这里会一直等待，直到socket被关闭
			f.channel().closeFuture().sync();
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			//关闭
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
		
	}
	
	//将规则跑起来
	public static void main(String[] args) throws Exception {
		int port;
		if(args.length>0){
			port=Integer.parseInt(args[0]);
			
		}else{
			port=8080;
		}
		
		new DiscardServer(port).run();
		System.out.println("Server:run()");
	}

}
