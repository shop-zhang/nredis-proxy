/**
 * 
 */
package com.opensource.netty.redis.proxy.net.server.support;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.commons.utils.ProtoUtils;
import com.opensource.netty.redis.proxy.core.client.impl.AbstractPoolClient;
import com.opensource.netty.redis.proxy.core.cluster.LoadBalance;
import com.opensource.netty.redis.proxy.core.cluster.impl.support.RedisQuestBean;
import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.core.config.LBRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerClusterBean;
import com.opensource.netty.redis.proxy.core.enums.RedisCommandEnums;
import com.opensource.netty.redis.proxy.core.reply.impl.ErrorRedisReply;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * redis服务端回答
 * 
 * @author liubing
 *
 */
public class LBRedisServerHandler extends SimpleChannelInboundHandler<RedisCommand> {

	  private Logger logger = LoggerFactory.getLogger(LBRedisServerHandler.class);
	  

	  private Map<String, AbstractPoolClient> ffanRedisServerBeanMap;
	  
	  private LBRedisServerMasterCluster ffanRedisServerMasterCluster;

	  
	  public LBRedisServerHandler(Map<String, AbstractPoolClient> ffanRedisServerBeanMap,LBRedisServerMasterCluster ffanRedisServerMasterCluster) {
		  this.ffanRedisServerMasterCluster=ffanRedisServerMasterCluster;
		  this.ffanRedisServerBeanMap=ffanRedisServerBeanMap;
	  }
	  
	 
	  @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		//init(ctx.channel());
	}
	  
	  /**
	   * 接受请求
	   */
	  @Override
	  protected void channelRead0(ChannelHandlerContext ctx, RedisCommand request) throws Exception {

			    String command=new String(request.getArgs().get(0));
			    if(request!=null&&request.getArgs().size()>1&&!command.equals(RedisConstants.KEYS)){//第一个是命令，第二个是key
			    	
			    	RedisCommandEnums commandEnums=getRedisCommandEnums(command);
			    	if(commandEnums!=null&&commandEnums.isIswrite()){//主
			    		AbstractPoolClient ffanRedisClient=getShardFfanRedisClient(request,command);//默认一致性hash算法				    	
			    		ffanRedisClient.write(request,ctx.channel());
			    	}else if(commandEnums!=null&&!commandEnums.isIswrite()){//从
			    		AbstractPoolClient ffanRedisClient=getShardClusterFfanRedisClient(request,command,commandEnums.isIswrite());//权重算法			    		
			    		ffanRedisClient.write(request,ctx.channel());
			    	}
			    }else if(request!=null&&request.getArgs().size()>1&&command.equals(RedisConstants.KEYS)){//keys 级别 找主
			    	for(LBRedisServerBean ffanRedisServerBean:ffanRedisServerMasterCluster.getMasters()){
			    		AbstractPoolClient ffanRedisClient=ffanRedisServerBeanMap.get(ffanRedisServerBean.getKey());
			    		ffanRedisClient.write(request,ctx.channel());
			    	}
			    }else if(request!=null&&request.getArgs().size()==1){//info 级别
			    	for(String key:ffanRedisServerBeanMap.keySet()){
			    		AbstractPoolClient ffanRedisClient=ffanRedisServerBeanMap.get(key);
			    		ffanRedisClient.write(request,ctx.channel());
			    	}
			    }   
	  }
	  
	  /**
	   * 根据参数获取枚举类
	   * @param command
	   * @return
	   */
	  private RedisCommandEnums getRedisCommandEnums(String command){
		  try{
			 return  RedisCommandEnums.valueOf(command.toUpperCase());
		  }catch(Exception e){
			  return null;
		  }
	  }
	  
	  /**
	   * 读写分离， 从采用权重算法
	   * @param request
	   * @param command
	   * @return
	   */
	  private AbstractPoolClient getShardClusterFfanRedisClient(RedisCommand request,String command,boolean flag){
		  RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1),true );
		  LoadBalance loadMasterBalance=ffanRedisServerMasterCluster.getLoadMasterBalance();
		  LBRedisServerBean ffanRedisServerBean=loadMasterBalance.select(redisQuestBean, null);
		  List<LBRedisServerBean> ffanRedisServerBeans=ffanRedisServerMasterCluster.getMasterFfanRedisServerBean(ffanRedisServerBean.getKey());
		  if(ffanRedisServerBeans!=null&&ffanRedisServerBeans.size()>0){
			  LBRedisServerClusterBean ffanRedisServerClusterBean= ffanRedisServerMasterCluster.getFfanRedisServerClusterBean(ffanRedisServerBean.getKey());
			  if(ffanRedisServerClusterBean!=null){
				  LoadBalance loadClusterBalance=ffanRedisServerClusterBean.getLoadClusterBalance();
				  loadClusterBalance.setFfanRedisServerMasterCluster(ffanRedisServerMasterCluster);
				  redisQuestBean.setWrite(flag);
				  LBRedisServerBean ffanClusterRedisServerBean=loadClusterBalance.select(redisQuestBean, ffanRedisServerBean);
				  if(ffanClusterRedisServerBean!=null){
					  String key=ffanClusterRedisServerBean.getKey();
					  return ffanRedisServerBeanMap.get(key);
				  }
			  }
		  }
		 String key=ffanRedisServerBean.getKey();
	     return ffanRedisServerBeanMap.get(key);
		  
	  }
	  
	  /**
	   * 一致性hash算法 主
	   * @param request
	   * @return
	   */
	  private AbstractPoolClient getShardFfanRedisClient(RedisCommand request,String command){
		  RedisQuestBean redisQuestBean=new RedisQuestBean(new String(request.getArgs().get(0)), request.getArgs().get(1), true);
		  LoadBalance loadMasterBalance=ffanRedisServerMasterCluster.getLoadMasterBalance();
		  LBRedisServerBean ffanRedisServerBean=loadMasterBalance.select(redisQuestBean, null);
		  String key=ffanRedisServerBean.getKey();
		  return ffanRedisServerBeanMap.get(key);
	  }
	  
	  @Override
	  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		 // destroy();
	  }
	  
	  @Override
	  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	    if (cause instanceof IOException){
	      String message = cause.getMessage();
	      if (message!=null && "远程主机强迫关闭了一个现有的连接。".equals(message)){
	        logger.warn("Client closed!");
	      }else {
	        logger.error("出错，客户端关闭连接");
	      }
	      ctx.channel().close();
	    }else {
	      logger.error("出错，关闭连接",cause);
	      ctx.channel().write(new ErrorRedisReply(ProtoUtils.buildErrorReplyBytes("closed by upstream")));
	    }
	  }
	  
	  
}
