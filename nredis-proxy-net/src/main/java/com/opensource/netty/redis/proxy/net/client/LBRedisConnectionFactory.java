/**
 * 
 */
package com.opensource.netty.redis.proxy.net.client;

import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.exception.LBRedisProxyPoolException;
/**
 * @author liubing
 *
 */
public class LBRedisConnectionFactory implements LBRedisProxyPooledObjectFactory<IConnection>{
	
	private LBRedisProxyPoolConfig ffanRedisProxyPoolConfig;
	
	/**
	 * 
	 * @param ffanRedisProxyPoolConfig
	 */
	public LBRedisConnectionFactory(LBRedisProxyPoolConfig ffanRedisProxyPoolConfig) {
		super();
		this.ffanRedisProxyPoolConfig = ffanRedisProxyPoolConfig;
	}

	@Override
	public IConnection createInstance() throws LBRedisProxyPoolException {
		LBRedisConnection ffanRedisConnection=new LBRedisConnection(ffanRedisProxyPoolConfig);
		ffanRedisConnection.open();
		return ffanRedisConnection;
	}



	@Override
	public Boolean validateEntry(IConnection connection)
			throws LBRedisProxyPoolException {
		return connection.isAvailable();
	}



	@Override
	public void destroyEntry(IConnection connection) throws LBRedisProxyPoolException {
		connection.close();
	}
	
	
	
}
