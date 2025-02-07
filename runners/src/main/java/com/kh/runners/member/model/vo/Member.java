package com.kh.runners.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	private Long userNo;
	private String userName;
	private String userId;
	private String userPwd;
	private String nickName;
	private String gender;
	private String email;
	private String address;
	private String phone;
	private String role;
	private String createAt;
	private String status;
	
}
