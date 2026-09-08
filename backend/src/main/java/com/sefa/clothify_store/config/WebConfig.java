package com.sefa.clothify_store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration 
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // /api/ ile başlayan tüm uç noktalara (endpoints) uygulanır
                // React uygulamanızın çalıştığı adresleri/portları buraya yazın:
                .allowedOrigins("http://localhost:3000") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // İzin verilen HTTP metotları
                .allowedHeaders("*") // Tüm isteklere (Header) izin ver
                .allowCredentials(true); // Çerez / Token iletimine izin ver
    }
    
} 
