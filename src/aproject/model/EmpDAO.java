package aproject.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.dbutil.OracleUtil;

import aproject.vo.EmpVO;

// DAO(Data Access Object) 이름을 붙인 이유 : DB업무를 여기서 하겠다는 뜻 (CRUD, insert, select, update, delete)
public class EmpDAO {

	Connection conn; // 저번에 null로 초기화한 이유는 그땐 지역변수라서 (지금처럼 필드에 선언한 애들은 굳이 초기화 하지 않아도 자동초기화 됨)(자바 다
						// 까먹었누 ㅋㅋ)
	Statement st;
	PreparedStatement pst; // ?(바인딩 변수) 지원
	ResultSet rs; // select로 가져온 데이터들 (따라서 insert와 같은 DML?과는 상관이 없음)
	int resultCount; // 대신 insert, update, delete 건수를 요 변수에 저장
	
	CallableStatement cst; // SP지원
	
	
	// SP(Stored Procedure, 저장 프로시저) 호출
	// 3.17) Procedure 中 "execute sp_salary(101, :sal);" 를 자바에서 하고 싶은 것!!
	public EmpVO getSalary(int empid) { // in 으로 받을 놈이니까 매개변수에 empid 써주고
		String sql = "{call sp_salary(?,?,?)}"; // 매개변수 3개가 그때그때 달라지니까 (?, ?, ?) 
		conn = OracleUtil.getConnection();
		
//		double salary = 0; // 이렇게 일일이 변수로 하지 않고, 아싸리 VO, DTO 형태로 주고 받아버리자고 (변수가 많아지고 있으니까)
		EmpVO emp = new EmpVO();
		
		try {
			cst = conn.prepareCall(sql);
			cst.setInt(1, empid);
			cst.registerOutParameter(2, Types.DOUBLE); // 두번째 ?인 salary 타입이 double이니까 이런 식으로 썼다고 함. (첨 봄)
			cst.registerOutParameter(3, Types.VARCHAR);
//			if(cst.execute()) {
			cst.execute(); // execute : resultSet이 있으면 true이고, 없으면 false 라는 함수 
						   // execute는 executeQuery랑 executeUpdate랑 뭐가 다르다고??
			emp.setSalary(cst.getDouble(2)); 
			emp.setFirst_name(cst.getString(3)); 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return emp;
		
	}
	

	// '모든 정보를 조회하는 함수'를 만들어보자 (매개, 입력값으로 받을 건 없고, 대신 리턴은 해야 하니까 저렇게 구성)
	public List<EmpVO> selectAll() {
		String sql = "select * from employees order by 1";
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection(); // 3.14에 한 getConnection 함수 (커넥과 디스커넥은 업무(지금 클래스에서 하려는 작업)와는 상관없으니까 이를 분리하기
											// 위해 만든 놈)

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpVO emp = makeEmp(rs); // rs로 받아 실행할 내용들을 while문 안에 다 쓰면 너무 길어지니까, 이처럼 아예 함수를 만들어서 더 간소화 시키려고 한 작업이라고
											// 함.
				emplist.add(emp); // 즉, 건이 여러개니까 makeEmp라는 덩어리로 묶은 것.
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}

		return emplist;
	}

	
	// 이번엔 특정직원 조회(상세보기)하는 함수를 만들어보자. (직원id를 입력하면(=id로 조회하면) 정보를 리턴하는, 근데 1건만 리턴하니까
	// List가 아니라는 거지)
	public EmpVO selectById(int empid) { // **이번엔 1건만 리턴하니까 List가 아니겠죵
		EmpVO emp = null;
		String sql = "select * from employees where employee_id = " + empid;
		conn = OracleUtil.getConnection();

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				emp = makeEmp(rs);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}

