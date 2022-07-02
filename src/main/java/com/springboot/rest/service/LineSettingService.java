package com.springboot.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.rest.date.CreateDate;
import com.springboot.rest.dto.LineSettingRequestDto;
import com.springboot.rest.dto.UserLineSettingDto;
import com.springboot.rest.repository.LineSettingRepository;

@Service
public class LineSettingService {

	@Autowired
	private LineSettingRepository repository;

	public List<UserLineSettingDto> findByUserid(long userId) {

		List<UserLineSettingDto> responseDtoList = new ArrayList<>();

		repository.findAllByUserid(String.valueOf(userId)).stream().forEach(e -> {
			UserLineSettingDto responseDto = new UserLineSettingDto();
			responseDto.setId(e.getId());
			responseDto.setUserId(e.getUserId());
			responseDto.setLineFlg(e.getLineFlg());
			responseDto.setAccessToken(e.getAccessToken());
			responseDtoList.add(responseDto);
		});
		if (responseDtoList.size() < 1) {
			return null;
		}

		return responseDtoList;

	}

	public boolean regist(long userId, LineSettingRequestDto requestDto) {

		repository.insert(String.valueOf(userId), requestDto.getLineFlg(), requestDto.getAccessToken(),
				CreateDate.getNowDateTime(), CreateDate.getNowDateTime());

		return false;

	}

	public boolean update(long userId, LineSettingRequestDto requestDto) {

		repository.update(requestDto.getLineFlg(), requestDto.getAccessToken(), CreateDate.getNowDateTime(),
				String.valueOf(userId));

		return false;
	}

}
