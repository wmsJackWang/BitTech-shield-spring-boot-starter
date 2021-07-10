package com.ruoyi;


import com.ruoyi.annotation.EnableShieldConfiguration;
import com.ruoyi.cache.Cache;
import com.ruoyi.cache.ConcurrentHashMapCache;
import com.ruoyi.cache.RedisCache;
import com.ruoyi.intercepter.ShieldIntercepter;
import com.ruoyi.property.ShieldProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;


@Configuration
@ConditionalOnBean(annotation = EnableShieldConfiguration.class)
@EnableConfigurationProperties(ShieldProperties.class)
public class ShieldAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ShieldAutoConfiguration.class);

    @Autowired
    ShieldProperties properties;

    @PostConstruct
    public void init() {
        log.info("shield has been turned on! Best wishes for you! ");
        log.info("You'll be safe with shield... ");
        log.info("properties:{}",properties.toString());
    }

    @Bean
    @ConditionalOnMissingBean(name = {"shieldProcessor"})
    ShieldProcessor shieldProcessor() {
        log.info("init shieldProcessor bean");
        return new ShieldShieldProcessor();
    }

    @Bean(name = "shieldCache")
    Cache shieldCache() {
        log.info("init shieldCache bean");
        ShieldCacheType type = properties.getType();
        if (type == ShieldCacheType.REDIS) {
            log.info("Enabling shield cache: [Redis]");
            return new RedisCache();
        }
        log.info("Enabling shield cache: [Map]");
        return new ConcurrentHashMapCache();
    }

    @Bean
    @ConditionalOnProperty(name = "shield.limit.access.switcher" , havingValue = "open")
    ShieldIntercepter shieldIntercepter(@Qualifier("shieldProcessor")ShieldProcessor shieldProcessor,ShieldProperties shieldProperties){
        log.info("init shieldIntercepter bean");
        ShieldIntercepter shieldIntercepter = new ShieldIntercepter();
        shieldIntercepter.setProcessor(shieldProcessor);
        shieldIntercepter.setShieldProperties(shieldProperties);
        return shieldIntercepter;
    }

    @Bean
    @Order(Integer.MIN_VALUE)//使用order来标记该bean最先创建，并将拦截器优先注册到register。
    @ConditionalOnProperty(name = "shield.limit.access.switcher" , havingValue = "open")
    BitTechShieldWebMvcConfig bitTechShieldWebMvcConfig(@Qualifier("shieldIntercepter") ShieldIntercepter shieldIntercepter){
        BitTechShieldWebMvcConfig bitTechShieldWebMvcConfig = new BitTechShieldWebMvcConfig();
        bitTechShieldWebMvcConfig.setShieldIntercepter(shieldIntercepter);
        return bitTechShieldWebMvcConfig;
    }
}
