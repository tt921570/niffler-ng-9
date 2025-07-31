package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.Authority;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

/**
 * @author Alexander
 */
public class UserDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
    private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
    private final UserdataUserDao udUserDaoSpringJdbc = new UserdataUserDaoSpringJdbc();
    private final UserdataUserDao udUserDaoJdbc = new UserdataUserDaoJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final JdbcTransactionManager authTxManager = new JdbcTransactionManager(
            DataSources.driverManagerDataSource(CFG.authJdbcUrl())
    );
    private final JdbcTransactionManager userDataTxManager = new JdbcTransactionManager(
            DataSources.driverManagerDataSource(CFG.userdataJdbcUrl())
    );
    private final TransactionTemplate chainedTxTemplate = new TransactionTemplate(
            new ChainedTransactionManager(authTxManager, userDataTxManager)
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUserSpringJdbcWithoutTx(UserJson user) {
        return createUser(user, authUserDaoSpringJdbc, authAuthorityDaoSpringJdbc, udUserDaoSpringJdbc);
    }

    public UserJson createUserSpringJdbcViaTx(UserJson user) {
        return xaTransactionTemplate.execute(() ->
                createUser(user, authUserDaoSpringJdbc, authAuthorityDaoSpringJdbc, udUserDaoSpringJdbc)
        );
    }

    public UserJson createUserJdbcWithoutTx(UserJson user) {
        return createUser(user, authUserDaoJdbc, authAuthorityDaoJdbc, udUserDaoJdbc);
    }

    public UserJson createUserJdbcViaTx(UserJson user) {
        return xaTransactionTemplate.execute(() ->
                createUser(user, authUserDaoJdbc, authAuthorityDaoJdbc, udUserDaoJdbc)
        );
    }

    public UserJson createUserViaChainedTx(UserJson user) {
        return chainedTxTemplate.execute(status ->
                createUser(user, authUserDaoSpringJdbc, authAuthorityDaoJdbc, udUserDaoJdbc));
    }

    private UserJson createUser(UserJson user, AuthUserDao authUserDao, AuthAuthorityDao authAuthorityDao,
                                UserdataUserDao userdataUserDao) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authAuthorityDao.create(authorityEntities);
        return UserJson.fromEntity(
                userdataUserDao.create(UserEntity.fromJson(user)),
                null
        );
    }
}
