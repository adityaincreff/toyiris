package com.increff.toyiris.spring;
import org.springframework.context.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@ComponentScan("com.increff.toyiris")
@PropertySources({ //
        @PropertySource(value = "file:./toyiris.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//		resolver.setDefaultEncoding("utf-8");
//		resolver.setMaxUploadSize(524288000); // 500 MB
//		resolver.setMaxUploadSizePerFile(524288000); // 500 MB
//		resolver.setMaxInMemorySize(524288000);
        return resolver;
    }
}
