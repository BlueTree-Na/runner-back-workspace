package com.kh.runners.auth.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.runners.auth.model.service.SnsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class SNSController {

	private final SnsService snsService;
	@Value("${naver.client_id}")
	private String clientId;
	
	@Value("${naver.client_secret}")
	private String clientSecret;
	
	@Value("${naver.redirect_uri}")
	 String redirectUri;

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

	@GetMapping("/naver/login-url")
	public ResponseEntity<String> getNaverLoginUrl() {
		
		String state = UUID.randomUUID().toString();
		log.info("state:{}", state);
		String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize"
	            + "?response_type=code"
	            + "&client_id=" + clientId
	            + "&redirect_uri=http://localhost:3000/auth"
	            + "&state=" + state;
	
		log.info("naverLoginUrl:{}", naverLoginUrl);
		 return ResponseEntity.ok(naverLoginUrl);
	}
	
	
    @GetMapping(value = "/naver/oauth", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Map<String, String>> naverOAuth(@RequestParam(value="code",required=false) String code, 
                                   @RequestParam(value="state",required=false) String state,
                                   HttpServletResponse response, HttpServletRequest request) {
    	
		// 네이버 로그인 후 회원가입/로그인 및 JWT 토큰 발급
//		log.info("code:{}, state:{}", code,state);
		// 회원없으면 회원가입, 있으면 로그인, tokenMap -> jwt..
    	Map<String, String> tokenMap = snsService.processNaverLogin(code, state);
		
    	//String accessToken = tokenMap.get("accessToken"); // 토큰 키에 맞게 수정

    
		// 클라이언트에 토큰 정보를 JSON으로도 반환 가능
		return ResponseEntity.ok(tokenMap);
    }
    

}
