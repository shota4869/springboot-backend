package com.springboot.rest.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.Entity.AmountSettingEntity;
import com.springboot.rest.date.CreateDate;
import com.springboot.rest.dto.UsableAmountDto;
import com.springboot.rest.repository.AmountSettingRepository;
import com.springboot.rest.repository.UsableAmountRepository;
import com.springboot.rest.repository.UserAmountRepository;

@Component
public class UsableAmountLogic {

	@Autowired
	private UsableAmountRepository usableAmountRepository;

	@Autowired
	private AmountSettingRepository amountSettingRepository;

	@Autowired
	private UserAmountRepository userAmountRepository;

	/**
	 * Common proccess.
	 * 
	 * @param userId
	 * @return
	 */
	public List<UsableAmountDto> commonProcess(long userId) throws SQLException {

		List<UsableAmountDto> dtoList = findAll(userId, CreateDate.getNowDate());

		//本日の使用可能金額が登録されていない場合は登録、それ以外は取得
		if (dtoList.size() < 1) {
			//登録処理
			registUsableAmount(userId, CreateDate.getNowDate());

			//再取得処理
			dtoList = findAll(userId, CreateDate.getNowDate());
		}

		return dtoList;
	}

	/**
	 * select user_amount.
	 * 
	 * @return
	 */
	public List<UsableAmountDto> findAll(long userId, String date) {

		List<UsableAmountDto> dtoList = new ArrayList<>();

		usableAmountRepository.findAll(String.valueOf(userId), date).stream().forEach((e) -> {
			UsableAmountDto dto = new UsableAmountDto();
			dto.setId(e.getId());
			dto.setUserId(e.getUserId());
			dto.setDate(e.getDate());
			dto.setUsableAmount(e.getUsableAmount());
			dtoList.add(dto);
		});

		return dtoList;
	}

	/**
	 * Regist usable amount.
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean registUsableAmount(long userId, String date) throws SQLException {

		int result = usableAmountRepository.insert(String.valueOf(userId),
				date,
				String.valueOf(calculateAmount(userId)), CreateDate.getNowDateTime(), CreateDate.getNowDateTime());
		//TOBE エラーハンドリング
		if (result < 1) {
			new SQLException();
		}

		return false;
	}

	/**
	 * Calculate usable amount.
	 * 
	 * @param userId
	 * @return
	 */
	private int calculateAmount(long userId) {

		//可処分所得計算
		//可処分所得=固定収入-固定支出-貯金金額
		List<AmountSettingEntity> entityList = amountSettingRepository.findByUseidAndMonth(String.valueOf(userId),
				CreateDate.getNowDate().substring(0, 7));

		int disposableIncome = 0;
		if (!CollectionUtils.isEmpty(entityList)) {
			AmountSettingEntity entity = entityList.get(0);
			disposableIncome = entity.getFixedIncome() - entity.getFixedExpenditure() - entity.getSaveAmount();
		}

		//実可処分所得計算
		//実可処分所得＝可処分所得+(月雑収入-月雑支出)=可処分所得+月収支金額
		List<Integer> incomeAmountList = userAmountRepository
				.findAllByUserIdAndMonth(String.valueOf(userId), CreateDate.getNowDate().substring(0, 7)).stream()
				.filter(e -> "0".equals(e.getBalanceFlg())).map(e -> e.getAmount()).collect(Collectors.toList());
		List<Integer> expenditureAmountList = userAmountRepository
				.findAllByUserIdAndMonth(String.valueOf(userId), CreateDate.getNowDate().substring(0, 7)).stream()
				.filter(e -> "1".equals(e.getBalanceFlg())).map(e -> e.getAmount()).collect(Collectors.toList());
		//収入合計
		int totalIncome = incomeAmountList.stream().mapToInt(Integer::intValue).sum();

		//支出合計
		int totalExpenditure = expenditureAmountList.stream().mapToInt(Integer::intValue).sum();

		//合計収支
		int totalBalance = totalIncome - totalExpenditure;

		//実可処分所得
		int realDisposableIncome = disposableIncome + totalBalance;

		//1日使える金額＝実可処分所得/残り日数
		int useableAmount = realDisposableIncome / calculateDays();

		return useableAmount;
	}

	/**
	 * 残日数計算
	 * 
	 * @return 残日数
	 */
	private int calculateDays() {

		Calendar calFrom = Calendar.getInstance();
		int date = calFrom.get(Calendar.DATE);

		Calendar calTo = Calendar.getInstance();
		int lastDay = calTo.getActualMaximum(Calendar.DATE);

		return lastDay - date + 1;
	}

}
