package com.digitalbooks.security;

import java.io.Serializable;
import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long TOKEN_VALIDITY = 5 * 60 * 60 * 1000; // milli secs
	
	@Value("${jwt.secret}")
	private String secret;
	
	public String getUsernameFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	 private Boolean ignoreTokenExpiration(String token) {
	        return false;
	    }
	 
	 public Boolean canTokenBeRefreshed(String token) {
	        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	    }

	 private Date calculateExpirationDate(Date createdDate) {
	        return new Date(createdDate.getTime() + TOKEN_VALIDITY * 1000);
	    }
	 
	    public String refreshToken(String token) {
	        final Date createdDate = new Date();
	        final Date expirationDate = calculateExpirationDate(createdDate);
	        final Claims claims = getAllClaimsFromToken(token);
	        claims.setExpiration(createdDate);
	        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	    }
	
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(UserDetails userDetails) {

		String token= doGenerateToken(userDetails.getUsername());
	    return token;
	}

	private String doGenerateToken(String subject) {
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
