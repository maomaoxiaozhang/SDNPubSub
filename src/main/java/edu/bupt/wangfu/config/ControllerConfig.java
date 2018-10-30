package edu.bupt.wangfu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {"edu.bupt.wangfu.*"})
//@PropertySource("classpath:/*.properties")
public class ControllerConfig {

    /*
     * PropertySourcesPlaceHolderConfigurer Bean only required for @Value("{}") annotations.
     * Remove this bean if you are not using @Value annotations for injecting properties.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
