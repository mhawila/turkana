package org.muzima.turkana.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static Log log = LogFactory.getLog(WebSecurityConfig.class);
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Configuring security");
        http.authorizeRequests().antMatchers("/api/**").permitAll().anyRequest().authenticated()
            .and().cors().and().csrf().disable();
    }
}
