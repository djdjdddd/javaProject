package toDB;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.sql.*;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class CoinDataInsert {

	public static void main(String[] args) {
		java.sql.Statement stmt = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String filePath = "JSON 파일의 위치";

		String name="";
		long soldAmount = 0;
		long price =0;
		
		try{
			String driverClassName = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "hr";
			String password = "hr";
			
			//JDBC Driver Loading
			Class.forName(driverClassName);
			
			//JDBC Connection getting
			conn = DriverManager.getConnection(url, user, password);
			
			System.out.println("DB 연결 성공");
			System.out.println("** Driver:" + driverClassName + ", Connection:" + conn);
		
			
			//테이블 없을 시 테이블 생성
			
			String query = "CREATE TABLE TEST(";
					query += "no number(4) primary key,";
					query += "name varchar2(30),";
					query += "soldAmount number(20),";
					query += "price number(20))";
			
			stmt = conn.createStatement();
			
			boolean b = stmt.execute(query);
			
			System.out.println("b : " + b);

		}catch(ClassNotFoundException ex){
			System.out.println("드라이버 로딩 실패");
			ex.printStackTrace();
		} catch (SQLException e) {
			System.out.println("sql오류 :이미 생성");
			//.printStackTrace();
		}finally {
			CloseUtil.close(null, stmt, conn);
		}
		
		//table create JDBC 로직 종료
		
		//insert into JDBC 로직
			
		try {
			String driverClassName = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "hr";
			String password = "hr";
			
			//JDBC Driver Loading
			Class.forName(driverClassName);
			
			//JDBC Connection getting
			conn = DriverManager.getConnection(url, user, password);
			
			System.out.println("DB 연결 성공");
			System.out.println("** Driver:" + driverClassName + ", Connection:" + conn);
			
			//JSON 읽어와서 쿼리에 담기위한 사전작업
			Reader reader = new FileReader(filePath);
		    
		    JSONParser parser = new JSONParser();
		    
		    Object obj = parser.parse(reader);
		    JSONArray jsonArr = (JSONArray) obj;
		    
			//SQL문 작성
			String SQL = "insert into test(no, name, soldAmount, price) values(?,?,?,?)";
			
			//PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			pstmt = conn.prepareStatement(SQL);
			
			//psmt.set<데이터타입><? 순서, 값)
			//다건 JSON객체 (JSONArray)
			if(jsonArr.size()>0) {
				for(int i=0; i<jsonArr.size(); i++) {
					JSONObject jsonObj = (JSONObject)jsonArr.get(i);
					System.out.println((String)jsonObj.get("name"));
					System.out.println((Long)jsonObj.get("soldAmount"));
					System.out.println((Long)jsonObj.get("price"));
					
					name=(String)jsonObj.get("name");
					soldAmount=(Long)jsonObj.get("soldAmount");
					price=(Long)jsonObj.get("price");
					
					pstmt.setInt(1, i);
					pstmt.setString(2, name);
					pstmt.setLong(3, soldAmount);
					pstmt.setLong(4, price);
					int r = pstmt.executeUpdate();
					
					System.out.println("SQL 실행:"+r+"개 의 row삽입");
				}
			}

			//SQL문 작성
			String SQL2 = "insert into test(no, name, soldAmount, price) values(?,?,?,?)";
			
			//PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			pstmt = conn.prepareStatement(SQL2);
			
			
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			CloseUtil.close(null, stmt, conn);
		}
		
	}

}
