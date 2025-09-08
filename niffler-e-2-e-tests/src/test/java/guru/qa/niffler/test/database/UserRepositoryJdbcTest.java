package guru.qa.niffler.test.database;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Alexander
 */
public class UserRepositoryJdbcTest {

    static UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    void createUserViaJdbcRepositoryTest() {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);
        assertThat(createdUser.id(), notNullValue());
    }

    @Test
    void updateUsernameViaJdbcRepositoryTest() {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        UserJson updatedUser = usersDbClient.updateUsernameViaRepositoryJdbc(
                createdUser.id(), RandomDataUtils.randomUsername()
        );
        System.out.println("Updated User: " + updatedUser);
    }

    @Test
    void findUserByUsernameViaJdbcTest() {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        UserJson foundUser = usersDbClient.findUserViaRepositoryJdbc(createdUser.username());
        System.out.println(foundUser);
        assertThat(createdUser.username(), is(foundUser.username()));
    }

}
