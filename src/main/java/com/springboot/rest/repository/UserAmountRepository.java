package com.springboot.rest.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.springboot.rest.Entity.CalculateAmountEntity;
import com.springboot.rest.Entity.UserAmountAndMCategoryJoinEntity;
import com.springboot.rest.Entity.UserAmountEntity;

/**
 * User amount reposiory.
 * 
 * @author takaseshota
 */
@Mapper
public interface UserAmountRepository {

	@Insert("INSERT INTO user_amount_detail"
			+ "(user_id,"
			+ "balance_year_month, "
			+ "balance_date, "
			+ "category_code, "
			+ "fix_flg, "
			+ "balance_name, "
			+ "balance_flg, "
			+ "amount, "
			+ "remarks, "
			+ "create_at, "
			+ "update_at)"
			+ "VALUES"
			+ "(#{userId},"
			+ "#{balanceYearMonth},"
			+ "#{balanceDate},"
			+ "#{categoryCode},"
			+ "#{fixFlg},"
			+ "#{balanceName},"
			+ "#{balanceFlg},"
			+ "#{amount},"
			+ "#{remarks},"
			+ "#{create},"
			+ "#{update})")
	int registUserAmount(String userId, String balanceYearMonth, String balanceDate, String categoryCode, String fixFlg,
			String balanceName,
			String balanceFlg, String amount, String remarks, String create, String update);

	@Select("SELECT * "
			+ "FROM user_amount_detail "
			+ "WHERE "
			+ "user_id = #{userId} "
			+ "AND "
			+ "balance_date = #{date} ")
	List<UserAmountEntity> findAllByUserId(String userId, String date);

	@Select(" SELECT "
			+ "user.id, "
			+ "user.user_id, "
			+ "user.balance_year_month, "
			+ "user.balance_date, "
			+ "user.category_code, "
			+ "user.fix_flg, "
			+ "user.balance_name, "
			+ "user.balance_flg, "
			+ "user.amount, "
			+ "user.remarks, "
			+ "category.category_name "
			+ "FROM user_amount_detail as user "
			+ "LEFT OUTER JOIN m_category_code as category "
			+ "ON user.category_code = category.category_code "
			+ "WHERE "
			+ "user.user_id = #{userId} "
			+ "AND "
			+ "user.balance_date = #{date} ")
	List<UserAmountAndMCategoryJoinEntity> findByUserIdAndDate(String userId, String date);

	@Select(" SELECT "
			+ "user.id, "
			+ "user.user_id, "
			+ "user.balance_year_month, "
			+ "user.balance_date, "
			+ "user.category_code, "
			+ "user.fix_flg, "
			+ "user.balance_name, "
			+ "user.balance_flg, "
			+ "user.amount, "
			+ "user.remarks, "
			+ "category.category_name "
			+ "FROM "
			+ "user_amount_detail as user "
			+ " LEFT OUTER JOIN m_category_code as category "
			+ "ON user.category_code = category.category_code "
			+ "WHERE "
			+ "user.user_id = #{userId} "
			+ "AND "
			+ "user.balance_year_month = #{month} ")
	List<UserAmountAndMCategoryJoinEntity> findByUserIdAndMonth(String userId, String month);

	@Delete("DELETE FROM user_amount_detail "
			+ " WHERE"
			+ " id = #{id} ;")
	int delete(String id);

	@Select("SELECT * "
			+ "FROM user_amount_detail "
			+ "WHERE "
			+ "user_id = #{userId} "
			+ "AND "
			+ "balance_year_month = #{month}")
	List<UserAmountEntity> findAllByUserIdAndMonth(String userId, String month);

	@Select("SELECT * "
			+ "FROM user_amount_detail "
			+ "WHERE "
			+ "user_id = #{userId} ")
	List<UserAmountEntity> findAll(String userId);

	@Select("SELECT "
			+ "balance_date, "
			+ "balance_flg, "
			+ "fix_flg ,"
			+ "sum(amount) as amount "
			+ "FROM "
			+ "user_amount_detail "
			+ "WHERE "
			+ "user_id = #{userId} "
			+ "GROUP BY "
			+ "balance_date, "
			+ "balance_flg, "
			+ "fix_flg;")
	List<CalculateAmountEntity> findTotalAmountGroupByDate(String userId);

}
