package com.kh.runners.auth.model.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.member.model.dto.LoginDTO;
import com.kh.runners.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	// 로그인 (Id,Pwd검증)

	@Override
	public Map<String, String> login(LoginDTO requestMember) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestMember.getUserId(), requestMember.getUserPwd()));
		
		log.info("auth = {}", authentication.getPrincipal());
		
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		log.info("로그인성공");
		log.info("DB사용자정보 : {}", user);

		Map<String, String> tokens = tokenService.generateToken(user.getUsername(), user.getUserNo());
		log.info("토큰:{}", tokens);
		return tokens;

	}

	/*
	 * @Override public Map<String, String> login(LoginDTO requestMember) {
	 * log.info("로그인 시도: userId={}, password={}", requestMember.getUserId(),
	 * requestMember.getUserPwd());
	 * 
	 * try { Authentication authentication = authenticationManager.authenticate( new
	 * UsernamePasswordAuthenticationToken(requestMember.getUserId(),
	 * requestMember.getUserPwd()) );
	 * 
	 * CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
	 * 
	 * log.info("로그인 성공: {}", user);
	 * 
	 * Map<String, String> tokens = tokenService.generateToken(user.getUsername(),
	 * user.getUserNo());
	 * 
	 * return tokens; } catch (Exception e) { log.error("로그인 실패: {}",
	 * e.getMessage(), e); throw e; } }
	 */

	@Override
	public CustomUserDetails getAuthenticatedUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validWriter(String writer, String username) {
		// TODO Auto-generated method stub

	}

}
