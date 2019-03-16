package com.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
@Autowired
	private AuthenticationSuccessHandler successHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String key = "";
		http.authorizeRequests()
		.antMatchers("/managers").hasRole("MANAGERS")
		.antMatchers("/employees").hasRole("EMPLOYEES")
		.anyRequest().fullyAuthenticated()
		.and().formLogin().loginPage("/login").successHandler(successHandler).permitAll()
		.and().logout().invalidateHttpSession(true).deleteCookies("JESSIONID").permitAll()
		.and().rememberMe().rememberMeServices(rememberMeServices(key)).key(key);
	}
@Bean
	public RememberMeServices rememberMeServices(String key) {
	BasicRemeberMeUserDetailsService remeberMeUserDetailsService = new BasicRemeberMeUserDetailsService();
	InMemoryTokenRepositoryImpl remeberTokenRepositoryImpl  = new InMemoryTokenRepositoryImpl();
	PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(key, remeberMeUserDetailsService, remeberTokenRepositoryImpl);
	services.setTokenValiditySeconds(3600);
	return services;
	}

public class BasicRemeberMeUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new org.springframework.security.core.userdetails.User(username, "", Collections.<GrantedAuthority>emptyList());
	}
	
}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups").contextSource(contextSource()).passwordCompare().passwordEncoder(new LdapShaPasswordEncoder()).passwordAttribute("userPassword");
	}

	@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		return new DefaultSpringSecurityContextSource(Collections.singletonList("ldap://localhost:12345"), "dc=memorynotfound,dc=com");
	}
}
