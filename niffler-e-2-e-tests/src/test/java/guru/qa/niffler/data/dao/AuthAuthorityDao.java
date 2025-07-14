package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author Alexander
 */
public interface AuthAuthorityDao {

    AuthorityEntity create(AuthorityEntity authorityEntity);

    List<AuthorityEntity> findAuthoritiesByUserId(UUID userId);

    void deleteAuthority(AuthorityEntity authorityEntity);

    void deleteAllAuthoritiesByUserId(UUID userId);
}
