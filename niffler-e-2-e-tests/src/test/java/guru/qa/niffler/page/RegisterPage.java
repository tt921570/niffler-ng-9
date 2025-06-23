package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

/**
 * @author Alexander
 */
public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement registerButton = $("#register-button");
    private final SelenideElement proceedLoginButton = $(".form_sign-in");
    private final SelenideElement errorContainer = $(".form__error");

    @Step("Set username: {0}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Set password: {0}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Confirm password: {0}")
    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.setValue(passwordSubmit);
        return this;
    }

    @Step("Submit register")
    public LoginPage successSubmit() {
        registerButton.click();
        proceedLoginButton.click();
        return new LoginPage();
    }

    @Step("Submit")
    public RegisterPage errorSubmit() {
        registerButton.click();
        return this;
    }

    @Step("Error message [{0}] is displayed")
    public void errorContains(String errorMessage) {
        errorContainer.shouldHave(text(errorMessage));
    }
}
