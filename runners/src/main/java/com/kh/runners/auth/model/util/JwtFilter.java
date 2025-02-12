package com.kh.runners.auth.model.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kh.runners.auth.model.service.UserServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; 

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil tokenUtil;
	private final UserServiceImpl userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		// log.info("토큰필터 {}", authorization);
		
		if(authorization == null || !authorization.startsWith("Bearer ")) {
			// log.error("authorization이 존재하지않습니다.");
			filterChain.doFilter(request, response);
			return;
		}
		
		
		// 토큰 추출
		String token = authorization.split(" ")[1];
		try {
			Claims claims = tokenUtil.pareJwt(token);
			
			String username = claims.getSubject();
			
			log.info("토큰주인 아이디:{}", username);
		
			UserDetails userDetails = userService.loadUserByUsername(username);
																									
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch(ExpiredJwtException e) {
			log.info("AccessToken이 만료되었습니다");

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Expired Token");
			return;	
			
		} catch(JwtException e) {
			log.info("Token검증에 실패했습니다.");

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("이상해");
		}
		
		filterChain.doFilter(request, response);
	}

}
