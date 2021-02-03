package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import sample.persistence.JpaRegisteredClientRepository;
import sample.persistence.dto.RegisteredClientBO;

/**
 * @created 2021/2/3
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public class RegisteredClientService implements RegisteredClientRepository {

	@Autowired
	private JpaRegisteredClientRepository repo;

	@Override
	public RegisteredClient findById(String id) {
		return repo.findById(id).map(RegisteredClientBO::toRegisteredClient).orElse(null);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		return repo.findByClientId(clientId).map(RegisteredClientBO::toRegisteredClient).orElse(null);
	}
}
