package com.kh.runners.schedule.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.runners.schedule.model.dto.ScheduleDTO;
import com.kh.runners.schedule.model.vo.Schedule;

@Mapper
public interface ScheduleMapper {

	void save(ScheduleDTO scheduleDto);
	
	List<Schedule> findAll();
	
	
	
}
