package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander
 */
public interface AuthAuthorityDao {

    void create(AuthorityEntity... authorities);

    Optional<AuthorityEntity> findById(UUID id);

    List<AuthorityEntity> findAuthoritiesByUserId(UUID userId);

    List<AuthorityEntity> findAll();

    void deleteAuthority(AuthorityEntity authorityEntity);

    void deleteAllAuthoritiesByUserId(UUID userId);
}
