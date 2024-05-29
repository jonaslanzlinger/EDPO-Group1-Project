package ch.unisg.order.rest.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This is a configuration class for Tomcat.
 * It uses Spring's @Controller annotation to indicate that it is a controller class.
 */
@Controller
public class TomcatConfiguration {

    /**
     * This method creates a WebMvcConfigurer which is responsible for configuring Spring MVC.
     * It uses Spring's @Bean annotation to indicate that it is a bean to be managed by Spring.
     * The WebMvcConfigurer adds a view controller that forwards requests to "order.html".
     * @return A WebMvcConfigurer for configuring Spring MVC.
     */
    @Bean
    public WebMvcConfigurer forwardToIndex() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("forward:/order.html");
            }
        };
    }
}