package sample.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationAttributeNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.TokenType;
import org.springframework.util.Assert;

import sample.persistence.JpaOAuth2AuthorizationRepository;
import sample.persistence.dto.OAuth2AuthorizationBO;

/**
 * @created 2021/2/3
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

	@Autowired
	JpaOAuth2AuthorizationRepository repo;

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		repo.save(new OAuth2AuthorizationBO(authorization));
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		repo.deleteByRegisteredClientIdAndPrincipalName(authorization.getRegisteredClientId(),
														authorization.getPrincipalName());
	}

	@Override
	public OAuth2Authorization findByToken(String token, TokenType tokenType) {
		if (tokenType == null) {
			OAuth2Authorization authorization = null;
			OAuth2AuthorizationBO c = repo.findFirstByState(token).map(Optional::of).orElseGet(
					() -> repo.findFirstByAccessToken(token)).map(Optional::of).orElseGet(
					() -> repo.findFirstByAuthorizationCode(token)).map(Optional::of).orElseGet(
					() -> repo.findFirstByRefreshToken(token)).orElse(null);
			if (c != null) {
				authorization = convertBO(c);
			}
			return authorization;
		}

		if (OAuth2AuthorizationAttributeNames.STATE.equals(tokenType.getValue())) {
			return repo.findFirstByState(token).map(this::convertBO).orElse(null);
		}

		if (TokenType.AUTHORIZATION_CODE.equals(tokenType)) {
			return repo.findFirstByAuthorizationCode(token).map(this::convertBO).orElse(null);
		}

		if (TokenType.ACCESS_TOKEN.equals(tokenType)) {
			return repo.findFirstByAccessToken(token).map(this::convertBO).orElse(null);
		}

		if (TokenType.REFRESH_TOKEN.equals(tokenType)) {
			return repo.findFirstByRefreshToken(token).map(this::convertBO).orElse(null);
		}

		return null;
	}

	private OAuth2Authorization convertBO(OAuth2AuthorizationBO bo) {
		return OAuth2Authorization.from(bo.getOAuth2Authorization()).build();
	}
}
