package com.springboot.rest.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

	private long id;

	private String name;

	public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
			String name, long id) {
		super(username, password, authorities);
		this.name = name;
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

}
