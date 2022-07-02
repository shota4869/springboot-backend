package com.springboot.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.rest.Entity.MCategoryEntity;
import com.springboot.rest.dto.CategoryDto;
import com.springboot.rest.dto.HomeInitResponseDto;
import com.springboot.rest.repository.MCategoryRepository;

@Service
public class MCategoryService {

	@Autowired
	private MCategoryRepository categoryRepository;

	public HomeInitResponseDto findCategory() {

		List<MCategoryEntity> targetList = categoryRepository.findAll();

		HomeInitResponseDto homeInitDto = new HomeInitResponseDto();

		List<CategoryDto> incomeCategoryList = new ArrayList<CategoryDto>();
		List<CategoryDto> expenditureCategoryList = new ArrayList<CategoryDto>();
		targetList.stream().filter(e -> "0".equals(e.getBalanceFlg()))
				.forEach(item -> {
					CategoryDto categoryDto = new CategoryDto();
					categoryDto.setId(item.getId());
					categoryDto.setCategoryCode(item.getCategoryCode());
					categoryDto.setCategoryName(item.getCategoryName());
					categoryDto.setBalanceFlg(item.getBalanceFlg());
					incomeCategoryList.add(categoryDto);
				});

		targetList.stream().filter(e -> "1".equals(e.getBalanceFlg()))
				.forEach(item -> {
					CategoryDto categoryDto = new CategoryDto();
					categoryDto.setId(item.getId());
					categoryDto.setCategoryCode(item.getCategoryCode());
					categoryDto.setCategoryName(item.getCategoryName());
					categoryDto.setBalanceFlg(item.getBalanceFlg());
					expenditureCategoryList.add(categoryDto);
				});

		System.out.println(incomeCategoryList.get(1).getCategoryCode());
		homeInitDto.setIncomeCategory(incomeCategoryList);
		homeInitDto.setExpenditureCategory(expenditureCategoryList);

		return homeInitDto;
	}
}
