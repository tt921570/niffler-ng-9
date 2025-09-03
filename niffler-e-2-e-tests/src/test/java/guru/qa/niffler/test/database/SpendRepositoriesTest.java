package guru.qa.niffler.test.database;

import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.SpendRepositorySpring;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author Alexander
 */
public class SpendRepositoriesTest {

    static final UsersDbClient usersDbClient = new UsersDbClient();

    @ParameterizedTest(name = "Create Spend via {0} repository")
    @MethodSource("testArgsSource")
    void createSpendTest(String repositoryType, SpendClient spendClient) {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        SpendJson spend = spendClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                RandomDataUtils.randomCategoryName(),
                                createdUser.username(),
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        RandomDataUtils.randomSentence(2),
                        createdUser.username()
                )
        );

        System.out.println(spend);
        assertThat(spend.id(), notNullValue());
    }

    @ParameterizedTest(name = "Update Spend via {0} repository")
    @MethodSource("testArgsSource")
    void updateSpendTest(String repositoryType, SpendClient spendClient) {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        String categoryName = RandomDataUtils.randomCategoryName();
        String spendDescription = RandomDataUtils.randomSentence(2);
        SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        categoryName,
                        createdUser.username(),
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                spendDescription,
                createdUser.username()
        );

        SpendJson createdSpend = spendClient.createSpend(spend);
        System.out.println("Created Spend: " + createdSpend);

        SpendJson spendWithNewAmount = new SpendJson(
                createdSpend.id(),
                createdSpend.spendDate(),
                createdSpend.category(),
                createdSpend.currency(),
                1234.0,
                createdSpend.description(),
                createdSpend.username()
        );

        SpendJson updatedSpend = spendClient.updateSpend(spendWithNewAmount);
        System.out.println("Updated Spend: " + updatedSpend);
        assertThat(updatedSpend.amount(), is(1234.0));
    }

    @ParameterizedTest(name = "Find Category by id via {0} repository")
    @MethodSource("testArgsSource")
    void findCategoryByIdTest(String repositoryType, SpendClient spendClient) {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        String categoryName = RandomDataUtils.randomCategoryName();
        String spendDescription = RandomDataUtils.randomSentence(2);
        SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        categoryName,
                        createdUser.username(),
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                spendDescription,
                createdUser.username()
        );

        SpendJson createdSpend = spendClient.createSpend(spend);
        System.out.println("Created Spend: " + createdSpend);
        System.out.println("Created Category: " + createdSpend.category());

        CategoryJson foundByIdCategory = spendClient.findCategoryById(createdSpend.category().id());
        System.out.println("Found by id Category: " + foundByIdCategory);
        assertThat(foundByIdCategory.name(), is(createdSpend.category().name()));
    }

    @ParameterizedTest(name = "Find Category by username and category name via {0} repository")
    @MethodSource("testArgsSource")
    void findCategoryByUsernameAndCategoryNameTest(String repositoryType, SpendClient spendClient) {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        String categoryName = RandomDataUtils.randomCategoryName();
        String spendDescription = RandomDataUtils.randomSentence(2);
        SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        categoryName,
                        createdUser.username(),
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                spendDescription,
                createdUser.username()
        );

        SpendJson createdSpend = spendClient.createSpend(spend);
        System.out.println("Created Spend: " + createdSpend);
        System.out.println("Created Category: " + createdSpend.category());

        CategoryJson foundByUsernameAnaNameCategory = spendClient.findCategoryByUsernameAndCategoryName(
                createdSpend.category().username(), createdSpend.category().name());
        System.out.println("Found by username and name Category: " + foundByUsernameAnaNameCategory);
        assertThat(foundByUsernameAnaNameCategory.id(), is(createdSpend.category().id()));
    }

    @ParameterizedTest(name = "Find Spend by Id via {0} repository")
    @MethodSource("testArgsSource")
    void findSpendByIdTest(String repositoryType, SpendClient spendClient) {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        String categoryName = RandomDataUtils.randomCategoryName();
        String spendDescription = RandomDataUtils.randomSentence(2);
        SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        categoryName,
                        createdUser.username(),
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                spendDescription,
                createdUser.username()
        );

        SpendJson createdSpend = spendClient.createSpend(spend);
        System.out.println("Created Spend: " + createdSpend);
        System.out.println("Created Category: " + createdSpend.category());

        SpendJson foundByIdSpend = spendClient.findSpendById(createdSpend.id());
        System.out.println("Found by id Spend: " + foundByIdSpend);
        assertThat(foundByIdSpend.description(), is(createdSpend.description()));
    }

    @ParameterizedTest(name = "Find Spend by username and description via {0} repository")
    @MethodSource("testArgsSource")
    void findSpendByUsernameAndDescriptionTest(String repositoryType, SpendClient spendClient) {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        String categoryName = RandomDataUtils.randomCategoryName();
        String spendDescription = RandomDataUtils.randomSentence(2);
        SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        categoryName,
                        createdUser.username(),
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                spendDescription,
                createdUser.username()
        );

        SpendJson createdSpend = spendClient.createSpend(spend);
        System.out.println("Created Spend: " + createdSpend);
        System.out.println("Created Category: " + createdSpend.category());

        SpendJson foundByIdSpend = spendClient.findSpendByUsernameAndDescription(
                createdSpend.username(), createdSpend.description()
        );
        System.out.println("Found by username and description Spend: " + foundByIdSpend);
        assertThat(foundByIdSpend.id(), is(createdSpend.id()));
    }

    @ParameterizedTest(name = "Delete Spend via {0} repository")
    @MethodSource("testArgsSource")
    void deleteSpendTest(String repositoryType, SpendClient spendClient) {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        String categoryName = RandomDataUtils.randomCategoryName();
        String spendDescription = RandomDataUtils.randomSentence(2);
        SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        categoryName,
                        createdUser.username(),
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                spendDescription,
                createdUser.username()
        );

        SpendJson createdSpend = spendClient.createSpend(spend);
        System.out.println("Created Spend: " + createdSpend);
        System.out.println("Created Category: " + createdSpend.category());

        spendClient.deleteSpend(createdSpend);
        System.out.println("Deleted by id Spend: " + createdSpend);
        assertThrows(RuntimeException.class, () -> spendClient.findSpendById(createdSpend.id()));
    }

    @ParameterizedTest(name = "Delete Category via {0} repository")
    @MethodSource("testArgsSource")
    void deleteCategoryTest(String repositoryType, SpendClient spendClient) {
        UserJson createdUser = usersDbClient.createUserViaRepositoryJdbc(
                RandomDataUtils.randomUsername(), "12345");
        System.out.println("Created User: " + createdUser);

        String categoryName = RandomDataUtils.randomCategoryName();
        String spendDescription = RandomDataUtils.randomSentence(2);
        SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        categoryName,
                        createdUser.username(),
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                spendDescription,
                createdUser.username()
        );

        SpendJson createdSpend = spendClient.createSpend(spend);
        System.out.println("Created Spend: " + createdSpend);
        System.out.println("Created Category: " + createdSpend.category());

        spendClient.deleteSpend(createdSpend);
        spendClient.deleteCategory(createdSpend.category());
        System.out.println("Deleted by id Spend: " + createdSpend);
        System.out.println("Deleted by id Category: " + createdSpend.category());
        assertThrows(RuntimeException.class, () -> spendClient.findCategoryById(createdSpend.category().id()));
    }

    public static Stream<Arguments> testArgsSource() {
        return Stream.of(
                arguments("JDBC", new SpendDbClient(new SpendRepositoryJdbc())),
                arguments("Spring", new SpendDbClient(new SpendRepositorySpring())),
                arguments("Hibernate", new SpendDbClient(new SpendRepositoryHibernate()))
        );
    }

}
