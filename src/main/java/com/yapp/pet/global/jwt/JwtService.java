package com.yapp.pet.global.jwt;

import com.yapp.pet.domain.account.AccountRepository;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.exception.jwt.AuthorityInfoNotFoundException;
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

import static com.yapp.pet.global.TogaetherConstants.AUTHORITIES_KEY;
import static com.yapp.pet.global.TogaetherConstants.JWT_HEADER_PARAM_TYPE;

@Slf4j
@Component
public class JwtService {

	private final long accessTime;
	private final String headerType;
	private final Key key;
	private final String issuer;

	public JwtService(@Value("${jwt.token.header-type}") String headerType,
					  @Value("${jwt.token.issuer}") String issuer,
					  @Value("${jwt.token.secret}") String secret,
					  @Value("${jwt.token.access-time}") long accessTime) {

		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.headerType = headerType;
		this.issuer = issuer;
		this.accessTime = accessTime;
	}

	public String createAccessToken(Account account) {
		long now = (new Date()).getTime();
		Date issuedAt = new Date();
		Date expiration = new Date(now + accessTime);

		return Jwts.builder()
				.signWith(key, SignatureAlgorithm.HS512)
				.setHeaderParam(JWT_HEADER_PARAM_TYPE, headerType)
				.setIssuer(issuer)
				.setSubject(TokenType.ACCESS.name())
				.setAudience(String.valueOf(account.getId()))
				.setExpiration(expiration)
				.setIssuedAt(issuedAt)
				.claim(AUTHORITIES_KEY, account.getRole())
				.compact();
	}

	public JwtAuthentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		long accountId = Long.parseLong(claims.getAudience());

		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

		return new JwtAuthentication(accountId, "", authorities);
	}

	private Claims parseClaims(String token) {
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

	public boolean isTokenExpired(Date tokenExpiredTime){
		Date now = new Date();

		if(now.after(tokenExpiredTime)){
			return true;
		}

		return false;
	}

	public boolean validateToken(String token){
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}

		return false;
	}

}