package dev.fadisarwat.bookstore.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Logger;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:persistence-mysql.properties")
public class MvcConfig implements WebMvcConfigurer {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Bean
    public DataSource getDataSource(Environment env) {
        ComboPooledDataSource source = new ComboPooledDataSource();
        Function<String, Integer> getProp = (prop) -> Integer.parseInt(Objects.requireNonNull(env.getProperty(prop)));

        try {
            source.setJdbcUrl(env.getProperty("jdbc.url"));
            source.setUser(env.getProperty("jdbc.user"));
            source.setPassword(env.getProperty("jdbc.password"));

            source.setInitialPoolSize(getProp.apply("connection.pool.initialPoolSize"));
            source.setMinPoolSize(getProp.apply("connection.pool.minPoolSize"));
            source.setMaxPoolSize(getProp.apply("connection.pool.maxPoolSize"));
            source.setMaxIdleTime(getProp.apply("connection.pool.maxIdleTime"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return source;
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/frontend/build/");
//
//        registry.addResourceHandler("/manifest.json")
//                .addResourceLocations("classpath:/static/frontend/build/manifest.json");
//    }

    @Bean
    public FactoryBean<SessionFactory> getSessionFactory(Environment environment) {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        factoryBean.setDataSource(getDataSource(environment));
        factoryBean.setPackagesToScan("dev.fadisarwat.bookstore.models");

        Properties props = new Properties();
        props.setProperty("hibernate.show_sql", "true");
        factoryBean.setHibernateProperties(props);

        return factoryBean;
    }

    @Bean
    public ResourceTransactionManager transactionManager(SessionFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
