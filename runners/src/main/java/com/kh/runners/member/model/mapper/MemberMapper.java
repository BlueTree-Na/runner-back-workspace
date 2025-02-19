package com.kh.runners.member.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.kh.runners.auth.model.vo.SocialUser;
import com.kh.runners.member.model.dto.UpdateMemberDTO;
import com.kh.runners.member.model.vo.Member;

@Mapper
public interface MemberMapper {
	// 회원 Id 조회
	Member findbyUserId(String userId);
	
	// 회원 조회 (userNo 사용)
	Member findByUserNo(Long userNo);
	
	// 소셜 회원 존재 여부 (카운트)
    int countBySocialId(String socialId);

    // 소셜 회원 정보 조회
    SocialUser findBySocialId(String socialId);

    // 새 소셜 회원 등록
    void insertSocialUser(SocialUser socialUser);
    
	// 회원 등록
	void insertUser(Member requestMember);
	
    // 수정
	void updateMember(UpdateMemberDTO updateMemberDTO);
    
	@Update("UPDATE TB_MEMBER SET USER_PWD=#{password} WHERE USER_NO=#{userNo}")
	void changePassword(Map<String, String> changeRequest);
	
	@Delete("DELETE FROM TB_MEMBER WHERE USER_NO=#{userNo}")
	void deleteByPassword(Long userNo);

	Object findByNickname(String randomNickName);






}
