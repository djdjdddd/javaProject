package com.shinhan.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// 참조 내용
// 쌤: MysqlOracleConnectionTest
// 참조 블로그
// https://kingpodo.tistory.com/17
public class MySqlUtil {

		// 1. DB연결
		public static Connection getConnectionMySql() {
			Connection conn = null;
			String url = "jdbc:mysql://localhost/hr"; // DB 접속 주소
			String userid = "hr";
			String password = "hr";
			
			try {
				Class.forName("com.mysql.jdbc.Driver"); // mysql의 jdbc Driver 주소
				conn = DriverManager.getConnection(url, userid, password);
				
				if(conn != null) {System.out.println("접속성공");}
				else {System.out.println("접속실패");}
				
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
		public static void dbDisconnectMySql(ResultSet rs, Statement st, Connection conn) {
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


