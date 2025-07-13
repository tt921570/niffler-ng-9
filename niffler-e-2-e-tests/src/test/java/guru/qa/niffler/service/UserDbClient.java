package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;

/**
 * @author Alexander
 */
public class UserDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public User createUserSpringJdbc(User user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.getAuthUsername());
        authUser.setPassword(pe.encode(user.getPassword()));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authUser);

        AuthorityEntity[] authorityEntities = user.getAuthorities().stream().map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authorityEntities);

        return user;
    }

    public User createUser(User user) {
        var authUserCreation = new Databases.XaFunction<>(connection -> {
            var authUserEntity = AuthUserEntity.fromUser(user);
            var createdUserEntity = new AuthUserDaoJdbc(connection).create(authUserEntity);
            authUserEntity.getAuthorities().forEach(authorityEntity ->
                    authorityEntity.setUser(createdUserEntity));
            new AuthAuthorityDaoJdbc(connection).create(authUserEntity.getAuthorities().toArray(AuthorityEntity[]::new));
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
