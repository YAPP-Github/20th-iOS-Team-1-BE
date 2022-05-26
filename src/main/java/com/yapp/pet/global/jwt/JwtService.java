package com.yapp.pet.global.jwt;

import com.yapp.pet.global.exception.jwt.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.yapp.pet.global.TogaetherConstants.*;

@Slf4j
@Component
public class JwtService {

	private final long accessTime;
	private final long refreshTime;
	private final String headerType;
	private final Key key;
	private final String issuer;

	public JwtService(@Value("${jwt.token.header-type}") String headerType,
					  @Value("${jwt.token.issuer}") String issuer,
					  @Value("${jwt.token.secret}") String secret,
					  @Value("${jwt.token.access-time}") long accessTime,
					  @Value("${jwt.token.refresh-time}") long refreshTime) {

		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.headerType = headerType;
		this.issuer = issuer;
		this.accessTime = accessTime;
		this.refreshTime = refreshTime;
	}

	public String createAccessToken(String uniqueIdBySocial) {
		long now = (new Date()).getTime();
		Date issuedAt = new Date();
		Date expiration = new Date(now + accessTime);

		return Jwts.builder()
				.signWith(key, SignatureAlgorithm.HS512)
				.setHeaderParam(JWT_HEADER_PARAM_TYPE, headerType)
				.setIssuer(issuer)
				.setSubject(uniqueIdBySocial)
				.setAudience(TokenType.ACCESS.toString())
				.setExpiration(expiration)
				.setIssuedAt(issuedAt)
				.claim(AUTHORITIES_KEY, ROLE)
				.compact();
	}

	public String createRefreshToken(String uniqueIdBySocial) {
		long now = (new Date()).getTime();
		Date issuedAt = new Date();
		Date expiration = new Date(now + refreshTime);

		return Jwts.builder()
				.signWith(key, SignatureAlgorithm.HS512)
				.setHeaderParam(JWT_HEADER_PARAM_TYPE, headerType)
				.setIssuer(issuer)
				.setSubject(uniqueIdBySocial)
				.setAudience(TokenType.REFRESH.toString())
				.setExpiration(expiration)
				.setIssuedAt(issuedAt)
				.claim(AUTHORITIES_KEY, ROLE)
				.compact();
	}

	public JwtAuthentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		String uniqueIdBySocial = claims.getSubject();

		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

		return new JwtAuthentication(uniqueIdBySocial, "", authorities);
	}

	public Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			throw new AuthorityInfoNotFoundException();
		}
	}

	public String getSubject(String token){
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
		} catch (ExpiredJwtException e) {
			throw new AuthorityInfoNotFoundException();
		}
	}

	public boolean validateToken(String token){
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
			throw new InvalidJwtSignatureException();
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
			throw new ExpiredJwtTokenException();
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
			throw new UnsupportedJwtTokenException();
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
			throw new InvalidJwtTokenException();
		}
	}

}