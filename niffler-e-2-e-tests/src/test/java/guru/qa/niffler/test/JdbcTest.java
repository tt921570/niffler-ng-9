package guru.qa.niffler.test;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.User;
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

        User user = User.builder()
                .authUsername("fox")
                .userdataUsername("buddy")
                .password("fox")
                .build();

        String errorMessage = "";
        try {
            userDbClient.createUser(user);
        } catch (RuntimeException e) {
            System.out.println(e.getLocalizedMessage());
            errorMessage = e.getLocalizedMessage();
        }

        assertThat(errorMessage, containsString("Key (username)=(buddy) already exists."));
        System.out.println(user);
    }

    @Test
    void springJdbcTest() {
        UserDbClient usersDbClient = new UserDbClient();
        User user = usersDbClient.createUserSpringJdbc(
                User.builder()
                        .authUsername("wombat")
                        .userdataUsername("wombat")
                        .password("wombat")
                        .build()
        );
        System.out.println(user);
    }
}