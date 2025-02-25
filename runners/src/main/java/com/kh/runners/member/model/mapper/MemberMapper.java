package com.kh.runners.member.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    int countBySocialId(@Param("socialId") String socialId);
    
    // 닉네임 중복 검증 (카운트)
    @Select("SELECT COUNT(*) FROM TB_MEMBER WHERE NICKNAME = #{nickName}")
    int countByNickname(@Param("nickName") String nickName);

    // 소셜 회원 정보 조회
    SocialUser findBySocialId(String socialId);
    
    // 회원 조회 (email 사용)
    @Select("SELECT COUNT(*) FROM TB_MEMBER WHERE EMAIL = #{email}")
    int countByEmail(String email);
    
    // 새 회원 등록
    void insertSocialUser(SocialUser socialUser);
    
    // 새 소셜 회원 등록
    void insertFristSocialUser(Member member);
    
	// 회원 등록
	void insertUser(Member requestMember);
	
    // 수정
	void updateMember(UpdateMemberDTO updateMemberDTO);
    
	@Update("UPDATE TB_MEMBER SET USER_PWD=#{password} WHERE USER_NO=#{userNo}")
	void changePassword(Map<String, String> changeRequest);
	
	@Update("UPDATE TB_MEMBER SET STATUS='3' WHERE USER_NO=#{userNo}")
	void deleteByPassword(Long userNo);
	
	// 소셜랜덤닉네임 체크용
	Integer findByNickname(String randomNickName);
	
	void updateProfileImage(Map<String, Object> params);










}
