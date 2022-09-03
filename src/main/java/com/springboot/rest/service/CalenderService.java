package com.springboot.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboot.rest.Entity.UserAmountEntity;
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
	 * Get calender info.
	 * 
	 * @param userId
	 * @return
	 */
	public CalenderInitResponseDto init() {

		CalenderInitResponseDto responseDto = new CalenderInitResponseDto();
		List<CalenderDto> dtoList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		List<String> fixedDateList = new ArrayList<>();

		CustomUserDetails user = getUserInfo();

		List<UserAmountEntity> entityList = userAmountRepository.findAll(String.valueOf(user.getId()));
		//雑支出を取得
		Map<String, Long> grpIncomeMap = entityList.stream()
				.filter(e -> BalanceFlag.INCOME.getCode().equals(e.getBalanceFlg()))//収入
				.filter(e -> FixFlag.NORMAL.getCode().equals(e.getFixFlg()))//雑収入
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		//雑収入を取得
		Map<String, Long> grpExpenditureMap = entityList.stream()
				.filter(e -> BalanceFlag.EXPENDTURE.getCode().equals(e.getBalanceFlg()))//支出
				.filter(e -> FixFlag.NORMAL.getCode().equals(e.getFixFlg()))//雑支出
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		//固定収入を取得
		Map<String, Long> grpFixedIncomeMap = entityList.stream()
				.filter(e -> BalanceFlag.INCOME.getCode().equals(e.getBalanceFlg()))//収入
				.filter(e -> FixFlag.FIXED.getCode().equals(e.getFixFlg()))//固定収入
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		//固定収入を取得
		Map<String, Long> grpFixedExpenditureMap = entityList.stream()
				.filter(e -> BalanceFlag.EXPENDTURE.getCode().equals(e.getBalanceFlg()))//支出
				.filter(e -> FixFlag.FIXED.getCode().equals(e.getFixFlg()))//固定支出
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		//支出が登録されている全日付
		for (Map.Entry<String, Long> expEntry : grpExpenditureMap.entrySet()) {
			dateList.add(expEntry.getKey());
		}
		//収入が登録されている全日付
		for (Map.Entry<String, Long> incEntry : grpIncomeMap.entrySet()) {
			dateList.add(incEntry.getKey());
		}
		//固定支出が登録されている全日付
		for (Map.Entry<String, Long> expEntry : grpFixedExpenditureMap.entrySet()) {
			fixedDateList.add(expEntry.getKey());
		}
		//固定収入が登録されている全日付
		for (Map.Entry<String, Long> incEntry : grpFixedIncomeMap.entrySet()) {
			fixedDateList.add(incEntry.getKey());
		}

		//収支で重複する日付を削除
		List<String> dates = dateList.stream().distinct().collect(Collectors.toList());
		//収支で重複する日付を削除
		List<String> fixedDate = fixedDateList.stream().distinct().collect(Collectors.toList());

		//収支設定値計算
		setResponceDto(dtoList, dates, grpIncomeMap, grpExpenditureMap, FixFlag.NORMAL.getCode());
		//固定収支設定理計算
		setResponceDto(dtoList, fixedDate, grpFixedIncomeMap, grpFixedExpenditureMap, FixFlag.FIXED.getCode());
		responseDto.setCalenderDtoList(dtoList);
		return responseDto;
	}

	/**
	 * Set response dto list.
	 * 
	 * @param dtoList
	 * @param dateList
	 * @param incomeMap
	 * @param expenditureMap
	 * @param fixFlg
	 */
	private void setResponceDto(List<CalenderDto> dtoList, List<String> dateList, Map<String, Long> incomeMap,
			Map<String, Long> expenditureMap, String fixFlg) {

		for (String date : dateList) {
			//変数初期化
			CalenderDto dto = new CalenderDto();
			Long totalBalance = 0L;
			Long income = 0L;
			Long exxpenditure = 0L;

			if (incomeMap.get(date) != null) {
				//収支の登録さレている全日付の中で収入が登録されている日付があった場合、その日の収入を設定
				income = incomeMap.get(date);
			}
			if (expenditureMap.get(date) != null) {
				//収支の登録さレている全日付の中で支出が登録されている日付があった場合、その日の支出を設定
				exxpenditure = expenditureMap.get(date);
			}

			//収支合計計算
			totalBalance = income - exxpenditure;

			//設定
			//収支合計設定
			dto.setTitle(String.format("%,d", totalBalance) + "円");
			//日付設定
			dto.setStart(date.replace("/", "-"));
			if (FixFlag.FIXED.getCode().equals(fixFlg)) {
				//固定収支の場合、イベントの背景色をグリーンにする設定
				dto.setColor("Green");
			}
			dtoList.add(dto);
		}
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
