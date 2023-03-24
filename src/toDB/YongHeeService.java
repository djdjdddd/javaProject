package toDB;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.CoinDAO;
import util.MySqlUtil;
import vo.JsonVO;

public class YongHeeService {
	
	public void insertData()  {
		try {
			List<JsonVO> datalist = dataGet();
			
			CoinDAO coinDao = new CoinDAO();
			coinDao.insertCoin(datalist);
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<JsonVO> dataGet() throws IOException, InterruptedException  {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.bithumb.com/public/ticker/ALL_KRW"))
				.header("accept", "application/json").method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println(response.body());
		String json = response.body();
		JSONObject root = new JSONObject(json);
		JSONObject jsonData = root.getJSONObject("data");
//		System.out.println(jsonData);

		Set<String> keys = jsonData.keySet();

		List<JsonVO> list = new ArrayList<>();
		for (String key : keys) {
			if(key.equals("date")) break;
			JsonVO vo = new JsonVO();
			vo.setCoin_name(key);
			
			JSONObject obj = jsonData.getJSONObject(key);
			double a = Double.parseDouble((String) obj.get("prev_closing_price"));
			vo.setPrev_closing_price(a);
			a = Double.parseDouble((String) obj.get("closing_price"));		
			vo.setClosing_price(a);
			list.add(vo);
		}
//		System.out.println(list);
		return list;
	}
}
