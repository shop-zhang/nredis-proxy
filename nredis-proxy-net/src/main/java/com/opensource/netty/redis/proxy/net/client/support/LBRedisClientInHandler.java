///**
// * 
// */
//package com.opensource.netty.redis.proxy.net.client.support;
//
//import com.opensource.netty.redis.proxy.core.reply.IRedisReply;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
///**
// * 目标服务器与客户端通道写入
// * @author liubing
// *
// */
//public class LBRedisClientInHandler extends SimpleChannelInboundHandler<IRedisReply> {
//	
//	private Channel frontChannel;
//	
//	/**
//	 * @param frontChannel
//	 */
//	public LBRedisClientInHandler(Channel frontChannel) {
//		super();
//		this.frontChannel = frontChannel;
//	}
//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		super.channelActive(ctx);
//		
//	}
//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, IRedisReply msg)
//			throws Exception {	
//		frontChannel.writeAndFlush(msg);
//	}
//	
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
//			throws Exception {
//		
//		super.exceptionCaught(ctx, cause);
//	}
//	
//	
//	
//}
