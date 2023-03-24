package aproject.view;

import java.util.List;

import aproject.vo.EmpVO;

public class EmpView {
	
	// 여러 건 (ex. 모든 코인 조회)
	public static void print(List<EmpVO> emplist) { // 이번엔 여러건(List)을 입력값으로 받아서 출력해야 하는 함수니까
		System.out.println("직원들 정보");
		for(EmpVO emp : emplist) {
			System.out.println(emp);
		}
	}
	
	// 1건 (ex. 특정 코인 조회)
	public static void print(EmpVO emp) {
		System.out.println("직원 상세보기");
		if(emp == null) { // 직원번호 입력했는데 없는 경우 null이 뜰 건데, 그때를 대비해서 이렇게 if문으로 대비해준 것뿐임. 
			print("직원이 존재하지 않습니다.");
		} else {
			System.out.println(emp);
		}
	}
	
	public static void print(String message) {
		System.out.println("[알림] " + message);
		
	}

}
