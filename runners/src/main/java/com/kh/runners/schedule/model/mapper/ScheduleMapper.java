package com.kh.runners.schedule.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kh.runners.schedule.model.dto.ScheduleDTO;

@Mapper
public interface ScheduleMapper {

	void save(ScheduleDTO scheduleDto);
	
	
	
	
	
}
