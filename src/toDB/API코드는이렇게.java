package toDB;

/* Java 샘플 코드 */


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;

// 참조 블로그
// https://fbtmdwhd33.tistory.com/264
public class API코드는이렇게 {
	
    public static void main(String[] args) throws IOException {
    	
    	// 1. URL을 만들기 위한 StringBuilder.
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1262000/CountryCovid19SafetyServiceNew/getCountrySafetyNewsListNew"); /*URL*/
        
        // 2. 오픈 API의요청 규격에 맞는 파라미터 생성,                               발급받은 인증키 입력
        // ★★ 이 부분은 API 종류에 따라 제공하는 코드별로 다르니까 그것 보고??
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=인코딩된 서비스키"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*XML 또는 JSON*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("cond[country_nm::EQ]","UTF-8") + "=" + URLEncoder.encode("가나", "UTF-8")); /*한글 국가명*/
        urlBuilder.append("&" + URLEncoder.encode("cond[country_iso_alp2::EQ]","UTF-8") + "=" + URLEncoder.encode("GH", "UTF-8")); /*ISO 2자리코드*/
       
        // 3. URL 객체 생성.
        URL url = new URL(urlBuilder.toString());
       
        // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // 5. 통신을 위한 메소드 SET.
        conn.setRequestMethod("GET");
       
        // 6. 통신을 위한 Content-type SET. 
        conn.setRequestProperty("Content-type", "application/json");
       
        // 7. 통신 응답 코드 확인.
        System.out.println("Response code: " + conn.getResponseCode());
       
        // 8. 전달받은 데이터를 BufferedReader 객체로 저장.
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
      
        // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
       
        // 10. 객체 해제.
        rd.close();
        conn.disconnect();
       
        // 11. 전달받은 데이터 확인.
        System.out.println(sb.toString());
        
       
        // 필자가 사용한 오픈 API는 결과값이 리스트 형태로 전달되기 때문에 이를 적절히 사용하기 위해선 적절한 자료구조에 담아주는 과정이 필요했는데, 
        // API문서에 교환 데이터의 표준이 JSON으로 되어있었기 때문에 JSON 형태로 변환하고자 했다. 아래가 그 코드이다.
        
        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
        JSONParser parser = new JSONParser();
        // 2. 문자열을 JSON 형태로 JSONObject 객체에 저장. 	
        JSONObject obj = (JSONObject)parser.parse(sb.toString());
        // 3. 필요한 리스트 데이터 부분만 가져와 JSONArray로 저장.
        JSONArray dataArr = (JSONArray) obj.get("data");
        // 4. model에 담아준다.
        model.addAttribute("data",dataArr);
        
        // 이와 같은 방식을 사용해 JSP에서 data를 참조할 수 있고, jstl의 c:if태그를 사용해 리스트의 각 요소들을 참조하여 사용할 수 있게 된다. 
    }
}
