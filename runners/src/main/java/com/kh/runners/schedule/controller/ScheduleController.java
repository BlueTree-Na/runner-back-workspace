package com.kh.runners.schedule.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.runners.schedule.model.dto.ScheduleDTO;
import com.kh.runners.schedule.model.service.ScheduleService;
import com.kh.runners.schedule.model.vo.Schedule;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="schedule", produces="application/json; charset=UTF-8")
@RequiredArgsConstructor
public class ScheduleController {
	
	private final ScheduleService scheduleService;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid ScheduleDTO scheduleDto){
		
		log.info("{}", scheduleDto);
		
		scheduleService.save(scheduleDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("일정 등록 성공!");
	}
	
	@GetMapping
	public ResponseEntity<List<Schedule>> findAll(){
		
		return ResponseEntity.ok(scheduleService.findAll());
	}
	
}
