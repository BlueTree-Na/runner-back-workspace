package com.kh.runners.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SocialUser {
    private String socialId; 
    private Long userNo;    
    private String nickName;
    private String userName;
    private String phone;
    private String gender;
    private String role;
    private String type;   
	private String fileUrl;
    private String createAt;
    private String status;
    private String email;
}
