package com.springboot.rest.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.springboot.rest.Entity.AmountSettingEntity;

/**
 * Amount setting repository.
 * 
 * @author takaseshota
 */
@Mapper
public interface AmountSettingRepository {

	@Select("SELECT "
			+ "id,"
			+ "user_id, "
			+ "month_year,"
			+ "save_amount ,"
			+ "usable_amount "
			+ "FROM user_amount_setting "
			+ "where "
			+ "user_id = #{userId} "
			+ "AND "
			+ "month_year = #{month}")
	List<AmountSettingEntity> findByUseidAndMonth(String userId, String month);

	@Insert("INSERT INTO user_amount_setting "
			+ "(user_id,"
			+ "month_year,"
			+ "save_amount,"
			+ "usable_amount,"
			+ "create_at,"
			+ "update_at)"
			+ "VALUES"
			+ "("
			+ "#{userId},"
			+ "#{month},"
			+ "#{saveAmount},"
			+ "#{usableAmount},"
			+ "#{create},"
			+ "#{update});")
	int insert(String userId, String month, String saveAmount, String usableAmount,
			String create, String update);

	@Update("UPDATE user_amount_setting "
			+ "SET "
			+ "save_amount = #{saveAmount}, "
			+ "usable_amount = #{usableAmount}, "
			+ "update_at = #{update} "
			+ "WHERE "
			+ "user_id = #{userId} "
			+ "AND "
			+ "month_year = #{month};")
	int update(String userId, String month, String saveAmount, String usableAmount,
			String update);

}
