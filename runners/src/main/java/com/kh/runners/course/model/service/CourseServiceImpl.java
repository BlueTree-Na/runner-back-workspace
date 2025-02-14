package com.kh.runners.course.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.runners.course.model.mapper.CourseMapper;
import com.kh.runners.course.model.vo.Running;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

	private final CourseMapper courseMapper;
	
	
	@Override
	public List<Running> findAll(int page) {
		
		int size = 10;

		RowBounds rowBounds = new RowBounds(page * size, size);
		
		return courseMapper.findAll(rowBounds);
	}

}
