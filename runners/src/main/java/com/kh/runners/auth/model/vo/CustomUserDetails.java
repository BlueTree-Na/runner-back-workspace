package com.kh.runners.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class CustomUserDetails {

	private Long userNo;
	private String userName;
	private String userId;
	private String userPwd;
	private String Email;
	private String phone;
	
}
