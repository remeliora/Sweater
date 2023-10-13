package com.example.sweater_1.config;

import com.example.sweater_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserService userService;
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/registration").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
    //Нижние два фрагмента сделаны с помощью ChatGPT
    //В случае возникновения ошибки или при переписывании фрагментов ниже приведены полезные ссылки:
    //https://stackoverflow.com/questions/72381114/spring-security-upgrading-the-deprecated-websecurityconfigureradapter-in-spring
    //https://www.codejava.net/frameworks/spring-boot/fix-websecurityconfigureradapter-deprecated
    //https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    @Bean
    protected UserDetailsService userDetailsService() {
        return userService;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
