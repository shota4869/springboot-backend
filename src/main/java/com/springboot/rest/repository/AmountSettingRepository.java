package com.springboot.rest.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.springboot.rest.Entity.AmountSettingEntity;

@Mapper
public interface AmountSettingRepository {

	@Select("SElECT id, user_id, month_year,save_amount, fixed_income, fixed_expenditure FROM user_amount_setting where user_id = #{userId} AND month_year = #{month}")
	List<AmountSettingEntity> findByUseidAndMonth(String userId, String month);

	@Insert("INSERT INTO user_amount_setting"
			+ "(user_id,"
			+ "month_year,"
			+ "save_amount,"
			+ "fixed_income,"
			+ "fixed_expenditure,"
			+ "create_at,"
			+ "update_at)"
			+ "VALUES"
			+ "("
			+ "#{userId},"
			+ "#{month},"
			+ "#{saveAmount},"
			+ "#{fixedIncome},"
			+ "#{fixedExpenditure},"
			+ "#{create},"
			+ "#{update});")
	int insert(String userId, String month, String saveAmount, String fixedIncome, String fixedExpenditure,
			String create, String update);

	@Update("UPDATE user_amount_setting "
			+ "SET "
			+ "save_amount = #{saveAmount}, "
			+ "fixed_income = #{fixedIncome}, "
			+ "fixed_expenditure = #{fixedExpenditure}, "
			+ "update_at = #{update} "
			+ "WHERE "
			+ "user_id = #{userId} "
			+ "and "
			+ "month_year = #{month};")
	int update(String userId, String month, String saveAmount, String fixedIncome, String fixedExpenditure,
			String update);

}
