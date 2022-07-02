package com.springboot.rest.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.springboot.rest.Entity.UsableAmountEntity;

@Mapper
public interface UsableAmountRepository {

	@Select("select * from usable_amount where user_id = #{userId} and date = #{date}")
	List<UsableAmountEntity> findAll(String userId, String date);

	@Insert("INSERT INTO usable_amount"
			+ "("
			+ "user_id,"
			+ "date,"
			+ "usable_amount,"
			+ "create_at,"
			+ "update_at"
			+ ")"
			+ "VALUES"
			+ "("
			+ "#{userId},"
			+ "#{date},"
			+ "#{usableAmount},"
			+ "#{create},"
			+ "#{update}"
			+ ");")
	int insert(String userId, String date, String usableAmount, String create, String update);

}
