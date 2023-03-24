package toDB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CoinToDb {

	public static void main(String[] args) {
		
		// 1. json 형태로 받아오기 (from 빗썸API)
		String coinName = "BTC"; // 디폴트
		String currency = "KRW"; // 디폴트

		try {
			// 현재가 정보 조회 (자산별)
//			URL url = new URL("https://api.bithumb.com/public/ticker/" + coinName + "_" + currency);
			// 현재가 정보 조회 (ALL)
			URL url = new URL("https://api.bithumb.com/public/ticker/ALL");

			URLConnection conn;
			conn = url.openConnection();

			BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			bf = new BufferedReader(new InputStreamReader(url.openStream()));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bf.readLine()) != null) {
				sb.append(line);
			}

			bf.close();
//			conn.disconnect();

			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(sb.toString());
			JSONObject obj2 = (JSONObject) obj.get("data");
			JSONObject data = (JSONObject) obj2.get("BTC");

			System.out.println(data.get("prev_closing_price").toString());
			System.out.println(obj2);
			
//			String cc = null;
//			List<String> keyList = new ArrayList<>();
//			for(Object key : obj2.keySet()) {
				
//				keyList = (List<String>) key;
//			}
			System.out.println(obj2.keySet());
			
			for(Object key : obj2.keySet()) {
				JSONObject data2 = (JSONObject) obj2.get(key);
				JSONObject data3 = (JSONObject) data2.get("prev_closing_price");
				
				System.out.println(data3);
			}
			
			
			// 위에서 얻은 키(key)(코인 이름)를 list로 만들어서?
			// 그 list를 for문으로 key를 돌려가면서 모든 종가를 뽑아내는 과정을 만들어보자.
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// CoinDataInsert (그냥 합쳐야 되나?? 따로 나누지 말고)
		CoinDataInsert cdi = new CoinDataInsert();
		
		
		
	}

}
