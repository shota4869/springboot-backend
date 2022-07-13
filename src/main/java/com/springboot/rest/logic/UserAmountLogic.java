package com.springboot.rest.logic;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.date.CreateDate;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.repository.UserAmountRepository;

@Component
public class UserAmountLogic {

	@Autowired
	private UserAmountRepository userAmountRepository;

	/**
	 * Regist save amount.
	 * 
	 * @param userAmountDto
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void registUserAmount(UserAmountRequestDto userAmountDto) throws SQLException {

		String balanceName = "";

		if ("1".equals(userAmountDto.getFixFlg())) {
			balanceName = "固定";
		}

		CustomUserDetails user = getUserInfo();

		int count = userAmountRepository.registUserAmount(String.valueOf(user.getId()),
				userAmountDto.getDate().substring(0, 7),
				userAmountDto.getDate(),
				String.format("%03d", Integer.valueOf(userAmountDto.getCategoryCode())),
				userAmountDto.getFixFlg(), balanceName,
				userAmountDto.getBalanceFlg(), userAmountDto.getAmount(), userAmountDto.getRemarks(),
				CreateDate.getNowDateTime(), CreateDate.getNowDateTime());

		if (count < 1) {
			throw new SQLException();
		}
	}

	/**
	 * Get user info.
	 * 
	 * @return CustomUserDetails.
	 */
	private CustomUserDetails getUserInfo() {
		return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}
}
