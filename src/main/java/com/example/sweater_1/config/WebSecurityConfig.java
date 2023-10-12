package com.example.sweater_1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private DataSource dataSource;
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
        JdbcDaoImpl userDetailsService = new JdbcDaoImpl();
        userDetailsService.setDataSource(dataSource);
        userDetailsService.setUsersByUsernameQuery("select username, password, active from usr where username=?");
        userDetailsService.setAuthoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?");
        return userDetailsService;
    }
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("select username, password, active from usr where username=?")
                .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?");
    }

}
