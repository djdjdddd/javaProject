package toDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

//Json 형태의 데이터를 (1)받아오고, (2)파싱하고, (3)DB에 insert하는 것까지 연습
public class JsonTest {
	// CoinToDb, YongHeeService 클래스 참조
	
	public static void main(String[] args) {
		try {
			URL url = new URL("https://api.bithumb.com/public/ticker/ALL");
			URLConnection urlConn;
			urlConn = url.openConnection();
			
			InputStream is = urlConn.getInputStream(); // URLConnection 추상클래스에 있는 getInputStream 메소드로 데이터 input
			InputStreamReader isr = new InputStreamReader(is); // InputStream 형태로 받은 데이터를 InputStreamReader로 읽기 위해 new & read
			BufferedReader bf = new BufferedReader(isr);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void dataGet(){
		
	}

}
