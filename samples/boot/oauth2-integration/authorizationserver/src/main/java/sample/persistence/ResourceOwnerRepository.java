package sample.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.persistence.dto.ResourceOwner;

/**
 * @created 2021/2/3
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public interface ResourceOwnerRepository extends JpaRepository<ResourceOwner, Long> {
	Optional<ResourceOwner> findByUsername(String username);

	boolean existsResourceOwnerByUsername(String username);

	void deleteByUsername(String username);
}
