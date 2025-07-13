package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander
 */
public interface AuthAuthorityDao {

    void create(AuthorityEntity... authorities);

    Optional<AuthUserEntity> findById(UUID id);

    List<AuthorityEntity> findAuthoritiesByUserId(UUID userId);

    void deleteAuthority(AuthorityEntity authorityEntity);

    void deleteAllAuthoritiesByUserId(UUID userId);
}
