package com.yapp.pet.global.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthentication extends AbstractAuthenticationToken {

	private final Object principal;
	private final String credentials;

	public JwtAuthentication(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		super.setAuthenticated(true);
		this.principal = principal;
		this.credentials = credentials;
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

}
