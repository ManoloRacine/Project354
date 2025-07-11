package com.project.backend354.config;

@Configuration
@EnableWebMvc
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+$");
        serializer.setUseHttpOnlyCookie(false); // Allow frontend to read cookie
        serializer.setSameSite("Lax");
        return serializer;
    }
}
