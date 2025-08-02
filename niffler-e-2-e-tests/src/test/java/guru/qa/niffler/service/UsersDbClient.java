package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.*;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

/**
 * @author Alexander
 */
public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
    private final AuthUserDao authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
    private final AuthAuthorityDao authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
    private final AuthUserRepository authUserRepositoryJdbc = new AuthUserRepositoryJdbc();
    private final AuthUserRepository authUserRepositorySpring = new AuthUserRepositorySpring();
    private final AuthUserRepository authUserRepositoryHibernate = new AuthUserRepositoryHibernate();
    private final UserdataUserDao udUserDaoJdbc = new UserdataUserDaoJdbc();
    private final UserdataUserDao udUserDaoSpringJdbc = new UserdataUserDaoSpringJdbc();
    private final UserdataUserRepository userdataUserRepositoryJdbc = new UserdataUserRepositoryJdbc();
    private final UserdataUserRepository userdataUserRepositorySpring = new UserdataUserRepositorySpring();
    private final UserdataUserRepository userdataUserRepositoryHibernate = new UserdataUserRepositoryHibernate();

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

    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    authUser.setAuthorities(
                            Arrays.stream(Authority.values()).map(
                                    e -> {
                                        AuthorityEntity ae = new AuthorityEntity();
                                        ae.setUser(authUser);
                                        ae.setAuthority(e);
                                        return ae;
                                    }
                            ).toList()
                    );
                    authUserRepositoryJdbc.create(authUser);
                    return UserJson.fromEntity(
                            userdataUserRepositoryJdbc.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public UserJson createUserViaRepositorySpring(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    authUser.setAuthorities(
                            Arrays.stream(Authority.values()).map(
                                    e -> {
                                        AuthorityEntity ae = new AuthorityEntity();
                                        ae.setUser(authUser);
                                        ae.setAuthority(e);
                                        return ae;
                                    }
                            ).toList()
                    );
                    authUserRepositorySpring.create(authUser);
                    return UserJson.fromEntity(
                            userdataUserRepositorySpring.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
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
                    ae.setUser(createdAuthUser);
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

    public UserJson findUserById(UUID id) {
        return xaTransactionTemplate.execute(() -> userdataUserRepositoryJdbc.findById(id)
                .map(userEntity -> UserJson.fromEntity(userEntity, null))
                .orElseThrow());
    }

    public void createFriendshipRepositoryJdbc(UserJson requesterUser, UserJson addresseeUser) {
        createFriendship(userdataUserRepositoryJdbc, requesterUser, addresseeUser);
    }

    public void createFriendshipRepositorySpring(UserJson requesterUser, UserJson addresseeUser) {
        createFriendship(userdataUserRepositorySpring, requesterUser, addresseeUser);
    }

    private void createFriendship(UserdataUserRepository repository, UserJson requesterUser, UserJson addresseeUser) {
        xaTransactionTemplate.execute(() -> {
            repository.addFriend(
                    UserEntity.fromJson(requesterUser), UserEntity.fromJson(addresseeUser)
            );
            return null;
        });
    }

    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepositoryHibernate.create(authUser);
                    return UserJson.fromEntity(
                            userdataUserRepositoryHibernate.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, "12345");
                            authUserRepositoryHibernate.create(authUser);
                            UserEntity adressee = userdataUserRepositoryHibernate.create(userEntity(username));
                            userdataUserRepositoryHibernate.addIncomeInvitation(targetEntity, adressee);
                            return null;
                        }
                );
            }
        }
    }

    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, "12345");
                            authUserRepositoryHibernate.create(authUser);
                            UserEntity adressee = userdataUserRepositoryHibernate.create(userEntity(username));
                            userdataUserRepositoryHibernate.addOutcomeInvitation(targetEntity, adressee);
                            return null;
                        }
                );
            }
        }
    }

    void addFriend(UserJson targetUser, int count) {

    }


    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}
