
package com.mamoori.mamooriback.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamoori.mamooriback.api.entity.Role;
import com.mamoori.mamooriback.api.repository.UserRepository;
import com.mamoori.mamooriback.api.service.UserService;
import com.mamoori.mamooriback.auth.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.mamoori.mamooriback.auth.filter.JwtAuthenticationProcessingFilter;
import com.mamoori.mamooriback.auth.handler.AuthFailureHandler;
import com.mamoori.mamooriback.auth.handler.AuthSuccessHandler;
import com.mamoori.mamooriback.auth.handler.OAuth2LoginFailureHandler;
import com.mamoori.mamooriback.auth.handler.OAuth2LoginSuccessHandler;
import com.mamoori.mamooriback.auth.service.CustomOAuth2UserService;
import com.mamoori.mamooriback.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    //    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
//                .httpBasic().disable()
                .csrf().disable()
//                .headers().frameOptions().disable()
//                .and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/auth/**").permitAll() // 접근 권한 설정
                .antMatchers("/api/**").hasAnyRole(Role.USER.name()) // 접근 권한 설정
                .anyRequest().authenticated().and()
//                .logout().logoutSuccessUrl("/").and() // logout 성공시 URL
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
//                .failureHandler(oAuth2LoginFailureHandler)
                .authorizationEndpoint().baseUri("/auth/signin/**").and() // 로그인 접근 URI
                .redirectionEndpoint().baseUri("/auth/callback/**").and() // redirect URI
                .userInfoEndpoint().userService(customOAuth2UserService) // 로그인 이후 service
                ;

//        http.addFilterBefore(new JwtAuthenticationProcessingFilter(jwtService, userRepository), CustomJsonUsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
/*
    *//**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     *
     *//*
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }*/

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public AuthSuccessHandler loginSuccessHandler() {
        return new AuthSuccessHandler(jwtService, userRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public AuthFailureHandler loginFailureHandler() {
        return new AuthFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     *//*
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }*/

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }

}

