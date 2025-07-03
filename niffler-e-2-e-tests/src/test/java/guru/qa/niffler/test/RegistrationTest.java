package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.api.ApiClient.CFG;

/**
 * @author Alexander
 */
@WebTest
public class RegistrationTest {

    @Test
    @DisplayName("New user registration")
    void shouldRegisterNewUser() {
        String newUsername = RandomDataUtils.randomUsername();
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
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(RandomDataUtils.randomUsername())
                .setPassword("1wsx")
                .setPasswordSubmit("2wsx")
                .errorSubmit()
                .errorContains("Passwords should be equal");
    }

}
