package com.kh.runners.member.model.vo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

	private String username;
	private Map<String, String> tokens; 
	
}
