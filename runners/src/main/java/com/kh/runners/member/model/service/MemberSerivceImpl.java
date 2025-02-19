package com.kh.runners.member.model.service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.runners.auth.model.vo.CustomUserDetails;
import com.kh.runners.auth.model.vo.SocialUser;
import com.kh.runners.exception.DuplicateUserException;
import com.kh.runners.exception.MissmatchPasswordException;
import com.kh.runners.member.model.dto.ChangePasswordDTO;
import com.kh.runners.member.model.dto.MemberDTO;
import com.kh.runners.member.model.dto.UpdateMemberDTO;
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
	private final FileService fileService;
	
	
	@Override
	public void insertUser(MemberDTO requestMember) {
		if ("".equals(requestMember.getUserId()) || "".equals(requestMember.getUserPwd())) {
			throw new InvalidParameterException("아이디 또는 비밀번호를 입력해주세요");
		}
		
		Member searched = memberMapper.findbyUserId(requestMember.getUserId());
		
		if(searched != null) {
			throw new DuplicateUserException("이미 존재하는 ID입니다.");
		}
		
		Member member = Member.builder().userName(requestMember.getUserName())
										.userId(requestMember.getUserId())
										.userPwd(passwordEncoder.encode(requestMember.getUserPwd()))
										.nickName(requestMember.getNickName())
										.gender(requestMember.getGender())
										.email(requestMember.getEmail())
										.phone(requestMember.getPhone())
										.role("ROLE_USER").build();
		memberMapper.insertUser(member);
		log.info("회원가입성공");
	}
	
	
	
	// 비밀번호 변경
	@Override
	public void changePassword(ChangePasswordDTO changeEntity) {
		
		Long userNo = passwordMatches(changeEntity.getCurrentPassword());
		String encoedPassword = passwordEncoder.encode(changeEntity.getNewPassword());
		
		
		Map<String, String> changeRequest = new HashMap<>();
		changeRequest.put("userNo", String.valueOf(userNo));
		changeRequest.put("password", encoedPassword);
		
		memberMapper.changePassword(changeRequest);
	}
	
	
	
	// 회원정보 수정
	@Override
	public UpdateMemberDTO updateMember(UpdateMemberDTO updateMemberDTO, MultipartFile file) {
		//log.info("member {}", updateMemberDTO);
		// 비밀번호 검증
	    Long userNo = passwordMatches(updateMemberDTO.getCurrentPassword());
	    //log.info("{userNo = {}", userNo);
	    updateMemberDTO.setUserNo(userNo);
	   // log.info("member {}", updateMemberDTO);
	    // 파일 확인
	    if (file != null && !file.isEmpty()) {
	    	String filePath = fileService.store(file);
	    // 파일담기..
	    	updateMemberDTO.setFileUrl(filePath);
	    }
	    
	    memberMapper.updateMember(updateMemberDTO);
	    
	    return updateMemberDTO;
	}





	
	
	
	
	// 회원 탈퇴
	@Override
	public void deleteByPassword(Map<String, String> password) {

		Long userNo = passwordMatches(password.get("password"));
		
		memberMapper.deleteByPassword(userNo);
	}
	
	
	// 비밀번호 공통 코드 중복제거 메서드
	private Long passwordMatches(String password) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails)auth.getPrincipal();
		
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			
			throw new MissmatchPasswordException("비밀번호가 일치하지 않습니다");
		}
		
		return userDetails.getUserNo();
	}

 
    // ===============================
    // 3) 소셜/일반 공통 로직 or 기존 메서드
    // ===============================

    @Override
    public int countBySocialId(String socialId) {
        return memberMapper.countBySocialId(socialId);
    }

    @Override
    public SocialUser findBySocialId(String socialId) {
        return memberMapper.findBySocialId(socialId);
    }

    @Override
    public void insertSocialUser(SocialUser socialUser) {
        memberMapper.insertSocialUser(socialUser);
    }

    @Override
    public Member findByUserNo(Long userNo) {
        return memberMapper.findByUserNo(userNo);
    }

    @Override
    public boolean existsByNickname(String randomNickName) {
        return memberMapper.findByNickname(randomNickName) != null;
    }

    @Override
    public void save(Member newMember) {
        memberMapper.insertUser(newMember);
    }

    // ===============================
    // 4) 비밀번호 변경, 회원정보 수정 등 (기존)
    // ===============================

    // ... (changePassword, updateMember, deleteByPassword 등 기존 메서드)

    // ===============================
    // 닉네임 랜덤 생성 로직 (공통)
    // ===============================
    private String generateRandomNickname() {
        String uuid = UUID.randomUUID().toString().substring(0, 7);
        return "user_" + uuid;
    }

	
}