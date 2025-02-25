package com.kh.runners.auth.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
//카카오, 네이버 공통으로 사용
@Slf4j
@Component
public class SnsLoginUtil {
	
	@Value("${naver.client_id}")
	private String clientId;
	
	@Value("${naver.client_secret}")
	private String clientSecret;
	
	@Value("${naver.redirect_uri}")
	 String redirectUri;
	
	/**
	 * API 호출
	 * 
	 * @param 	- String apiUrl - API 호출 URL
	 * 		 	- Map<String, String> requestHeaders - 요청 헤더 
	 * 				EX) requestHeaders.put("Content-Type", "application/json");
	 *			- String method - 요청 메소드(GET,POST)
	 *
	 * @return HashMap<String, String>
	 */
	private static String send(String apiUrl, Map<String, String> requestHeaders, String method){
		
        
		HttpURLConnection con = connect(apiUrl);
			
		try {
		
			con.setRequestMethod(method);
			
			for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
			    con.setRequestProperty(header.getKey(), header.getValue());
			}
			
			int responseCode = con.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
			        return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	/**
	 * URL 연결
	 * 
	 * @param 	- String apiUrl - API 호출 URL
	 * 		 	
	 * @return HttpURLConnection
	 */
	 private static HttpURLConnection connect(String apiUrl){
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection)url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

   
	 /**
	 * 응답받은 boby -> String 변환
	 * 
	 * @param 	- InputStream body - 응답받은 Stream
	 * 		 	
	 * @return HttpURLConnection
	 */

	private static String readBody(InputStream body){
	
		InputStreamReader streamReader = new InputStreamReader(body);
		
		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			
			StringBuilder responseBody = new StringBuilder();
			String line;
				
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			
			return responseBody.toString();
			
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}
	
	
	/**
	 * 토큰 발급
	 * 
	 * @param 	- String code 
	 * 		 	- String state - 사이트 간 요청 위조(cross-site request forgery) 공격을 방지하기 위해 애플리케이션에서 생성한 상태 토큰값으로 URL 인코딩을 적용한 값을 사용
	 * 		 	- String type - 연동타입(naver, kakao)
	 *
	 * @return HashMap<String, String>
	 */

	public HashMap<String, String> getAccessToken(HashMap<String, String> paramMap) {
		
		HashMap<String, String> result = new HashMap<String, String>();
		
		try {
			
			String access_Token = "";
		    String refresh_Token = "";
			String url = "";
			Map<String, String> requestHeaders = new HashMap<String, String>(); 
			
			
			if("naver".equals(paramMap.get("type"))) {
				url = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&"
						+ "client_id=" + clientId + "&client_secret=" + clientSecret 
						+ "&code=" + paramMap.get("code") + "&state=" + paramMap.get("state");
			} else if("kakao".equals(paramMap.get("type"))) {
				url = "kakao";
			}
			
			
			requestHeaders.put("Content-Type", "application/json");
			requestHeaders.put("auth", "myAuth");
			log.info("url:{}", url);
			String res = send(url, requestHeaders, "POST");
			log.info("res:{}", res);
			
			JSONParser jsonParser = new JSONParser();
			Object e = jsonParser.parse(res.toString());
			JSONObject obj = (JSONObject) e; // json으로 변경 
			
			result.put("access_token", (String) obj.get("access_token"));
			result.put("refresh_token", (String) obj.get("refresh_token"));
			result.put("token_type", (String) obj.get("token_type"));
			result.put("expires_in", String.valueOf(obj.get("expires_in")));

			//result.put("code", (String) obj.get("code"));
			//result.put("message", (String)obj.get("message"));
			
		} catch (Exception e) {
			
			result.put("code", "");
			result.put("message", "FAIL");
		}
		
		return result;
	}
	
	/**
	 * 유저 프로필 조회
	 * 
	 * @param 	- String accessToken - 토큰 발급 시 발급 받은 토큰
	 * 		 	- String type - 연동타입(naver, kakao)
	 *
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getUserInfo(HashMap<String, String> paramMap) {
		
		HashMap<String, String> result = new HashMap<String, String>();
		
		try {
			Map<String, String> requestHeaders = new HashMap<String, String>();
		    
			requestHeaders.put("Authorization", "Bearer " + paramMap.get("accessToken"));
			
			String urlStr = "";
			
			if("naver".equals(paramMap.get("type"))) {
				urlStr = "https://openapi.naver.com/v1/nid/me";
			} else if("kakao".equals(paramMap.get("type"))) {
				urlStr = "";
			}
			
			String res = send(urlStr, requestHeaders, "GET");
			log.info("urlStr:{}",urlStr);
			log.info("requestHeaders:{}",requestHeaders);
			log.info("res:{}",res);
			
			JSONParser jsonParser = new JSONParser();
	        JSONObject obj = (JSONObject) jsonParser.parse(res.toString());
	        
	        JSONObject responseObj = (JSONObject) obj.get("response");
	        
	        result.put("id", (String) responseObj.get("id"));
	        result.put("nickname", (String) responseObj.get("nickname"));
	        result.put("email", (String) responseObj.get("email"));
	        result.put("mobile", (String) responseObj.get("mobile"));
	        
		} catch (Exception e) {
			log.info("result4 :: {}", result.toString());
			  e.printStackTrace();
			  result.put("code", "");
			  result.put("message", "FAIL");
		}
		log.info("result5 :: {}", result.toString());
		
		return result;
	}
	
}