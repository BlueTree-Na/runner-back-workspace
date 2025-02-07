package com.kh.runners.member.model.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.kh.runners.member.model.vo.Member;

@Mapper
public interface MemberMapper {
	
	@Insert("INSERT INTO TB_MEMBER VALUES(SEQ_UNO.NEXTVAL, #{userId}, #{userName}, #{userPwd}, #{nickName}, #{gender}, #{email}, #{address}, #{phone}, #{role},#{status},")
	void save(Member requestMember);
	
	Member findbyId(String userId);

}
