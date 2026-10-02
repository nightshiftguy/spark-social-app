package com.nightguy.spark.rateLimiting;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitingConfig {
  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.username:}")
  private String redisUsername;

  @Value("${spring.data.redis.password:}")
  private String redisPassword;

  @Bean(destroyMethod = "shutdown")
  public RedisClient redisClient() {
    var builder = RedisURI.builder().withHost(redisHost).withPort(redisPort);

    if (!redisPassword.isBlank()) {
      if (redisUsername.isBlank()) {
        builder.withPassword(redisPassword.toCharArray());
      } else {
        builder.withAuthentication(redisUsername, redisPassword.toCharArray());
      }
    }

    return RedisClient.create(builder.build());
  }

  @Bean
  public ProxyManager<String> proxyManager(RedisClient redisClient) {
    var redisConnection =
        redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

    // Define TTL for buckets
    var expirationStrategy =
        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(5));

    return Bucket4jLettuce.casBasedBuilder(redisConnection)
        .expirationAfterWrite(expirationStrategy)
        .build();
  }
}
