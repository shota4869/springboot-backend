package com.springboot.rest.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.springboot.rest.Entity.UserAmountAndMCategoryJoinEntity;
import com.springboot.rest.Entity.UserAmountEntity;

@Mapper
public interface UserAmountRepository {

	@Insert("INSERT INTO `health_management`.`user_amount_detail`"
			+ "(`user_id`,"
			+ "`balance_year_month`,"
			+ "`balance_date`,"
			+ "`category_code`,"
			+ "`balance_flg`,"
			+ "`amount`,"
			+ "`remarks`,"
			+ "`create_at`,"
			+ "`update_at`)"
			+ "VALUES"
			+ "(#{userId},"
			+ "#{balanceYearMonth},"
			+ "#{balanceDate},"
			+ "#{categoryCode},"
			+ "#{balanceFlg},"
			+ "#{amount},"
			+ "#{remarks},"
			+ "#{create},"
			+ "#{update})")
	int registUserAmount(String userId, String balanceYearMonth, String balanceDate, String categoryCode,
			String balanceFlg, String amount, String remarks, String create, String update);

	@Select("SELECT * FROM `health_management`.`user_amount_detail` WHERE user_id = #{userId} and balance_date = #{date} ")
	List<UserAmountEntity> findAllByUserId(String userId, String date);

	@Select(" SELECT  user.id, user.user_id, user.balance_year_month, user.balance_date,user.category_code, user.balance_flg, user.amount, user.remarks,category.category_name FROM user_amount_detail as user "
			+ " INNER JOIN m_category_code as category  ON user.category_code = category.category_code where user.user_id = #{userId} and user.balance_date = #{date} ")
	List<UserAmountAndMCategoryJoinEntity> findByUserIdAndDate(String userId, String date);

	@Delete("DELETE FROM user_amount_detail "
			+ " WHERE id = #{id} ;")
	int delete(String id);

	@Select("SELECT * FROM `health_management`.`user_amount_detail` WHERE user_id = #{userId} AND balance_year_month = #{month}")
	List<UserAmountEntity> findAllByUserIdAndMonth(String userId, String month);

	@Select("SELECT * FROM `health_management`.`user_amount_detail` WHERE user_id = #{userId} ")
	List<UserAmountEntity> findAll(String userId);
}