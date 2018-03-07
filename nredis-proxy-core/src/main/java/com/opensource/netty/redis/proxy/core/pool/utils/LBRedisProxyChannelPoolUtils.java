/**
 * 
 */
package com.opensource.netty.redis.proxy.core.pool.utils;

import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyIdleEntriesQueue;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolObjectEntryFactory;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.impl.LBRedisProxyBasicPool;
import com.opensource.netty.redis.proxy.pool.impl.LBRedisProxyPoolBasicObjectEntryFactory;
import com.opensource.netty.redis.proxy.pool.impl.LBRedisProxyPoolBasicIdleEntriesQueue;



/**
 * @author liubing
 *
 */
public class LBRedisProxyChannelPoolUtils {
	
	public static  LBRedisProxyBasicPool<IConnection> createPool(LBRedisProxyPoolConfig config, LBRedisProxyPooledObjectFactory<IConnection> factory) throws Exception {
		return createPool( config, createQueue(config),createPoolEntryFactory(factory));
	}
	
	private static  LBRedisProxyPoolObjectEntryFactory<IConnection> createPoolEntryFactory(LBRedisProxyPooledObjectFactory<IConnection> objectFactory) {

		return new LBRedisProxyPoolBasicObjectEntryFactory<IConnection>(objectFactory);
	}
	
	private static  LBRedisProxyIdleEntriesQueue<IConnection> createQueue( LBRedisProxyPoolConfig config) {
		return new LBRedisProxyPoolBasicIdleEntriesQueue<IConnection>(config);
	}

	
	public static  LBRedisProxyBasicPool<IConnection> createPool(LBRedisProxyPoolConfig config, LBRedisProxyIdleEntriesQueue<IConnection> queue, LBRedisProxyPoolObjectEntryFactory<IConnection> factory) throws Exception {
		return new LBRedisProxyBasicPool<IConnection>(config, queue, factory);
	}
}
