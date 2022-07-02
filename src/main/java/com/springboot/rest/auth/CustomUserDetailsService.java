package com.springboot.rest.auth;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.rest.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		return userRepository.findByEmail(email)
				.map(
						user -> new CustomUserDetails(
								user.getEmail(),
								user.getPassword(),
								Collections.emptyList(),
								user.getUsername(),
								user.getId()))
				.orElseThrow(
						() -> new UsernameNotFoundException(
								"Given username is not found. (username = '" + email + "')"));
	}

}
