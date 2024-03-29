package com.springboot.rest.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.springboot.rest.Entity.UserEntity;

/**
 * User repository.
 * 
 * @author takaseshota
 */
@Mapper
public interface UserRepository {

	@Select("SELECT * "
			+ "FROM users "
			+ "WHERE "
			+ "email = #{email} ")
	Optional<UserEntity> findByEmail(String email);

	@Insert("INSERT INTO users "
			+ "(username,"
			+ "email,"
			+ "password,"
			+ "create_at,"
			+ "update_at) "
			+ "VALUES "
			+ "("
			+ "#{username},"
			+ "#{email},"
			+ "#{password},"
			+ "#{create},"
			+ "#{update})")
	int registUser(String username, String email, String password, String create,
			String update);
}
