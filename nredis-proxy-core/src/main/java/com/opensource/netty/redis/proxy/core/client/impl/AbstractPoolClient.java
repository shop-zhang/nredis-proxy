/**
 * 
 */
package com.opensource.netty.redis.proxy.core.client.impl;


import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensource.netty.redis.proxy.commons.exception.RedisException;
import com.opensource.netty.redis.proxy.core.client.Client;
import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.core.pool.utils.LBRedisProxyChannelPoolUtils;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.impl.LBRedisProxyBasicPool;
/**
 * @author liubing
 *
 */
public abstract class AbstractPoolClient implements Client{
	
	protected LBRedisProxyBasicPool<IConnection>  pool;
    protected LBRedisProxyPoolConfig ffanRedisProxyPoolConfig;
    protected LBRedisProxyPooledObjectFactory<IConnection> factory;
	private Logger logger = LoggerFactory.getLogger(AbstractPoolClient.class);

	/**
	 * 
	 */
	public AbstractPoolClient(LBRedisProxyPoolConfig ffanRedisProxyPoolConfig) {
		super();
		this.ffanRedisProxyPoolConfig=ffanRedisProxyPoolConfig;
	}
    
	protected void initPool() {
		try{
            factory = createChannelFactory();
            pool = LBRedisProxyChannelPoolUtils.createPool(ffanRedisProxyPoolConfig, factory);
		}catch(Exception e){
			logger.error("initPool fail,reason:"+e.getCause()+",message:"+e.getMessage(), e);
		}
	}
	
	/**
	 * 创建一个工厂类
	 * @return
	 */
    protected abstract LBRedisProxyPooledObjectFactory<IConnection> createChannelFactory();
    
    public abstract void write(RedisCommand request,Channel frontChannel);
    
    protected LBRedisProxyPoolEntry<IConnection> borrowObject() throws Exception {
    	LBRedisProxyPoolEntry<IConnection> nettyChannelEntry=pool.borrowEntry();
        if (nettyChannelEntry != null&&nettyChannelEntry.getObject()!=null) {
            return nettyChannelEntry;
        }
        
        String errorMsg = this.getClass().getSimpleName() + " borrowObject Error";
        logger.error(errorMsg);
        throw new RedisException(errorMsg);
    }


    protected void returnObject(LBRedisProxyPoolEntry<IConnection> entry) {
        if (entry == null) {
            return;
        }
        try {
        	pool.returnEntry(entry);
        } catch (Exception ie) {
        	logger.error(this.getClass().getSimpleName() + " return client Error" , ie);
        }
    }

}
