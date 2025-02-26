package com.kh.runners.course.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.runners.course.model.vo.Running;

@Mapper
public interface CourseMapper {

	List<Running> findAll(RowBounds rowBounds);
	
	Running findById(String runningId);
	
}
