/**
 *
 */
package com.opensource.netty.redis.proxy.net.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.opensource.netty.redis.proxy.core.config.LBRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerClusterBean;
import com.opensource.netty.redis.proxy.core.protocol.RedisReplyEncoder;
import com.opensource.netty.redis.proxy.core.protocol.RedisRequestDecoder;
import com.opensource.netty.redis.proxy.net.client.LBRedisClient;
import com.opensource.netty.redis.proxy.net.server.support.LBRedisServerHandler;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;

/**
 * @author liubing
 */
public class LBRedisServer {

    private Logger logger = LoggerFactory.getLogger(LBRedisServer.class);

    private LBRedisServerMasterCluster ffanRedisServerMasterCluster;

    // 线程组
    private static EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime
            .getRuntime().availableProcessors());
    private static EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime
            .getRuntime().availableProcessors());

    /**
     * @param conf
     */
    public LBRedisServer(LBRedisServerMasterCluster ffanRedisServerMasterCluster) {
        super();
        this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
        init();
    }


    /**
     * 销废
     */
    public void destroy() {
        for (String key : ffanRedisServerMasterCluster.getFfanRedisClientBeanMap().keySet()) {
            ffanRedisServerMasterCluster.getFfanRedisClientBeanMap().get(key).close();
        }
    }

    /**
     * 初始化客户端
     */
    private void init() {
        if (ffanRedisServerMasterCluster != null && ffanRedisServerMasterCluster.getRedisServerClusterBeans() != null && ffanRedisServerMasterCluster.getRedisServerClusterBeans().size() > 0) {
            for (LBRedisServerClusterBean ffanRedisServerClusterBean : ffanRedisServerMasterCluster.getRedisServerClusterBeans()) {
                LBRedisServerBean lbRedisServerBean = ffanRedisServerClusterBean.getFfanMasterRedisServerBean();
                if (lbRedisServerBean != null) {//主
                    LBRedisProxyPoolConfig lbRedisProxyPoolConfig = convertLBRedisProxyPoolConfig(lbRedisServerBean);
                    LBRedisClient ffanRedisClient = new LBRedisClient(lbRedisProxyPoolConfig);
                    ffanRedisServerMasterCluster.getFfanRedisClientBeanMap().put(lbRedisServerBean.getKey(), ffanRedisClient);
                }
                List<LBRedisServerBean> ffanRedisServerClusterBeans = ffanRedisServerClusterBean.getFfanRedisServerClusterBeans();
                if (ffanRedisServerClusterBeans != null && ffanRedisServerClusterBeans.size() > 0) {
                    for (LBRedisServerBean ffanRedisServerSlave : ffanRedisServerClusterBeans) {

                        LBRedisProxyPoolConfig lbRedisProxyPoolConfig = convertLBRedisProxyPoolConfig(lbRedisServerBean);
                        LBRedisClient ffanRedisClient = new LBRedisClient(lbRedisProxyPoolConfig);
                        ffanRedisServerMasterCluster.getFfanRedisClientBeanMap().put(ffanRedisServerSlave.getKey(), ffanRedisClient);
                    }
                }

            }
        }
    }


    /**
     * 转换
     *
     * @param lbRedisServerBean
     * @return
     */
    private LBRedisProxyPoolConfig convertLBRedisProxyPoolConfig(LBRedisServerBean lbRedisServerBean) {
        LBRedisProxyPoolConfig lbRedisProxyPoolConfig = new LBRedisProxyPoolConfig();
        lbRedisProxyPoolConfig.setConnectionTimeout(lbRedisServerBean.getConnectionTimeout());
        lbRedisProxyPoolConfig.setHost(lbRedisServerBean.getHost());
        lbRedisProxyPoolConfig.setInitialEntries(lbRedisServerBean.getInitialConnection());
        lbRedisProxyPoolConfig.setMaxActiveEntries(lbRedisServerBean.getMaxActiveConnection());
        lbRedisProxyPoolConfig.setMaxWaitMillisOnBorrow(lbRedisServerBean.getMaxWaitMillisOnBorrow());
        lbRedisProxyPoolConfig.setMinActiveEntries(lbRedisServerBean.getMinConnection());
        lbRedisProxyPoolConfig.setMinEvictableIdleTimeMillis(lbRedisServerBean.getMinEvictableIdleTimeMillis());
        lbRedisProxyPoolConfig.setMinIdleEntries(lbRedisServerBean.getMinIdleEntries());
        lbRedisProxyPoolConfig.setPort(lbRedisServerBean.getPort());
        lbRedisProxyPoolConfig.setTestOnBorrow(lbRedisServerBean.isTestOnBorrow());
        lbRedisProxyPoolConfig.setTestOnReturn(lbRedisServerBean.isTestOnReturn());
        lbRedisProxyPoolConfig.setTestWhileIdle(lbRedisServerBean.isTestWhileIdle());
        lbRedisProxyPoolConfig.setTimeBetweenEvictionRunsMillis(lbRedisServerBean.getTimeBetweenEvictionRunsMillis());
        return lbRedisProxyPoolConfig;
    }

    /**
     * 启动系统，开启接收连接，处理业务
     */
    public void start() {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class).childOption(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)

                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast("RedisRequestDecoder",
                                new RedisRequestDecoder());
                        ch.pipeline().addLast("RedisReplyEncoder",
                                new RedisReplyEncoder());
                        ch.pipeline().addLast(
                                "FfanRedisServerHandler",
                                new LBRedisServerHandler(
                                        ffanRedisServerMasterCluster.getFfanRedisClientBeanMap(), ffanRedisServerMasterCluster));
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind(
                ffanRedisServerMasterCluster.getRedisProxyHost(),
                ffanRedisServerMasterCluster.getRedisProxyPort());
        channelFuture.syncUninterruptibly();
        logger.info("RedisProxy_Server 已经启动");
    }
}
