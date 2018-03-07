/**
 * 
 */
package com.opensource.netty.redis.proxy.spring.schema.support;

import java.io.Serializable;
import java.util.List;

import com.opensource.netty.redis.proxy.core.cluster.LoadBalance;
/**
 * 主节点
 * @author liubing
 *
 */
public class RedisProxyMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6861660674247475024L;
	
	private String host;//主机名
	
	private int port;//端口号
	
	private int maxActiveConnection;//最大活动连接数
	
	private int maxIdleConnection;//最大 空闲连接数
	
	private int minConnection;//最小连接数
	
	private int initialConnection = 0;//初始化值
	
	private long maxWaitMillisOnBorrow; // 最大等待时间
	
	private long timeBetweenEvictionRunsMillis;//回收间隔时间点
	
	private long minEvictableIdleTimeMillis;//池中最小生存的时间
	
	private int minIdleEntries = 0;//最小等待时间
	
	private Boolean testOnBorrow=false;//取出验证
	
	private int connectionTimeout;//连接超时
	
	private Boolean testOnReturn=false;//回收时验证
	
	private Boolean testWhileIdle=false;//回收空闲验证
	
	private List<RedisProxySlave> redisProxyClusters;//多个从
	
	private LoadBalance loadClusterBalance;//从权重
	
	/**
	 * 
	 */
	public RedisProxyMaster() {
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
	 * @return the redisProxyClusters
	 */
	public List<RedisProxySlave> getRedisProxyClusters() {
		return redisProxyClusters;
	}

	/**
	 * @param redisProxyClusters the redisProxyClusters to set
	 */
	public void setRedisProxyClusters(List<RedisProxySlave> redisProxyClusters) {
		this.redisProxyClusters = redisProxyClusters;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(host).append("-").append(port);
		return stringBuilder.toString();
	}

	/**
	 * @return the loadClusterBalance
	 */
	public LoadBalance getLoadClusterBalance() {
		return loadClusterBalance;
	}

	/**
	 * @param loadClusterBalance the loadClusterBalance to set
	 */
	public void setLoadClusterBalance(LoadBalance loadClusterBalance) {
		this.loadClusterBalance = loadClusterBalance;
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
	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	/**
	 * @param testOnBorrow the testOnBorrow to set
	 */
	public void setTestOnBorrow(Boolean testOnBorrow) {
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
	public Boolean getTestOnReturn() {
		return testOnReturn;
	}

	/**
	 * @param testOnReturn the testOnReturn to set
	 */
	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	/**
	 * @return the testWhileIdle
	 */
	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}

	/**
	 * @param testWhileIdle the testWhileIdle to set
	 */
	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	
	
}
