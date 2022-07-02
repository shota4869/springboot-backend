package com.springboot.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.rest.Entity.UserEntity;
import com.springboot.rest.date.CreateDate;
import com.springboot.rest.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean registUser(String username, String email, String password) {

		String pass = passwordEncoder.encode(password);

		int count = userRepository.registUser(username, email, pass, CreateDate.getNowDateTime(),
				CreateDate.getNowDateTime());
		if (count < 1) {
			return false;
		}

		return true;
	}

	public UserEntity selectUser(String email) {
		return userRepository.findByEmail(email).get();
	}

}
