package com.kh.runners.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	
	private Long userNo;
	
	@NotBlank(message = "이름을 입력해주세요.")
	@Size(max = 20, message = "이름은 최대 20자까지 입력 가능합니다.")
	@Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "이름은 영문과 한글만 사용할 수 있습니다.")
	private String userName;
	
	@Size(min = 4, max = 15, message = "아이디는 4~15자여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문자와 숫자로만 구성되어야 합니다.")
	@NotBlank(message = "아이디를 입력해주세요.")
	private String userId;
	
	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 20, message = "비밀번호는 8~20자로 입력해주세요.")
	private String userPwd;

	
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 영문, 숫자, 한글만 가능합니다.")
	private String nickName;

	
	private String gender;
	
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일 형식을 입력해주세요.")
	@Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
	private String email;

	

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Size(min = 13, max = 13, message = "전화번호는 하이픈(-)을 포함한 13자리여야 합니다.")
	@Pattern(regexp = "^01[0-9]-[0-9]{4}-[0-9]{4}$", message = "전화번호는 010-1234-5678 형식으로 입력해주세요.")
	private String phone;

	private String role;
	private String createAt;
	private String status;
	private String fileUrl;
	private String socialId;
	private boolean isSocialUser;

}
