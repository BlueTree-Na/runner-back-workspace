package com.kh.runners.schedule.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.runners.auth.model.service.AuthenticationService;
import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.course.model.service.CourseService;
import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.schedule.model.dto.ScheduleDTO;
import com.kh.runners.schedule.model.mapper.ScheduleMapper;
import com.kh.runners.schedule.model.vo.Schedule;
import com.kh.runners.schedule.model.vo.SchedulePart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
	
	private final ScheduleMapper scheduleMapper;
	private final AuthenticationService authService;
	private final CourseService courseService;
	
	@Override
	public void save(ScheduleDTO scheduleDto) {
		
		// 등록 페이지 권한 => SecurityConfiguration => 토큰으로 인가
		
		// 검증 절차 이후 Data Insert
		scheduleMapper.save(scheduleDto);
	}

	@Override
	public List<Schedule> findAll(int page) {
		
		// 첫 페이지 => 더보기 => 마지막 페이지
		int size = 5; // 일정 리스트 가져오는 개수
		
		// 요청받은 페이지 마다 동적으로 행 자르기
		RowBounds rowBounds = new RowBounds(page * size, size);
		
		return scheduleMapper.findAll(rowBounds);
	}

	@Override
	public Schedule findById(Long scheduleNo) {
		
		// 잘못된 접근 scheduleNo ==> 1보다 작은 수
		if(scheduleNo == null || scheduleNo < 1) {
			throw new RuntimeException("잘못된 접근입니다.");
		}
		
		// 일정 존재 여부 확인
		if(scheduleMapper.increaseCount(scheduleNo) < 1) {
			throw new RuntimeException("존재하지 않는 일정입니다.");
		}
		Schedule findSchedule = scheduleMapper.findById(scheduleNo);
		
//		log.info("일정 상세 : {}", findSchedule);
		
		return findSchedule;
	}

	@Override
	public ScheduleDTO update(Long scheduleNo, ScheduleDTO scheduleDto) {

		CustomUserDetails userDetails = validAuthUser(scheduleDto);
		validSchedule(scheduleNo);
		
		scheduleDto.setScheduleNo(scheduleNo);
		scheduleDto.setScheduleWriter(userDetails.getUserNo().toString());
		
		scheduleMapper.update(scheduleDto);
		
		scheduleDto.setScheduleWriter(userDetails.getUsername());
		
		return scheduleDto;
	}

	@Override
	public void delete(Long scheduleNo, ScheduleDTO scheduleDto) {
		
		CustomUserDetails userDetails = validAuthUser(scheduleDto);
		
		validSchedule(scheduleNo);
		
		scheduleDto.setScheduleWriter(userDetails.getUserNo().toString());
		scheduleDto.setScheduleNo(scheduleNo);
		scheduleMapper.delete(scheduleDto);
		
		// 참여자들에게(참여목록테이블) 알림 추가 로직 ++
	}
	
	// 데이터 요청자와 토큰으로 확인한 사용자가 동일한지
	private CustomUserDetails validAuthUser(ScheduleDTO scheduleDto) {
		
		CustomUserDetails userDetails = authService.getAuthenticatedUser();
		
		String writer = scheduleDto.getScheduleWriter();
		authService.validWriter(writer, userDetails.getUsername());
		
		return userDetails;
	}
	
	// 일정 존재 여부
	private void validSchedule(Long scheduleNo) {
		
		Schedule findSchedule = scheduleMapper.findById(scheduleNo);
		
		if(findSchedule == null) {
			throw new RuntimeException("삭제되었거나 존재하지 않는 일정 입니다.");
		}
	}

	@Override
	public void insertGroup(ScheduleDTO scheduleDto, MemberDTO memberDto) {
		
		
		
	}

	@Override
	public List<SchedulePart> selectByScheduleMember() {
		
		
		
		return null;
	}
	
}
