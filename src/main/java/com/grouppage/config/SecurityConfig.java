package com.grouppage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web){
        web
                .ignoring()
                .antMatchers("/h2-console/*");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                //.addFilterBefore(customHeaderFilter, BasicAuthenticationFilter.class)
                //.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
//                .authorizeRequests()
//                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("/**/*.{js,html,css}").permitAll()
//                .antMatchers("/login", "/register").permitAll()
//                .antMatchers("/api", "/api/*").permitAll()
//                .antMatchers("/api/problem/*", "/api/zabieg/*", "/api/zabieg/**").permitAll()
//                .antMatchers("/files/**").permitAll()
//                .antMatchers("/api/admin/**").hasRole("ADMIN")
//                .antMatchers("/api/admin").hasRole("ADMIN")
//                .antMatchers("/files/uploadfile/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and()
                //.exceptionHandling().accessDeniedHandler(getADHandler())
                //.and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}
