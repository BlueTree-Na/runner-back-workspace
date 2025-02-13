package com.kh.runners.schedule.model.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kh.runners.auth.model.service.AuthenticationService;
import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.schedule.model.dto.ScheduleDTO;
import com.kh.runners.schedule.model.mapper.ScheduleMapper;
import com.kh.runners.schedule.model.vo.Schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
	
	private final ScheduleMapper scheduleMapper;
	// private final AuthenticationService authService;
	
	@Override
	public void save(ScheduleDTO scheduleDto) {
		
		// 등록 페이지 권한 => SecurityConfiguration => 토큰으로 인가
		
		// CustomUserDetails userDetails = authService.getAuthenticatedUser();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		
		Long scheduleWriter = Long.parseLong(scheduleDto.getScheduleWriter());
		
		// 데이터 요청자와 토큰으로 확인한 사용자가 동일한지
		if(scheduleWriter != null && !scheduleWriter.equals(user.getUserNo())) {
			throw new RuntimeException("요청한 사용자와 작성자가 일치하지 않습니다.");
		}
		
		// 검증 절차 이후 Data Insert
		scheduleMapper.save(scheduleDto);
	}

	@Override
	public List<Schedule> findAll() {
		
		// 첫 페이지 => 더보기 => 마지막 페이지
		
		// 
		
		
		
		return null;
	}

	@Override
	public Schedule findById(Long scheduleNo) {
		return null;
	}

	@Override
	public void updateSchedule(ScheduleDTO scheduleDto) {
		
	}

	@Override
	public void deleteSchedule(ScheduleDTO scheduleDto) {
		
	}
	
	
	
}
