package com.springboot.rest.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.springboot.rest.Entity.LineSettingEntity;

@Mapper
public interface LineSettingRepository {

	@Select("SELECT id, user_id, line_flg, access_token from user_line_setting where user_id = #{userId}")
	List<LineSettingEntity> findAllByUserid(String userId);

	@Insert("INSERT INTO user_line_setting"
			+ "("
			+ "user_id,"
			+ "line_flg,"
			+ "access_token,"
			+ "create_at, "
			+ "update_at)"
			+ " VALUES"
			+ "( "
			+ "#{userId},"
			+ "#{lineFlg},"
			+ "#{accessToken},"
			+ "#{create},"
			+ "#{update}); ")
	int insert(String userId, String lineFlg, String accessToken, String create, String update);

	@Update("UPDATE user_line_setting "
			+ "SET "
			+ "line_flg = #{lineFlg}, "
			+ "access_token = #{accessToken}, "
			+ "update_at = #{update} "
			+ "WHERE user_id = #{userId} ; ")
	int update(String lineFlg, String accessToken, String update, String userId);

	@Select("SELECT id, user_id, line_flg, access_token from user_line_setting where line_flg = 1")
	List<LineSettingEntity> findAll();

}
