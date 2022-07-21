package com.springboot.rest.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.date.CreateDate;
import com.springboot.rest.dto.AmountSettingRequestDto;
import com.springboot.rest.dto.LineSettingRequestDto;
import com.springboot.rest.dto.SettingResponseDto;
import com.springboot.rest.dto.SettingSaveResponceDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.dto.UserAmountSettingDto;
import com.springboot.rest.dto.UserLineSettingDto;
import com.springboot.rest.logic.FixAmountLogic;
import com.springboot.rest.logic.UsableAmountLogic;
import com.springboot.rest.logic.UserAmountLogic;
import com.springboot.rest.repository.AmountSettingRepository;
import com.springboot.rest.repository.LineSettingRepository;

@Service
public class SettingService {

	@Autowired
	private AmountSettingRepository amountSettingRepository;

	@Autowired
	private LineSettingRepository lineSettingRepository;

	@Autowired
	private UserAmountLogic userAmountLogic;

	@Autowired
	private FixAmountLogic fixAmountLogic;

	@Autowired
	private UsableAmountLogic usableAmountLogic;

	/**
	 * Inital process.
	 * 
	 * @return SettingResponseDto list.
	 */
	public SettingResponseDto init() throws Exception {
		CustomUserDetails user = getUserInfo();
		SettingResponseDto responceDto = new SettingResponseDto();
		List<UserAmountSettingDto> userSettingList = usableAmountLogic.getAmountSettingByUseid(user.getId());
		List<UserLineSettingDto> lineList = getLineSettingByUserid(user.getId());

		if (userSettingList.size() > 2 || lineList.size() > 2) {
			//2レコード以上登録されていた場合はエラーを返す。
			throw new Exception();
		}

		if (!CollectionUtils.isEmpty(lineList)) {
			//ライン設定がある場合
			responceDto.setLineSetting(lineList.get(0));
		}

		if (CollectionUtils.isEmpty(userSettingList)) {
			//ユーザ設定がされていない場合、ライン設定のみ返却
			return responceDto;
		}
		//今月のユーザ設定がある場合、レスポンスに設定の上返却
		responceDto.setAmountSetting(userSettingList.get(0));
		//固定収入固定支出の取得
		responceDto.setFixIncomeAmount(fixAmountLogic.getFixIncome(user.getId()));
		responceDto.setFixExpenditureAmount(fixAmountLogic.getFixExpenditure(user.getId()));

		return responceDto;

	}

	/**
	 * Regist fixed amount setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	public void saveAmountSetting(AmountSettingRequestDto requestDto) throws Exception {

		CustomUserDetails user = getUserInfo();
		int usableAmount = 0;
		int fixAmount = fixAmountLogic.calculateFixAmount(user.getId());

		//固定収入登録されていれば
		//固定収入-固定支出-目標貯金額/月の日数
		if (0 < fixAmount) {
			usableAmount = (fixAmount - Integer.valueOf(requestDto.getGoalAmount())) / fixAmountLogic.getDays();
		}

		int judge = usableAmountLogic.getAmountSettingByUseid(user.getId()).size();

		if (judge < 1) {
			//登録されていなかったら登録処理
			registSetting(user.getId(), requestDto, usableAmount);
		} else if (judge > 2) {
			//2レコード登録されていた場合はエラーを返す。（デッドコード）
			throw new Exception();
		} else {

			updateSetting(user.getId(), requestDto, usableAmount);
		}

	}

	/**
	 * Regist fix amount setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	public void saveBalanceSetting(UserAmountRequestDto requestDto) throws Exception {

		CustomUserDetails user = getUserInfo();
		userAmountLogic.registUserAmount(requestDto);

		//user_amount_settingの更新処理
		//目標貯金額取得
		List<UserAmountSettingDto> dtoList = usableAmountLogic.getAmountSettingByUseid(user.getId());
		int goalAmount = 0;
		if (!CollectionUtils.isEmpty(dtoList)) {
			goalAmount = dtoList.get(0).getSaveAmount();
		}
		//使用金額計算
		int usableAmount = fixAmountLogic.getUsableAmount(user.getId(), goalAmount);

		//更新処理
		usableAmountLogic.updateSetting(usableAmount, goalAmount);
	}

	/**
	 * Get fixed balance amount
	 * 
	 * @return
	 */
	public SettingSaveResponceDto getFixBalance() {
		SettingSaveResponceDto responceDto = new SettingSaveResponceDto();
		CustomUserDetails user = getUserInfo();
		responceDto.setFixIncomeAmount(fixAmountLogic.getFixIncome(user.getId()));
		responceDto.setFixExpenditureAmount(fixAmountLogic.getFixExpenditure(user.getId()));

		return responceDto;

	}

	/**
	 * Regist line setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	public void saveLineSetting(LineSettingRequestDto requestDto) throws Exception {

		CustomUserDetails user = getUserInfo();

		int judge = getLineSettingByUserid(user.getId()).size();
		if (judge < 1) {
			//登録処理
			regist(user.getId(), requestDto);

		} else if (judge > 2) {
			//2レコード登録されていた場合はエラーを返す。（デッドコード）
			throw new Exception();
		} else {
			//すでに登録されていれば登録されていれば更新処理
			updateLineSetting(user.getId(), requestDto);
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

	/**
	 * regist setting.
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean registSetting(long userId, AmountSettingRequestDto requestDto, int usableAmount)
			throws SQLException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		Date date = new Date();
		int result = amountSettingRepository.insert(String.valueOf(userId), sdf.format(date).toString(),
				requestDto.getGoalAmount(), String.valueOf(usableAmount),
				CreateDate.getNowDateTime(), CreateDate.getNowDateTime());

		if (result < 1) {
			return false;
		}

		return true;
	}

	/**
	 * update setting.
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean updateSetting(long userId, AmountSettingRequestDto requestDto, int usableAmount)
			throws SQLException {

		int result = amountSettingRepository.update(String.valueOf(userId), CreateDate.getNowDate().substring(0, 7),
				requestDto.getGoalAmount(), String.valueOf(usableAmount),
				CreateDate.getNowDateTime());

		if (result < 1) {
			return false;
		}
		return true;
	}

	/**
	 * Get line info.
	 * 
	 * @param userId
	 * @return UserLineSettingDto list.
	 */
	public List<UserLineSettingDto> getLineSettingByUserid(long userId) {

		List<UserLineSettingDto> responseDtoList = new ArrayList<>();

		lineSettingRepository.findAllByUserid(String.valueOf(userId)).stream().forEach(e -> {
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

	/**
	 * Regist line setting.
	 * 
	 * @param userId
	 * @param requestDto
	 * @return
	 */
	public boolean regist(long userId, LineSettingRequestDto requestDto) {

		lineSettingRepository.insert(String.valueOf(userId), requestDto.getLineFlg(), requestDto.getAccessToken(),
				CreateDate.getNowDateTime(), CreateDate.getNowDateTime());

		return false;

	}

	/**
	 * Update line setting.
	 * 
	 * @param userId
	 * @param requestDto
	 * @return
	 */
	public boolean updateLineSetting(long userId, LineSettingRequestDto requestDto) {

		lineSettingRepository.update(requestDto.getLineFlg(), requestDto.getAccessToken(), CreateDate.getNowDateTime(),
				String.valueOf(userId));

		return false;
	}

}
