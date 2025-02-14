package com.kh.runners.schedule.model.service;

import java.util.List;

import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.schedule.model.dto.ScheduleDTO;
import com.kh.runners.schedule.model.vo.Schedule;
import com.kh.runners.schedule.model.vo.SchedulePart;

public interface ScheduleService {

	// 일정 등록
	void save(ScheduleDTO scheduleDto);
	// 일정 전체 조회	
	List<Schedule> findAll(int page);
	// 일정 상세 조회
	Schedule findById(Long scheduleNo);
	// 일정 수정
	ScheduleDTO update(Long scheduleNo, ScheduleDTO scheduleDto);
	// 일정 삭제
	void delete(Long scheduleNo, ScheduleDTO scheduleDto);
	
	/* 일정 그룹 관련 */
	
	// 그룹 참여 => 바로참여, 수락 후 참여
	void insertGroup(ScheduleDTO scheduleDto, MemberDTO memberDto); // Token으로 getPricipal()

	// 참여자 명단 => 그룹장 권한
	List<SchedulePart> selectByScheduleMember();

	// 명단 수정(신청 수락/거절) => 그룹장 권한
	
	// 그룹원 수정(추방) => 그룹장 권한 delete
	
	
}
