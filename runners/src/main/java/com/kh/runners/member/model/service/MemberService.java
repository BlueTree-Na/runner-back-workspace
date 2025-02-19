package com.kh.runners.member.model.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kh.runners.auth.model.vo.SocialUser;
import com.kh.runners.member.model.dto.ChangePasswordDTO;
import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.member.model.dto.UpdateMemberDTO;
import com.kh.runners.member.model.vo.Member;

public interface MemberService {

	// ========== 일반 회원 로직 ==========
	void insertUser(MemberDTO requestMember);

	void changePassword(ChangePasswordDTO changeEntity);

	void deleteByPassword(Map<String, String> password);
	

	UpdateMemberDTO updateMember(UpdateMemberDTO updateMemberDTO, MultipartFile file);

	 // ========== 공통/기존 메서드 ==========
	Member findByUserNo(Long userNo);

	boolean existsByNickname(String randomNickName);

	void save(Member newMember);

	Long verifyPassword(String password);
	// ========== 소셜 회원 로직 ==========
	int countBySocialId(String socialId);

	SocialUser findBySocialId(String socialId);

	void insertSocialUser(SocialUser socialUser);

	void uploadProfileImage(MultipartFile file);

	Map<String, String> getUserProfile();

	Member findByUserId(String userId);



	
	// ========== 소셜 회원 가입을 위한 메서드 =============
	//Member createSocialMember(String socialId, String nickname, String type);








	
		
	

}
