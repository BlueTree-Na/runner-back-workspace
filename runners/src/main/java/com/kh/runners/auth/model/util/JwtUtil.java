package com.kh.runners.auth.model.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component

public class JwtUtil {
	
	
	@Value("${jwt.secret}")
	private String secretKey;
	// javax.crypto.Secretkey타입의 필드로 JWT서명에 사용할 수 있음
	private SecretKey key;
	
	// 토큰 유효기간 설정
	private long ACCESS_TOKEN_EXPIRED = 3600000L * 24; // 1일
	private long REFRESH_TOKEN_EXPIRED = 3600000L * 72; // 3일
	
	// 의존성 주입 후 초기화 할 때 사용할 메서드
	@PostConstruct
	public void init() { 
		byte[] keyArr = Base64.getDecoder().decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyArr); 
		
		// 현재 시간을 Long타입으로 바꿔주는 메서드
		//long now = System.currentTimeMillis() + 3600000L * 24;
	}
	
	// 중복제거하기 위해 따로 메서드 만들어서 사용하면 간결해짐!
	private Date buildExpirationDate(long date) {
		long now = System.currentTimeMillis();
		return new Date(now + date);
	}
	
	
	
	// AccessToken 생성 - 토큰에 username을 담아 생성 (Id는 공개된거니까 Id로 진행)
	public String getAccessToken(String username) {
		return Jwts.builder().subject(username)			// 사용자 이름
							 .issuedAt(new Date())		// 발급일 
							 // .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED))	// 만료일
							 .expiration(buildExpirationDate(ACCESS_TOKEN_EXPIRED))	// 만료일
							 // .expiration(new Date())	// 발급하자마자없어지는키
							 .signWith(key)				// 비밀키로 만든 서명
							 .compact();
	}
	
	// RefreschToken 생성
	public String getRefreshToken(String username) {
		return Jwts.builder().subject(username)			// 사용자 이름
							 .issuedAt(new Date())		// 발급일 
							 //.expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED))	// 만료일
							 .expiration(buildExpirationDate(REFRESH_TOKEN_EXPIRED))	// 만료일
							 .signWith(key)				// 비밀키로 만든 서명
							 .compact();
	}
	
	
	public Claims pareJwt(String token) {
		
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
}
