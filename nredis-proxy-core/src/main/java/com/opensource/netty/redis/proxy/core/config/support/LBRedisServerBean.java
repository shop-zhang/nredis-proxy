/**
 * 
 */
package com.opensource.netty.redis.proxy.core.config.support;

import com.opensource.netty.redis.proxy.commons.algorithm.impl.support.RedisWeight;
import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;

/**
 * @author liubing
 *
 */
public class LBRedisServerBean implements RedisWeight{
	
	private String host;//主机名
	
	private int port;//端口号
	
	private int timeout;//超时时间
	
	private int maxActiveConnection;//最大活动连接数
	
	private int maxIdleConnection;//最大 空闲连接数
	
	private int minConnection;//最小连接数
	
	private int initialConnection = 0;//初始化值
	
	private long maxWaitMillisOnBorrow; // 最大等待时间
	
	private long timeBetweenEvictionRunsMillis;//回收间隔时间点
	
	private long minEvictableIdleTimeMillis;//池中最小生存的时间
	
	private int minIdleEntries = 0;//最小等待时间
	
	private boolean testOnBorrow=false;//取出验证
	
	private int connectionTimeout;//连接超时
	
	private boolean testOnReturn=false;//回收时验证
	
	private boolean testWhileIdle=false;//回收空闲验证
	
	private int weight=1;//默认权重比例为1
	
	

	/**
	 * @param host
	 * @param port
	 * @param timeout
	 * @param maxActiveConnection
	 * @param maxIdleConnection
	 * @param minConnection
	 * @param initialConnection
	 * @param maxWaitMillisOnBorrow
	 * @param timeBetweenEvictionRunsMillis
	 * @param minEvictableIdleTimeMillis
	 * @param minIdleEntries
	 * @param testOnBorrow
	 * @param connectionTimeout
	 * @param testOnReturn
	 * @param testWhileIdle
	 * @param weight
	 */
	public LBRedisServerBean(String host, int port, int timeout,
			int maxActiveConnection, int maxIdleConnection, int minConnection,
			int initialConnection, long maxWaitMillisOnBorrow,
			long timeBetweenEvictionRunsMillis,
			long minEvictableIdleTimeMillis, int minIdleEntries,
			boolean testOnBorrow, int connectionTimeout, boolean testOnReturn,
			boolean testWhileIdle, int weight) {
		super();
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.maxActiveConnection = maxActiveConnection;
		this.maxIdleConnection = maxIdleConnection;
		this.minConnection = minConnection;
		this.initialConnection = initialConnection;
		this.maxWaitMillisOnBorrow = maxWaitMillisOnBorrow;
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		this.minIdleEntries = minIdleEntries;
		this.testOnBorrow = testOnBorrow;
		this.connectionTimeout = connectionTimeout;
		this.testOnReturn = testOnReturn;
		this.testWhileIdle = testWhileIdle;
		this.weight = weight;
	}

	/**
	 * 
	 */
	public LBRedisServerBean() {
		super();
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the maxActiveConnection
	 */
	public int getMaxActiveConnection() {
		return maxActiveConnection;
	}

	/**
	 * @param maxActiveConnection the maxActiveConnection to set
	 */
	public void setMaxActiveConnection(int maxActiveConnection) {
		this.maxActiveConnection = maxActiveConnection;
	}

	/**
	 * @return the maxIdleConnection
	 */
	public int getMaxIdleConnection() {
		return maxIdleConnection;
	}

	/**
	 * @param maxIdleConnection the maxIdleConnection to set
	 */
	public void setMaxIdleConnection(int maxIdleConnection) {
		this.maxIdleConnection = maxIdleConnection;
	}

	/**
	 * @return the minConnection
	 */
	public int getMinConnection() {
		return minConnection;
	}

	/**
	 * @param minConnection the minConnection to set
	 */
	public void setMinConnection(int minConnection) {
		this.minConnection = minConnection;
	}
	
	/**
	 * 关键key
	 * @return
	 */
	public String getKey(){
		StringBuffer sbBuffer=new StringBuffer();
		sbBuffer.append(RedisConstants.REDIS_PROXY).append(host).append(RedisConstants.SEPERATOR_ACCESS_LOG).append(port);
		return sbBuffer.toString();
	}
	
	public String getServerKey(){
		StringBuffer sbBuffer=new StringBuffer();
		sbBuffer.append(host).append(RedisConstants.PROTOCOL_SEPARATOR).append(port);
		return sbBuffer.toString();
	}
	
	@Override
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + maxActiveConnection;
		result = prime * result + maxIdleConnection;
		result = prime * result + minConnection;
		result = prime * result + port;
		result = prime * result + timeout;
		result = prime * result + weight;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LBRedisServerBean other = (LBRedisServerBean) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (maxActiveConnection != other.maxActiveConnection)
			return false;
		if (maxIdleConnection != other.maxIdleConnection)
			return false;
		if (minConnection != other.minConnection)
			return false;
		if (port != other.port)
			return false;
		if (timeout != other.timeout)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	/**
	 * @return the initialConnection
	 */
	public int getInitialConnection() {
		return initialConnection;
	}

	/**
	 * @param initialConnection the initialConnection to set
	 */
	public void setInitialConnection(int initialConnection) {
		this.initialConnection = initialConnection;
	}

	/**
	 * @return the maxWaitMillisOnBorrow
	 */
	public long getMaxWaitMillisOnBorrow() {
		return maxWaitMillisOnBorrow;
	}

	/**
	 * @param maxWaitMillisOnBorrow the maxWaitMillisOnBorrow to set
	 */
	public void setMaxWaitMillisOnBorrow(long maxWaitMillisOnBorrow) {
		this.maxWaitMillisOnBorrow = maxWaitMillisOnBorrow;
	}

	/**
	 * @return the timeBetweenEvictionRunsMillis
	 */
	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	/**
	 * @param timeBetweenEvictionRunsMillis the timeBetweenEvictionRunsMillis to set
	 */
	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	/**
	 * @return the minEvictableIdleTimeMillis
	 */
	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	/**
	 * @param minEvictableIdleTimeMillis the minEvictableIdleTimeMillis to set
	 */
	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	/**
	 * @return the minIdleEntries
	 */
	public int getMinIdleEntries() {
		return minIdleEntries;
	}

	/**
	 * @param minIdleEntries the minIdleEntries to set
	 */
	public void setMinIdleEntries(int minIdleEntries) {
		this.minIdleEntries = minIdleEntries;
	}

	/**
	 * @return the testOnBorrow
	 */
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	/**
	 * @param testOnBorrow the testOnBorrow to set
	 */
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout the connectionTimeout to set
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return the testOnReturn
	 */
	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	/**
	 * @param testOnReturn the testOnReturn to set
	 */
	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	/**
	 * @return the testWhileIdle
	 */
	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	/**
	 * @param testWhileIdle the testWhileIdle to set
	 */
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	
	
}
