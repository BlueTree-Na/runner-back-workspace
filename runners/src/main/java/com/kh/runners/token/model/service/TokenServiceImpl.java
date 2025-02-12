package com.kh.runners.token.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kh.runners.auth.model.util.JwtUtil;
import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.token.model.dto.RefreshTokenDTO;
import com.kh.runners.token.model.mapper.TokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final JwtUtil tokenUtil;
	private final TokenMapper tokenMapper;
	
	// 토큰
	@Override
	public Map<String, String> generateToken(String username, Long userNo){
		
		Map<String, String> tokens = createTokens(username);
		insertToken(tokens.get("refreshToken"), userNo);
		
		deleteExpiredRefreshToken(userNo);
		
		return tokens;
	}
	
	
	// 토큰 생성
	private Map<String, String> createTokens(String username) {
		String accessToken = tokenUtil.getAccessToken(username);
		String refreshToken = tokenUtil.getRefreshToken(username);
		
		Map<String, String> tokens = new HashMap<>();
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);
		
		return tokens;
	}
	
	// 리프래시 토큰 DB insert
	private void insertToken(String refreshToken, Long userNo) {
		RefreshTokenDTO token = RefreshTokenDTO.builder()
												.token(refreshToken)
												.userNo(userNo)
												.expiration(System.currentTimeMillis() + 3600000L * 72)
												.build();
		
		tokenMapper.insertToken(token);
	}

	
	// 토큰 DB 조회
	@Override
	public Map<String, String> refreshTokens(String refreshToken) {

		RefreshTokenDTO token = tokenMapper.findByToken(refreshToken);
		
		if(token == null || token.getExpiration() < System.currentTimeMillis()) {
			throw new RuntimeException("알 수 없는 리프레시 토큰입니다."); // 유효기간 만료 등등 예외발생
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		
		return generateToken(user.getUsername(), user.getUserNo());
	}
	
		
	// 토근 삭제 
	private void deleteExpiredRefreshToken(Long userNo) {
		
		Map<String, Long> params = new HashMap();
		
		params.put("userNo", userNo);
		params.put("currentTime", System.currentTimeMillis());
		log.info("currentTime: {}", System.currentTimeMillis());
		tokenMapper.deleteExpiredRefreshToken(params);
		
		
		
		
	}
}
