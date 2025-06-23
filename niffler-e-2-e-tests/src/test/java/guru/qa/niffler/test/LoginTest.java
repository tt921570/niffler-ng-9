package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginTest {

  private static final Config CFG = Config.getInstance();

  @Test
  @DisplayName("Main page is displayed after successful login")
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage("duck", "12345")
        .submit()
        .checkThatPageLoaded();
  }

  @Test
  @DisplayName("Login with bad credentials is impossible")
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage("duck", "1qaz")
            .errorSubmit()
            .errorContains("Неверные учетные данные пользователя");
  }
}
