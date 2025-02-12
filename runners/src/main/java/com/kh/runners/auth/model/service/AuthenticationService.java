package com.kh.runners.auth.model.service;

import java.util.Map;

import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.member.model.dto.LoginDTO;

public interface AuthenticationService {

	
	Map<String, String> login(LoginDTO requestMember);
	
	CustomUserDetails getAuthenticatedUser();
	
	void validWriter(String writer, String username);

}
