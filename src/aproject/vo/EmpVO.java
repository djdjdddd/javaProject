package aproject.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// sql 프롬프트에서 "alt누른 상태로 하면 수직으로 드래그 가능" (+ 우클릭 누르면 복사됨)
// SQL에서 데이터 타입 보고 그에 맞는 데이터 타입도 넣어주기 (int, String 등)

// VO(또는 DTO) : 이동하는 데이터 묶음?? (왜 아래처럼 클래스를 만들었을지 생각해보자)

// JavaBeans 기술을 쓰는 놈들의 규칙 : 1)변수 접근지정자는 private 2)setter, getter 3)기본생성자
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class EmpVO {
	
	private int employee_id;
	private String first_name;
	private String last_name;
	private String email;
	private String phone_number;
	private Date hire_date;
	private String job_id;
	private double salary;
	private double commission_pct;
	private int manager_id;
	private int department_id;

}
