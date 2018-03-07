/**
 * 
 */
package com.opensource.netty.redis.proxy.spring.schema;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.commons.utils.StringUtils;
import com.opensource.netty.redis.proxy.spring.schema.support.RedisProxySlave;
import com.opensource.netty.redis.proxy.spring.schema.support.RedisProxyConstant;
import com.opensource.netty.redis.proxy.spring.schema.support.RedisProxyMaster;
import com.opensource.netty.redis.proxy.spring.schema.support.RedisProxyNode;

/**
 * 
 * @author liubing
 *
 */
public class RedisProxyNodeParser extends AbstractSimpleBeanDefinitionParser {
	
	public RedisProxyNodeParser(){
		
	}
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		return RedisProxyNode.class;
	}
	
	/**
	 * 解析
	 */
	@Override
	protected void doParse(Element element, ParserContext parserContext,BeanDefinitionBuilder builder) {
		try{
			String redisProxyHost = element.getAttribute(RedisProxyConstant.REDISPROXYHOST);
			String redisProxyPort=element.getAttribute(RedisProxyConstant.REDISPROXYPORT);
			String address=element.getAttribute(RedisConstants.ADDRESS);
			
			String algorithmRefName=element.getAttribute(RedisProxyConstant.ALGORITHMREF);
			RuntimeBeanReference ruleAlgorithm = new RuntimeBeanReference(algorithmRefName, false);
	        ruleAlgorithm.setSource(parserContext.getDelegate().getReaderContext().extractSource(element));
			builder.setLazyInit(false);
			builder.addPropertyValue(RedisProxyConstant.LOADMASTERRBALANCE, ruleAlgorithm);//主 的算法
			builder.addPropertyValue(RedisProxyConstant.REDISPROXYHOST, redisProxyHost);
			builder.addPropertyValue(RedisConstants.ADDRESS, address);
			builder.addPropertyValue(RedisProxyConstant.REDISPROXYPORT, Integer.parseInt(redisProxyPort));
			builder.addPropertyValue(RedisProxyConstant.REDISPROXYMASTERS,
					getRedisProxyMasters(element, parserContext, builder));
		}catch(Exception e){
			parserContext.getReaderContext().error(
					"class " + RedisProxyNode.class.getName()
							+ " can not be create", element, e);
		}
		super.doParse(element, parserContext, builder);
	}
	
	/**
	 * 获取主
	 * @param mapEle
	 * @param parserContext
	 * @param builder
	 * @return
	 */
	private List<BeanDefinition> getRedisProxyMasters(Element mapEle, ParserContext parserContext,
			BeanDefinitionBuilder builder){
		List<?> entryEles = DomUtils.getChildElementsByTagName(mapEle, RedisProxyConstant.REDISPROXYMASTER);
		List<BeanDefinition> result = new ManagedList<>(entryEles.size());
		if(entryEles!=null&&entryEles.size()>0){//主
			
			for (Iterator<?> it = entryEles.iterator(); it.hasNext();) {
				BeanDefinitionBuilder redisProxyMasterFactory = BeanDefinitionBuilder.rootBeanDefinition(RedisProxyMaster.class);
				Element masterElement = (Element) it.next();
				parseMasterElement(masterElement,parserContext, redisProxyMasterFactory);				
	            result.add(redisProxyMasterFactory.getBeanDefinition());
			}
			
		}
		return result;
	}
	
	/**
	 * 处理主
	 * @param element
	 * @param builder
	 */
	private void parseMasterElement(Element element,ParserContext parserContext,BeanDefinitionBuilder builder){
		String host = element.getAttribute(RedisProxyConstant.HOST);
		String port=element.getAttribute(RedisProxyConstant.PORT);
		String connectionTimeout=element.getAttribute(RedisProxyConstant.CONNECTIONTIMEOUT);
		int maxActiveConnection=Integer.parseInt(element.getAttribute(RedisProxyConstant.MAXACTIVECONNECTION));
		int maxIdleConnection=Integer.parseInt(element.getAttribute(RedisProxyConstant.MAXIDLECONNECTION));
		int minConnection=Integer.parseInt(element.getAttribute(RedisProxyConstant.MINCONNECTION));
		
		int initialConnection=Integer.parseInt(element.getAttribute(RedisProxyConstant.INITIALCONNECTION));
		int maxWaitMillisOnBorrow=Integer.parseInt(element.getAttribute(RedisProxyConstant.MAXWAITMILLISONBORROW));
		int timeBetweenEvictionRunsMillis=Integer.parseInt(element.getAttribute(RedisProxyConstant.TIMEBETWEENEVICTIONRUNSMILLIS));
		int minEvictableIdleTimeMillis=Integer.parseInt(element.getAttribute(RedisProxyConstant.MINEVICTABLEIDLETIMEMILLIS));
		int minIdleEntries=Integer.parseInt(element.getAttribute(RedisProxyConstant.MINIDLEENTRIES));
		Boolean  testOnBorrow =Boolean.parseBoolean(element.getAttribute(RedisProxyConstant.TESTONBORROW));
		Boolean  testOnReturn =Boolean.parseBoolean(element.getAttribute(RedisProxyConstant.TESTONRETURN));
		Boolean  testWhileIdle=Boolean.parseBoolean(element.getAttribute(RedisProxyConstant.TESTWHILEIDLE));
		
		
		String algorithmRefName=element.getAttribute(RedisProxyConstant.ALGORITHMREF);
		if(StringUtils.isNotBlank(algorithmRefName)){
			RuntimeBeanReference ruleAlgorithm = new RuntimeBeanReference(algorithmRefName, false);
	        ruleAlgorithm.setSource(parserContext.getDelegate().getReaderContext().extractSource(element));
	        builder.addPropertyValue(RedisProxyConstant.LOADCLUSTERBALANCE, ruleAlgorithm);//从 的算法
		}		      
		builder.setLazyInit(false);
		builder.addPropertyValue(RedisProxyConstant.HOST, host);
		builder.addPropertyValue(RedisProxyConstant.PORT, Integer.parseInt(port));
		builder.addPropertyValue(RedisProxyConstant.CONNECTIONTIMEOUT, Integer.parseInt(connectionTimeout));
		builder.addPropertyValue(RedisProxyConstant.MAXACTIVECONNECTION, maxActiveConnection);
		builder.addPropertyValue(RedisProxyConstant.MAXIDLECONNECTION, maxIdleConnection);
		builder.addPropertyValue(RedisProxyConstant.MINCONNECTION, minConnection);
		
		builder.addPropertyValue(RedisProxyConstant.INITIALCONNECTION, initialConnection);
		builder.addPropertyValue(RedisProxyConstant.MAXWAITMILLISONBORROW, maxWaitMillisOnBorrow);
		builder.addPropertyValue(RedisProxyConstant.TIMEBETWEENEVICTIONRUNSMILLIS, timeBetweenEvictionRunsMillis);
		builder.addPropertyValue(RedisProxyConstant.MINEVICTABLEIDLETIMEMILLIS, minEvictableIdleTimeMillis);
		builder.addPropertyValue(RedisProxyConstant.MINIDLEENTRIES, minIdleEntries);
		builder.addPropertyValue(RedisProxyConstant.TESTONBORROW, testOnBorrow);
		builder.addPropertyValue(RedisProxyConstant.TESTONRETURN, testOnReturn);
		builder.addPropertyValue(RedisProxyConstant.TESTWHILEIDLE, testWhileIdle);
		
		
		List<BeanDefinition> proxySlaves=getRedisProxySlaves(element);
		builder.addPropertyValue(RedisProxyConstant.REDISPROXYCLUSTERS, proxySlaves);
	}
	
	/**
	 * 处理从
	 * @param masterElement
	 * @return
	 */
	private List<BeanDefinition> getRedisProxySlaves(Element masterElement){
		List<?> entryClusterEles = DomUtils.getChildElementsByTagName(masterElement, RedisProxyConstant.REDISPROXYSLAVE);
		List<BeanDefinition> result = new ManagedList<>(entryClusterEles.size());
		if(entryClusterEles!=null&&entryClusterEles.size()>0){//从
			for (Iterator<?> it = entryClusterEles.iterator(); it.hasNext();) {
				BeanDefinitionBuilder redisProxySlaveFactory = BeanDefinitionBuilder.rootBeanDefinition(RedisProxySlave.class);
				Element clusterElement = (Element) it.next();
				parseSlaveElement(clusterElement, redisProxySlaveFactory);
				result.add(redisProxySlaveFactory.getBeanDefinition());
			}
		}
		return result;
	}
	
	private void parseSlaveElement(Element element,BeanDefinitionBuilder builder){
		String host = element.getAttribute(RedisProxyConstant.HOST);
		String port=element.getAttribute(RedisProxyConstant.PORT);
		String connectionTimeout=element.getAttribute(RedisProxyConstant.CONNECTIONTIMEOUT);
		int maxActiveConnection=Integer.parseInt(element.getAttribute(RedisProxyConstant.MAXACTIVECONNECTION));
		int maxIdleConnection=Integer.parseInt(element.getAttribute(RedisProxyConstant.MAXIDLECONNECTION));
		int minConnection=Integer.parseInt(element.getAttribute(RedisProxyConstant.MINCONNECTION));
		int initialConnection=Integer.parseInt(element.getAttribute(RedisProxyConstant.INITIALCONNECTION));
		int maxWaitMillisOnBorrow=Integer.parseInt(element.getAttribute(RedisProxyConstant.MAXWAITMILLISONBORROW));
		int timeBetweenEvictionRunsMillis=Integer.parseInt(element.getAttribute(RedisProxyConstant.TIMEBETWEENEVICTIONRUNSMILLIS));
		int minEvictableIdleTimeMillis=Integer.parseInt(element.getAttribute(RedisProxyConstant.MINEVICTABLEIDLETIMEMILLIS));
		int minIdleEntries=Integer.parseInt(element.getAttribute(RedisProxyConstant.MINIDLEENTRIES));
		Boolean  testOnBorrow =Boolean.parseBoolean(element.getAttribute(RedisProxyConstant.TESTONBORROW));
		Boolean  testOnReturn =Boolean.parseBoolean(element.getAttribute(RedisProxyConstant.TESTONRETURN));
		Boolean  testWhileIdle=Boolean.parseBoolean(element.getAttribute(RedisProxyConstant.TESTWHILEIDLE));
		
		int weight=1;
		if(element.getAttribute(RedisProxyConstant.WEIGHT)!=null){
			weight=Integer.parseInt(element.getAttribute(RedisProxyConstant.WEIGHT));
			builder.addPropertyValue(RedisProxyConstant.WEIGHT, weight);
		}
		builder.setLazyInit(false);
		builder.addPropertyValue(RedisProxyConstant.HOST, host);
		builder.addPropertyValue(RedisProxyConstant.PORT, Integer.parseInt(port));
		builder.addPropertyValue(RedisProxyConstant.CONNECTIONTIMEOUT, Integer.parseInt(connectionTimeout));
		builder.addPropertyValue(RedisProxyConstant.MAXACTIVECONNECTION, maxActiveConnection);
		builder.addPropertyValue(RedisProxyConstant.MAXIDLECONNECTION, maxIdleConnection);
		builder.addPropertyValue(RedisProxyConstant.MINCONNECTION, minConnection);
		builder.addPropertyValue(RedisProxyConstant.INITIALCONNECTION, initialConnection);
		builder.addPropertyValue(RedisProxyConstant.MAXWAITMILLISONBORROW, maxWaitMillisOnBorrow);
		builder.addPropertyValue(RedisProxyConstant.TIMEBETWEENEVICTIONRUNSMILLIS, timeBetweenEvictionRunsMillis);
		builder.addPropertyValue(RedisProxyConstant.MINEVICTABLEIDLETIMEMILLIS, minEvictableIdleTimeMillis);
		builder.addPropertyValue(RedisProxyConstant.MINIDLEENTRIES, minIdleEntries);
		builder.addPropertyValue(RedisProxyConstant.TESTONBORROW, testOnBorrow);
		builder.addPropertyValue(RedisProxyConstant.TESTONRETURN, testOnReturn);
		builder.addPropertyValue(RedisProxyConstant.TESTWHILEIDLE, testWhileIdle);
	}
}
