package com.kh.runners.member.model.service;

import java.security.InvalidParameterException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.runners.exception.DuplicateUserException;
import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.member.model.mapper.MemberMapper;
import com.kh.runners.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSerivceImpl implements MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	
	@Override
	public void insert(MemberDTO requestMember) {
		if ("".equals(requestMember.getUserId()) || "".equals(requestMember.getUserPwd())) {
			throw new InvalidParameterException("아이디 또는 비밀번호를 입력해주세요");
		}
		
		Member searched = memberMapper.findbyId(requestMember.getUserId());
		
		if(searched != null) {
			throw new DuplicateUserException("이미 존재하는 ID입니다.");
		}
		
		Member member = Member.builder().userName(requestMember.getUserName())
										.userId(requestMember.getUserId())
										.userPwd(passwordEncoder.encode(requestMember.getUserPwd()))
										.nickName(requestMember.getNickName())
										.gender(requestMember.getGender())
										.email(requestMember.getEmail())
										.address(requestMember.getAddress())
										.phone(requestMember.getPhone())
										.role("ROLE_USER").build();
		memberMapper.save(member);
		log.info("회원가입성공");
	}


	
	
	
}
