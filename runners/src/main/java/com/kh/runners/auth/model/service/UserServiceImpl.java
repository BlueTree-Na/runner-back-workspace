package com.kh.runners.auth.model.service;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.member.model.mapper.MemberMapper;
import com.kh.runners.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService{
	
	private final MemberMapper mapper; 

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//log.info("유저조회:{}", username);
		Member user = mapper.findbyUserId(username);
		//log.info("user:{}", user);
		if(user == null) {
			log.info("조회:{}", username);
			throw new UsernameNotFoundException("존재하지 않는 사용자 입니다.");
		}
		if("3".equals(user.getStatus())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다.");
		}
		
		return CustomUserDetails.builder()
								.userNo(user.getUserNo())
								.username(user.getUserId())
								.password(user.getUserPwd())
								.authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
								.build();
	}

}
