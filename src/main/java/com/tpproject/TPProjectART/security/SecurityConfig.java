package com.tpproject.TPProjectART.security;

import com.tpproject.TPProjectART.database.*;
import com.tpproject.TPProjectART.repositories.*;
import com.tpproject.TPProjectART.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.servlet.config.annotation.*;

import javax.activation.*;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private NDataSource DataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    } // НЕОБХОДИМО СМЕНИТЬ ШИФРОВАНИЕ

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(DataSource.getDataSource())
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select username, password, active from userdata where username=?")
                .authoritiesByUsernameQuery("select u.username, ur.roles from userdata u inner join user_role ur on u.id = ur.user_id where u.username=?");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/img/**").permitAll()
                    .antMatchers("/**").permitAll()
                .and()
                    .formLogin()
                    .loginProcessingUrl("/perform-login")
                    .loginPage("/login").permitAll()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/main")
                    .failureUrl("/login-error");

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
