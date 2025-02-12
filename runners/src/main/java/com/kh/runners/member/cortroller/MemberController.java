package com.kh.runners.member.cortroller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
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
import com.kh.runners.member.model.dto.ChangePasswordDTO;
import com.kh.runners.member.model.dto.LoginDTO;
import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.member.model.dto.UpdateMemberDTO;
import com.kh.runners.member.model.service.MemberService;
import com.kh.runners.member.model.vo.LoginResponse;
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
	public ResponseEntity<?> insertUser(@Valid @RequestBody MemberDTO requestMember) {

		memberService.insertUser(requestMember);

		return ResponseEntity.ok("회원가입에 성공했습니다.");
	}
	
	
	// 로그인
	@PostMapping("login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO requestMember) {
		
		log.info("userId: {}", requestMember.getUserId());
		Map<String, String> tokens = authService.login(requestMember);
		
		log.info("발급된토큰: {}",tokens);
		
		LoginResponse response = LoginResponse.builder().username(requestMember.getUserId()).tokens(tokens).build();
		
		return ResponseEntity.ok(response);
	}
		


	// 회원정보수정
	@PutMapping("profile")
	public ResponseEntity<String> updateProfile(@Valid @ModelAttribute("updateMemberDTO") UpdateMemberDTO updateMemberDTO,
												@RequestParam(name = "file", required = false) MultipartFile file) {
	
		memberService.updateMember(updateMemberDTO, file);
		
		return ResponseEntity.ok("회원정보 수정이 완료되었습니다.");
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
