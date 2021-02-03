package sample.persistence.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationAttributeNames;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AuthorizationCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sample.persistence.dto.OAuth2AuthorizationBO.OAuth2AuthorizationId;
import sample.persistence.dto.converter.HashMapConverter;
import sample.persistence.dto.converter.OAuth2AuthorizationConverter;

/**
 * @created 2021/1/22
 * @author tom.lin(tom.lin @ linecorp.com)
 */
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "oauth_authorization")
@IdClass(OAuth2AuthorizationId.class)
@NoArgsConstructor
public class OAuth2AuthorizationBO {

	@Id
	@Column(name = "registered_client_id")
	private String registeredClientId;

	@Id
	@Column(name = "principal_name")
	private String principalName;

	@Column(name = "oauth2_authorization")
	@Lob
	@Convert(converter = OAuth2AuthorizationConverter.class)
	private OAuth2Authorization oAuth2Authorization;

	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> attributes;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "authorization_code")
	private String authorizationCode;

	@Column(name = "state")
	private String state;

	@PreUpdate
	@PrePersist
	public void updateTokens() {
		accessToken = Optional.ofNullable(oAuth2Authorization.getTokens().getAccessToken())
							  .map(AbstractOAuth2Token::getTokenValue).orElse(null);
		refreshToken = Optional.ofNullable(oAuth2Authorization.getTokens().getRefreshToken())
							   .map(AbstractOAuth2Token::getTokenValue).orElse(null);
		authorizationCode = Optional.ofNullable(oAuth2Authorization.getTokens().getToken(OAuth2AuthorizationCode.class))
									.map(AbstractOAuth2Token::getTokenValue).orElse(null);
		state = oAuth2Authorization.getAttribute(OAuth2AuthorizationAttributeNames.STATE);
	}

	public OAuth2AuthorizationBO(OAuth2Authorization authorization) {
		registeredClientId = authorization.getRegisteredClientId();
		principalName = authorization.getPrincipalName();
		oAuth2Authorization = authorization;
		attributes = authorization.getAttributes();
		updateTokens();
	}

	@NoArgsConstructor
	public static class OAuth2AuthorizationId implements Serializable {
		private static final long serialVersionUID = 4821243742441268482L;
		private String registeredClientId;
		private String principalName;

		public OAuth2AuthorizationId(String registeredClientId, String principalName) {
			this.registeredClientId = registeredClientId;
			this.principalName = principalName;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) { return true; }
			if (o == null || getClass() != o.getClass()) { return false; }
			final OAuth2AuthorizationId that = (OAuth2AuthorizationId) o;
			return Objects.equals(registeredClientId, that.registeredClientId) &&
				   Objects.equals(principalName, that.principalName);
		}

		@Override
		public int hashCode() {
			return Objects.hash(registeredClientId, principalName);
		}
	}
}
