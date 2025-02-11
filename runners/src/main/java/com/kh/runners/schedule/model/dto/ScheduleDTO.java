package com.kh.runners.schedule.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
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
public class ScheduleDTO {
	
	private Long scheduleNo;
	
	private String scheduleWriter;
	
	private String scheduleTitle;
	
	private String scheduleContent;
	
	private LocalDateTime enrollDate;
	
	private LocalDateTime selectDate;
	
	private Long count;
	
	private int maxMember;
	
}
