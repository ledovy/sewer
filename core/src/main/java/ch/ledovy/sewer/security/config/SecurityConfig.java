package ch.ledovy.sewer.security.config;

import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
@EnableConfigurationProperties(H2ConsoleProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String	ROLENAME_ADMIN	= "ADMIN";
	private static final String	ROLENAME_USER	= "USER";
	public static final String	ROLE_ADMIN		= "ROLE_" + ROLENAME_ADMIN;
	public static final String	ROLE_USER		= "ROLE_" + ROLENAME_USER;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http
				.csrf().disable()
				.authorizeRequests();
		authorizeRequests
				.anyRequest().permitAll()
				.and()
				.httpBasic();
		http.logout();
	}
	
}
