package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander
 */
public interface AuthUserDao {

    UserEntity create(UserEntity userEntity);

    Optional<UserEntity> findUserByUserName(String username);

    Optional<UserEntity> findUserById(UUID id);

    List<UserEntity> findUsersByActiveProperties(boolean isEnabled, boolean isAccountNonExpired,
                                                 boolean isAccountNonLocked, boolean isCredentialsNonExpired);

    void deleteUser(UserEntity userEntity);
}
