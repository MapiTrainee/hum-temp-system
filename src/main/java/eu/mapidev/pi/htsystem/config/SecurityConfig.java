package eu.mapidev.pi.htsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String ENCODED_SECRET = "{bcrypt}$2a$10$3HIDKuRhkj1ptK045z6T7.xk29qtpxGXmIGtwloLzTDe1aX87/5.K";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	auth.inMemoryAuthentication().withUser("admin").password(ENCODED_SECRET).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.authorizeRequests()
		.antMatchers("/profile").hasRole("ADMIN")
		.antMatchers("/console/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.GET, "/measurement/**").permitAll()
		.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
		.antMatchers("/").permitAll()
		.anyRequest().authenticated()
		.and().httpBasic()
		.and().csrf().ignoringAntMatchers("/console/**").ignoringAntMatchers("/measurement/**")
		.and().headers().frameOptions().sameOrigin();
    }
}
