package guru.qa.niffler.test;

import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.impl.UsersDbClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class JdbcTest {

    static UsersDbClient usersDbClient = new UsersDbClient();
    static SpendDbClient spendDbClient = new SpendDbClient(new SpendRepositoryJdbc());

    @Test
    void txTest() {
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-2",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        null
                )
        );

        System.out.println(spend);
    }

    @Test
    void authUserTxTest() {
        UserJson user = new UserJson(
                null,
                "fox",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );

        String errorMessage = "";
        try {
            usersDbClient.createUserSpringJdbcViaTx(user);
        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
            errorMessage = e.getLocalizedMessage();
        }

        assertThat(errorMessage, containsString("Key (username)=(fox) already exists."));
        System.out.println(user);
    }

    @Test
    void springJdbcViaTxTest() {
        UserJson user = new UserJson(
                null,
                "wombat",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        String errorMessage = "";
        try {
            UserJson createdUser = usersDbClient.createUserSpringJdbcViaTx(user);
            System.out.println("Created User: " + createdUser);
        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
            errorMessage = e.getLocalizedMessage();
        }
        assertThat(errorMessage, containsString("Key (username)=(wombat) already exists."));
    }

    @Test
    void createUserSpringJdbcWithoutTx() {
        UserJson user = new UserJson(
                null,
                "falcon",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserSpringJdbcWithoutTx(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserSpringJdbcViaTx() {
        UserJson user = new UserJson(
                null,
                "bear",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserSpringJdbcViaTx(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserJdbcWithoutTxTest() {
        UserJson user = new UserJson(
                null,
                "beaver",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserJdbcWithoutTx(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserJdbcViaTxTest() {
        UserJson user = new UserJson(
                null,
                "squirrel",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserJdbcViaTx(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void addFriendshipViaRepositoryJdbcTest() {
        UserJson requesterUser = new UserJson(
                UUID.fromString("6998e039-d10d-4dbd-a6a5-df64b220e2cb"),
                "dove",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson addresseeUser = new UserJson(
                UUID.fromString("30fbb230-8076-4c9f-b210-13750ea0c2db"),
                "goose",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        usersDbClient.createFriendshipRepositoryJdbc(requesterUser, addresseeUser);
    }

    @Test
    void addFriendshipViaRepositorySpringTest() {
        UserJson requesterUser = new UserJson(
                UUID.fromString("b815b120-6eaf-11f0-bd0f-0242ac110002"),
                "Spring Repo User",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson addresseeUser = new UserJson(
                UUID.fromString("8f6d8e58-6f01-11f0-b0eb-0242ac110002"),
                "repoUser_1",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        usersDbClient.createFriendshipRepositorySpring(requesterUser, addresseeUser);
    }

    @ValueSource(strings = {
            "valentin-10"
    })
    @ParameterizedTest
    void springJdbcTest(String uname) {
        UserJson user = usersDbClient.createUser(
                uname,
                "12345"
        );

        usersDbClient.createIncomeInvitations(user, 1);
        usersDbClient.createOutcomeInvitations(user, 1);
    }

    @ValueSource(strings = {
            "valentin-11"
    })
    @ParameterizedTest
    void createFriendsViaHibernateTest(String uname) {
        UserJson user = usersDbClient.createUser(
                uname,
                "12345"
        );

        usersDbClient.createFriends(user, 1);
    }
}