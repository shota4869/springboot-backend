package com.springboot.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.Entity.CalculateAmountEntity;
import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.common.BalanceFlag;
import com.springboot.rest.common.FixFlag;
import com.springboot.rest.dto.CalenderDto;
import com.springboot.rest.dto.CalenderInitResponseDto;
import com.springboot.rest.repository.UserAmountRepository;

/**
 * Calendar service
 * 
 * @author takaseshota
 */
@Service
public class CalenderService {

	@Autowired
	private UserAmountRepository userAmountRepository;

	/**
	 * Initialize.
	 * 
	 * @return CalenderInitResponseDto
	 */
	public CalenderInitResponseDto init() {

		CalenderInitResponseDto responseDto = new CalenderInitResponseDto();
		List<CalenderDto> dtoList = new ArrayList<>();

		CustomUserDetails user = getUserInfo();

		//ユーザ毎に登録した日付を取得（カレンダーの日付ごとに収支値を表示するため）①
		List<CalculateAmountEntity> entityList = userAmountRepository
				.findTotalAmountGroupByDate(String.valueOf(user.getId()));

		if (CollectionUtils.isEmpty(entityList)) {
			//収支の登録がなかった場合、処理を中断する
			return responseDto;
		}

		// ①について重複する日付を削る。日付のみのリストを作成。②
		List<String> dateList = entityList.stream().map(e -> e.getBalanceDate()).distinct()
				.collect(Collectors.toList());

		//②で作成した日付分繰り返しレスポンスのDTOに計算結果を設定する。③
		for (String date : dateList) {
			int income = 0;
			int expenditure = 0;
			int fixedIncome = 0;
			int fixedExpenditure = 0;

			//①で取得した日付に合致した②のリストを取得 ④
			List<CalculateAmountEntity> currentList = entityList.stream()
					.filter(e -> date.equals(e.getBalanceDate())).collect(Collectors.toList());

			//④のリストで繰り返し、それぞれの条件に基づいた変数に設定。
			for (CalculateAmountEntity current : currentList) {
				if (BalanceFlag.EXPENDTURE.getCode().equals(current.getBalanceFlg())
						&& FixFlag.NORMAL.getCode().equals(current.getFixFlg())) {
					//雑支出の場合
					expenditure = current.getAmount();
				} else if (BalanceFlag.INCOME.getCode().equals(current.getBalanceFlg())
						&& FixFlag.NORMAL.getCode().equals(current.getFixFlg())) {
					//雑収入の場合
					income = current.getAmount();

				} else if (BalanceFlag.EXPENDTURE.getCode().equals(current.getBalanceFlg())
						&& FixFlag.FIXED.getCode().equals(current.getFixFlg())) {
					//固定支出の場合
					fixedExpenditure = current.getAmount();

				} else if (BalanceFlag.INCOME.getCode().equals(current.getBalanceFlg())
						&& FixFlag.FIXED.getCode().equals(current.getFixFlg())) {
					//固定収入の場合
					fixedIncome = current.getAmount();
				} else {
					continue;
				}
			}
			//CalenderDtoに設定
			if (income - expenditure != 0) {
				//雑収支合計の設定
				setCalendarDto(FixFlag.NORMAL.getCode(), income - expenditure, date,
						dtoList);
			}
			if (fixedIncome - fixedExpenditure != 0) {
				//固定収支合計の計算
				setCalendarDto(FixFlag.FIXED.getCode(), fixedIncome - fixedExpenditure,
						date,
						dtoList);
			}
		}

		responseDto.setCalenderDtoList(dtoList);
		return responseDto;
	}

	/**
	 * Set calendar dto
	 * 
	 * @param fixFlg
	 * @param totalBalance
	 * @param currentDate
	 * @param dtoList
	 */
	private void setCalendarDto(String fixFlg, int totalBalance, String currentDate,
			List<CalenderDto> dtoList) {

		CalenderDto dto = new CalenderDto();
		dto.setStart(currentDate.replace("/", "-"));
		dto.setTitle(String.format("%,d", totalBalance));

		//固定の場合カラーも設定
		if (FixFlag.FIXED.getCode().equals(fixFlg)) {
			//固定収支の場合、イベントの背景色をグリーンにする設定
			dto.setColor("Green");
		}
		dtoList.add(dto);
	}

	/**
	 * Get usr info.
	 * 
	 * @return
	 */
	private CustomUserDetails getUserInfo() {
		return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}
}
