package sample;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationAttributeNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import sample.persistence.JpaOAuth2AuthorizationRepository;
import sample.persistence.JpaRegisteredClientRepository;
import sample.persistence.dto.OAuth2AuthorizationBO;
import sample.persistence.dto.RegisteredClientBO;

/**
 * @created 2021/1/15
 * @author tom.lin(tom.lin @ linecorp.com)
 */
@RestController
public class TestController {

	@Autowired
	JpaRegisteredClientRepository repo;

	@Autowired
	JpaOAuth2AuthorizationRepository oauthRepo;

//	@GetMapping("")
//	public String getClientById(@RequestParam String id) {
//		RegisteredClient r = repo.findByClientId(id);
//		return r.toString();
//	}

//	@GetMapping("/test/get")
//	public String getOauthClientById(@RequestParam String id) {
//		OAuth2AuthorizationBO o = new OAuth2AuthorizationBO();
//		Set<String> scopes = new HashSet<>();
//		scopes.add("a");
//		scopes.add("b");
//		Map<Integer, String> map = new HashMap<>();
//		map.put(1, "a");
//		map.put(2, "b");
//		final byte[] obj = SerializationUtils.serialize(scopes);
//		o.setObj(obj);
//		o.setMap(SerializationUtils.serialize(map));
//		o.setId(Long.valueOf(id));
//		o.setRegisteredClientId("client" + id);
//		oauthRepo.save(o);
//		OAuth2AuthorizationBO r = oauthRepo.findByRegisteredClientId(o.getRegisteredClientId());
//		Object s = SerializationUtils.deserialize(r.getObj());
//		Map<Integer, String> m = (Map<Integer, String>) SerializationUtils.deserialize(r.getMap());
//		System.out.println("scope: " + s);
//		System.out.println("MAP: " + m);
//		return r.toString();
//	}

	/*@GetMapping("/test/att")
	public String testAtt() {
		OAuth2AuthorizationBO o = new OAuth2AuthorizationBO();
		o.setRegisteredClientId("client-" + Instant.now());
		Map<String, Object> att = new HashMap<>();
		att.put("state", "mystate:" + Instant.now());

		OAuth2Tokens tokens = OAuth2Tokens.builder().token(new OAuth2AccessToken(TokenType.BEARER, "token",
																				 Instant.now(), Instant.MAX)).build();
//		att.put("token", tokens);
		OAuth2AuthorizationRequest request = OAuth2AuthorizationRequest.authorizationCode().authorizationUri(
				"http://google.com").clientId("clientId").build();
		att.put("request", request);
		HashSet<String> scopes = new HashSet<String>();
		scopes.add("A");
		scopes.add("B");
		scopes.add("C");
		att.put("scopes", scopes);
		o.setAttributes(att);
		o.setTokens(tokens);
		oauthRepo.save(o);
		OAuth2AuthorizationBO r = oauthRepo.findByRegisteredClientId(o.getRegisteredClientId());
//		Map<String, Object> atts = r.getAttributes();
//		atts.forEach((k, v) -> System.out.println("key:" + k + ", value:" + v));
//		System.out.println("state = " + (String) r.getAttribute("state"));
		System.out.println("attributes = " + r.getAttributes());
		System.out.println("state = " + r.getAttribute("state"));
		System.out.println("request = " + r.getAttribute("request"));
		System.out.println("scopes = " + r.getAttribute("scopes"));
		System.out.println("tokens = " + r.getTokens());
		return r.toString();
	}*/

	@GetMapping("/test/client/add")
	public String addClient() {
		final RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
																  .clientId("my-client-02")
																  .clientSecret("secret")
																  .clientAuthenticationMethod(
																		  ClientAuthenticationMethod.POST)
																  .authorizationGrantType(
																		  AuthorizationGrantType.AUTHORIZATION_CODE)
																  .authorizationGrantType(
																		  AuthorizationGrantType.REFRESH_TOKEN)
																  .authorizationGrantType(
																		  AuthorizationGrantType.CLIENT_CREDENTIALS)
																  .redirectUri(
																		  "http://localhost:8080/login/oauth2/code/messaging-client-oidc")
																  .redirectUri("http://localhost:8080/authorized")
																  .scope(OidcScopes.OPENID)
																  .scope("message.read")
																  .scope("message.write")
																  .clientSettings(clientSettings -> clientSettings
																		  .requireUserConsent(false))
																  .build();
		RegisteredClientBO result = repo.save(new RegisteredClientBO(registeredClient));
		return result.toString();
	}

	@GetMapping("/test/scope")
	public String getScope() {
//		oauthRepo.findByToken("", TokenType.AUTHORIZATION_CODE);
		OAuth2AuthorizationBO bo = oauthRepo.findFirstByAuthorizationCode(
				"ge7-QS7xEMJgoSs7WNujyojn2vGtsnrqKeQ0jGUJXeQbyOEhKQIXXGkh4zg1hOmqRz8V4jgh_Njp4126OBkco_jqw58HDFZw_YROSYEKf04U5z7dHNC5exBPOcn4qgdH")
											.get();
		OAuth2Authorization result = OAuth2Authorization.from(bo.getOAuth2Authorization()).build();
		final Set<String> authorizedScopes = result.getAttribute(OAuth2AuthorizationAttributeNames.AUTHORIZED_SCOPES);
		System.out.println("authorizedScopes=" + authorizedScopes);
		return authorizedScopes.toString();
	}

//	@GetMapping("/test/scope/add")
//	public String getOauthClientById() {
//		OAuth2AuthorizationBO o = new OAuth2AuthorizationBO();
//		Map<String, Object> att = new HashMap<>();
//		Set<String> scopes = new HashSet<>();
//		scopes.add("a");
//		scopes.add("b");
//		att.put(OAuth2AuthorizationAttributeNames.AUTHORIZED_SCOPES, scopes);
//		final String clientId = "client-" + Instant.now();
//		o.setRegisteredClientId(clientId);
//		final String principalName = "principalName-" + UUID.randomUUID();
//		o.setPrincipalName(principalName);
//		o.setAttributes(att);
//		RegisteredClient client = RegisteredClient.withId(clientId).clientId(clientId).authorizationGrantType(
//				AuthorizationGrantType.CLIENT_CREDENTIALS).build();
//		OAuth2Authorization auth = OAuth2Authorization.withRegisteredClient(client).principalName(principalName)
//													  .attributes(at -> at.putAll(att))
//													  .build();
//		o.setOAuth2Authorization(auth);
//
//		oauthRepo.save(o);
//		OAuth2AuthorizationBO bo = oauthRepo.findByRegisteredClientIdAndPrincipalName(clientId, principalName).get();
//		OAuth2Authorization result = OAuth2Authorization.from(bo.getOAuth2Authorization()).build();
//		System.out.println("bo.getAttributes() = " + bo.getAttributes());
//		System.out.println("result.getAttributes() = " + result.getAttributes());
//		System.out.println("bo scopes = " + bo.getAttribute(OAuth2AuthorizationAttributeNames.AUTHORIZED_SCOPES));
//		System.out.println(
//				"result scopes = " + result.getAttribute(OAuth2AuthorizationAttributeNames.AUTHORIZED_SCOPES));
//		final Set<String> deserializeScope = result.getAttribute(OAuth2AuthorizationAttributeNames.AUTHORIZED_SCOPES);
//		System.out.println("deserializeScope = " + deserializeScope);
//		return result.toString();
//	}
}
