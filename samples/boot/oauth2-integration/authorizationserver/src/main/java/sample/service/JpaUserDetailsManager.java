package sample.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

import sample.persistence.ResourceOwnerRepository;
import sample.persistence.dto.ResourceOwner;

/**
 * @created 2021/2/3
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public class JpaUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

	@Autowired
	private ResourceOwnerRepository repo;

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		final ResourceOwner ro = toResourceOwner(user);
		ro.setPassword(newPassword);
		repo.save(ro);
		return toUserDetails(ro);
	}

	@Override
	public void createUser(UserDetails user) {
		repo.save(toResourceOwner(user));
	}

	@Override
	public void updateUser(UserDetails user) {
		repo.save(toResourceOwner(user));
	}

	@Override
	public void deleteUser(String username) {
		repo.deleteByUsername(username);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		final Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if (currentUser == null) {
			// This would indicate bad coding somewhere
			throw new AccessDeniedException(
					"Can't change password as no Authentication object found in context " + "for current user.");
		}
		final String username = currentUser.getName();
		System.out.println(LogMessage.format("Changing password for user '%s'", username));
		// If an authentication manager has been set, re-authenticate the user with the
		// supplied password.
//		if (authenticationManager != null) {
//			System.out.println(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
//			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
//		} else {
		System.out.println("No authentication manager set. Password won't be re-checked.");
//		}
		final Optional<ResourceOwner> userOpt = repo.findByUsername(username);
		Assert.state(userOpt.isPresent(), "Current user doesn't exist in database.");
		final ResourceOwner user = userOpt.get();
		user.setPassword(newPassword);
		repo.save(user);
	}

	@Override
	public boolean userExists(String username) {
		return repo.existsResourceOwnerByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repo.findByUsername(username).map(JpaUserDetailsManager::toUserDetails).orElseThrow(
				() -> new UsernameNotFoundException("username \"" + username + "\" not found"));
	}

	private static ResourceOwner toResourceOwner(UserDetails user) {
		final ResourceOwner ro = new ResourceOwner();
		ro.setUsername(user.getUsername());
		ro.setPassword(user.getPassword());
		ro.setAuthorities((Set<GrantedAuthority>) user.getAuthorities());
		ro.setAccountNonExpired(user.isAccountNonExpired());
		ro.setAccountNonLocked(user.isAccountNonLocked());
		ro.setCredentialsNonExpired(user.isCredentialsNonExpired());
		ro.setEnabled(user.isEnabled());
		return ro;
	}

	private static UserDetails toUserDetails(ResourceOwner ro) {
		return User.withUsername(ro.getUsername())
				   .password(ro.getPassword())
				   .disabled(!ro.isEnabled())
				   .authorities(ro.getAuthorities())
				   .accountExpired(!ro.isAccountNonExpired())
				   .accountLocked(!ro.isAccountNonLocked())
				   .credentialsExpired(!ro.isCredentialsNonExpired()).build();
	}
}
