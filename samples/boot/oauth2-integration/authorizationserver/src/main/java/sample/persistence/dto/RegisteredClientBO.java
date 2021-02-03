package sample.persistence.dto;

import java.time.Duration;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sample.persistence.dto.converter.AuthorizationGrantTypeSetConverter;
import sample.persistence.dto.converter.ClientAuthenticationMethodConverter;
import sample.persistence.dto.converter.DurationConverter;
import sample.persistence.dto.converter.StringSetConverter;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "oauth_client_details")
@NoArgsConstructor
public class RegisteredClientBO {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "client_secret")
	private String clientSecret;

	@Convert(converter = ClientAuthenticationMethodConverter.class)
	@Column(name = "client_authentication_method")
	private Set<ClientAuthenticationMethod> clientAuthenticationMethods;

	@Convert(converter = AuthorizationGrantTypeSetConverter.class)
	@Column(name = "authorized_grant_types")
	private Set<AuthorizationGrantType> authorizationGrantTypes;

	@Convert(converter = StringSetConverter.class)
	@Column(name = "redirect_uri")
	private Set<String> redirectUris;

	@Convert(converter = StringSetConverter.class)
	@Column(name = "scope")
	private Set<String> scopes;

	@Column(name = "require_proof_key")
	private Boolean requireProofKey;

	@Column(name = "require_user_consent")
	private Boolean requireUserConsent;

	@Convert(converter = DurationConverter.class)
	@Column(name = "access_token_time_to_live")
	private Duration accessTokenTimeToLiveInSec;

	@Column(name = "reuse_refresh_tokens")
	private Boolean reuseRefreshTokens;

	@Convert(converter = DurationConverter.class)
	@Column(name = "refresh_token_time_to_live")
	private Duration refreshTokenTimeToLiveInSec;

	//	@Column(name = "client_settings")
//	@Convert(converter = ClientSettingsConverter.class)
	@Transient
	private ClientSettings clientSettings;
	//
//	@Column(name = "token_settings")
//	@Convert(converter = TokenSettingsConverter.class)
	@Transient
	private TokenSettings tokenSettings;

	public ClientSettings getClientSettings() {
		if (null == clientSettings) {
			final ClientSettings cs = new ClientSettings();
			setClientSettings(cs.requireProofKey(requireProofKey).requireUserConsent(requireUserConsent));
		}
		return clientSettings;
	}

	private void setClientSettings(ClientSettings cs) {
		clientSettings = cs;
		requireProofKey = cs.requireProofKey();
		requireUserConsent = cs.requireUserConsent();
	}

	public TokenSettings getTokenSettings() {
		if (null == tokenSettings) {
			final TokenSettings ts = new TokenSettings();
			setTokenSettings(ts.accessTokenTimeToLive(accessTokenTimeToLiveInSec)
							   .reuseRefreshTokens(reuseRefreshTokens)
							   .refreshTokenTimeToLive(refreshTokenTimeToLiveInSec));
		}
		return tokenSettings;
	}

	private void setTokenSettings(TokenSettings ts) {
		tokenSettings = ts;
		accessTokenTimeToLiveInSec = ts.accessTokenTimeToLive();
		refreshTokenTimeToLiveInSec = ts.refreshTokenTimeToLive();
		reuseRefreshTokens = ts.reuseRefreshTokens();
	}

	public RegisteredClientBO(RegisteredClient from) {
		id = from.getId();
		clientId = from.getClientId();
		clientSecret = from.getClientSecret();
		clientAuthenticationMethods = from.getClientAuthenticationMethods();
		authorizationGrantTypes = from.getAuthorizationGrantTypes();
		redirectUris = from.getRedirectUris();
		scopes = from.getScopes();
		clientSettings = from.getClientSettings();
		setClientSettings(from.getClientSettings());
		tokenSettings = from.getTokenSettings();
		setTokenSettings(from.getTokenSettings());
	}

	public RegisteredClient toRegisteredClient() {
		return RegisteredClient.withId(id)
							   .clientId(clientId)
							   .clientSecret(clientSecret)
							   .clientAuthenticationMethods(m -> m.addAll(clientAuthenticationMethods))
							   .authorizationGrantTypes(a -> a.addAll(authorizationGrantTypes))
							   .redirectUris(r -> r.addAll(redirectUris))
							   .scopes(s -> s.addAll(scopes))
							   .clientSettings(cs -> cs.requireProofKey(getClientSettings().requireProofKey()))
							   .clientSettings(cs -> cs.requireUserConsent(getClientSettings().requireUserConsent()))
							   .tokenSettings(
									   ts -> ts.accessTokenTimeToLive(getTokenSettings().accessTokenTimeToLive()))
							   .tokenSettings(
									   ts -> ts.refreshTokenTimeToLive(getTokenSettings().refreshTokenTimeToLive()))
							   .tokenSettings(ts -> ts.reuseRefreshTokens(getTokenSettings().reuseRefreshTokens()))
							   .build();
	}
}

