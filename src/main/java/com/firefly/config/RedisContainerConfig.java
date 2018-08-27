package com.firefly.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisContainerConfig {

	@Autowired
	private RedisExpiredListener redisExpiredListener;
	
	@Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(redisConnection);
        // 设置监听使用的线程池
        // 设置监听器
        container.addMessageListener(redisExpiredListener, new PatternTopic(RedisExpiredListener.LISTENER_PATTERN));
        return container;
    }
}
