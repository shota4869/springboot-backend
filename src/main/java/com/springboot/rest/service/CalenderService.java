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

	public List<CalenderDto> getCallenderList(long userId) {

		List<CalenderDto> dtoList = new ArrayList<>();

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

		List<String> dateList = new ArrayList<>();
		for (Map.Entry<String, Long> expEntry : grpExpenditureMap.entrySet()) {
			dateList.add(expEntry.getKey());
		}

		for (Map.Entry<String, Long> incEntry : grpMap.entrySet()) {
			dateList.add(incEntry.getKey());
		}

		List<String> dates = dateList.stream().distinct().collect(Collectors.toList());
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
