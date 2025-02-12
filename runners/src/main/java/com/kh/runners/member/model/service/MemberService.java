package com.kh.runners.member.model.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kh.runners.member.model.dto.ChangePasswordDTO;
import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.member.model.dto.UpdateMemberDTO;

public interface MemberService {


	void insertUser(MemberDTO requestMember);

	void changePassword(ChangePasswordDTO changeEntity);

	void deleteByPassword(Map<String, String> password);

	UpdateMemberDTO updateMember(UpdateMemberDTO updateMemberDTO, MultipartFile file);







	
		
	

}
