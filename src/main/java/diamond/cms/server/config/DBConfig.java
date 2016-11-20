package diamond.cms.server.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.alibaba.druid.pool.DruidDataSource;

import diamond.cms.server.model.jooq.Cms;

@Configuration
@ImportResource("classpath:spring-tx.xml")
public class DBConfig{

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("database.url"));
        datasource.setUsername(env.getProperty("database.user"));
        datasource.setPassword(env.getProperty("database.password"));
        try {
            datasource.init();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return new TransactionAwareDataSourceProxy(datasource);
    }


    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(){
        return new DataSourceConnectionProvider(dataSource());
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
