/**
 * 
 */
package com.opensource.netty.redis.proxy.core.connection;

import io.netty.channel.Channel;

import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.pool.commons.Pool;

/**
 * @author liubing
 *
 */
public interface IConnection extends Pool{
	
	public void write(RedisCommand request,Channel frontChannel);
	
	 /**
     * open the channel
     * 
     * @return
     */
    boolean open();

    /**
     * close the channel.
     */
    void close();

    /**
     * close the channel gracefully.
     */
    void close(int timeout);

    /**
     * is closed.
     * 
     * @return closed
     */
    boolean isClosed();

    /**
     * the node available status
     * 
     * @return
     */
    boolean isAvailable();
}
