/**
 * 
 */
package com.opensource.netty.redis.proxy.net.client;

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensource.netty.redis.proxy.core.client.impl.AbstractPoolClient;
import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;
/**
 * shard connection
 * @author liubing
 *
 */
public class LBRedisClient extends AbstractPoolClient{

	private Logger logger = LoggerFactory.getLogger(LBRedisClient.class);
	
	private LBRedisProxyPoolConfig lbRedisProxyPoolConfig;

	/**
	 * @param conf
	 */
	public LBRedisClient(LBRedisProxyPoolConfig lbRedisProxyPoolConfig) {
		 super(lbRedisProxyPoolConfig);		 
		 this.lbRedisProxyPoolConfig=lbRedisProxyPoolConfig;
		 super.initPool();//初始化连接池
	}
	
	/**
	 * 关闭
	 */
	@Override
	public synchronized void close() {
		 try {	
	        	super.pool.shutDown();//连接池关闭,所有都关闭
	        } catch (Exception e) {
	        	logger.error("NettyClient close Error,HOST:"+lbRedisProxyPoolConfig.getHost()+",PORT:"+lbRedisProxyPoolConfig.getPort(), e);
	        }
	}

	/**
	 * 创建对象
	 */
	@Override
	protected LBRedisProxyPooledObjectFactory<IConnection> createChannelFactory() {
		return new LBRedisConnectionFactory(super.ffanRedisProxyPoolConfig);
	}
    
	@Override
	public void write(RedisCommand request,Channel frontChannel) {
		IConnection connection=null;
		LBRedisProxyPoolEntry<IConnection> entry=null;
		try{
			entry  = borrowObject();
        	if(entry==null||entry.getObject()==null){
        		logger.error("NettyClient borrowObject null");
                return ;
        	}
        	connection=entry.getObject();
            connection.write(request,frontChannel);
            
		}catch(Exception e){
			logger.error("NettyClient write request Error :" , e);
		}finally{
			returnObject(entry);
		}
		
	}
   
   
}
