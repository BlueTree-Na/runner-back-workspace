package com.kh.runners.course.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Running {
	
	private String runningId; 		  // 고유ID
	private String courseFlagName; 	  // 산책경로구분명
	private String courseName; 		  // 산책경로명
	private String courseContent;	  // 경로설명
	private String sigunguName; 	  // 시군구명
	private String courseLevelName;	  // 경로레벨명
	private String courseLengthName;  // 경로길이내용
	private String courseLenDetail;	  // 경로상세길이내용
	private String aditContent;		  // 추가설명
	private String courseTime;	  	  // 경로시간내용
	private String optionContent;	  // 옵션설명
	private String toiletContent;	  // 화장실설명
	private String cvntlName;		  // 편의시설명
	private String loadAddr;		  // 지번주소
	private String courseLat;		  // 경로지점위도
	private String courseLon; 		  // 경로지점경도

}
