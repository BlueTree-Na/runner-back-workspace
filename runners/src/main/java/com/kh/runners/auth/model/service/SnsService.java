package com.kh.runners.auth.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.runners.auth.model.util.SnsLoginUtil;
import com.kh.runners.auth.model.vo.SocialUser;
import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.member.model.service.MemberService;
import com.kh.runners.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnsService {
	
	private final SnsLoginUtil snsLoginUtil;		
	private final MemberService memberService;
	private final TokenService tokenService;	
	
	/**
	 * 네이버 로그인 후 회원가입/로그인 및 JWT 토큰 발급
	 *
	 * @param code  네이버 인가 코드
	 * @param state 네이버 상태값
	 * @return JWT 토큰 정보 (accessToken, refreshToken)
	 */
	@Transactional
	public Map<String, String> processNaverLogin(String code, String state) {
		
//		log.info("code:{}, state:{}", code,state);
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
		log.info("네이버 로그인 응답 데이터: {}", userInfo);

		// id 소셜아이디 값, 닉네임
		String socialId = userInfo.get("id");
		String userName = userInfo.get("name"); 
		String nickname = userInfo.get("nickname"); 
		String phone = userInfo.get("mobile");
		String email = userInfo.get("email");
		String gender = userInfo.get("gender");
		String profileImage = userInfo.get("profile_image");
		
		if (socialId == null || socialId.isEmpty()) {
			throw new RuntimeException("네이버 사용자 정보에 'id'가 포함되어 있지 않습니다.");
		}
	
		// 3. TB_SOCIAL_USER 존재여부 확인 (countBySocialId)
//		log.info("socialId:{}", socialId);
		int count = memberService.countBySocialId(socialId);
		Long userNo;
		
		// DB에 없는 경우 → 신규 소셜 회원 INSERT
		if (count == 0) {
			String finalNickname = (nickname != null && !nickname.isEmpty()) 
					? nickname : generateRandomNickname();		// 중복 닉네임이면 랜덤 닉네임 생성
			
			if(memberService.existsByNickname(finalNickname) > 0) {
				finalNickname = generateRandomNickname();
			}
			
			
			// TB_MEMBER INSERT
			MemberDTO newMember = MemberDTO.builder()
									        .userName(userName)
									        .nickName(finalNickname)
									        .email(email)
									        .phone(phone)
									        .gender(gender)
									        .fileUrl(profileImage)
									        .role("ROLE_USER") // 일반 사용자
									        .build();
			
	
			userNo = memberService.insertFristSocialUser(newMember);
			
			log.info("신규 회원가입 완료: {}, userNo : {}", newMember, userNo);
			
			// TB_SOCIALUSER INSERT
			SocialUser newSocialUser = SocialUser.builder()
												.socialId(socialId)
												.userNo(userNo)
												.build();
			
			log.info("소셜 계정 정보 저장: {}", newSocialUser);
			memberService.insertSocialUser(newSocialUser);
			
		} else {
			log.info("Is this your socialId?? : {}", socialId);
			SocialUser socialUser = memberService.findBySocialId(socialId);
			
			if (socialUser == null) {
	            throw new RuntimeException("소셜 회원 정보 조회에 실패했습니다.");
	        }
			
	        userNo = socialUser.getUserNo();
	    }

	    // 6️. JWT 토큰 발급
	    Map<String, String> tokenDto = tokenService.generateToken(socialId, userNo);
	    return tokenDto;
	}

	
	
	// 랜덤 닉네임 생성 (예: "user_ab12cd3")
	private String generateRandomNickname() {
		
		String uuid = UUID.randomUUID().toString().substring(0, 7);
		
		return "user_" + uuid; 
	}
		
		
	
		
	}
