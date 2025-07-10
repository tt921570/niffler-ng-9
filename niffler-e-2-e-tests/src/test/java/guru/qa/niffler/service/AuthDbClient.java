package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.data.Databases.transaction;

/**
 * @author Alexander
 */
public class AuthDbClient {

    private static final Config CFG = Config.getInstance();

    public UserEntity createAuthUser(UserEntity userEntity) {
        return transaction(connection -> {
                    var createsUserEntity = new AuthUserDaoJdbc(connection).create(userEntity);
                    List<AuthorityEntity> newAuthorities = new ArrayList<>();
                    userEntity.getAuthorities().forEach(authorityEntity -> {
                        authorityEntity.setUser(createsUserEntity);
                        newAuthorities.add(new AuthAuthorityDaoJdbc(connection).create(authorityEntity));
                    });
                    createsUserEntity.setAuthorities(newAuthorities);
                    return createsUserEntity;
                },
                CFG.authJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE);
    }

}
