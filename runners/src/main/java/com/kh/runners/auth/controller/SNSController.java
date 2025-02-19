package com.kh.runners.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.runners.auth.model.service.NaverService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/naver")
@RequiredArgsConstructor
public class SNSController {

	private final NaverService naverService;

	/**
	 * 네이버 로그인 후 콜백 엔드포인트
	 * - 네이버에서 인가 코드(code)와 상태(state)를 받아서
	 *   회원가입/로그인 처리를 진행하고, JWT 토큰을 쿠키에 저장한 후
	 *   JSON 형태의 응답(토큰 정보)을 반환.
	 *
	 * @param code     네이버 인가 코드
	 * @param state    네이버 상태 토큰
	 * @param response HttpServletResponse (쿠키 저장용)
	 * @return ResponseEntity<Map<String, String>> - JWT 토큰 정보를 포함한 JSON 응답
	 */

    @GetMapping("/oauth")
    public ResponseEntity<Map<String, String>> naverOAuth(@RequestParam("code") String code, 
                                   @RequestParam("state") String state,
                                   HttpServletResponse response) {
		// 네이버 로그인 후 회원가입/로그인 및 JWT 토큰 발급
		Map<String, String> tokenMap = naverService.processNaverLogin(code, state);
		String accessToken = tokenMap.get("accessToken"); // 토큰 키에 맞게 수정
		
		// 쿠키 생성 (예: accessToken 쿠키)
		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
		accessTokenCookie.setHttpOnly(true);		// 자바스크립트에서 접근하지 못하도록 설정
		accessTokenCookie.setSecure(true); 			// HTTPS 환경일 경우 true (개발환경에서는 false 가능)
		accessTokenCookie.setPath("/");				// 전체 경로에서 사용
		accessTokenCookie.setMaxAge(60 * 60 * 3);	// 쿠키 유효기간 (초 단위, 3시간)
		
		// 응답에 쿠키 추가
		response.addCookie(accessTokenCookie);
		
		// 클라이언트에 토큰 정보를 JSON으로도 반환 가능
		return ResponseEntity.ok(tokenMap);
    }

}
