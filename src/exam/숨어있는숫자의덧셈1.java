package exam;

public class 숨어있는숫자의덧셈1 {
	
	class Solution {
	    public int solution(String my_string) {
	        int answer = 0;
	        char ch;
		    int sum = 0;
	        String str = "";
	        
		    for(int i=0; i<my_string.length(); i++){
	            ch = my_string.charAt(i);
	            if(Character.isDigit(ch)){
	            	str = Character.toString(ch);
	                sum += Integer.parseInt(str);
	            }
	        }
	        answer = sum;
	        return answer;
	    }
	}

}
