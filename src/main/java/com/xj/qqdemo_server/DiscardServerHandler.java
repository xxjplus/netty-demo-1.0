package com.xj.qqdemo_server;



import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelHandlerAdapter {
	
	/**
	 * 重写channelRead()时间处理方法。
	 * 每当从客户端接收到新的数据时，这个方法会在收到消息时调用
	 * 该方法中收到的消息类型是bytebuf
	 */
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		try{
		ByteBuf in = (ByteBuf) msg;
		//打印客户端输入，传输过来的字符
		System.out.println(in.toString(CharsetUtil.UTF_8));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//ByteBuf是一个引用计数对象，这个对象必须显示地调用release()
			//方法来释放
			//废弃收到的数据
			ReferenceCountUtil.release(msg);
		}
	}
	
	/**
	 * 该方法会在发生异常时触发
	 */
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
		
		//出现异常就关闭
		cause.printStackTrace();
		ctx.close();
		
		
	}

}
