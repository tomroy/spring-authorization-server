package sample.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.persistence.dto.RegisteredClientBO;

/**
 * @created 2021/1/15
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public interface JpaRegisteredClientRepository extends JpaRepository<RegisteredClientBO, String> {
	Optional<RegisteredClientBO> findByClientId(String clientId);
}
