package aproject.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

	public static Date convertToDate(String sdate){
		
		SimpleDateFormat sdf = // 그걸 여기다 받은 다음
		new SimpleDateFormat("yyyy/MM/dd"); // 내가 "yyyy/MM/dd" 형태의 날짜를 줄 거야.
		                                    // 아까  20230316 이렇게 줬더니 에러났음 ㅋㅋ 
		                                    //      2023/03/16 이렇게 주라고 내가 설정해놓고 뭐함?
		// 에러메세지 : java.text.ParseException: Unparseable date: "20230316"
	
		Date sqlDate = null; 
		
		try {
			// util의 Date
			java.util.Date d = sdf.parse(sdate); // 여기서의 Date는 sql이 아니라 util의 Date를 import 해야 된다고?? why??
			
			// 얘는 sql의 Date
			sqlDate = new Date(d.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		return sqlDate;
		  
	}
}
