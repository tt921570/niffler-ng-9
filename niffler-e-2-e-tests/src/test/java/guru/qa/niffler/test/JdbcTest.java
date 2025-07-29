package guru.qa.niffler.test;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@Disabled
public class JdbcTest {

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
        UserDbClient userDbClient = new UserDbClient();

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
            userDbClient.createUserSpringJdbcViaTx(user);
        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
            errorMessage = e.getLocalizedMessage();
        }

        assertThat(errorMessage, containsString("Key (username)=(fox) already exists."));
        System.out.println(user);
    }

    @Test
    void springJdbcTest() {
        UserDbClient usersDbClient = new UserDbClient();
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
        UserDbClient usersDbClient = new UserDbClient();
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
        UserDbClient usersDbClient = new UserDbClient();
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
        UserDbClient usersDbClient = new UserDbClient();
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
        UserDbClient usersDbClient = new UserDbClient();
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
        UserDbClient usersDbClient = new UserDbClient();
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
    void findAllTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        spendDbClient.findAll().forEach(System.out::println);
    }
}