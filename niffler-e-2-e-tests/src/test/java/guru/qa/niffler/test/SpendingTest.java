package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.api.ApiClient.CFG;

@WebTest
public class SpendingTest {

  @User(
          username = "duck",
          spendings = @Spending(
                  amount = 89990.00,
                  description = "Advanced 9 поток!",
                  category = "Обучение"
          )
  )
  @DisabledByIssue("2")
  @Test
  @DisplayName("Main page is displayed after success login")
  void mainPageShouldBeDisplayedAfterSuccessLogin(SpendJson spendJson) {
    final String newDescription = ":)";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .fillLoginPage("duck", "12345")
            .submit()
            .checkThatPageLoaded()
            .editSpending(spendJson.description())
            .setNewSpendingDescription(newDescription)
            .save()
            .checkThatTableContainsSpending(newDescription);
  }
}