		return emp;
	}

	
	// 특정부서의 직원 조회 (부서id로 직원을 조회하는)
	public List<EmpVO> selectByDept(int deptid) {
		List<EmpVO> emplist = new ArrayList<>();
		String sql = "select * from employees where department_id = " + deptid;
		conn = OracleUtil.getConnection();

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpVO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}

		return emplist;
	}

	
	// 특정 부서, 특정 jobid, 특정 salary로 직원조회
	public List<EmpVO> selectByCondition(int deptid, String jobid, double salary) {
		List<EmpVO> emplist = new ArrayList<>();
		String sql = "select * " + " from employees " + " where department_id = ? " // + 는 지금처럼 여러건을 처리할 때 좋지 않으므로 대신 ?
																					// 를 사용했다. (따라서 Statement 대신
																					// PreparedStatement를 사용했음)
				+ " and job_id = ? " + " and salary >= ? "; // 이렇게 줄 띄울때는 select * from 요 사이 간격을 띄워주도록 (" from 이렇게)
		conn = OracleUtil.getConnection();

		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, deptid); // 1번째 ?는 deptid야
			pst.setString(2, jobid); // 2번째 ?는 jobid야
			pst.setDouble(3, salary);
			rs = pst.executeQuery();
			while (rs.next()) {
				EmpVO emp = makeEmp(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, pst, conn); // **꼭 이렇게 자원반납 해주는 코드도 써줘야 한다.
		}

		return emplist;
	}

	// 신규 직원을 등록(insert)하는 함수를 만들어보자
	public int empInsert(EmpVO emp) { // EmpVO => VO : 데이터를 담는 가방 (직원의 정보를 int a, int b, String c 이런 식으로 받으면 어지러우니까
		String sql = """
				insert into employees
				values(seq_employee.nextval,?,?,?,?,?,?,?,?,?,?)
				"""; // 단, 이렇게 칼럼을 지정해주지 않으면 => 내가 따로 순서를 지정해줘야 한다! (아래 pst에서 1번째 물음표엔 ~~를, 2번째엔 ~~를
						// 이런 식으로)
						// 시퀀스 왜 빼먹을라 했누?

		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, emp.getFirst_name()); // **순서에 맞게 줘야겠죵? 위의 SQL문장에서 따로 순서(칼럼)를 정해주지 않았으니
			pst.setString(2, emp.getLast_name());
			pst.setString(3, emp.getEmail());
			pst.setString(4, emp.getPhone_number());
			pst.setDate(5, emp.getHire_date());
			pst.setString(6, emp.getJob_id());
			pst.setDouble(7, emp.getSalary());
			pst.setDouble(8, emp.getCommission_pct());
			pst.setInt(9, emp.getManager_id());
			pst.setInt(10, emp.getDepartment_id()); // getter 씀 : 1번째 ?에 last_name을 넣어줘야 하니까

			// **DML문장 실행한다. 영향받은 건수를 return
			resultCount = pst.executeUpdate(); // **executeQuery 가 아니라 executeUpdate (insert와 같은 DML은 이걸 써야 하나 봄?)
												// executeUpdate는 리턴 타입이 int (result를 count하려고 이거 쓰나 봐)
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // 절대 지우지마 (에러가 어디서 났는지를 알아야 고칠 수 있으니까. 개발자인 우리는)
		} finally {
			OracleUtil.dbDisconnect(null, pst, conn);
		}

		return resultCount; // insert, delete, update와 같은 DML?과 관련이 있는
	}

	
	// 신규 정보를 수정(update)하는 함수를 만들어보자
	public int empUpdate(EmpVO emp) { // EmpVO => VO : 데이터를 담는 가방 (직원의 정보를 int a, int b, String c 이런 식으로 받으면 어지러우니까
		String sql = """
				update employees 
				set email = ?, department_id = ?,
					job_id = ?,	salary = ?,
					hire_date = ?, manager_id = ?
				where employee_id = ?
				"""; 

		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, emp.getEmail());
			pst.setInt(2, emp.getDepartment_id());
			pst.setString(3, emp.getJob_id());
			pst.setDouble(4, emp.getSalary());
			pst.setDate(5, emp.getHire_date());
			pst.setInt(6, emp.getManager_id());
			pst.setInt(7, emp.getEmployee_id());

			resultCount = pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			resultCount = -1;
			e.printStackTrace(); // 절대 지우지마 (에러가 어디서 났는지를 알아야 고칠 수 있으니까. 개발자인 우리는)
		} finally {
			OracleUtil.dbDisconnect(null, pst, conn);
		}

		System.out.println("update결과: " + resultCount);
		return resultCount; // insert, delete, update와 같은 DML?과 관련이 있는
	}

	
	// 삭제(delete)하는 함수 (한 건의 직원 데이터 삭제)
	public int empDelete(int empid) { // 한 건의 직원id를 입력하면 그걸 삭제하는
		String sql = """
				delete from employees
				where employee_id = ?
				"""; 

		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, empid);

			resultCount = pst.executeUpdate();

		} catch (SQLException e) {
			resultCount = -1; // 임시방편(API 읽어보니까 ??)
			e.printStackTrace(); 
		} finally {
			OracleUtil.dbDisconnect(null, pst, conn);
		}

		System.out.println("delete결과: " + resultCount);
		return resultCount;
	}

	
	//
	private EmpVO makeEmp(ResultSet rs2) throws SQLException {

		EmpVO emp = new EmpVO();
		emp.setCommission_pct(rs.getDouble("Commission_pct")); // setOO은 그냥 순서대로 지정했음 (따로 하려면 귀찮으니까)
		emp.setDepartment_id(rs.getInt("Department_id")); // ( ) 안은 타입에 맞는 놈을 읽게끔 설정했음. (getDouble, getInt, getString,
															// getDate 등)
		emp.setEmail(rs.getString("Email")); // ("칼럼이름") 이렇게 넣어주면 됨. (순서 넣어도 되긴 하는데 이게 더 낫지)
		emp.setEmployee_id(rs.getInt("Employee_id"));
		emp.setFirst_name(rs.getString("First_name"));
		emp.setHire_date(rs.getDate("Hire_date"));
		emp.setJob_id(rs.getString("Job_id"));
		emp.setLast_name(rs.getString("Last_name"));
		emp.setManager_id(rs.getInt("Manager_id"));
		emp.setPhone_number(rs.getString("Phone_number"));
		emp.setSalary(rs.getInt("Salary"));

		return emp; // 위에 getter로 읽고, setter로 설정해준 다음 그 emp 내용을 리턴
	}

	private EmpVO makeEmp2(ResultSet rs2) throws SQLException {

		EmpVO emp = new EmpVO();
		emp.setDepartment_id(rs.getInt("Department_id"));
		emp.setEmployee_id(rs.getInt("Employee_id"));
		emp.setFirst_name(rs.getString("First_name"));
		emp.setSalary(rs.getInt("Salary"));

		return emp; // 위에 getter로 읽고, setter로 설정해준 다음 그 emp 내용을 리턴
	}

	// (3.15)
	public List<EmpVO> selectLAB() {
		String sql = """
				select employee_id, first_name, salary, department_id
				from employees join (select department_id, avg(salary) 평균급여
				                	 from employees
				                	 group by department_id) 뷰 using(department_id)
				where employees.salary < 뷰.평균급여
				""";
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpVO emp = makeEmp2(rs);
				emplist.add(emp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}

		return emplist;
	}

}
