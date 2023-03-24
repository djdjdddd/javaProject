package toDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Parsing {
	
	public static void main(String[] args) {
		
		try {
		// 자신이 조회를 원하는
		String nx = "";
		String ny = "";
		String baseDate = "";
		String baseTime = "";
		
		// 서비스 인증키. 공공데이터 포털에서 제공해준 인증키를 넣으셈
		String serviceKey = "49C600132A884C5FA2CD2708FC290A9AD9678562";
		
		// 정보를 모아서 URL정보를 만들면 된다. 맨 마지막 "&_type=json"에 따라 반환 데이터의 형태가 정해짐.
		String urlStr = "http://data-dbg.krx.co.kr/svc/apis/sto/stk_bydd_trd"
				+ "serviceKey"
				+ "";
		
		URL url;
		
			url = new URL(urlStr);
		
				
		BufferedReader bf;
		String line = "";
		String result = "";
		
		// OO 정보를 받아옵니다.
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		
		// 버퍼에 있는 정보를 하나의 문자열로 변환
		while((line = bf.readLine()) != null) {
			result = result.concat(line);
			// System.out.println(result); // 받아온 데이터를 확인
		}
		
		// Json parser를 만들어 만들어진 문자열 데이터를 객체화
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(result);
		
		// TOP 레벨 단계인 response 키를 갖고 데이터를 파싱
		JSONObject parse_response = (JSONObject) obj.get("response");
		
		// response로부터 body 얻기
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		
		// body로부터 items 받기 (**json 파일 구성에 따라 여기는 가변적)
		JSONObject parse_items = (JSONObject)parse_body.get("items");
		
		// items로부터 itemlist를 받기 (itemlist : 뒤에 [로 시작하므로 jsonarrasy이다)
		JSONArray parse_item = (JSONArray)parse_items.get("item");
		
		String category;
		JSONObject weather; // parse_item은 배열 형태이기 때문에 하나씩 데이터를 가져올 때 사용
		
		// 필요한 데이터만 가져오려고 한다.
		for(int i = 0; i < parse_item.size(); i++) {
			weather = (JSONObject)parse_item.get(i);
			String base_Date = (String)weather.get("baseDate");
			String base_Date = (String)weather.get("baseDate");
			String base_Date = (String)weather.get("baseDate");
			String base_Date = (String)weather.get("baseDate");
			String base_Date = (String)weather.get("baseDate");
			String base_Date = (String)weather.get("baseDate");
			String base_Date = (String)weather.get("baseDate");
			String base_Date = (String)weather.get("baseDate");
			
			// 출력합니다. 
		}
		
		bf.close();
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
