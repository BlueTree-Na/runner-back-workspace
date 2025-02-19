package com.kh.runners.member.cortroller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.runners.auth.model.service.AuthenticationService;
import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.exception.DuplicateUserException;
import com.kh.runners.exception.MissmatchPasswordException;
import com.kh.runners.member.model.dto.ChangePasswordDTO;
import com.kh.runners.member.model.dto.LoginDTO;
import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.member.model.dto.UpdateMemberDTO;
import com.kh.runners.member.model.service.MemberService;
import com.kh.runners.member.model.vo.LoginResponse;
import com.kh.runners.member.model.vo.Member;
import com.kh.runners.token.model.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "members", produces = "application/json; charset=UTF-8")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final AuthenticationService authService;
	private final TokenService tokenService;

	
	// 회원가입
	@PostMapping
	public ResponseEntity<Map<String, String>> insertUser(@Valid @RequestBody MemberDTO requestMember) {

	    // ID 중복 체크 → 중복이면 예외 던지기
	    if (memberService.findByUserId(requestMember.getUserId()) != null) {
	        throw new DuplicateUserException("이미 존재하는 아이디입니다.");
	    }

	    // 닉네임 중복 체크 → 중복이면 예외 던지기
	    if (memberService.existsByNickname(requestMember.getNickName())) {
	        throw new DuplicateUserException("이미 존재하는 닉네임입니다.");
	    }

	    // 회원가입 진행
	    memberService.insertUser(requestMember);
	    return ResponseEntity.ok(Map.of("message", "회원가입에 성공했습니다."));
	}
	
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO requestMember) {
		
		log.info("userId: {}", requestMember.getUserId());
		Map<String, String> tokens = authService.login(requestMember);
		
		log.info("발급된토큰: {}",tokens);
		
		LoginResponse response = LoginResponse.builder().username(requestMember.getUserId()).tokens(tokens).build();
		
		return ResponseEntity.ok(response);
	}
		
	// 로그인한 사용자 정보 조회
	@GetMapping("/profile")
	public ResponseEntity<MemberDTO> getProfile() {
	    // 현재 로그인한 사용자 정보 가져오기
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

	    // userNo로 회원 정보 조회
	    Member member = memberService.findByUserNo(userDetails.getUserNo());
	    
	    if (member == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 회원 정보 없음
	    }

	    MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setUserNo(member.getUserNo());
	    memberDTO.setNickName(member.getNickName());
	    memberDTO.setGender(member.getGender());
	    memberDTO.setPhone(member.getPhone());
	    memberDTO.setEmail(member.getEmail());

	    return ResponseEntity.ok(memberDTO);
	}

	  
	
	// 회원 프로필 조회 (닉네임 & 프로필 이미지)
	@GetMapping("/profile/image")
	public ResponseEntity<Map<String, String>> getUserProfile() {
		Map<String, String> userProfile = memberService.getUserProfile();
		if (userProfile.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap());
		}
		return ResponseEntity.ok(userProfile);
	}
	
	// 🚀 프로필 이미지 업로드
	@PostMapping("/uploadProfile")
	public ResponseEntity<String> uploadProfileImage(@RequestParam("profileImage") MultipartFile file) {
			memberService.uploadProfileImage(file);
		return ResponseEntity.ok("프로필 이미지 업로드 성공");
    }
	

	
	// 회원정보수정
	@PutMapping("/profileUpdate")
	public ResponseEntity<String> updateProfile(@Valid @ModelAttribute UpdateMemberDTO updateMemberDTO,
												@RequestParam(name = "file", required = false) MultipartFile file) {
	
		memberService.updateMember(updateMemberDTO, file);
		
		return ResponseEntity.ok("회원정보 수정이 완료되었습니다.");
	}

	// 회원 수정 시 비밀번호 검증
	@PostMapping("/verify-password")
	public ResponseEntity<Map<String, Integer>> verifyPassword(@RequestBody Map<String, String> requestBody) {
	    String currentPassword = requestBody.get("currentPassword");

	    try {
	        memberService.verifyPassword(currentPassword);
	        return ResponseEntity.ok(Map.of("isValid", 1)); // 검증 성공 시 1 반환
	    } catch (MissmatchPasswordException e) {
	        return ResponseEntity.ok(Map.of("isValid", 0)); // 검증 실패 시 0 반환
	    }
	}


	
	
	// 비밀번호 변경
	@PutMapping
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changeEntity) {
		
		// log.info("changeEntity : {}", changeEntity );
		 
		memberService.changePassword(changeEntity);
		
		return ResponseEntity.ok("업데이트에 성공했습니다");
	}
	
	
	// 회원탈퇴
	@DeleteMapping
	public ResponseEntity<String> deleteByPassword(@RequestBody Map<String,String> password) {
		log.info("password{}", password);
		memberService.deleteByPassword(password);
		
		return ResponseEntity.ok("탈퇴가 완료되었습니다");
	}
	
	
	// 리프래시 토큰 갱신
	@PostMapping("refresh")
	public ResponseEntity<Map> refresh(@RequestBody Map<String, String> tokens) {
		log.info("아아악");
		String refreshToken = tokens.get("refreshToken");
		Map<String, String> newTokens = tokenService.refreshTokens(refreshToken);
		
		return ResponseEntity.ok(newTokens);
	}
	
	

}
