package aproject.model;

import java.util.List;

import aproject.vo.EmpVO;

// Service의 역할 : 업무로직 담당 (DAO도 Service도 둘다 업무 담당함. 다만, DAO는 DB특화이고, Service는 Controller와 DAO의 중간다리 역할의 업무를)
public class EmpService {

	EmpDAO empDao = new EmpDAO();
	
	// SP호출
	public EmpVO getSalary(int empid) {
		return empDao.getSalary(empid);
	}
	

	public List<EmpVO> selectAll() {
		return empDao.selectAll();
//	           서비스에서 DAO를 부르고, 그렇게 얻은 값을
//		리턴
	}

	public EmpVO selectById(int empid) {
		return empDao.selectById(empid);
	}

	public List<EmpVO> selectByDept(int deptid) {
		return empDao.selectByDept(deptid);
	}

	public List<EmpVO> selectByCondition(int deptid, String jobid, double salary) {
		return empDao.selectByCondition(deptid, jobid, salary);
	}

	// (3.15) 자신이 속한 부서의 평균 급여보다 더 적은 급여를 받는 직원들을 조회
	public List<EmpVO> selectLAB() {
		List<EmpVO> emplist = empDao.selectLAB(); // 만약 추가적인 업무가 있다면 바로 리턴하는 게 아니라 지금처럼 변수에 받아놓고

		System.out.println("[EmpService] 실행건수 " + emplist.size()); // 그 변수를 통해 추가적인 업무 로직 짜는 거죵.

		return emplist;
	}

	// 신규 직원을 등록(insert)하는 함수를 만들어보자
	public String empInsert(EmpVO emp) {
			int result = empDao.empInsert(emp);
			return result > 0? "입력성공" : "입력실패"; 
		}

	// 신규 정보를 수정(update)하는 함수를 만들어보자
	public String empUpdate(EmpVO emp) {
			int result = empDao.empUpdate(emp);
			return result > 0? "수정성공" : "수정실패"; 
	}
	
	// 삭제(delete)하는 함수 (한 건의 직원 데이터 삭제)
	public String empDelete(int empid) {
			int result = empDao.empDelete(empid);
			return result > 0? "삭제성공" : "삭제실패"; 
		}
		
	
	}
