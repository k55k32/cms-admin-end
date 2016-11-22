package diamond.cms.server.config;

import javax.sql.DataSource;

import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.alibaba.druid.pool.DruidDataSource;

import diamond.cms.server.model.jooq.Cms;

@Configuration
@ImportResource("classpath:spring-tx.xml")
public class DBConfig{

    @Bean(initMethod="init", destroyMethod="close")
    @ConfigurationProperties(prefix="database")
    public DataSource dataSource(){
        return new DruidDataSource();
    }


    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(){
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource()));
    }

    @Bean
    public DefaultConfiguration jooqConfiguration(){
        DefaultConfiguration config = new DefaultConfiguration();
        config.setSQLDialect(SQLDialect.MYSQL);
        config.setConnectionProvider(dataSourceConnectionProvider());
        return config;
    }

    @Bean
    public DefaultDSLContext dslContext(){
        DefaultDSLContext context = new DefaultDSLContext(jooqConfiguration());
        return context;
    }

    @Bean
    public Schema schema(){
        return Cms.CMS;
    }

}
