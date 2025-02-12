package com.kh.runners.schedule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.runners.schedule.model.dto.ScheduleDTO;
import com.kh.runners.schedule.model.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(name="schedule", produces="application/json; charset=UTF-8")
@RequiredArgsConstructor
@Validated
public class ScheduleController {
	
	private final ScheduleService scheduleService;

	@PostMapping
	public ResponseEntity<?> save(@ModelAttribute @Validated ScheduleDTO scheduleDto){
		
		scheduleService.save(scheduleDto);
		
//		return ResponseEntity.status(HttpStatus.CREATED).body("일정 등록 성공!");
		return null;
	}
	
	
}
