package com.springboot.rest.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	ObjectMapper objectMapper = new ObjectMapper();

	String usernameParameter = "username";
	String passwordParameter = "password";

	public JsonUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher("/login", "POST"));
		this.setAuthenticationManager(authenticationManager);
	}

	public String getUsernameParameter() {
		return usernameParameter;
	}

	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		Map<String, Object> requestObject;
		try {
			requestObject = objectMapper.readValue(request.getInputStream(), Map.class);
		} catch (IOException e) {
			requestObject = new HashMap<>();
		}

		String username = Optional
				.ofNullable(requestObject.get(usernameParameter))
				.map(Object::toString)
				.map(String::trim)
				.orElse("");
		String password = Optional
				.ofNullable(requestObject.get(passwordParameter))
				.map(Object::toString)
				.orElse("");

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);

		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

		//CORS??????????????????????????????
		//		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		//		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "86400");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
