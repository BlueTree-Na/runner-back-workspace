package com.kh.runners.course.model.service;

import java.util.List;

import com.kh.runners.course.model.vo.Running;

public interface CourseService {
	
	List<Running> findAll(int page);
	
}
