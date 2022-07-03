package com.springboot.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.rest.Entity.UserAmountEntity;
import com.springboot.rest.dto.CalenderDto;
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
	public List<CalenderDto> getCallenderList(long userId) {

		List<CalenderDto> dtoList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();

		Map<String, Long> grpMap = userAmountRepository.findAll(String.valueOf(userId)).stream()
				.filter(e -> "0".equals(e.getBalanceFlg()))
				.collect(
						Collectors.groupingBy(UserAmountEntity::getBalanceDate,
								Collectors.summingLong(UserAmountEntity::getAmount)));

		Map<String, Long> grpExpenditureMap = userAmountRepository.findAll(String.valueOf(userId)).stream()
				.filter(e -> "1".equals(e.getBalanceFlg()))
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

		//収支で重複する日付を削除
		List<String> dates = dateList.stream().distinct().collect(Collectors.toList());

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

		return dtoList;
	}

}
