package com.yapp.pet.global.jwt;

import com.yapp.pet.domain.account.AccountRepository;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.exception.jwt.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
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
	private final AccountRepository accountRepository;

	public JwtService(@Value("${jwt.token.header-type}") String headerType,
					  @Value("${jwt.token.issuer}") String issuer,
					  @Value("${jwt.token.secret}") String secret,
					  @Value("${jwt.token.access-time}") long accessTime,
					  AccountRepository accountRepository) {

		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.headerType = headerType;
		this.issuer = issuer;
		this.accessTime = accessTime;
		this.accountRepository = accountRepository;
	}

	public String createAccessToken(Account account) {
		long now = (new Date()).getTime();
		Date issuedAt = new Date();
		Date expiration = new Date(now + accessTime);

		return Jwts.builder()
				.signWith(key, SignatureAlgorithm.HS512)
				.setHeaderParam(JWT_HEADER_PARAM_TYPE, headerType)
				.setIssuer(issuer)
				.setSubject(String.valueOf(account.getId()))
				.setExpiration(expiration)
				.setIssuedAt(issuedAt)
				.claim(AUTHORITIES_KEY, account.getRole())
				.compact();
	}

	public JwtAuthentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		long accountId = Long.parseLong(claims.getSubject());

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

	public Account getAccount(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();

			Long accountId = Long.parseLong(claims.getSubject());

			return accountRepository.findById(accountId).orElseThrow(EntityNotFoundException::new);

		} catch (SecurityException | MalformedJwtException e) {
			throw new InvalidJwtSignatureException();
		} catch (ExpiredJwtException e) {
			throw new ExpiredJwtTokenException();
		} catch (UnsupportedJwtException e) {
			throw new UnsupportedJwtTokenException();
		} catch (IllegalArgumentException e) {
			throw new InvalidJwtTokenException();
		}
	}

}