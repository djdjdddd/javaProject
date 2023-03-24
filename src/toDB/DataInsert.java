package toDB;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// 참조 블로그
// https://tangle1996.tistory.com/30
public class DataInsert {

	public static void main(String[] args) {
		java.sql.Statement stmt = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String filePath = "JSON 파일의 위치"; // API가 아니라 파일 형태로 하는 거였네.. 일단 이렇게라도 구성하자 DB를 (API랑 연결시키고 싶은데, 좀 더 연구가 필요)

		// ★JSON 으로 받은 데이터에 대한 변수 선언 (기준일자? 종목명, 종목코드, 종가/시가/고가/저가, 거래량, 거래대금 등)
		String name="";
		long soldAmount = 0;
		long price =0;
		
		try{
			// OracleUtil 클래스의 connect 하는 걸로 대체 가능
			String driverClassName = "oracle.jdbc.driver.OracleDriver";
			String url = "오라클 URL입력 !! ";
			String user = "아이디!!";
			String password = "비번!!";
			
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
					// 왜 이렇게 query를 구성한거지?? 그냥 """ 쓰면 안되나?
			
			stmt = conn.createStatement();
			
			boolean b = stmt.execute(query);
			
			System.out.println("b : " + b);

		}catch(ClassNotFoundException ex){
			System.out.println("드라이버 로딩 실패");
			ex.printStackTrace();
		} catch (SQLException e) {
			System.out.println("sql오류 :이미 생성");
			//.printStackTrace();
		} finally {
			// 이 자리에 OracleUtil 클래스 써서 disconnect 하는 걸로 대체
			CloseUtil.close(null, stmt, conn);
		}
		
		//table create JDBC 로직 종료
		
		
		
		
		
		//insert into JDBC 로직
			
		try {
			String driverClassName = "oracle.jdbc.driver.OracleDriver";
			String url = "오라클 주소 입력 !!";
			String user = "아이디!!";
			String password = "비번!!";
			
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
		    
			//SQL문 작성                     여기 변수에 맞게 바꿔줘야 함                ? 개수도
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
					
					// ★
					// VO(DTO) 클래스의 setter로 데이터를 세팅하는 그 코드인가??
					// 아니면 단순히 preparedStatement의 ? 를 설정해주는 코드인가??
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
