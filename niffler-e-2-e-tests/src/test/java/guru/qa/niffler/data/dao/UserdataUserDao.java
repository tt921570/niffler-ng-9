package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander
 */
public interface UserdataUserDao {
    UserdataUserEntity createUser(UserdataUserEntity user);

    Optional<UserdataUserEntity> findById(UUID id);

    Optional<UserdataUserEntity> findByUserName(String username);

    void delete(UserdataUserEntity user);
}
