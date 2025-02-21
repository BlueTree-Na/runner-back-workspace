package com.kh.runners.member.model.service;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	
	
	// 일반 회원가입 INSERT
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
		
		// 비밀번호 검증
		Long userNo = passwordMatches(changeEntity.getCurrentPassword());
		String encoedPassword = passwordEncoder.encode(changeEntity.getNewPassword());
		
		
		Map<String, String> changeRequest = new HashMap<>();
		changeRequest.put("userNo", String.valueOf(userNo));
		changeRequest.put("password", encoedPassword);
		
		memberMapper.changePassword(changeRequest);
	}
	
	// 회원정보 조회
	@Override
	public Member findByUserNo(Long userNo) {
	    return memberMapper.findByUserNo(userNo);
	}
	
	
	// 회원 프로필 조회 (닉네임 & 프로필 이미지)
    @Override
    public Map<String, String> getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        Member member = memberMapper.findByUserNo(userDetails.getUserNo());

        if (member == null) {
            return Collections.emptyMap();
        }

        Map<String, String> response = new HashMap<>();
        response.put("nickname", member.getNickName());
        
        // 프로필 이미지가 null이면 기본 이미지 반환
        
        response.put("profileImage", member.getFileUrl() != null ? member.getFileUrl() : "/default-profile.png");

        return response;
    }

    // 프로필 이미지 업로드
    @Override
    public void uploadProfileImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userNo = userDetails.getUserNo();

        // 파일 저장 및 URL 반환
        String filePath = fileService.store(file);

        // DB 업데이트
        updateProfileImage(userNo, filePath);
    }

    // 프로필 이미지 DB 업데이트
    private void updateProfileImage(Long userNo, String filePath) {
        Map<String, Object> params = new HashMap<>();
        params.put("userNo", userNo);
        params.put("fileUrl", filePath);
        memberMapper.updateProfileImage(params);
    }

	
	// 회원정보 수정
	@Override
	public UpdateMemberDTO updateMember(UpdateMemberDTO updateMemberDTO, MultipartFile file) {
		//log.info("member {}", updateMemberDTO);
		if(updateMemberDTO.getCurrentPassword() != null && !updateMemberDTO.getCurrentPassword().isEmpty()) {
			
			Long userNo = passwordMatches(updateMemberDTO.getCurrentPassword());
	        updateMemberDTO.setUserNo(userNo);
	    } else {
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
	        updateMemberDTO.setUserNo(userDetails.getUserNo());
	    }
	    // 파일 확인
	    if (file != null && !file.isEmpty()) {
	    	String filePath = fileService.store(file);
	    // 파일담기..
	    	updateMemberDTO.setFileUrl(filePath);
	    }
	    
	    memberMapper.updateMember(updateMemberDTO);
	    
	    return updateMemberDTO;
	}


	// 회원정보 수정 시 비밀번호 검증
	@Override
	public Long verifyPassword(String password) {
	    return passwordMatches(password); // 기존의 passwordMatches() 활용
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

 
    // 소셜 로그인

    @Override
    public int countBySocialId(String socialId) {
        return memberMapper.countBySocialId(socialId);
    }

    @Override
    public SocialUser findBySocialId(String socialId) {
        return memberMapper.findBySocialId(socialId);
    }
    
    
    
    // 소셜 로그인 TB_MEMBER INSERT
    @Transactional
 	@Override
 	public Long insertFristSocialUser(MemberDTO requestMember) {
 		
// 		int searched = memberMapper.countBySocialId(requestMember.getSocialId());
//	
// 		if(searched < 1) {
// 			throw new DuplicateUserException("이미 존재하는 ID입니다.");
// 		}
 		
    	
 		Member member = Member.builder().userName(requestMember.getUserName())
 										.userId(null)
 										.userPwd(null)
 										.nickName(requestMember.getNickName())
 										.gender(requestMember.getGender())
 										.email(requestMember.getEmail())
 										.phone(requestMember.getPhone())
 										.fileUrl(requestMember.getFileUrl())
 										.role("ROLE_USER").build();
 		log.info("회원 가입 Member 정보 : userNo={}, email={}", member.getUserNo(), member.getEmail());
// 		return memberMapper.insertFristSocialUser(member).getUserNo();
// 		return memberMapper.insertFristSocialUser(member);
 		
 		memberMapper.insertFristSocialUser(member);
 		log.info("왔냐고 안왔냐고 재대로 오라고 : {}", member.getUserNo());
 		return member.getUserNo();
 	}
    
    
    
    // 소셜 로그인 TB_SOCIALUSER INSERT
    @Transactional
	@Override
	public void insertSocialUser(SocialUser socialUser) {
	    // 중복 체크 (소셜 ID 기준)
		log.info("회원정보:{}",socialUser);
		log.info("DB인서트 socialId: {}", socialUser.getSocialId());
		SocialUser existingUser = memberMapper.findBySocialId(socialUser.getSocialId());
		if (existingUser != null) {
		    throw new DuplicateUserException("이미 존재하는 소셜 계정입니다.");
		}
		
		log.info("TB_SOCAIL_USER 정보 : {}", socialUser);
		memberMapper.insertSocialUser(socialUser);
	}


    // 
    @Override
    public int existsByNickname(String randomNickName) {
        return memberMapper.findByNickname(randomNickName);
    }

    @Override
    public void save(Member newMember) {
        memberMapper.insertUser(newMember);
    }



    // 닉네임 랜덤 생성 로직 (공통)
    private String generateRandomNickname() {
        String uuid = UUID.randomUUID().toString().substring(0, 7);
        return "user_" + uuid;
    }


    // id중복체크용
	@Override
	public Member findByUserId(String userId) {
	    Member searched = memberMapper.findbyUserId(userId);
	    return searched; 
	}





	
	
}