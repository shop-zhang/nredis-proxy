/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.opensource.netty.redis.proxy.pool.LBRedisProxyIdleEntriesQueue;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.commons.Pool;



/**
 * 队列实现类
 * @author liubing
 *
 */
public class LBRedisProxyPoolBasicIdleEntriesQueue<T extends Pool> implements LBRedisProxyIdleEntriesQueue<T> {
	
	private final ArrayBlockingQueue<LBRedisProxyPoolEntry<T>> idleEntries;
	
	public LBRedisProxyPoolBasicIdleEntriesQueue(LBRedisProxyPoolConfig config) {
		idleEntries = new ArrayBlockingQueue<LBRedisProxyPoolEntry<T>>(
				config.getMaxActiveEntries());
		
	}
	
	@Override
	public LBRedisProxyPoolEntry<T> poll(long timeout, TimeUnit unit) throws InterruptedException {
		
		LBRedisProxyPoolEntry<T> idle = idleEntries.poll(timeout,TimeUnit.MILLISECONDS);
		return idle;	
	}

	@Override
	public boolean offer(LBRedisProxyPoolEntry<T> entry) throws NullPointerException {
		if (entry == null)
			throw new NullPointerException("entry is null.");

		boolean offerSuccessful = idleEntries.offer(entry);
		return offerSuccessful;
	}

	public ArrayBlockingQueue<LBRedisProxyPoolEntry<T>> getIdleEntries() {
		return idleEntries;
	}

	@Override
	public int getIdleEntriesCount() {
		return idleEntries.size();
	}

	@Override
	public void clear() {
		
		idleEntries.clear();
	}

	@Override
	public LBRedisProxyPoolEntry<T> poll() {
		LBRedisProxyPoolEntry<T> idle = idleEntries.poll();
		return idle;
	}
	
	
}
