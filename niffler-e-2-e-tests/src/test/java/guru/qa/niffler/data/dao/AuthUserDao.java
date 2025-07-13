package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander
 */
public interface AuthUserDao {

    AuthUserEntity create(AuthUserEntity authUserEntity);

    Optional<AuthUserEntity> findUserByUserName(String username);

    Optional<AuthUserEntity> findUserById(UUID id);

    List<AuthUserEntity> findUsersByActiveProperties(boolean isEnabled, boolean isAccountNonExpired,
                                                     boolean isAccountNonLocked, boolean isCredentialsNonExpired);

    void deleteUser(AuthUserEntity authUserEntity);

    Optional<AuthUserEntity> findById(UUID id);
}
