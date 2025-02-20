package com.kh.runners.schedule.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.runners.schedule.model.dto.ScheduleDTO;
import com.kh.runners.schedule.model.vo.Schedule;

@Mapper
public interface ScheduleMapper {

	void save(ScheduleDTO scheduleDto);
	
	List<Schedule> findAll(RowBounds rowBounds);
	
	Schedule findById(Long scheduleNo);

	int increaseCount(Long scheduleNo);
	
	void update(ScheduleDTO scheduleDto);

	void delete(ScheduleDTO scheduleDto);
	
}
