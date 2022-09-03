package com.springboot.rest.logic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.Entity.AmountSettingEntity;
import com.springboot.rest.Entity.LineSettingEntity;
import com.springboot.rest.common.CreateDate;
import com.springboot.rest.repository.AmountSettingRepository;
import com.springboot.rest.repository.LineSettingRepository;

/**
 * Line notify logic.
 * 
 * @author takaseshota
 */
@Component
public class LineNotify {

	@Autowired
	private LineSettingRepository lineSettingRepository;

	@Autowired
	private AmountSettingRepository amountSettingRepository;

	@Autowired
	private CalculateBalanceLogic calculateBalanceLogic;

	@Autowired
	private FixAmountLogic fixAmountLogic;

	/**
	 * DEFAULT MESSAGE. 
	 */
	private final String DEFAULT_MESSAGE = "今日も1日頑張れ、いいことあるぞ";

	/**
	 * TEST MESSAGE.
	 */
	private final String TEST_MESSAGE = "テスト接続に成功しました。";

	/**
	 * API URL.
	 */
	private final String API_URL = "https://notify-api.line.me/api/notify";

	/**
	 *  Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(LineNotify.class);

	/**
	 * Daily line notify logic.
	 */
	@Scheduled(cron = "0 0 8 * * *", zone = "Asia/Tokyo")
	public void executeNotification() {
		StringBuilder sb = new StringBuilder();
		String message = null;
		//user_line_settingからライン連携フラグが1のデータを取得
		List<LineSettingEntity> entityList = lineSettingRepository.findAll();

		if (CollectionUtils.isEmpty(entityList)) {
			//ライン連携フラグ1のデータがない場合処理中止
			return;
		}

		//上記データのuserId毎に繰り返す。
		for (LineSettingEntity entity : entityList) {
			//目標貯金額取得
			List<AmountSettingEntity> amountSettingEntityList = amountSettingRepository
					.findByUseidAndMonth(String.valueOf(entity.getUserId()), CreateDate.getNowDate().substring(0, 7));

			if (amountSettingEntityList.size() < 1) {
				//目標金額設定がない場合デフォルトメッセージ設定
				message = DEFAULT_MESSAGE;
			} else {
				//目標貯金額が設定されている場合
				int resultBalance = fixAmountLogic.getUsableAmount(entity.getUserId(),
						amountSettingEntityList.get(0).getSaveAmount())
						+ calculateBalanceLogic.getPreviousDaybalanceCalculete(entity.getUserId());

				//メッセージ設定
				if (0 < fixAmountLogic.getFixIncome(entity.getUserId())) {
					//固定収入が入力されてない場合
					sb.append("\r・1日に使える金額：");
					sb.append("\r");
					sb.append(String.format("%,d", fixAmountLogic.getUsableAmount(entity.getUserId(),
							amountSettingEntityList.get(0).getSaveAmount())));
					sb.append("円");
				}
				sb.append("\r・昨日の収入：");
				sb.append("\r");
				sb.append(String.format("%,d", calculateBalanceLogic.getPreviousDayIncomeAmount(entity.getUserId())));
				sb.append("円");
				sb.append("\r・昨日の支出：");
				sb.append("\r");
				sb.append(String.format("%,d",
						calculateBalanceLogic.getPreviousDayExpenditureAmount(entity.getUserId())));
				sb.append("円");
				sb.append("\r・昨日の収支：");
				sb.append("\r");
				sb.append(
						String.format("%,d", calculateBalanceLogic.getPreviousDaybalanceCalculete(entity.getUserId())));
				sb.append("円");
				sb.append("\r・昨日の結果：");
				sb.append("\r");
				if (0 < resultBalance) {
					sb.append("+");
				}
				sb.append(String.format("%,d", resultBalance));
				sb.append("円");

				message = sb.toString();
			}

			//アクセストークンを設定
			String token = entity.getAccessToken();

			try {
				sendNotification(message, token);
			} catch (Exception e) {
				log.error(e + String.valueOf(entity.getUserId()));
			}
		}
	}

	/**
	 * Test message excute.
	 * 
	 * @param accessToken
	 * @throws Exception
	 */
	public void testExcecute(String accessToken) throws Exception {

		sendNotification(TEST_MESSAGE, accessToken);

	}

	/**
	 * Line API excute.
	 * 
	 * @param message
	 * @param token
	 * @throws Exception
	 */
	private void sendNotification(String message, String token) throws Exception {
		HttpURLConnection connection = null;

		try {
			URL url = new URL(API_URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Authorization", "Bearer " + token);
			connection.setUseCaches(false);
			connection.connect();
			try (OutputStream outputStream = connection.getOutputStream();
					PrintWriter writer = new PrintWriter(outputStream)) {
				writer.append("message=").append(URLEncoder.encode(message, "UTF-8")).flush();
				try (InputStream is = connection.getInputStream();
						BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
					String res = r.lines().collect(Collectors.joining());
					if (!res.contains("\"message\":\"ok\"")) {
						throw new Exception();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
