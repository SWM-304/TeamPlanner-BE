package com.tbfp.teamplannerbe.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbfp.teamplannerbe.domain.chat.service.pobsub.RedisSubscriber;
import com.tbfp.teamplannerbe.domain.notification.dto.response.NotificationResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Profile("local")
public class LocalRedisConfig {

    private static final String TOPIC_NAME = "topic";

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

    /**
     * RedisMessageListenerContainer는 Spring Data Redis에서 제공하는 클래스이다.
     * 컨테이너는 메시지가 도착하면 등록된 MessageListener를 호출하여 메시지를 처리한다.
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MessageListenerAdapter listenerAdapter,
                                                                       ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
//        redisStandaloneConfiguration.setPassword(redisPassword);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(TOPIC_NAME);
    }

    /**
     * RedisOperations은 RedisTemplate의 인터페이스이다. 인터페이스로 정의한 이유는
     * 테스트 코드에서 테스트하기 편하게 하기 위해서이다.
     * redisOperations를 통해 RedisConnection에서 넘겨준 byte 값을 객체 직렬화한다.
     * Redis와 통신할때 사용
     */
    @Bean
    public RedisOperations<String, NotificationResponseDto> eventRedisOperations(
            RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        final Jackson2JsonRedisSerializer<NotificationResponseDto> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                NotificationResponseDto.class);
        jsonRedisSerializer.setObjectMapper(objectMapper);
        final RedisTemplate<String, NotificationResponseDto> eventRedisTemplate = new RedisTemplate<>();
        eventRedisTemplate.setConnectionFactory(redisConnectionFactory);
        eventRedisTemplate.setKeySerializer(RedisSerializer.string());
        eventRedisTemplate.setValueSerializer(jsonRedisSerializer);
        eventRedisTemplate.setHashKeySerializer(RedisSerializer.string());
        eventRedisTemplate.setHashValueSerializer(jsonRedisSerializer);
        return eventRedisTemplate;
    }
}