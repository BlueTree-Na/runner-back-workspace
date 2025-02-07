package com.kh.runners.member.cortroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "members", produces="application/json; charset=UTF-8")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	
	// 회원가입
	@PostMapping
	public ResponseEntity<?> insert(@Valid @RequestBody MemberDTO requestMember) {
		
		memberService.insert(requestMember);
		
		return ResponseEntity.ok("회원가입에 성공했습니다.");
	}
	
	
	/*
	 * // 로그인
	 * 
	 * @PostMapping("login") public ResponseEntity<LoginResponse>
	 * login(@Valid @RequestBody MemberDTO requestMember) {
	 * 
	 * Map<String>
	 * 
	 * 
	 * }
	 */
	
	
	
	
	
	// 회원정보수정
	
	
	// 회원탈퇴
	
	
}
