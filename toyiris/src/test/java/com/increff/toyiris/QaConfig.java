package com.increff.toyiris;

import com.increff.toyiris.spring.SpringConfig;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = { "com.increff.toyiris" },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class }))
@PropertySources({ //
        @PropertySource(value = "file:./toyirisTest.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {
}
