package com.kh.runners.schedule.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SchedulePart {
	
	private Long partNo;
	private Long refSno;
	private Long userNo;
	private String partStatus;

}
