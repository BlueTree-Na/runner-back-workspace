package com.kh.runners.token.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.runners.token.model.dto.RefreshTokenDTO;

@Mapper
public interface TokenMapper {

	// 등록
	@Insert("INSERT INTO TB_TOKEN VALUES(#{userNo}, #{token}, #{expiration})")
	void insertToken(RefreshTokenDTO refreshToken);
	
	// 조회
	@Select("SELECT USER_NO userNo, token, EXPIRED_AT expiration FROM TB_REFRESH_TOKEN WHERE TOKEN=#{refreshToken}")
	RefreshTokenDTO findByToken(String refreshToken);
	
	// 삭제	
	@Delete("DELETE FROM TB_TOKEN WHERE USER_NO=#{userNo} AND EXPIRED_AT < #{currentTime}")
	void deleteExpiredRefreshToken(Map<String, Long> params);

	

}
