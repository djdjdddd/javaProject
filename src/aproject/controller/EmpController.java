package aproject.controller;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import aproject.model.EmpService;
import aproject.view.EmpView;
import aproject.vo.EmpVO;

// Controller는 내가 하기 나름이다? (client의 요청을 받는 곳이라 그런가?)

public class EmpController {
	
	public static void main(String[] args) {
		EmpService eService = new EmpService();
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("1.모든 직원 조회하기");
			System.out.println("2.직원 상세 조회하기");
			System.out.println("3.특정 부서 직원 조회하기");
			System.out.println("4.부서, 직책, 급여로 조회하기");
			System.out.println("5.부서 평균급여 이하를 받는 직원 조회하기");
			System.out.println("6.신규직원등록");
			System.out.println("7.직원정보수정 ");
			System.out.println("8.직원정보삭제");
			System.out.println("9.직원 급여조회(SP)");
			System.out.println("나가기");
			System.out.print("===작업선택>>");
			
			String job = sc.next(); // 유저에게 키보드로 입력받은 값을 job 변수에 대입
		
			if(job.equals("나가기")) break;
			
			switch(job) {
			case "1" :
				EmpView.print(eService.selectAll()); break;
			case "2" :{ // 특정 직원 조회
				System.out.print("조회할 직원번호>>"); // 혹시 이렇게 했는데 직원정보가 안뜨고 주소가 뜬다면 VO에서 toString을!! (왜냐면 VO 클래스 필드에 직원 정보를 저장해놨으니까)
				int empid = sc.nextInt();
				EmpView.print(eService.selectById(empid)); break;
			}
			case "3" :{
				System.out.print("조회할 부서번호>>");
				int deptid = sc.nextInt();
				EmpView.print(eService.selectByDept(deptid)); break;
			}
			case "4" :{ // 요 {} 블락 처리를 한 이유는 안하면 deptid 변수가 중복돼서 그럼. (이렇게 처리하면 지역변수가 되니까 또 써도 아무 상관 없자나)
				System.out.print("조회할 부서번호, job, salary>>");
				int deptid = sc.nextInt();
				String jobid = sc.next();
				double salary = sc.nextDouble();
				EmpView.print(eService.selectByCondition(deptid, jobid, salary)); break; 
			}
			case "5" :{
				List<EmpVO> emplist = eService.selectLAB();
				System.out.println("[Controller] 5번작업: " + emplist.size());
				EmpView.print(emplist);
				break;
			}
			case "6" :{ // 6. 신규 직원 등록
				EmpVO emp = makeEmp(sc); // 1~10번을 입력해야 하는 긴 코드를 여기에 넣으면 너무 복잡하니까 아예 함수(문장들의 묶음) 만들어서 따로 빼줬다~
				EmpView.print(eService.empInsert(emp));
				break; // case문 쓸 땐 case마다 break 필요할 수도 (안 그럼 계속 내려가자너)
			}
			case "7" : { // 7. 직원 정보 수정
				System.out.print("===수정할 직원ID>>");
				int empid = sc.nextInt();
				EmpView.print(eService.selectById(empid));
				
				EmpVO emp = makeEmp2(sc, empid); // (sc) 한 이유는 여기서 만든 Scanner 객체를 저 함수에서도 쓸려고 (그럼 굳이 저 함수에서 스캐너 생성 안해도 바로 쓸 수 있더라고. 해보니까 이제 이해가 되네)
				EmpView.print(eService.empUpdate(emp));
				break;
			}
			case "8" : {
				System.out.print("===삭제할 직원ID>>");
				int empid = sc.nextInt();
				EmpView.print(eService.selectById(empid)); // 내가 삭제할 놈이 진짜 있는 조회(selectById)해서 보여주기(EmpView.print) 위해 쓴 코드
				System.out.print("삭제를 계속진행(Y/N)?");
				String yesOrno = sc.next();
				if(yesOrno.equals("Y")) {
					EmpView.print(eService.empDelete(empid));
				}
			}
			case "9" :{
				System.out.print("조회할 직원번호>>"); // 혹시 이렇게 했는데 직원정보가 안뜨고 주소가 뜬다면 VO에서 toString을!! (왜냐면 VO 클래스 필드에 직원 정보를 저장해놨으니까)
				int empid = sc.nextInt();
				EmpView.print("급여는 " + eService.getSalary(empid)); break;
			}
			default :
				break;
			}
		}
		System.out.println("수고하셨습니다.");
	}

	
	private static EmpVO makeEmp2(Scanner sc, int empid) {
		
		System.out.print("1.직원의 email>>");
		String email = sc.next();
		System.out.print("2.직원의 jobid>>");
		String jobid = sc.next();
		System.out.print("3.직원의 dept>>");
		int deptid = sc.nextInt();
		System.out.print("4.직원의 salary>>");
		double sal = sc.nextDouble();
		System.out.print("5.직원의 hire_date \"yyyy/MM/dd\">>");
		String sdate = sc.next();
		Date hireData = DateUtil.convertToDate(sdate); 
		System.out.print("6.직원의 manager_id>>");
		int mid = sc.nextInt();
		
		
		EmpVO emp = new EmpVO(); // VO객체(EmpVO) 생성하고
		emp.setEmployee_id(empid);
		emp.setEmail(email);
		emp.setJob_id(jobid);
		emp.setDepartment_id(deptid);
		emp.setSalary(sal);
		emp.setHire_date(hireData);
		emp.setManager_id(mid);
		
		return emp;
		
	}


	// 너무 길어서 따로 빼준 함수(문장들의 묶음)
	private static EmpVO makeEmp(Scanner sc) {

		System.out.print("1.신규직원의 first_name>>");
		String firstName = sc.next();
		System.out.print("2.신규직원의 last_name>>");
		String lastName = sc.next();
		System.out.print("3.신규직원의 email>>");
		String email = sc.next();
		System.out.print("4.신규직원의 jobid>>");
		String jobid = sc.next();
		System.out.print("5.신규직원의 phone>>");
		String phone = sc.next();
		System.out.print("6.신규직원의 manager>>");
		int mid = sc.nextInt();
		System.out.print("7.신규직원의 dept>>");
		int deptid = sc.nextInt();
		System.out.print("8.신규직원의 commission>>");
		double comm = sc.nextDouble();
		System.out.print("9.신규직원의 salary>>");
		double sal = sc.nextDouble();
		
		System.out.print("10.신규직원의 입사일 yyyy/mm/dd>>");
		String sdate = sc.next();
		// String -> Date 로 바꾸는 코드가 필요하다. 
		Date hireData = DateUtil.convertToDate(sdate); // *만약 이런 기능을 자주 쓸 거 같으면 클래스로 만들어주면 좋다. (OracleUtil 클래스 만들었던 것처럼)
		
		
		EmpVO emp = new EmpVO(); // VO객체(EmpVO) 생성하고
		emp.setFirst_name(firstName);
		emp.setLast_name(lastName); // setter 이용해서 입력한 값으로 설정해버리기
		emp.setEmail(email);
		emp.setPhone_number(phone);
		emp.setManager_id(mid);
		emp.setDepartment_id(deptid);
		emp.setCommission_pct(comm);
		emp.setSalary(sal);
		emp.setHire_date(hireData);
		emp.setJob_id(jobid);
		
		return emp;
	}
}
