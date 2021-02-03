package sample.provider;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * @created 2021/1/13
 * @author tom.lin(tom.lin @ linecorp.com)
 */
@Component
public class TestProvider implements AuthenticationProvider {
	private final String adminName = "root";
	private final String adminPassword = "root";
	private final List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROOT"));

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (isMatch(authentication)) {
			User user = new User(authentication.getName(), authentication.getCredentials().toString(), authorities);
			return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), authorities);
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	private boolean isMatch(Authentication authentication) {
		if (authentication.getName().equals(adminName) && authentication.getCredentials().equals(adminPassword)) {
			return true;
		} else { return false; }
	}
}
