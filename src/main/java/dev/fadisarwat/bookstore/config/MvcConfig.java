package dev.fadisarwat.bookstore.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.stripe.Stripe;
import dev.fadisarwat.bookstore.rules.UniqueConstraintValidator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Logger;

@Configuration
@EnableTransactionManagement
@EnableSpringConfigured
@ComponentScan( basePackages = {"dev.fadisarwat.bookstore"})
@PropertySource("classpath:persistence-mysql.properties")
public class MvcConfig implements WebMvcConfigurer {

    private final Logger logger = Logger.getLogger(getClass().getName());

    MvcConfig(Environment env) {
        Stripe.apiKey = env.getProperty("stripe.api");
    }

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

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("POST", "GET", "DELETE", "PATCH", "PUT")
                        .allowedOriginPatterns("*");
            }
        };
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
        props.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql", "false"));
        factoryBean.setHibernateProperties(props);

        return factoryBean;
    }

    @Bean
    public ResourceTransactionManager transactionManager(SessionFactory factory) {
        return new JpaTransactionManager(factory);
    }

    @Bean
    public UniqueConstraintValidator uniqueConstraintValidator() {
        return new UniqueConstraintValidator();
    }
}
