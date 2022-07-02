package com.springboot.rest.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.springboot.rest.Entity.UserEntity;

@Mapper
public interface UserRepository {

	@Select("select * from users where email = #{email}")
	Optional<UserEntity> findByEmail(String email);

	@Insert("INSERT INTO health_management.users"
			+ "(username,"
			+ "email,"
			+ "password,"
			+ "create_at,"
			+ "update_at)"
			+ "VALUES"
			+ "("
			+ "#{username},"
			+ "#{email},"
			+ "#{password},"
			+ "#{create},"
			+ "#{update})")
	int registUser(String username, String email, String password, String create,
			String update);
}
