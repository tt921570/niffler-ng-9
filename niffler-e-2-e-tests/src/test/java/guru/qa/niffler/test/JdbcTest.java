package guru.qa.niffler.test;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

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
            userDbClient.createUserSpringJdbc(user);
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
            UserJson createdUser = usersDbClient.createUserSpringJdbc(user);
            System.out.println("Created User: " + createdUser);
        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
            errorMessage = e.getLocalizedMessage();
        }
        assertThat(errorMessage, containsString("Key (username)=(wombat) already exists."));
    }

    @Test
    void findAllTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        spendDbClient.findAll().forEach(System.out::println);
    }
}