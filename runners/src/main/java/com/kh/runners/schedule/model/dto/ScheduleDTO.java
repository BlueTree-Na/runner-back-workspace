package com.kh.runners.schedule.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ScheduleDTO {
	
	private Long scheduleNo;
	
	@NotBlank(message="로그인하지 않았거나 존재하지 않는 사용자 입니다.")
	private String scheduleWriter;
	
	@NotBlank(message="제목/이름은 비어있을 수 없습니다.")
	private String scheduleTitle;
	
	@NotBlank(message="내용은 비어있을 수 없습니다.")
	private String scheduleContent;
	
	private LocalDateTime enrollDate;
	
	@NotBlank(message="날짜를 입력해주세요")
	private LocalDateTime selectDate;
	
	private Long count;
	
	private int maxMember;
	
}
