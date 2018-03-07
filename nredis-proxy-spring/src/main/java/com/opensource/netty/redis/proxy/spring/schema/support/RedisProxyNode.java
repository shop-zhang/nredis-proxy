/**
 * 
 */
package com.opensource.netty.redis.proxy.spring.schema.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;
import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.core.cluster.LoadBalance;
import com.opensource.netty.redis.proxy.core.config.LBRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerClusterBean;
import com.opensource.netty.redis.proxy.core.enums.RedisProxyParamType;
import com.opensource.netty.redis.proxy.core.registry.Registry;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;
import com.opensource.netty.redis.proxy.net.server.LBRedisServer;
import com.opensource.netty.redis.proxy.zk.registry.ZookeeperRegistryFactory;
import com.opensource.netty.redis.proxy.zk.registry.listen.ZookeeperRegistryListen;

/**
 * redis proxy节点
 * @author liubing
 *
 */
public class RedisProxyNode implements InitializingBean {

	
	private List<RedisProxyMaster> redisProxyMasters;
	
	private String redisProxyHost;//主机名
	
	private int redisProxyPort;//端口号
	
	private String algorithmRef;
	
	private LoadBalance loadMasterBalance;//主的一致性算法
	
	private String address;//ZK地址
	/**
	 * 
	 */
	public RedisProxyNode() {
		super();
	}

	/**
	 * @return the redisProxyMasters
	 */
	public List<RedisProxyMaster> getRedisProxyMasters() {
		return redisProxyMasters;
	}

	/**
	 * @param redisProxyMasters the redisProxyMasters to set
	 */
	public void setRedisProxyMasters(List<RedisProxyMaster> redisProxyMasters) {
		this.redisProxyMasters = redisProxyMasters;
	}
	
	/**
	 * 获取 master-cluster
	 * @return
	 */
	private LBRedisServerMasterCluster getFfanRedisServerMasterCluster(){
		
		
		List<LBRedisServerClusterBean> redisServerClusterBeans=new ArrayList<LBRedisServerClusterBean>();
		if(redisProxyMasters!=null&&redisProxyMasters.size()>0){
			for(RedisProxyMaster redisProxyMaster:redisProxyMasters){
				LBRedisServerClusterBean ffanRedisServerClusterBean=new LBRedisServerClusterBean();
				LBRedisServerBean lbRedisServerBean=convertLBRedisServerBean(redisProxyMaster);
				ffanRedisServerClusterBean.setLoadClusterBalance(redisProxyMaster.getLoadClusterBalance());
				ffanRedisServerClusterBean.setFfanMasterRedisServerBean(lbRedisServerBean);
				
				List<LBRedisServerBean> ffanRedisServerBeans=new ArrayList<LBRedisServerBean>();
				
				for(RedisProxySlave proxySlave:redisProxyMaster.getRedisProxyClusters()){
					LBRedisServerBean redisServerBean=convertSlaveLBRedisServerBean(proxySlave);
					ffanRedisServerBeans.add(redisServerBean);
					ffanRedisServerClusterBean.setFfanRedisServerClusterBeans(ffanRedisServerBeans);
				}
				redisServerClusterBeans.add(ffanRedisServerClusterBean);
			}
		}
		LBRedisServerMasterCluster ffanRedisServerMasterCluster=new LBRedisServerMasterCluster(redisServerClusterBeans);
		ffanRedisServerMasterCluster.setRedisProxyHost(redisProxyHost);
		ffanRedisServerMasterCluster.setRedisProxyPort(redisProxyPort);
		ffanRedisServerMasterCluster.setLoadMasterBalance(getLoadMasterBalance());
		return ffanRedisServerMasterCluster;

	}
	
	 /**
	   * 转换
	   * @param lbRedisServerBean
	   * @return
	   */
	  private LBRedisServerBean convertSlaveLBRedisServerBean(RedisProxySlave proxySlave){
		  LBRedisServerBean lbRedisServerBean=new LBRedisServerBean();
		  lbRedisServerBean.setConnectionTimeout(proxySlave.getConnectionTimeout());
		  lbRedisServerBean.setHost(proxySlave.getHost());
		  lbRedisServerBean.setInitialConnection(proxySlave.getInitialConnection());
		  lbRedisServerBean.setMaxActiveConnection(proxySlave.getMaxActiveConnection());
		  lbRedisServerBean.setMaxWaitMillisOnBorrow(proxySlave.getMaxWaitMillisOnBorrow());
		  lbRedisServerBean.setMinConnection(proxySlave.getMinConnection());
		  lbRedisServerBean.setMinEvictableIdleTimeMillis(proxySlave.getMinEvictableIdleTimeMillis());
		  lbRedisServerBean.setMinIdleEntries(proxySlave.getMinIdleEntries());
		  lbRedisServerBean.setPort(proxySlave.getPort());
		  lbRedisServerBean.setTestOnBorrow(proxySlave.getTestOnBorrow());
		  lbRedisServerBean.setTestOnReturn(proxySlave.getTestOnReturn());
		  lbRedisServerBean.setTestWhileIdle(proxySlave.getTestWhileIdle());
		  lbRedisServerBean.setTimeBetweenEvictionRunsMillis(proxySlave.getTimeBetweenEvictionRunsMillis());
		  return lbRedisServerBean;
	  }
	
