package com.digitalbooks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.digitalbooks.constants.URLConstants;


/**
 * @author cogjava2212
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private UserDetailsService jwtUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		    return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
//		auth.jdbcAuthentication()
//		auth.ldapAuthentication()
//		auth.inMemoryAuthentication()
		
 auth.userDetailsService(jwtUserDetailsService);
//		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	

	// updated-> new
		@Bean
	    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception 
		{
				httpSecurity.cors().and()
				.csrf().disable().authorizeRequests()
	            .antMatchers( "/swagger-ui/**","/v3/api-docs").permitAll()
				.antMatchers(HttpMethod.POST,URLConstants.LoginAuthor).permitAll()
				.antMatchers(HttpMethod.GET,"/api/v1/digitalbooks/BookPurchase/").permitAll()
				.antMatchers(HttpMethod.POST,URLConstants.CreateAuthor).permitAll()
				.antMatchers(HttpMethod.GET, URLConstants.FetchAuthorList).permitAll()
				.antMatchers(HttpMethod.GET, URLConstants.FetchBookList).permitAll()
				.anyRequest().authenticated().and()
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);  
				httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
				return httpSecurity.build();
	}
		
}
