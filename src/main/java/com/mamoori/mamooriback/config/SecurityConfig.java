
package com.mamoori.mamooriback.config;

import com.mamoori.mamooriback.api.entity.Role;
import com.mamoori.mamooriback.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**").permitAll() // 접근 권한 설정
                .antMatchers("/auth/**", "/api/**").hasRole(Role.USER.name()) // 접근 권한 설정
                .anyRequest().authenticated().and()
                .logout().logoutSuccessUrl("/").and() // logout 성공시 URL
                .oauth2Login()
                .authorizationEndpoint().baseUri("/auth/signin").and() // 로그인 접근 URI
                .redirectionEndpoint().baseUri("/auth/callback/**").and() // redirect URI
                .userInfoEndpoint().userService(customOAuth2UserService) // 로그인 이후 service
                ;
        return http.build();

    }
}