	 /**
	   * 转换
	   * @param lbRedisServerBean
	   * @return
	   */
	  private LBRedisServerBean convertLBRedisServerBean(RedisProxyMaster redisProxyMaster){
		  LBRedisServerBean lbRedisServerBean=new LBRedisServerBean();
		  lbRedisServerBean.setConnectionTimeout(redisProxyMaster.getConnectionTimeout());
		  lbRedisServerBean.setHost(redisProxyMaster.getHost());
		  lbRedisServerBean.setInitialConnection(redisProxyMaster.getInitialConnection());
		  lbRedisServerBean.setMaxActiveConnection(redisProxyMaster.getMaxActiveConnection());
		  lbRedisServerBean.setMaxWaitMillisOnBorrow(redisProxyMaster.getMaxWaitMillisOnBorrow());
		  lbRedisServerBean.setMinConnection(redisProxyMaster.getMinConnection());
		  lbRedisServerBean.setMinEvictableIdleTimeMillis(redisProxyMaster.getMinEvictableIdleTimeMillis());
		  lbRedisServerBean.setMinIdleEntries(redisProxyMaster.getMinIdleEntries());
		  lbRedisServerBean.setPort(redisProxyMaster.getPort());
		  lbRedisServerBean.setTestOnBorrow(redisProxyMaster.getTestOnBorrow());
		  lbRedisServerBean.setTestOnReturn(redisProxyMaster.getTestOnReturn());
		  lbRedisServerBean.setTestWhileIdle(redisProxyMaster.getTestWhileIdle());
		  lbRedisServerBean.setTimeBetweenEvictionRunsMillis(redisProxyMaster.getTimeBetweenEvictionRunsMillis());
		  return lbRedisServerBean;
	  }
	
	/**
	 * 设置以后初始化
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		LBRedisServerMasterCluster ffanRedisServerMasterCluster=getFfanRedisServerMasterCluster();
		initRegistry(ffanRedisServerMasterCluster);
		startRedisProxyServer(ffanRedisServerMasterCluster);
	
	}
	
	/**
	 * 初始化zk
	 * @param ffanRedisServerMasterCluster
	 */
	private void initRegistry(LBRedisServerMasterCluster ffanRedisServerMasterCluster){
		ZookeeperRegistryFactory zookeeperRegistryFactory=new ZookeeperRegistryFactory();

		RedisProxyURL redisProxyURL=new RedisProxyURL();
		redisProxyURL.addParameter(RedisConstants.ADDRESS, this.getAddress());
		Registry registry=zookeeperRegistryFactory.getRegistry(redisProxyURL);
		ZookeeperRegistryListen registryListen=new ZookeeperRegistryListen(ffanRedisServerMasterCluster);
		if(ffanRedisServerMasterCluster.getMasters()!=null&&ffanRedisServerMasterCluster.getMasters().size()>0){
			for(LBRedisServerBean ffanRedisServerBean:ffanRedisServerMasterCluster.getMasters()){
				RedisProxyURL newRedisProxyURL=new RedisProxyURL(ffanRedisServerBean.getHost(),ffanRedisServerBean.getPort(),ffanRedisServerBean.getTimeout());
				newRedisProxyURL.addParameter(RedisProxyParamType.REDISSERVER.getName(), JSONObject.toJSONString(ffanRedisServerBean));
				registry.register(newRedisProxyURL, registryListen);//监听主的变化
				List<LBRedisServerBean> ffanRedisServerBeans=ffanRedisServerMasterCluster.getMasterFfanRedisServerBean(ffanRedisServerBean.getKey());
				if(ffanRedisServerBeans!=null){
					for(LBRedisServerBean ffanClusterRedisServerBean:ffanRedisServerBeans){//从
						RedisProxyURL newClusterRedisProxyURL=new RedisProxyURL(ffanClusterRedisServerBean.getHost(),ffanClusterRedisServerBean.getPort(),ffanClusterRedisServerBean.getTimeout());
						newClusterRedisProxyURL.setParentServerPath(ffanRedisServerBean.getServerKey());
						registry.createPersistent(newClusterRedisProxyURL, JSONObject.toJSONString(ffanClusterRedisServerBean));//写入从
					}
				}
			}
			
		}
		
	}
	
	/**
	 * 启动server 容器
	 */
	private void startRedisProxyServer(LBRedisServerMasterCluster ffanRedisServerMasterCluster){
		LBRedisServer ffanRedisServer=new LBRedisServer(ffanRedisServerMasterCluster);
		
		ffanRedisServer.start();
	}
	/**
	 * @return the redisProxyHost
	 */
	public String getRedisProxyHost() {
		return redisProxyHost;
	}

	/**
	 * @param redisProxyHost the redisProxyHost to set
	 */
	public void setRedisProxyHost(String redisProxyHost) {
		this.redisProxyHost = redisProxyHost;
	}

	/**
	 * @return the redisProxyPort
	 */
	public int getRedisProxyPort() {
		return redisProxyPort;
	}

	/**
	 * @param redisProxyPort the redisProxyPort to set
	 */
	public void setRedisProxyPort(int redisProxyPort) {
		this.redisProxyPort = redisProxyPort;
	}

	/**
	 * @return the loadMasterBalance
	 */
	public LoadBalance getLoadMasterBalance() {
		return loadMasterBalance;
	}

	/**
	 * @param loadMasterBalance the loadMasterBalance to set
	 */
	public void setLoadMasterBalance(LoadBalance loadMasterBalance) {
		this.loadMasterBalance = loadMasterBalance;
	}

	/**
	 * @return the algorithmRef
	 */
	public String getAlgorithmRef() {
		return algorithmRef;
	}

	/**
	 * @param algorithmRef the algorithmRef to set
	 */
	public void setAlgorithmRef(String algorithmRef) {
		this.algorithmRef = algorithmRef;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
