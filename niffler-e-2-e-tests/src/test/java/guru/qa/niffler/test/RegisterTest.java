package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Alexander
 */
@WebTest
public class RegisterTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("New user registration")
    void shouldRegisterNewUser() {
        Faker faker = new Faker();
        String newUsername = faker.name().username();
        String newUserPassword = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(newUsername)
                .setPassword(newUserPassword)
                .setPasswordSubmit(newUserPassword)
                .successSubmit()
                .fillLoginPage(newUsername, newUserPassword)
                .submit()
                .checkThatPageLoaded();
    }

    @Test
    @DisplayName("Register existing user is impossible")
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername("duck")
                .setPassword("1qaz")
                .setPasswordSubmit("1qaz")
                .errorSubmit()
                .errorContains("Username `duck` already exists");

    }

    @Test
    @DisplayName("Error if password and confirmation are not equal")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Faker faker = new Faker();
        String newUsername = faker.name().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(newUsername)
                .setPassword("1wsx")
                .setPasswordSubmit("2wsx")
                .errorSubmit()
                .errorContains("Passwords should be equal");
    }

}
