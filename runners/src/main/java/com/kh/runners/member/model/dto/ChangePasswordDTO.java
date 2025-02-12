package com.kh.runners.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordDTO {
	
	@NotBlank(message = "현재 비밀번호를 입력해주세요.")
	private String currentPassword;
	@NotBlank(message = "새 비밀번호를 입력해주세요.")
	private String newPassword;
}
