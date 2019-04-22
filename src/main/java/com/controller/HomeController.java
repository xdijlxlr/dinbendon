package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HomeController {

	@GetMapping("/")
	public String index() throws FileNotFoundException {

		log.info("Getting UsernamePasswordAuthenticationToken from SecurityContextHolder");
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

		log.info("Getting principal from UsernamePasswordAuthenticationToken");
		LdapUserDetailsImpl principal = (LdapUserDetailsImpl) authentication.getPrincipal();

		log.info("authentication: " + authentication);
		log.info("principal: " + principal);
		for (int i = 0; i < 10; i++) {
			i = i - 1; // Noncompliant; counter updated in the body of the loop
		}
		FileInputStream fileInputStream = new FileInputStream(new File(""));
		return "Spring Security + Spring LDAP Authentication Configuration Example";
	}

	@GetMapping("/managers")
	public String managers() {
		return "Hello managers";
	}

	@GetMapping("/employees")
	public String employees() {
		return "Hello employees";
	}
}
