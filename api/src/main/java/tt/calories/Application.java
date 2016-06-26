/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.validation.Validator;
import tt.calories.data.DemoData;
import tt.calories.data.SeedData;
import tt.calories.repo.UserRepository;

import java.util.TimeZone;

/**
 * Entry-point for running as a stand-alone application
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@SpringBootApplication
@Configuration
public class Application {


    static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        // For consistency, all dates are in stored and manipulated in UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        context = SpringApplication.run(Application.class, args);
        loadData(context);
    }

    /**
     * Load seed and, possibly, test data on initialization if required.
     * <p>
     * We assume a initialized DB will have at least one user (the admininistrator).
     * If we get an empty database, we load the data.
     */
    private static void loadData(ConfigurableApplicationContext context) {
        UserRepository userRepo = context.getBean(UserRepository.class);
        if (userRepo.count() == 0) {
            SeedData seedData = context.getBean(SeedData.class);
            seedData.load();

            if (context.getEnvironment().getProperty("application.loadDemoData", Boolean.class, false)) {
                DemoData demoData = context.getBean(DemoData.class);
                demoData.load();
            }
        }
    }

    /**
     * Static method to get DI in non context-aware situations (like JPA EntityListeners)
     */
    public static void autowire(Object bean) {
        context.getAutowireCapableBeanFactory().autowireBean(bean);
    }


    @Bean
    public Validator validator() {
        return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
    }
}
