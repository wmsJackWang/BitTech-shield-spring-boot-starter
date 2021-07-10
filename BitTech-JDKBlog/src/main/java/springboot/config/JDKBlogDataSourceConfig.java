package springboot.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@EnableCaching
public class JDKBlogDataSourceConfig {

    // datasource配置，阿里的druid连接池
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")//对容器中的对象 通过setter方法进行装载配置信息
    public DataSource jdkBlogDataSource() {
        return new DruidDataSource();
    }
	
    // 目标数据源，代码构建需要去目标库查询数据表的结构。
    // datasource配置，阿里的druid连接池
    // 
    @Bean
    @ConfigurationProperties(prefix = "spring.target-data-source")//对容器中的对象 通过setter方法进行装载配置信息
    public DataSource genCodeDataSource() {
        return new DruidDataSource();
    }
}
