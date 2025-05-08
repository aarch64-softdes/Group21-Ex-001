package com.tkpm.sms.infrastructure.config;

import com.tkpm.sms.infrastructure.interceptor.ContentLanguageInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private ContentLanguageInterceptor contentLanguageInterceptor;

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
    StringHttpMessageConverter converter = new StringHttpMessageConverter(
    StandardCharsets.UTF_8);
    converter.setWriteAcceptCharset(false);
    return converter;
    }
    
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilterUTF8() {
    CharacterEncodingFilter filter = new CharacterEncodingFilter();
    filter.setEncoding("UTF-8");
    filter.setForceEncoding(true);
    FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new
    FilterRegistrationBean<>();
    registrationBean.setFilter(filter);
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(responseBodyConverter());
    
    // Ensure JSON is also using UTF-8
    MappingJackson2HttpMessageConverter jsonConverter = new
    MappingJackson2HttpMessageConverter();
    jsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
    
    converters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    
    converters.add(jsonConverter);
    }
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(contentLanguageInterceptor);
    }
}
