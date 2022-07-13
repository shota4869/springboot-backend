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
import com.springboot.rest.dto.CalenderDto;
import com.springboot.rest.dto.CalenderInitResponseDto;
import com.springboot.rest.repository.UserAmountRepository;

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
	public CalenderInitResponseDto getCallenderList() {

		CalenderInitResponseDto responseDto = new CalenderInitResponseDto();
		List<CalenderDto> dtoList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		List<String> fixedDateList = new ArrayList<>();

		CustomUserDetails user = getUserInfo();

		List<UserAmountEntity> entityList = userAmountRepository.findAll(String.valueOf(user.getId()));
		//雑支出を取得
		Map<String, Long> grpMap = entityList.stream()
				.filter(e -> "0".equals(e.getBalanceFlg()))//収入
				.filter(e -> "0".equals(e.getFixFlg()))//雑収入
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		//雑収入を取得
		Map<String, Long> grpExpenditureMap = entityList.stream()
				.filter(e -> "1".equals(e.getBalanceFlg()))//支出
				.filter(e -> "0".equals(e.getFixFlg()))//雑支出
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		//固定収入を取得
		Map<String, Long> grpFixedIncomeMap = entityList.stream()
				.filter(e -> "0".equals(e.getBalanceFlg()))//収入
				.filter(e -> "1".equals(e.getFixFlg()))//固定収入
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		//固定収入を取得
		Map<String, Long> grpFixedExpenditureMap = entityList.stream()
				.filter(e -> "1".equals(e.getBalanceFlg()))//支出
				.filter(e -> "1".equals(e.getFixFlg()))//固定支出
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		//支出が登録されてい全日付
		for (Map.Entry<String, Long> expEntry : grpExpenditureMap.entrySet()) {
			dateList.add(expEntry.getKey());
		}
		//収入が登録されている全日付
		for (Map.Entry<String, Long> incEntry : grpMap.entrySet()) {
			dateList.add(incEntry.getKey());
		}
		//固定支出が登録されてい全日付
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

		//取得した前日付の収支計算の結果をresponseDtoに設定する
		for (String date : dates) {
			CalenderDto dto = new CalenderDto();
			Long totalBalance = 0L;
			Long income = 0L;
			Long exxpenditure = 0L;
			if (grpMap.get(date) != null) {
				income = grpMap.get(date);
			}
			if (grpExpenditureMap.get(date) != null) {
				exxpenditure = grpExpenditureMap.get(date);
			}
			totalBalance = income - exxpenditure;
			dto.setTitle(String.format("%,d", totalBalance) + "円");
			dto.setStart(date.replace("/", "-"));
			dtoList.add(dto);
		}
		//固定収支
		for (String date : fixedDate) {
			CalenderDto dto = new CalenderDto();
			Long totalBalance = 0L;
			Long income = 0L;
			Long exxpenditure = 0L;
			if (grpFixedIncomeMap.get(date) != null) {
				income = grpFixedIncomeMap.get(date);
			}
			if (grpFixedExpenditureMap.get(date) != null) {
				exxpenditure = grpFixedExpenditureMap.get(date);
			}
			totalBalance = income - exxpenditure;
			dto.setTitle(String.format("%,d", totalBalance) + "円");
			dto.setStart(date.replace("/", "-"));
			dto.setColor("Green");
			dtoList.add(dto);
		}
		responseDto.setCalenderDtoList(dtoList);
		return responseDto;
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
