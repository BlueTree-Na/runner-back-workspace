package com.kh.runners.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	@Size(min = 4, max = 20, message = "비밀번호는 8~20자여야 합니다.")
	private String newPassword;
}
