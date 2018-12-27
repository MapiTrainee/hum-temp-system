package eu.mapidev.pi.htsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

    @Profile(Profiles.DEVELOPMENT)
    @Configuration
    public class DevConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests()
		    .antMatchers("/").permitAll()
		    .antMatchers("/console/**").permitAll();
	    http.csrf().disable();
	    http.headers().frameOptions().disable();
	}
    }
}
