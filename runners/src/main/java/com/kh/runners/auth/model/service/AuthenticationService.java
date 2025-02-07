package com.kh.runners.auth.model.service;

import java.util.Map;

import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.member.model.dto.MemberDTO;

public interface AuthenticationService {

	
	Map<String, String> login(MemberDTO requestMember);
	
	CustomUserDetails getAuthenticatedUser();
	

}
