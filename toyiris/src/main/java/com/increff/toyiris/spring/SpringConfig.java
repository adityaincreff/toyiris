package com.increff.toyiris.spring;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.increff.toyiris")
@PropertySources({ //
        @PropertySource(value = "file:./toyiris.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {
}
