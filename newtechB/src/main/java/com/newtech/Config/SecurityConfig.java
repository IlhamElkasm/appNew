package com.newtech.Config;

import com.newtech.Services.Security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(mvc.pattern("/api/auth/**")).permitAll()
                                .requestMatchers(mvc.pattern("/api/public/**")).permitAll()
//                                .requestMatchers(mvc.pattern("/api/admin/init")).permitAll()
//                                .requestMatchers(mvc.pattern("/h2-console/**")).permitAll()
                                .requestMatchers(mvc.pattern("/api/projects/admin/**")).hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.POST, "/api/projects/admin/create").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/projects/admin/{id}").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/api/projects/admin/{id}").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/projects/all").hasAnyAuthority("ADMIN", "CLIENT")
                                .requestMatchers(mvc.pattern("/api/formations/admin/**")).hasRole("ADMIN")
                                .requestMatchers(mvc.pattern("/api/reservations/client/**")).hasRole("CLIENT")
                                .requestMatchers(mvc.pattern("/api/client/**")).hasRole("CLIENT")
                                .requestMatchers(mvc.pattern("/api/secretaire/**")).hasRole("SECRETAIRE")
                                .anyRequest().authenticated()
                );


        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // Updated H2 console configuration for Spring Security 6.1+
        http.headers(headers ->
                headers.frameOptions(frameOptions -> frameOptions.disable())
        );

        return http.build();
    }
}

