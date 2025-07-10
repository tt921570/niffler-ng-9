package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.data.Databases.xaTransaction;

/**
 * @author Alexander
 */
public class UserDbClient {

    private static final Config CFG = Config.getInstance();

    public User createUser(User user) {
        var authUserCreation = new Databases.XaFunction<>(connection -> {
            var authUserEntity = AuthUserEntity.fromUser(user);
            var createdUserEntity = new AuthUserDaoJdbc(connection).create(authUserEntity);
            List<AuthorityEntity> newAuthorities = new ArrayList<>();
            authUserEntity.getAuthorities().forEach(authorityEntity -> {
                authorityEntity.setUser(createdUserEntity);
                newAuthorities.add(new AuthAuthorityDaoJdbc(connection).create(authorityEntity));
            });
            createdUserEntity.setAuthorities(newAuthorities);
            user.setAuthId(createdUserEntity.getId());
            return user;
        },
                CFG.authJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE
        );

        var userdataCreation = new Databases.XaFunction<>(connection -> {
            var userdataEntity = UserdataUserEntity.fromUser(user);
            var createdUserdataUserEntity = new UserdataUserDaoJdbc(connection).createUser(userdataEntity);
            user.setDataId(createdUserdataUserEntity.getId());
            return user;
        },
                CFG.userdataJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE
        );

        xaTransaction(authUserCreation, userdataCreation);
        return user;
    }

}
