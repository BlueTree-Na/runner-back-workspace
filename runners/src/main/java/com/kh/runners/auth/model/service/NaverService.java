package com.kh.runners.auth.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kh.runners.auth.model.util.SNSLoginUtil;
import com.kh.runners.auth.model.vo.SocialUser;
import com.kh.runners.member.model.service.MemberService;
import com.kh.runners.member.model.vo.Member;
import com.kh.runners.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {
	
	private final SNSLoginUtil snsLoginUtil;		
	private final MemberService memberService;
	private final TokenService tokenService;	
	
	/**
	 * 네이버 로그인 후 회원가입/로그인 및 JWT 토큰 발급
	 *
	 * @param code  네이버 인가 코드
	 * @param state 네이버 상태값
	 * @return JWT 토큰 정보 (accessToken, refreshToken)
	 */
	public Map<String, String> processNaverLogin(String code, String state) {
		
		log.info("code:{}, state{}:", code,state);
		// 1. 네이버 토큰 발급
		HashMap<String, String> tokenParams = new HashMap<>();
		tokenParams.put("type", "naver");
		tokenParams.put("code", code);
		tokenParams.put("state", state);
		HashMap<String, String> tokenResponse = snsLoginUtil.getAccessToken(tokenParams);
		
		String accessToken = tokenResponse.get("access_token");
		
		if (accessToken == null || accessToken.isEmpty()) {
			throw new RuntimeException("네이버 토큰 발급에 실패했습니다.");
		}
		
		// 2. 네이버 사용자 정보 조회
		HashMap<String, String> userParams = new HashMap<>();
		userParams.put("type", "naver");
		userParams.put("accessToken", accessToken);
		
		
		HashMap<String, String> userInfo = snsLoginUtil.getUserInfo(userParams);
		
		// id 소셜아이디 값, 닉네임
		String socialId = userInfo.get("id");
		String nickname = userInfo.get("nickName"); 
		
		if (socialId == null || socialId.isEmpty()) {
			throw new RuntimeException("네이버 사용자 정보에 'id'가 포함되어 있지 않습니다.");
		}
		
		// 3. TB_SOCIAL_USER 존재여부 확인 (countBySocialId)
		int count = memberService.countBySocialId(socialId);
		
		// DB에 없는 경우 → 신규 소셜 회원 INSERT
		if (count == 0) {
			String finalNickname = (nickname != null && !nickname.isEmpty()) 
			? nickname : generateRandomNickname();		// 중복 닉네임이면 랜덤 닉네임 생성
			
			while (memberService.existsByNickname(finalNickname)) {
				finalNickname = generateRandomNickname();
			}
	        
			SocialUser newUser = SocialUser.builder()
											.socialId(socialId)
											.nickname(finalNickname)
											.userPwd("")
											.role("USER")
											.build();

			    memberService.insertSocialUser(newUser);
			}
		
		// 4. 소셜 회원 정보 조회
		SocialUser socialUser = memberService.findBySocialId(socialId);
		if (socialUser == null) {
            throw new RuntimeException("소셜 회원 정보 조회에 실패했습니다.");
        }
		
		// 5. JWT 토큰 발급 (TokenService의 generateToken 메서드는 username과 userNo를 이용)
		Map<String, String> tokenDto = tokenService.generateToken(socialId, socialUser.getUserNo());
			return tokenDto;
		}
		
		// 랜덤 닉네임 생성 (예: "user_ab12cd3")
		private String generateRandomNickname() {
			String uuid = UUID.randomUUID().toString().substring(0, 7);
		return "user_" + uuid;
		}
		
		
	
		
	}
