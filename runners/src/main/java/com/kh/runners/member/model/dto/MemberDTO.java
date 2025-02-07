package com.kh.runners.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	
	private Long userNo;
	
	private String userName;
	
	@Size(min = 4, max = 15, message = "아이디는 4~15자여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문자와 숫자로만 구성되어야 합니다.")
	@NotBlank(message = "아이디를 입력해주세요.")
	private String userId;
	
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+]).+$",
	    	 message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
	private String userPwd;
	
	@Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 영문, 숫자, 한글만 가능합니다.")
	@NotBlank(message = "닉네임을 입력해주세요.")
	private String nickName;
	
	private String gender;
	
	@Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
  
	private String Email;
	
	private String address;
	
	@Size(min = 11, max = 11, message = "하이픈 없이 입력 해주세요 ") 
	@Pattern(regexp = "^010[0-9]{8}$", message = "하이픈 없이 11자리 입력해주세요. ex. 01012345678")
	@NotBlank(message = "전화번호를 입력해주세요.")
	private String phone;
	private String role;
	private String createAt;
	private String status;

}
