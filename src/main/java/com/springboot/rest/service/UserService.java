package com.springboot.rest.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.rest.date.CreateDate;
import com.springboot.rest.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Regist user.
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void registUser(String username, String email, String password) throws SQLException {

		//パスワードのエンコード
		String pass = passwordEncoder.encode(password);

		int count = userRepository.registUser(username, email, pass, CreateDate.getNowDateTime(),
				CreateDate.getNowDateTime());

		if (count < 1) {
			throw new SQLException();
		}
	}
}
