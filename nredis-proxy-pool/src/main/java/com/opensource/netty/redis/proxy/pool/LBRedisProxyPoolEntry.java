/**
 * 
 */
package com.opensource.netty.redis.proxy.pool;

import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolEntryState;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
/**
 * 对象T接口封装
 * @author liubing
 *
 */
public interface LBRedisProxyPoolEntry<T extends Pool> {
	
	T getObject();

	
	LBRedisProxyPoolEntryState getState();
}
