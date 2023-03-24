package toDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.MySqlUtil;
import vo.CoinVO;

public class 여러문장한번에수행 {

	// 여러문장 한번에 수행
	// 구매:insert 재고:update
	public int insertUpdate() {
		String sqlInsert = """
				insert into employees
				values(seq_employee.nextval,?,?,?,?,?,?,?,?,?,?)
				""";
		String sqlUpdate = """
				update employees
				set EMAIL = ?, DEPARTMENT_ID =?,
				JOB_ID=?, SALARY=?,
				hire_date=?, MANAGER_ID=?
				where EMPLOYEE_ID = ?
				""";
		// 기본설정이 autocommit;
		// 모두 성공하면 commit해야한다.
		conn = OracleUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sqlInsert);
			pst.setString(1, "aaa");
			int a = pst.executeUpdate();
			PreparedStatement pst2 = conn.prepareStatement(sqlUpdate);
			pst2.setString(1, "wed0406@daum.net");
			int b = pst2.executeUpdate();
			conn.commit();
			resultCount = a + b;
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return resultCount;
	}
	
	// 2. 모든 코인 종가 조회하기 (read)
		public List<CoinVO> selectAll() {

			// select문 바꿔줘야 함.
			String sql = "select * from coin";
			List<CoinVO> coinList = new ArrayList<>();
			conn = MySqlUtil.getConnectionMySql();

			try {
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					CoinVO coin = makeCoin(rs);
					coinList.add(coin);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				MySqlUtil.dbDisconnectMySql(rs, st, conn);
			}
			return coinList;
		}

		private CoinVO makeCoin(ResultSet rs2) throws SQLException {
			CoinVO coin = new CoinVO();
			coin.setCoin_name(rs.getString("coin_name"));
			coin.setPrev_closing_price(rs.getInt("recent_prev_closing_price"));

			return coin;
		}

}
