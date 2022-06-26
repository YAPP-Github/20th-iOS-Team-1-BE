package com.yapp.pet.global.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.pet.global.exception.jwt.*;
import com.yapp.pet.web.oauth.apple.AppleClient;
import com.yapp.pet.web.oauth.apple.model.ApplePublicKeyResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static com.yapp.pet.global.TogaetherConstants.*;

@Slf4j
@Component
public class JwtService {

	private final long accessTime;
	private final long refreshTime;
	private final String headerType;
	private final Key key;
	private final String issuer;

	private final AppleClient appleClient;

	public JwtService(@Value("${jwt.token.header-type}") String headerType,
					  @Value("${jwt.token.issuer}") String issuer,
					  @Value("${jwt.token.secret}") String secret,
					  @Value("${jwt.token.access-time}") long accessTime,
					  @Value("${jwt.token.refresh-time}") long refreshTime,
					  AppleClient appleClient) {

		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.headerType = headerType;
		this.issuer = issuer;
		this.accessTime = accessTime;
		this.refreshTime = refreshTime;
		this.appleClient = appleClient;
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
			throw new ExpiredJwtTokenException();
		}
	}

	public String getSubjectByAppleToken(String token) {
		try {
			ApplePublicKeyResponse response = appleClient.getApplePublicKey();

			String headerOfIdentityToken = token.substring(0, token.indexOf("."));

			Map<String, String> header = new ObjectMapper().readValue(
					new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class
			);

			ApplePublicKeyResponse.Key key = response.getKeys().stream()
					.filter(k -> k.getKid().equals(header.get("kid")) && k.getAlg().equals(header.get("alg")))
					.findFirst().orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

			byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
			byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

			BigInteger n = new BigInteger(1, nBytes);
			BigInteger e = new BigInteger(1, eBytes);

			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
			KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

			return Jwts.parserBuilder()
					.setSigningKey(publicKey)
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
		} catch (ExpiredJwtException | UnsupportedEncodingException e) {
			throw new ExpiredJwtTokenException();
		} catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return "";
	}

	public void validateToken(String jwt){
		try {
			Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(jwt);
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