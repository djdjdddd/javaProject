package toDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mysql.cj.protocol.OutputStreamWatcher;

public class CoinDbTest {

//	OkHttpClient client = new OkHttpClient();
//
//	Request request = new Request.Builder()
//	  .url("https://api.bithumb.com/public/ticker/ALL_KRW")
//	  .get()
//	  .addHeader("accept", "application/json")
//	  .build();
//
//	Response response = client.newCall(request).execute();

	public static void main(String[] args) {

		// 1. json 형태로 받아오기 (from 빗썸API)
		String coinName = "BTC"; // 디폴트
		String currency = "KRW"; // 디폴트

		try {
			// 현재가 정보 조회 (자산별)
//			URL url = new URL("https://api.bithumb.com/public/ticker/" + coinName + "_" + currency);
			// 현재가 정보 조회 (ALL)
			URL url = new URL("https://api.bithumb.com/public/ticker/ALL");

			// 두번째 url은 서버에 보내줄 request의 설정값(parameter)입니다.
			String postsql = "&offset=0&count=20";
			URLConnection conn;
			conn = url.openConnection();

//			conn.setDoOutput(true);
//			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//			wr.write(postsql);
//			wr.flush();
			BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// 그냥 스트링 사용시
//			String result = "";
//			String line = "";

			// 스트링 버퍼 사용시
//			StringBuilder sb = new StringBuilder();
//			String line = null;

			// OO 정보를 받아옵니다.
			bf = new BufferedReader(new InputStreamReader(url.openStream()));

			// 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = bf.readLine()) != null) {
	            sb.append(line);
	        }
	       
	        // 10. 객체 해제.
	        bf.close();
//	        conn.disconnect();
	       
	        // 11. 전달받은 데이터 확인.
	        System.out.println(sb.toString());
	        
	       
	        // 필자가 사용한 오픈 API는 결과값이 리스트 형태로 전달되기 때문에 이를 적절히 사용하기 위해선 적절한 자료구조에 담아주는 과정이 필요했는데, 
	        // API문서에 교환 데이터의 표준이 JSON으로 되어있었기 때문에 JSON 형태로 변환하고자 했다. 아래가 그 코드이다.
	        
	        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
	        JSONParser parser = new JSONParser();
	        // 2. 문자열(스트링빌더의 sb)을 JSON 형태로 JSONObject 객체에 저장.(★URL을 통해 가져오고 append로 붙인 문자열 sb를 파싱!! 하여 json 오브젝트 타입으로 저장하는 것)
	        // (왜냐면 이렇게 해야 key값을 주면 value값을 얻을 수 있는 json이 되거든)
	        JSONObject obj = (JSONObject)parser.parse(sb.toString());
	        // 3. 껍데기 벗기기
	        // (1) "data" 키 넣어서 value 얻음. 근데?! 그렇게 얻은 형태가 또 json이라서
	        JSONObject obj2 = (JSONObject) obj.get("data");
	        // (2) 또 "BTC"와 같은 키 넣어서 거기에 들은 opening_price ~ fluctate_rate_24H 까지 얻음.
	        JSONObject data = (JSONObject) obj2.get("BTC");
	        
	        // (3) 그중에서 또 key값으로 "prev_closing_price"를 주어서 거기에 들은 value값을 뽑아내 필요한 값을 추출하였음. 
	        System.out.println(data.get("prev_closing_price").toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
