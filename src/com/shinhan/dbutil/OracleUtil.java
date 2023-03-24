package com.shinhan.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// DB connect하는 애를 매번 만들기 싫어서 아래의 클래스를 만들었다. 
// 왜냐면 '작업 내용은 그때그때 바뀌니까' 바뀌는 놈은 바뀌는 놈끼리
// 이렇게 'DB connect과 disconnect만 전용으로 해주기 위한' 놈을 생성. (안 바뀌는 놈은 따로 빼줘서 클래스로 만들어줬다)

public class OracleUtil {

	// 1. DB연결
	public static Connection getConnection() {
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		String userid = "hr";
		String password = "hr";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, userid, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}
	
	// 2. 자원반납 (disconnect)
	public static void dbDisconnect(ResultSet rs, Statement st, Connection conn) {
		try {
			if(rs != null) rs.close(); 
			if(st != null) st.close(); 
			if(conn != null) conn.close(); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
