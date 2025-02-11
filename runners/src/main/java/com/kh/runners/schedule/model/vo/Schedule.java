package com.kh.runners.schedule.model.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

	private Long scheduleNo;
	
	private String scheduleWriter;
	
	private String scheduleTitle;
	
	private String scheduleContent;
	
	private LocalDateTime enrollDate;
	
	private LocalDateTime selectDate;
	
	private Long count;
	
	private int maxMember;
	
}
