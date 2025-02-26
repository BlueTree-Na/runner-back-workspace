package com.kh.runners.course.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.runners.course.model.service.CourseService;
import com.kh.runners.course.model.vo.Running;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("course")
@RequiredArgsConstructor
public class CourseController {

	private final CourseService courseService;
	
	@GetMapping
	public ResponseEntity<List<Running>> findAll(@RequestParam(name="page") int page){
		
		return ResponseEntity.ok().body(courseService.findAll(page));
	}
	
	
	@GetMapping("/{runningId}")
	public ResponseEntity<Running> findById(@PathVariable(name="runningId") String runningId){
		
		return ResponseEntity.ok().body(courseService.findById(runningId));
	}
	
}
