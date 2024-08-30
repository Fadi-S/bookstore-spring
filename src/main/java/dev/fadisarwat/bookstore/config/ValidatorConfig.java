package dev.fadisarwat.bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfig {

    @Autowired
    private SpringConstraintValidatorFactory springConstraintValidatorFactory;

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setConstraintValidatorFactory(springConstraintValidatorFactory);
        return bean;
    }
}

