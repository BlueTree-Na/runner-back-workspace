package com.kh.runners.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemberDTO {
	
	private Long userNo;
	private String currentPassword;
	private String gender;
	private String nickName;
	private String phone;
	private String fileUrl;
	private String status;

}
