package com.tkpm.sms.infrastructure.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Slf4j
@Component
public class ContentLanguageInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        log.info("Intercepting request for {}", request.getRequestURI());

        String language = request.getHeader("Content-Language");
        if (language != null) {
            Locale locale = Locale.forLanguageTag(language);
            LocaleContextHolder.setLocale(locale);
        }
        return true;
    }
}
