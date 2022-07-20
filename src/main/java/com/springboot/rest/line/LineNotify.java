package com.springboot.rest.line;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.Entity.AmountSettingEntity;
import com.springboot.rest.Entity.LineSettingEntity;
import com.springboot.rest.date.CreateDate;
import com.springboot.rest.logic.CalculateBalanceLogic;
import com.springboot.rest.logic.FixAmountLogic;
import com.springboot.rest.repository.AmountSettingRepository;
import com.springboot.rest.repository.LineSettingRepository;

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

	private final String DEFALT_MESSAGE = "今日も1日頑張れ、いいことあるぞ";

	final int time = 9;

	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")
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
				message = DEFALT_MESSAGE;
			} else {
				//目標貯金額が設定されている場合
				int resultBalance = fixAmountLogic.getUsableAmount(entity.getUserId(),
						amountSettingEntityList.get(0).getSaveAmount())
						+ calculateBalanceLogic.getPreviousDaybalanceCalculete(entity.getUserId());
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

			sendNotification(message, token);
		}
	}

	public void sendNotification(String message, String token) {
		HttpURLConnection connection = null;

		try {
			URL url = new URL("https://notify-api.line.me/api/notify");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Authorization", "Bearer " + token);
			connection.setUseCaches(false);
			connection.connect();
			try (OutputStream outputStream = connection.getOutputStream();
					PrintWriter writer = new PrintWriter(outputStream)) {
				writer.append("message=").append(URLEncoder.encode(message, "UTF-8")).flush();
				System.out.println("Bearer " + token);
				try (InputStream is = connection.getInputStream();
						BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
					String res = r.lines().collect(Collectors.joining());
					if (!res.contains("\"message\":\"ok\"")) {
						System.out.println(res);
						System.out.println("なんか失敗したっぽい");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("なんか失敗したっぽい2");

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
