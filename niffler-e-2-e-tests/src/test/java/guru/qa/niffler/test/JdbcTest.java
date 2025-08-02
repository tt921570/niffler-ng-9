package guru.qa.niffler.test;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Disabled;
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

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

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
    void chainedTxTest() {
        UserJson user = new UserJson(
                null,
                "whale",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserViaChainedTx(user);
        System.out.println("Created User: " + createdUser);
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
    void createUserViaJdbcRepository() {
        UserJson user = new UserJson(
                null,
                "repoUser_1",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUser(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void createUserViaSpringRepository() {
        UserJson user = new UserJson(
                null,
                "Spring Repo User 1",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
        UserJson createdUser = usersDbClient.createUserViaRepositorySpring(user);
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void findUserById() {
        UserJson user = usersDbClient.findUserById(UUID
                .fromString("d149c9c5-f8a0-42fa-823e-c0d9f9f94797"));
        System.out.println(user);
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

    @Test
    void findAllTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        spendDbClient.findAll().forEach(System.out::println);
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

        usersDbClient.addIncomeInvitation(user, 1);
        usersDbClient.addOutcomeInvitation(user, 1);
    }
}