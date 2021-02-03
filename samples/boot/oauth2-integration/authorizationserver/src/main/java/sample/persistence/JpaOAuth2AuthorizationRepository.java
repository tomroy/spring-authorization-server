package sample.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.persistence.dto.OAuth2AuthorizationBO;
import sample.persistence.dto.OAuth2AuthorizationBO.OAuth2AuthorizationId;

/**
 * @created 2021/1/21
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public interface JpaOAuth2AuthorizationRepository extends JpaRepository<OAuth2AuthorizationBO, OAuth2AuthorizationId> {

	Optional<OAuth2AuthorizationBO> findByRegisteredClientIdAndPrincipalName(String clientId, String principalName);

	Optional<OAuth2AuthorizationBO> findFirstByState(String state);

	Optional<OAuth2AuthorizationBO> findFirstByAccessToken(String accessToken);

	Optional<OAuth2AuthorizationBO> findFirstByRefreshToken(String refreshToken);

	Optional<OAuth2AuthorizationBO> findFirstByAuthorizationCode(String code);

	void deleteByRegisteredClientIdAndPrincipalName(String clientId, String principalName);
}
