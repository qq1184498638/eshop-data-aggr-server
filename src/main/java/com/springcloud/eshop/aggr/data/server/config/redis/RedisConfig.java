package com.springcloud.eshop.aggr.data.server.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisCluster;

import java.net.UnknownHostException;

/**
 * Title: RedisProperties 
 * @author: gdq
 * @date 2018年5月7日
 * @version V1.0 Description: redis集群实体类
 */

@EnableCaching
@Configuration
@ConditionalOnClass({ JedisCluster.class })
public class RedisConfig {
//	@Value("${spring.redis.cluster.nodes}")
//    private String clusterNodes;
    @Value("${spring.redis.timeout}")  
    private int timeout;  
    @Value("${spring.redis.jedis.pool.max-idle}")  
    private int maxIdle;  
    @Value("${spring.redis.jedis.pool.max-wait}")  
    private long maxWaitMillis;  
    @Value("${spring.redis.commandTimeout}")
    private int commandTimeout;

/*	@Bean
	public JedisCluster getJedisCluster() {
		String[] cNodes = clusterNodes.split(",");
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		// 分割出集群节点
		for (String node : cNodes) {
			String[] hp = node.split(":");
			nodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
		}
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		*//*GenericObjectPoolConfig jedisPoolConfig=new GenericObjectPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);*//*
		// 创建集群对象
		// JedisCluster jedisCluster = new JedisCluster(nodes,commandTimeout);
		JedisCluster jedisCluster = new JedisCluster(nodes,commandTimeout,timeout,maxIdle,jedisPoolConfig);
		//JedisCluster jedisCluster=new JedisCluster(nodes,commandTimeout,jedisPoolConfig);
		return jedisCluster;
	}*/
 
	/**
	 * 设置数据存入 redis 的序列化方式 </br>
	 * redisTemplate 序列化默认使用的jdkSerializeable, 存储二进制字节码, 导致key会出现乱码，所以自定义 序列化类
	 *
	 * @param redisConnectionFactory
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
 
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
 
		redisTemplate.afterPropertiesSet();
 
		return redisTemplate;
	}
}
