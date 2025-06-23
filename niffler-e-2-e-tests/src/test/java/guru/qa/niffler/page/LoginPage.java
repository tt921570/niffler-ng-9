package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $("#register-button");
  private final SelenideElement errorContainer = $(".form__error");

  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  public MainPage submit() {
    submitButton.click();
    return new MainPage();
  }

  public LoginPage errorSubmit() {
    submitButton.click();
    submitButton.shouldBe(visible);
    registerButton.shouldBe(visible);
    return this;
  }

  @Step("Error message [{0}] is displayed")
  public void errorContains(String errorMessage) {
    errorContainer.shouldHave(text(errorMessage));
  }

  public RegisterPage createNewAccount() {
    registerButton.click();
    return new RegisterPage();
  }
}
