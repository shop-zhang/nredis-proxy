/**
 * 
 */
package com.opensource.netty.redis.proxy.net.client.support;

import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.core.log.impl.LoggerUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
/**
 * 解码处理
 * 
 * @author liubing
 *
 */
public class LBRedisClientOutHandler extends ChannelOutboundHandlerAdapter {
	
		
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {		
		if(msg instanceof RedisCommand){
				ctx.write(msg, promise);
		}else{
			LoggerUtils.error("write redis server msg not instanceof RedisCommand");
		}
		
		
	}
}
